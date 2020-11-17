package com.atguigu.gmall.bean;


import java.io.Serializable;
import java.util.List;

public class PmsSearchParam implements Serializable{

    private String catalog3Id; //三级分类ID

    private String keyword;//关键字

    private List<PmsSkuAttrValue> skuAttrValueList; //平台属性集合     《是PmsBaseAttrInfo以及PmsBaseAttrValue两张表的集合》

    //面包屑

    public String getCatalog3Id () {
        return catalog3Id;
    }

    public void setCatalog3Id ( String catalog3Id ) {
        this.catalog3Id = catalog3Id;
    }

    public String getKeyword () {
        return keyword;
    }

    public void setKeyword ( String keyword ) {
        this.keyword = keyword;
    }

    public List < PmsSkuAttrValue > getSkuAttrValueList () {
        return skuAttrValueList;
    }

    public void setSkuAttrValueList ( List < PmsSkuAttrValue > skuAttrValueList ) {
        this.skuAttrValueList = skuAttrValueList;
    }




}
