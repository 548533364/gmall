package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class SkuSerivceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void saveSkuInfo ( PmsSkuInfo pmsSkuInfo ) {

        //插入skuInfo
        int i = pmsSkuInfoMapper.insertSelective ( pmsSkuInfo );
        String skuId = pmsSkuInfo.getId ( );
        //有主键返回策略，插入平台属性关联
        List < PmsSkuAttrValue > skuAttrValueList = pmsSkuInfo.getSkuAttrValueList ( );
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId ( skuId );
            pmsSkuAttrValueMapper.insertSelective ( pmsSkuAttrValue );
        }

        //插入销售属性关联
        List < PmsSkuSaleAttrValue > skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList ( );
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId ( skuId );
            pmsSkuSaleAttrValueMapper.insertSelective ( pmsSkuSaleAttrValue );
        }

        //插入图片信息
        List < PmsSkuImage > skuImageList = pmsSkuInfo.getSkuImageList ( );
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId ( skuId );
            pmsSkuImageMapper.insertSelective ( pmsSkuImage );
        }
    }

    public PmsSkuInfo getSkuByIdFromDb ( String skuId ) {

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo ();
        pmsSkuInfo.setId ( skuId );
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne ( pmsSkuInfo );

        //获取图片列表，这是外键查询
        PmsSkuImage pmsSkuImage = new PmsSkuImage ();
        pmsSkuImage.setSkuId ( skuId );//外键
        List < PmsSkuImage > pmsSkuImages = pmsSkuImageMapper.select ( pmsSkuImage );
        skuInfo.setSkuImageList ( pmsSkuImages );
        return skuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById ( String skuId, String ip) {

         System.out.println ("ip为"+ip+Thread.currentThread ().getName ()+"进入了商品详情的请求" );

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo ();
        //链接缓存
        Jedis jedis = redisUtil.getJedis();

        //查询缓存
        String skuKey = "sku:" + skuId + ":info";

        String skuJson = jedis.get ( skuKey );  //skuJson这个不能为空

        if (StringUtils.isNotBlank ( skuJson )){//if(skuJson!=null&&!skuJson.equals(""))

            System.out.println ("ip为"+ip+Thread.currentThread ().getName ()+"从缓存中获取商品详情" );

           pmsSkuInfo= JSON.parseObject ( skuJson,PmsSkuInfo.class );//将java的字符串转化为java的对象类
        }else {
            //如果缓存中没有，查询数据库，如果出现A,B同时访问都没有在rediss中查询到，这个问题怎么处理
             System.out.println ("ip为"+ip+Thread.currentThread ().getName ()+"发现缓存中没有，申请分布式锁" +"sku:" + skuId + ":lock");

            //设置分布式锁
            //可以用value去设置当前线程锁的token，防止出现锁过期复活之后删除其他线程锁的情况（两个线程拿到的key一样，value不一样来确定删除的锁是不是自己的）
            String token = UUID.randomUUID ( ).toString ( );

            String OK = jedis.set ( "sku:" + skuId + ":lock",token,"nx","px",10 *1000);//拿到锁的线程有10秒的过期时间

            if (StringUtils.isNotBlank ( OK )&&OK.equals("OK")){// if (StringUtils.isNotBlank ( OK )&&OK.equals ( "OK" ))
                //设置成功，有权在10秒之内访问数据库
                System.out.println ( "ip为" + ip + Thread.currentThread ( ).getName ( ) + "成功拿到锁，有权在10秒钟之内访问数据库" + "sku:" + skuId + ":lock" );

                pmsSkuInfo = getSkuByIdFromDb ( skuId );

                if (pmsSkuInfo != null) {

                    //数据库查询结果存入Redis
                    jedis.set ( "sku:" + skuId + ":info",JSON.toJSONString ( pmsSkuInfo ) );
                } else {

                    //数据库中不存在的sku
                    //为了防止缓存穿透，null或者字符串值设置给redis
                    jedis.setex ( "sku:" + skuId + ":info",60 * 3,JSON.toJSONString ( "" ) );
                }
                //在访问mysql后，将mysql的分布锁释放
                System.out.println ( "ip为" + ip + ":" + Thread.currentThread ( ).getName ( ) + "使用完毕，将锁归还：" + "sku:" + skuId + ":lock" );
                String lockToken = jedis.get ( "sku:" + skuId + ":lock" );

                 if (StringUtils.isNotBlank ( lockToken ) && lockToken.equals ( token )) {

                     //jedis.eval("lua");可与用lua脚本，在查询到key的同时删除该key，防止高并发下的意外的发生
                     String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                      jedis.eval(script, Collections.singletonList("lock"),Collections.singletonList(token));

                     jedis.del ( "sku:" + skuId + ":lock" );// 用token确认删除的是自己的sku的锁
                 }
            }else {
                // 设置失败，自旋（该线程在睡眠几秒后，重新尝试访问本方法）
                System.out.println("ip为"+ip+":"+Thread.currentThread().getName()+"没有拿到锁，开始自旋");
                return getSkuById(skuId,ip);
            }
        }
        jedis.close ();
        return pmsSkuInfo;
    }

    @Override
    public List < PmsSkuInfo > getSkuSaleAttrValueListBySpu ( String productId ) {

        List < PmsSkuInfo > pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu ( productId );

        return  pmsSkuInfos;
    }

    @Override
    public List < PmsSkuInfo > getAllSku ( String catalog3Id ) {//查询skuservice的全部信息
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll ();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId ( );//返回本地的变量

            PmsSkuAttrValue pmsSkuAttrValue =new PmsSkuAttrValue ();
            pmsSkuAttrValue.setSkuId ( skuId );
            List < PmsSkuAttrValue > change  = pmsSkuAttrValueMapper.select ( pmsSkuAttrValue );
            pmsSkuInfo.setSkuAttrValueList ( change );
        }
        return pmsSkuInfos;
    }
}
