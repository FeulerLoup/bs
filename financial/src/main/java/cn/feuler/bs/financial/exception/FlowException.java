package cn.feuler.bs.financial.exception;

/**
 * Create by: Feuler
 * Date: 2019/3/24
 **/

public class FlowException extends RuntimeException {
    private Integer code;

    public FlowException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
