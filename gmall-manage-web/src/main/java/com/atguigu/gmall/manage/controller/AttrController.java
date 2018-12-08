package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.service.BaseAttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AttrController {

    @Reference
    BaseAttrService baseAttrService;

//    保存属性、属性值到数据库
    @ResponseBody
    @RequestMapping("saveAttr")
    public String saveAttr(BaseAttrInfo baseAttrInfo){

//        //保存属性信息
        baseAttrService.saveAttr(baseAttrInfo);

        return "success";
    }

    @RequestMapping(value = "getAttrValueList")
    @ResponseBody
    public List<BaseAttrValue> getAttrValueList(String attrId){


        List<BaseAttrValue> attrValue = baseAttrService.getAttrValue(attrId);

        return attrValue;

    }
    @RequestMapping(value = "removeAttrValueList")
    @ResponseBody
    public String removeAttrValueList(String attrId){


        baseAttrService.removeAttrValue(attrId);

        return "success";
    }


}
