package com.atguigu.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;

    @RequestMapping ("attrInfoList")
    @ResponseBody
    public List< PmsBaseAttrInfo > attrInfoList( String catalog3Id){
        List< PmsBaseAttrInfo > pmsBaseAttrInfos =attrService.attrInfoList ( catalog3Id );
        return pmsBaseAttrInfos;
    }


    @RequestMapping ("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo( @RequestBody PmsBaseAttrInfo pmsBaseAttrInfo ){
        String sucess = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }


    @RequestMapping ("baseSaleAttrList")
    @ResponseBody
    public List< PmsBaseSaleAttr > baseSaleAttrList( ){
        List< PmsBaseSaleAttr > pmsBaseSaleAttrs =attrService.baseSaleAttrList (  );
        return pmsBaseSaleAttrs;
    }


}
