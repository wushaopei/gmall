package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.manage.Utils.GmallUploadUtil;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
public class SpuManageController {

    @Reference
    SpuService spuService;


//    由ul中的js方法实现页面填充，跳转到填充的页面
    @RequestMapping("spuListPage")
    public String getSpuListPage(){
        return "spuListPage";
    }


    @RequestMapping(value="fileUpload",method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile){

        String imgUrl = GmallUploadUtil.uploadImge(multipartFile);
        //保存url
        return imgUrl;
    }


    @RequestMapping("getSpuList")
    @ResponseBody
    public List<SpuInfo> getSpuList(String catalog3Id){

        List<SpuInfo> spuInfoList = spuService.getSpuInfoList(catalog3Id);
        return spuInfoList;
    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<BaseSaleAttr> baseSaleAttrList(String catalog3Id){

        //调用后台查询一级分类订单集合
        List<BaseSaleAttr> spuSaleAttrList = spuService.baseSaleAttr();

        return spuSaleAttrList;
    }

    @RequestMapping("saveSpu")
    @ResponseBody
    public String saveSpu(SpuInfo spuInfo){

        spuService.saveSpu(spuInfo);

        return "success";
    }
}
