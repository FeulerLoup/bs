package cn.feuler.bs.basic.repository;

import cn.feuler.bs.basic.dataobject.Bptact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

public interface ActRepository extends JpaRepository<Bptact,Integer> {

    List<Bptact> findAllByTlrNo(String tlrNo);
}
