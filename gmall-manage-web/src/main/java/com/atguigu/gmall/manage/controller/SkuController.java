package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.Utils.GmallUploadUtil;
import com.atguigu.gmall.service.BaseAttrService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SkuController {

    @Reference
    SkuService skuService;

    @Reference
    BaseAttrService baseAttrService;

    @Reference
    SpuService spuService;


    //根据spuId获取所有的image
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<SpuImage> spuImageList(String spuId){

        List<SpuImage> spuImageList =  spuService.getspuImageList(spuId);

        return spuImageList;
    }

    @RequestMapping("saveSku")
    @ResponseBody
    public String saveSku(SkuInfo skuInfo){

         skuService.saveSku(skuInfo);

        return "success";
    }

    @RequestMapping("skuInfoListBySpu")
    @ResponseBody
    public List<SkuInfo> skuInfoListBySpu(String spuId){

        List<SkuInfo> skuInfoList = skuService.getSkuInfoList(spuId);

        return skuInfoList;
    }


    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public  List<SpuSaleAttr>  spuSaleAttrList(String spuId){

        List<SpuSaleAttr> spuSaleAttrs = spuService.spuSaleAttrList(spuId);

        return spuSaleAttrs;
    }

    @RequestMapping("attrInfoList")
    @ResponseBody
    public  List<BaseAttrInfo>  attrInfoList(String catalog3Id){

        List<BaseAttrInfo> baseAttrInfo = baseAttrService.attrInfoList(catalog3Id);

        return baseAttrInfo;
    }
}
