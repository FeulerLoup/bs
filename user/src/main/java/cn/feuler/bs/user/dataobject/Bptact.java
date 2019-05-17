package cn.feuler.bs.user.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
public class Bptact implements Serializable {

    private String recNo;

    private String tlrNo;

    private String actType;

    private String actSts;

    private String dateTime;

    public Bptact(String tlrNo,String actType,String actSts){
        this.tlrNo=tlrNo;
        this.actType=actType;
        this.actSts=actSts;
    }
}
