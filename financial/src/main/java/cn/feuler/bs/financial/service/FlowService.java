package cn.feuler.bs.financial.service;

import cn.feuler.bs.financial.dataobject.Ddtflow;
import cn.feuler.bs.financial.dto.FlowDTO;

import java.util.List;
import java.util.Map;

/**
 * Create by: Feuler
 * Date: 2019/3/20
 **/

public interface FlowService {

    Ddtflow findByRecNo(Integer RecNo);

    List<Ddtflow> findAllByAcNo(String AcNo);

    List<Ddtflow> getRecsList(String CiNo);

    List<Ddtflow> getRecList(String AcNo);

    List<Ddtflow> getAllRecList();

    List<Ddtflow> findAllByRecvAcNo(String RecvAcNo);

    List<Ddtflow> findAllByRecvCiNo(String RecvCiNo);

    List<Ddtflow> findAllByOperator(String Operator);

    Map<String,String> create(FlowDTO flowDTO);
}
