package cn.feuler.bs.basic.service;


import cn.feuler.bs.basic.dataobject.Bptlvl;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

public interface LvlService {

    Bptlvl getPermission(String lvlNo);

    Bptlvl modPermission(Bptlvl bptlvl);

    List<Bptlvl> getPermissionList();

    String getNewLvlNo();
}
