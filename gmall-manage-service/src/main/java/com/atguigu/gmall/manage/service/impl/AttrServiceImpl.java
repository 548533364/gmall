package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    //查询PmsBaseAttrInfo全部catalog3Id属性值
    @Override
    public List < PmsBaseAttrInfo > attrInfoList ( String catalog3Id ) {
        PmsBaseAttrInfo pmsBaseAttrInfo= new PmsBaseAttrInfo ();
        pmsBaseAttrInfo.setCatalog3Id ( catalog3Id );
        List < PmsBaseAttrInfo > pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select ( pmsBaseAttrInfo );


        //添加平台属性值
        //将SKU列表信息添加进SPU属性列表。
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {

           List< PmsBaseAttrValue > pmsBaseAttrValues =new ArrayList <> (  );
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue ( );
            pmsBaseAttrValue.setAttrId ( baseAttrInfo.getId () );
            pmsBaseAttrValues = pmsBaseAttrValueMapper.select ( pmsBaseAttrValue );
            baseAttrInfo.setAttrValueList ( pmsBaseAttrValues );
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo ( PmsBaseAttrInfo pmsBaseAttrInfo ) {
       String id= pmsBaseAttrInfo.getId ();
       if(StringUtils.isBlank ( id )){
           //id为空，保存平台属性值
           //保存属性
           //insert和insertSelective的区别在于：
           //insert和insertSelective的区别在于是否将null插入数据库，insert全插入。
           pmsBaseAttrInfoMapper.insertSelective ( pmsBaseAttrInfo );
           //保存平台属性值
           List<PmsBaseAttrValue> attrValueList=pmsBaseAttrInfo.getAttrValueList ();
           for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
               pmsBaseAttrValue.setAttrId ( pmsBaseAttrInfo.getId () );
               pmsBaseAttrValueMapper.insertSelective ( pmsBaseAttrValue );
           }
       }else {
           //id不为空，修改平台属性值
           //属性
           Example example = new Example ( PmsBaseAttrInfo.class );
           example.createCriteria ().andEqualTo ( "id",pmsBaseAttrInfo.getId () );//正则表达式，根据id修改（修改条件表达式）
           pmsBaseAttrInfoMapper.updateByExampleSelective ( pmsBaseAttrInfo,example);

           //属性值
          List<PmsBaseAttrValue>attrValueList= pmsBaseAttrInfo.getAttrValueList ();
          //根据属性id删除所有属性值
           PmsBaseAttrValue pmsBaseAttrValueDel= new PmsBaseAttrValue ();
           pmsBaseAttrValueDel.setAttrId ( pmsBaseAttrInfo.getId () );
           pmsBaseAttrValueMapper.delete ( pmsBaseAttrValueDel );
           //删除之后，将新的属性值插入
           for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
               pmsBaseAttrValueMapper.insertSelective ( pmsBaseAttrValue );
           } //这里可以给生产环境来一个try抛异常，这里我给写死
       }
        return "success";
    }
    @Override
    public List < PmsBaseSaleAttr > baseSaleAttrList () {
        return  pmsBaseSaleAttrMapper.selectAll ();
    }
}
