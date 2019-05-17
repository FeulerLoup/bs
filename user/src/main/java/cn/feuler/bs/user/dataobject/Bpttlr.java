package cn.feuler.bs.user.dataobject;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@ToString
@Data
@Table(name = "bpttlr")
@Entity
public class Bpttlr implements Serializable {
    @Id
    @Column(nullable = false, length = 18)
    private String tlrNo;

    @Column(nullable = false)
    private String tlrPasswd;

    @Column(nullable = false)
    private String tlrName;

    @Column(nullable = false, length = 1)
    private String lvlNo;

    @Column(nullable = false, length = 1)
    private String tlrSts;

    @Column(nullable = false, length = 18)
    private String updTlr;

    @Column(nullable = false, length = 23)
    private String dateTime;
}
