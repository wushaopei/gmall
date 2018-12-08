package com.atguigu.gmall.manage.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    SpuInfoMapper spuInfoMapper;



    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    SpuImageMapper spuImageMapper;

    //sku查询spu图片用于添加sku（初始化sku添加图片）
    @Override
    public List<SpuImage> getspuImageList(String spuId) {

        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        List<SpuImage> spuImageList = spuImageMapper.select(spuImage);


        return spuImageList;
    }

    // 初始化销售属性
    @Override
    public List<SpuSaleAttr> spuSaleAttrList(String spuId) {
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuId);
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrMapper.select(spuSaleAttr);

        for (SpuSaleAttr saleAttr : spuSaleAttrs) {

            SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
            spuSaleAttrValue.setSpuId(spuId);
            spuSaleAttrValue.setSaleAttrId(saleAttr.getSaleAttrId());
            List<SpuSaleAttrValue> spuSaleAttrValues = spuSaleAttrValueMapper.select(spuSaleAttrValue);
            saleAttr.setSpuSaleAttrValueList(spuSaleAttrValues);

        }

        return spuSaleAttrs;
    }
    @Override
    public List<SpuInfo> getSpuInfoList(String catalog3Id) {

        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        List<SpuInfo> select = spuInfoMapper.select(spuInfo);

        return select;
    }

    @Override
    public List<BaseSaleAttr> baseSaleAttr() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public void saveSpu(SpuInfo spuInfo) {
        //保存spu信息
        spuInfoMapper.insertSelective(spuInfo);
        //获取spuid作为属性外键
        String spuid = spuInfo.getId();

        //保存spu图片信息
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuid);
            spuImageMapper.insertSelective(spuImage);
        }


        //保存spu的销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {

//            //销售属性
            spuSaleAttr.setSpuId(spuid);
            spuSaleAttrMapper.insertSelective(spuSaleAttr);

            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {

                //销售属性值
                spuSaleAttrValue.setSpuId(spuid);
                spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
            }
        }
    }


    //    根据spuId查询销售属性集合
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListBySpuId(String spuId,String skuId) {

      Map<String, Object> map = new HashMap<>();
        map.put("skuId",skuId);
        map.put("spuId",spuId);

        List<SpuSaleAttr> spuSaleAttrs =  spuSaleAttrMapper.selectSpuSaleAttrListBySpuId(map);

        return spuSaleAttrs;
    }



}
