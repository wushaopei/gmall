package com.atguigu.gmall.item.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;



    //item的功能就是sku的功能，item是前台的sku功能，sku是后台sku功能
    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable("skuId")String skuId,ModelMap map){


        SkuInfo skuInfo = skuService.getSkuById(skuId);
        map.put("skuInfo",skuInfo);

//        通过skuId 获得spuId
        String spuId = skuInfo.getSpuId();

//        根据spuId查询销售属性集合
        List<SpuSaleAttr> spuSaleAttrList =  spuService.getSpuSaleAttrListBySpuId(spuId,skuId);
        map.put("spuSaleAttrListCheckBySku",spuSaleAttrList);

        // 根据spuId制作页面销售属性的hash表
        // 销售属性组合：skuId
        List<SkuInfo> skuInfos = skuService.skuSaleAttrValueListBySpu(spuId);

        HashMap<String, String> stringStringHashMap = new HashMap<>();

        for (SkuInfo info : skuInfos) {
            String skuSaleAttrValueIdsKey = "";

//            获取sku销售属性值的集合
            List<SkuSaleAttrValue> skuSaleAttrValueList = info.getSkuSaleAttrValueList();
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {

//            sku 销售属性值id +spu销售属性值id
                skuSaleAttrValueIdsKey = skuSaleAttrValueIdsKey+"|" + skuSaleAttrValue.getSaleAttrValueId();

            }
            String skuIdValue = info.getId();
            stringStringHashMap.put(skuSaleAttrValueIdsKey,skuIdValue);
        }

        String s = JSON.toJSONString(stringStringHashMap);

        map.put("valuesSkuJson",s);

        return "item";
    }













//
//    @RequestMapping("test")
//    public String getSkuInfo(ModelMap model, HttpSession session){
//
//        model.put("item","WOSHISKU");
//        model.put("hello","WOSHISKU");
//
//        Map<Object, Object> map = new HashMap<>();
//        map.put("1","s");
//        map.put("2","ss");
//        map.put("3","sss");
//        map.put("4","ssss");
//
//        map.put("num","2");
//
//        session.setAttribute("userName","获取session成功");
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            list.add("循环第"+i+"个");
//
//        }
//
//        map.put("list",list);
//
//
//
//        return "item";
//    }
}
