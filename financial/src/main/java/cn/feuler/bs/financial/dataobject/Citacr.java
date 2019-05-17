package cn.feuler.bs.financial.dataobject;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
@Table(name="citacr")
@Entity
public class Citacr {

    @Id
    @Column(nullable = false, length = 18)
    private String acNo;

    @Column(nullable = false, length = 18)
    private String ciNo;

    @Column(nullable = false, length = 1)
    private String acSts;

    @Column(nullable = false, length = 3)
    private String ccy;

    @Column(nullable = false)
    private BigDecimal acBal;

    @Column(nullable = false, length = 18)
    private String updTlr;

    @Column(nullable = false, length = 23)
    private String dateTime;

}
