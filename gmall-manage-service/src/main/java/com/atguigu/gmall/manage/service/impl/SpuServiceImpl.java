package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsProductImageMapper;
import com.atguigu.gmall.manage.mapper.PmsProductInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;


    @Override
    public List < PmsProductInfo > spuList ( String catalog3Id ) {
        PmsProductInfo pmsProductInfo =new PmsProductInfo ();
        pmsProductInfo.setCatalog3Id ( catalog3Id );
        List < PmsProductInfo > pmsProductInfos = pmsProductInfoMapper.select ( pmsProductInfo );
        return pmsProductInfos;
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        // 保存商品信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        // 生成商品主键
        String productId = pmsProductInfo.getId();

        // 保存商品图片信息
        List< PmsProductImage > spuImageList = pmsProductInfo.getSpuImageList ();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(productId);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }
        // 保存销售属性信息
        List< PmsProductSaleAttr > spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList ();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);

            // 保存销售属性值
            List< PmsProductSaleAttrValue > spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList ();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(productId);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }

    }

    //展示销售属性列表，获取所有销售属性值PmsProductSaleAttr、PmsProductSaleAttrValue、PmsSkuInfo这三张表
    @Override
    public List < PmsProductSaleAttr > spuSaleAttrListCheckBySku ( String productId ,String skuId) {
        // 干嘛要生气呢，能走到最后的？我现在开始怀疑我自己了。
        //注释的代码的错误代码，通用mapper不能查询多表，用mybatis数据库查询语句
        // PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr ( );
        // pmsProductSaleAttr.setProductId ( productId );
        // List < PmsProductSaleAttr > pmsProductSaleAttrs = pmsProductSaleAttrMapper.select ( pmsProductSaleAttr );
        //
        // for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
        //     String saleAttrId = productSaleAttr.getSaleAttrId ( );
        //     PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue ( );
        //     pmsProductSaleAttrValue.setSaleAttrId ( saleAttrId );
        //     pmsProductSaleAttrValue.setProductId ( productId );
        //     List < PmsProductSaleAttrValue > pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select ( pmsProductSaleAttrValue );
        //     productSaleAttr.setSpuSaleAttrValueList ( pmsProductSaleAttrValues );
        //
        // }
        List < PmsProductSaleAttr > pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
        return pmsProductSaleAttrs;
    }

    //属性列表的
    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr ( );
        pmsProductSaleAttr.setProductId ( spuId );
        List < PmsProductSaleAttr > PmsProductSaleAttrs = pmsProductSaleAttrMapper.select ( pmsProductSaleAttr );
        for (PmsProductSaleAttr productSaleAttr : PmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue ( );
            pmsProductSaleAttrValue.setProductId ( spuId );
            //主键Id--setSaleAttrId;销售属性id用的是系统的字典表中id，不是销售属性表的主键
            pmsProductSaleAttrValue.setSaleAttrId ( productSaleAttr.getSaleAttrId ( ) );// 销售属性id用的是系统的字典表中id，不是销售属性表的主键
            List < PmsProductSaleAttrValue > pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select ( pmsProductSaleAttrValue );
            productSaleAttr.setSpuSaleAttrValueList ( pmsProductSaleAttrValues );
        }
        return PmsProductSaleAttrs;
       /* PmsProductSaleAttr pmsProductSaleAttr =new PmsProductSaleAttr ();
        pmsProductSaleAttr.setProductId ( spuId );
        List<PmsProductSaleAttr> PmsProductSaleAttrs = pmsProductSaleAttrMapper.select ( pmsProductSaleAttr );
        for (PmsProductSaleAttr productSaleAttr : PmsProductSaleAttrs){
            PmsProductSaleAttrValue pmsProductSaleAttrValue =new PmsProductSaleAttrValue ();
            pmsProductSaleAttrValue.setProductId ( spuId );
            pmsProductSaleAttrValue.setSaleAttrId ( productSaleAttr.getSaleAttrId () );// 销售属性id用的是系统的字典表中id，不是销售属性表的主键
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select ( pmsProductSaleAttrValue );
            productSaleAttr.setPmsProductSaleAttrValueList ( pmsProductSaleAttrValues );
        }*/
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

}
/**
 * 平台属性与销售属性的区别：SPU与SKU的区别
 * 平台属性属于全部商品，由电商网站来维护管理
 * 销售属性是属于商家，由商家来维护管理
 *
 **/
