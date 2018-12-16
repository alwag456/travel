package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    boolean regist(User user,HttpServletRequest request);

    boolean contrast(String code);

    User login(User user);
}
