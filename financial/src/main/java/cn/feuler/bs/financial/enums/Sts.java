package cn.feuler.bs.client.enums;

import lombok.Getter;

/**
 * Create by: Feuler
 * Date: 2019/3/20
 **/

@Getter
public enum Sts {
    NORMAL("N", "正常"),
    CLOSED("C", "注销"),
    FREEZE("F", "冻结"),
    ACTIVE("A", "待激活"),
    RESET("R", "待重置密码"),
    ;
    private String code;

    private String msg;

    Sts(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
