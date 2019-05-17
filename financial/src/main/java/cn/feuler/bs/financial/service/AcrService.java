package cn.feuler.bs.financial.service;

import cn.feuler.bs.financial.dataobject.Citacr;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/20
 **/

public interface AcrService {

    Citacr getAcBal(String acNo);

    List<Citacr> getAcList(String CiNo);

    String getAcCiNo(String acNo);

    String getAcCcy(String acNo);

    Citacr getCiAc(String ciNo,String acNo);

    Citacr updateAc(Citacr citacr);

    Citacr create(Citacr citacr);

    List<Citacr> getAcList();

    String getNewAcNo();

    Citacr getAcInfo(String acNo);

    Citacr freezeAc(Citacr citacr);

}
