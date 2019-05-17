package cn.feuler.bs.basic.VO;

import lombok.Data;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
public class PermissionVO {

    private String ciOpenAc;

    private String ciInq;

    private String ciDeposit;

    private String ciWithdrawal;

    private String ciTransfer;

    private String tlrInq;

    private String suInq;

    private String suMang;

    private String suAp;

    private String suBrCtrl;
}
