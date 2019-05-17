package cn.feuler.bs.user.enums;


import lombok.Getter;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Getter
public enum ActSts {

    NORMAL("N", "正常"),

    ERROR("E", "异常"),

    ;

    private String code;

    private String msg;

    ActSts(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
