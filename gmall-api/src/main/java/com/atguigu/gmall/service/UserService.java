package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    List<UserInfo> userList();

}
