package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.manage.controller.util.PmsUploadUtil;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping ("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo( @RequestBody PmsProductInfo pmsProductInfo ){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping("spuList")
    @ResponseBody
    public List< PmsProductInfo > spuList(String catalog3Id){
        List< PmsProductInfo > pmsProductInfos=spuService. spuList( catalog3Id);
        return pmsProductInfos;
    }

    @RequestMapping ("fileUpload")
    @ResponseBody
    public String fileUpload( @RequestParam ("file") MultipartFile multipartFile ){  //spring-mvc将图片封装成MultipartFile，二进制的混合式媒体格式
        //将图片上或者视频传到分布式存储系统

        //将图片的存储路径返回给页面
        String imgUrl= PmsUploadUtil.uploadImage(multipartFile);
        System.out.println (imgUrl );
        return imgUrl;
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List< PmsProductSaleAttr > spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public List< PmsProductImage > spuImageList(String spuId){
        List<PmsProductImage>pmsProductImages =spuService.spuImageList(spuId);
        return pmsProductImages;
    }

}