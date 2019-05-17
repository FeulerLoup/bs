package cn.feuler.bs.user.VO;

import lombok.Data;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
public class ResultVO<T> {

    private Integer code;

    private String msg;

    private T data;
}
