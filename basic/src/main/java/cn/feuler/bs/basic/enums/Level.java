package cn.feuler.bs.basic.enums;

import lombok.Getter;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Getter
public enum Level {
    CI(1, "客户"),

    TLR(2, "普通用户"),

    SU(3, "管理员"),
    ;

    private Integer code;

    private String level;

    Level(Integer code, String level) {
        this.code = code;
        this.level = level;
    }
}
