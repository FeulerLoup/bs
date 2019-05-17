package cn.feuler.bs.financial.dataobject;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
@Table(name = "ddtflow")
@Entity
public class Ddtflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recNo;

    @Column(nullable = false, length = 1)
    private String act;

    @Column(nullable = false, length = 18)
    private String ciNo;

    @Column(nullable = false, length = 10)
    private String acNo;

    @Column(nullable = false, length = 18)
    private String recvCiNo;

    @Column(nullable = false, length = 10)
    private String recvAcNo;

    @Column(nullable = false)
    private BigDecimal amt;

    @Column(nullable = false, length = 18)
    private String operator;

    @Column(nullable = false, length = 23)
    private String dateTime;

}
