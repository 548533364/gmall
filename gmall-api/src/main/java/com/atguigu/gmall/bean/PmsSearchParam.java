package com.atguigu.gmall.bean;


import java.io.Serializable;
import java.util.List;

public class PmsSearchParam implements Serializable{

    private String catalog3Id; //��������ID

    private String keyword;//�ؼ���

    private List<PmsSkuAttrValue> skuAttrValueList; //ƽ̨���Լ���     ����PmsBaseAttrInfo�Լ�PmsBaseAttrValue���ű�ļ��ϡ�

    //���м

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
