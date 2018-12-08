package com.atguigu.gmall.manage.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.manage.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.service.BaseAttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Decoder;

import java.util.List;
@Service
public class BaseAttrServiceImpl implements BaseAttrService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
//
//    @Override
//    public List<BaseAttrInfo> getAttrInfo(String catalog3Id) {
////        根据第三级分类的id获取属性
////       创建属性对象
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
////        降三级分类id作为属性的外键id
//        baseAttrInfo.setCatalog3Id(catalog3Id);
////        通过查询外键获取对应的属性
//        List<BaseAttrInfo> select = baseAttrInfoMapper.select(baseAttrInfo);
////      返回集合
//        return select;
//    }



    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo) {

        String id = baseAttrInfo.getId();

        if(StringUtils.isBlank(id)){

            baseAttrInfoMapper.insertSelective(baseAttrInfo);

            String attrid = baseAttrInfo.getId();

            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(attrid);

                baseAttrValueMapper.insertSelective(baseAttrValue);
            }

        }else{
//            编辑使用先删除，后重新插入的方式
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(baseAttrInfo.getId());

            baseAttrValueMapper.delete(baseAttrValue);

//          重新插入属性
            if(baseAttrInfo.getAttrValueList()!=null && baseAttrInfo.getAttrValueList().size()>0 ){

                String attrid = baseAttrInfo.getId();

                List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

                for (BaseAttrValue baseAttrValues : attrValueList) {
                    baseAttrValues.setAttrId(attrid);

                    baseAttrValueMapper.insertSelective(baseAttrValues);
                }

            }
        }



    }

    @Override
    public List<BaseAttrValue> getAttrValue(String id) {

        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(id);
        List<BaseAttrValue> select = baseAttrValueMapper.select(baseAttrValue);


        return select;
    }

    @Override
    public void removeAttrValue(String attrId) {

        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        baseAttrValueMapper.delete(baseAttrValue);

        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfoMapper.deleteByPrimaryKey(attrId);


    }

    @Override
    public List<BaseAttrInfo> attrInfoList(String catalog3Id) {

        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.select(baseAttrInfo);

        for (BaseAttrInfo attrInfo : baseAttrInfos) {
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(attrInfo.getId());
            List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.select(baseAttrValue);

            attrInfo.setAttrValueList(baseAttrValues);
        }
        return baseAttrInfos;
    }

}
