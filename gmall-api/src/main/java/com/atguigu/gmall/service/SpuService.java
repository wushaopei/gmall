package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpuService {

    List<SpuInfo> getSpuInfoList(String catalog3Id);

    List<BaseSaleAttr> baseSaleAttr();

    void saveSpu(SpuInfo spuInfo);

    List<SpuSaleAttr> spuSaleAttrList(String spuId);

    List<SpuImage> getspuImageList(String spuId);

    List<SpuSaleAttr> getSpuSaleAttrListBySpuId(String spuId,String skuId);
}
