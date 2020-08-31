package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {
    List< PmsProductInfo> spuList ( String catalog3Id );
    List< PmsProductSaleAttr > spuSaleAttrList ( String spuId );
    List< PmsProductImage > spuImageList ( String spuId );
    void saveSpuInfo ( PmsProductInfo pmsProductInfo );

    List< PmsProductSaleAttr> spuSaleAttrListCheckBySku ( String productId ,String skuId);
}
