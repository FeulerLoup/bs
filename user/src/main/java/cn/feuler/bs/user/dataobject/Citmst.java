package cn.feuler.bs.user.dataobject;

import lombok.Data;

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
public class Citmst implements Serializable {

    private String ciNo;

    private String ciName;

    private String ciPasswd;

    private String ciPhone;

    private String ciAddress;

    private String lvlNo;

    private String ciLvl;

    private String ciSts;

    private String updTlr;

    private String dateTime;
}
