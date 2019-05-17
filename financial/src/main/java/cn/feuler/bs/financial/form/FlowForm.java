package cn.feuler.bs.financial.form;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * Create by: Feuler
 * Date: 2019/3/24
 **/

@Data
@ToString
public class FlowForm {

    @NotEmpty
    private String act;

    @NotEmpty
    private String ciNo;

    @NotEmpty
    private String acNo;

    private String recvCiNo;

    private String recvAcNo;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal amt;

    @NotEmpty
    private String operator;

}
