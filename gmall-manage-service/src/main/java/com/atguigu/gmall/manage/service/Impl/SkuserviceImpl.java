package com.atguigu.gmall.manage.service.Impl;
import com.atguigu.gmall.common.FixUtils;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
@Service
public class SkuserviceImpl implements SkuService{

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuImageMapper spuImageMapper;

    @Autowired
    SkuImageMapper skuImageMapper;

    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;


    @Autowired
    RedisUtil redisUtil;

    //获取当前spuid对应的所有skuInfo(sku的刷新按钮)
    @Override
    public List<SkuInfo> getSkuInfoList(String spuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSpuId(spuId);
        List<SkuInfo> skuInfoList = skuInfoMapper.select(skuInfo);
        return skuInfoList;
    }

    /**
     *  1、 保存skuInfo库存单元数据
     *  2、 获取库存单元id
     *  3、 保存sku平台属性值
     *  4、 根据库存单元数据获取sku平台属性数据，并遍历保存到数据库
     * */
    @Override
    public void saveSku(SkuInfo skuInfo) {
        //1
        skuInfoMapper.insertSelective(skuInfo);
        //2
        String skuId = skuInfo.getId();

        //3
        //4
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {

            skuAttrValue.setSkuId(skuId);
            skuAttrValueMapper.insertSelective(skuAttrValue);
        }

        //保存sku商品图片数据
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {

            skuImage.setSkuId(skuId);
            skuImageMapper.insertSelective(skuImage);
        }


        //保存sku销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuId);

            skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
        }


    }


    /*
        分析：
    * 缓存的使用：不直接向数据库查询，已起到保护数据库不宕机
    * 1、从缓存查，没有就查数据库
    * 2、数据库查询完毕，将数据库的信息同步到缓存
    * 使用同步锁对应缓存击穿的问题（保证同一线程下）
    * 1.让正在取锁的对象进行查询缓存
    * a.未取到缓存，掉数据库
    * a.取到sql数据的进行睡眠 （并同步数据到缓存）
    * b.未取到sql的给与空值回复
    * 2.对等待的对象进行自旋排队（等待取锁并前往sql获取数据）
    *   类似递归
    * */
    @Override
    public SkuInfo getSkuById(String skuId) {
        SkuInfo skuInfo = null;
        // 查询缓存
        Jedis jedis = redisUtil.getJedis();
        String cacheJson = jedis.get(FixUtils.PREFIX + skuId + FixUtils.SUFFIX);
        //1、缓存查询——取锁查缓存层
        if(StringUtils.isBlank(cacheJson)){

            //分布式缓存锁服务器取锁
            String OK = jedis.set(FixUtils.PREFIX + skuId + FixUtils.SUFFIX,"1","nx","px",10000);

            //2、高并发锁查询层
            if(StringUtils.isNotBlank(OK)){

                // 缓存查询未果,查询数据库
                skuInfo =  getSkuByIdFromDb(skuId);

                if(skuInfo!=null){
                    //将sql的信息同步到缓存
                    try{
                        Thread.sleep(9000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    jedis.set(FixUtils.PREFIX + skuId + FixUtils.SUFFIX,JSON.toJSONString(skuInfo));

                    jedis.del(FixUtils.PREFIX + skuId + FixUtils.SUFFIX);
                }
            //2、高并发锁排队查sql层
            }else{
                try{
                    Thread.sleep(3000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //自旋
                return getSkuById(skuId);
            }
        //1、缓存查询层
        }else{
            skuInfo = JSON.parseObject(cacheJson, SkuInfo.class);
        }
        //关闭当前redis线程
        jedis.close();
        return skuInfo;
    }

    private SkuInfo getSkuByIdFromDb(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);

        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImages = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImages);

        return skuInfo;
    }


    @Override
    public List<SkuInfo> skuSaleAttrValueListBySpu(String spuId) {

        List<SkuInfo> skuInfos = skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);

        return skuInfos;
    }


}
