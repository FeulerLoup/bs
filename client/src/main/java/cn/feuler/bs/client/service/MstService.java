package cn.feuler.bs.client.service;

import cn.feuler.bs.client.dataobject.Citmst;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/27
 **/

public interface MstService {


    Citmst getCiInfo(String ciNo);

    String getCiNo(String ciName);

    Citmst getCiInfo(String ciNo, String ciPasswd);

    Citmst login(String ciNo, String ciPasswd);

    Citmst create(Citmst citmst);

    Citmst mod(Citmst citmst);

    String getCiName(String ciNo);

    List<Citmst> getAll();

    Citmst updateCiInfo(Citmst citmst);

    Citmst reset(String ciNo);

}
