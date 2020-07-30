package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.bean.PmsBaseCatalog3;
import com.atguigu.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.atguigu.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.atguigu.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.atguigu.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;//使用的是通用mapper，直接绑定数据映射

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List < PmsBaseCatalog1 > getCatalog1 () {
        return pmsBaseCatalog1Mapper.selectAll ();
}

    @Override
    public List < PmsBaseCatalog2 > getCatalog2 ( String catalog1Id ) {//查询所有二级分类列表，返回格式是json格式，用@Requstbody注解
        PmsBaseCatalog2 pmsBaseCatalog2 =new PmsBaseCatalog2 ();
        pmsBaseCatalog2.setCatalog1Id ( catalog1Id );
        List < PmsBaseCatalog2 > pmsBaseCatalog2s = pmsBaseCatalog2Mapper.select ( pmsBaseCatalog2 );
        return pmsBaseCatalog2s;
    }

    @Override
    public List < PmsBaseCatalog3 > getCatalog3 ( String catalog2Id ) {
        PmsBaseCatalog3 pmsBaseCatalog3 =new PmsBaseCatalog3 ();
        pmsBaseCatalog3.setCatalog2id ( catalog2Id );
        List < PmsBaseCatalog3 > pmsBaseCatalog3s = pmsBaseCatalog3Mapper.select ( pmsBaseCatalog3 );
        return pmsBaseCatalog3s;
    }

}
