package cn.feuler.bs.user.dataobject;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
@ToString
public class Citacr implements Serializable {

    private String acNo;

    private String ciNo;

    private String acSts;

    private String ccy;

    private BigDecimal acBal;

    private String updTlr;

    private String dateTime;

}
