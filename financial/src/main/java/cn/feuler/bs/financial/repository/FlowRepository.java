package cn.feuler.bs.financial.repository;

import cn.feuler.bs.financial.dataobject.Ddtflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/20
 **/

public interface FlowRepository extends JpaRepository<Ddtflow,Integer> {

    Ddtflow findByRecNo(Integer RecNo);

    List<Ddtflow> findAllByAcNo(String AcNo);

    List<Ddtflow> findAllByCiNo(String CiNo);

    List<Ddtflow> findAllByRecvAcNo(String RecvAcNo);

    List<Ddtflow> findAllByRecvCiNo(String RecvCiNo);

    List<Ddtflow> findAllByOperator(String Operator);


}
