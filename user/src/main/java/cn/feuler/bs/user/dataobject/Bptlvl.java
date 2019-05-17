package cn.feuler.bs.user.dataobject;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
@ToString
public class Bptlvl implements Serializable {

    private String lvlNo;

    private String ciFinancial;

    private String ciInq;

    private String ciMod;

    private String tlrAp;

    private String tlrMod;

    private String tlrInq;

    private String suInq;

    private String suMod;

    private String suAp;

    private String updTlr;

    private String dateTime;

}
