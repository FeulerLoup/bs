package cn.feuler.bs.basic.dataobject;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
@Table(name = "bptlvl")
@Entity
public class Bptlvl {
    @Id
    @Column(nullable = false, length = 10)
    private String lvlNo;

    @Column(nullable = false, length = 1)
    private String ciFinancial;

    @Column(nullable = false, length = 1)
    private String ciInq;

    @Column(nullable = false, length = 1)
    private String ciMod;

    @Column(nullable = false, length = 1)
    private String tlrAp;

    @Column(nullable = false, length = 1)
    private String tlrMod;

    @Column(nullable = false, length = 1)
    private String tlrInq;

    @Column(nullable = false, length = 1)
    private String suInq;

    @Column(nullable = false, length = 1)
    private String suMod;

    @Column(nullable = false, length = 1)
    private String suAp;

    @Column(nullable = false, length = 18)
    private String updTlr;

    @Column(nullable = false, length = 23)
    private String dateTime;

}
