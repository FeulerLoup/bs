package cn.feuler.bs.basic.service;

import cn.feuler.bs.basic.dataobject.Bptact;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

public interface ActService {

    Bptact create(Bptact bptact);

    List<Bptact> getActList(String tlrNo);

    List<Bptact> getSuActList();

}
