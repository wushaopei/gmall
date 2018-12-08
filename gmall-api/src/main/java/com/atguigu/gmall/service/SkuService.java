package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.SpuSaleAttr;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkuService  {
    List<SkuInfo> getSkuInfoList(String spuId);

    void saveSku(SkuInfo skuInfo);



    SkuInfo getSkuById(String skuId);

    List<SkuInfo> skuSaleAttrValueListBySpu(String spuId);
}
