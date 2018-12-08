package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseAttrService {

//    public List<BaseAttrInfo> getAttrInfo(String id);

    public void saveAttr(BaseAttrInfo baseAttrInfo);

    public List<BaseAttrValue> getAttrValue(String id);

    public void  removeAttrValue(String attrId);

    List<BaseAttrInfo> attrInfoList(String catalog3Id);
}
