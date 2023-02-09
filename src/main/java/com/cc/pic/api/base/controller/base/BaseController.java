package com.cc.pic.api.base.controller.base;

import cn.hutool.core.lang.Assert;
import com.cc.pic.api.base.service.ISysLogService;
import com.cc.pic.api.pojo.sys.User;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2020/6/9 14:30
 * @Version 1.0
 */
@Slf4j
public class BaseController {
    @Resource
    protected ISysLogService sysLogService;


    /**
     * 验证当前账号是否具有管理员操作权限
     */
    protected void validateAdmin(User user) {
        Assert.notNull(user, "账号不存在");
        Assert.isTrue(this.isAdmin(user), "无权操作");
        //Assert.isTrue(Enable.DISENABLE.getCode().equals(user.getFrozen()), "此账号被冻结");
    }


    private boolean isAdmin(User user) {
        return user != null;//&& UserTypeEnum.ADMIN.getType().equals(user.getUserType());
    }

}
