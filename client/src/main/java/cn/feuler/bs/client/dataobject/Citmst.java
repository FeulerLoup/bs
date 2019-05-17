package cn.feuler.bs.client.dataobject;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Create by: Feuler
 * Date: 2019/3/26
 **/
@Data
@Entity
@Table(name = "citmst")
@ToString
public class Citmst implements Serializable{

    @Id

    @Column(nullable = false, length = 18)
    private String ciNo;

    @Column(nullable = false)
    private String ciName;

    @Column(nullable = false)
    private String ciPasswd;

    @Column(nullable = false)
    private String ciPhone;

    @Column(nullable = false)
    private String ciAddress;

    @Column(nullable = false, length = 1)
    private String lvlNo;

    @Column(nullable = false, length = 2)
    private String ciLvl;

    @Column(nullable = false, length = 1)
    private String ciSts;

    @Column(nullable = false, length = 18)
    private String updTlr;

    @Column(nullable = false, length = 23)
    private String dateTime;
}
