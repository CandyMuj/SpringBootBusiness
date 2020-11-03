package com.cc.pic.api.src.controller;

import com.cc.pic.api.pojo.sys.User;
import com.cc.pic.api.src.service.ISystemLogService;

import javax.annotation.Resource;

/**
 * @ProjectName api
 * @FileName BaseController
 * @Description
 * @Author CandyMuj
 * @Date 2020/6/9 14:30
 * @Version 1.0
 */
public class BaseController {
    @Resource
    protected ISystemLogService systemLogService;


    /**
     * 验证当前账号是否具有管理员操作权限
     */
    protected String validateAdmin(User user) {
        if (user == null) {
            return "账号不存在";
        }
        if (!this.isAdmin(user)) {
            return "无权操作";
        }
//        if (!Enable.DISENABLE.getCode().equals(user.getFrozen())) {
//            return "此账号被冻结";
//        }

        return null;
    }


    private boolean isAdmin(User user) {
        return user != null;//&& UserTypeEnum.ADMIN.getType().equals(user.getUserType());
    }

}