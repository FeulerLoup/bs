package cn.feuler.bs.client.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
public class Citacr implements Serializable{

    private String acNo;

    private String ciNo;

    private String acSts;

    private String ccy;

    private BigDecimal acBal;

    private String updTlr;

    private String dateTime;

}
