package cn.feuler.bs.financial.enums;


/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

public enum Act {

    DEPOSIT(1, "存款"),

    WITHDRAWAL(2, "取款"),

    TRANSFER(3, "转账"),
    ;

    private Integer code;

    private String msg;

    Act(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
