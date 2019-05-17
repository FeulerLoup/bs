package cn.feuler.bs.user.enums;

import lombok.Getter;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@Getter
public enum ActType {

    LOGIN("登录"),

    LOGOUT("登出"),

    BROWSECLIENT("浏览客户列表"),

    ACTIVECLIENT("激活客户"),

    INQCLIENTINFO("查看客户信息"),

    MODCLIENTINFO("修改客户信息"),

    MODCLIENTSTS("修改客户状态"),

    MODCLIENTPASSWD("修改客户密码"),

    BROWSEACCOUNT("浏览账户列表"),

    BROWSEACCOUNTFLOW("浏览账户流水"),

    BROWSEFLOW("浏览总流水"),

    INQACCOUNT("查看账户信息"),

    OPENACCOUNT("开通账户"),

    ACTIVEACCOUNT("激活账户"),

    FINANCIAL("存取款"),

    DEPOSIT("存款"),

    WITHDRAW("取款"),

    MODACCOUNTSTS("修改账户状态"),

    BROWSETELLER("浏览用户列表"),

    INQTELLERINFO("查看用户信息"),

    MODTELLERPASSWD("修改用户密码"),

    MODTELLERINFO("修改用户信息"),

    ADDTELLER("新增用户"),

    BROWSEPERMISSION("浏览权限信息"),

    MODPERMISSION("修改权限信息"),

    BROWSEACTLIST("浏览操作日志"),

    ;


    private String act;

    ActType(String act) {
        this.act = act;
    }
}
