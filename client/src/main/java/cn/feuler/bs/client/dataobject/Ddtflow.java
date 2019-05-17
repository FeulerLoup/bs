package cn.feuler.bs.client.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
public class Ddtflow implements Serializable{

    private Integer recNo;

    private String act;

    private String ciNo;

    private String acNo;

    private String recvCiNo;

    private String recvAcNo;

    private BigDecimal amt;

    private String operator;

    private String dateTime;

}
