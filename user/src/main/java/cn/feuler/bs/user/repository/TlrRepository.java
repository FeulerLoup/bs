package cn.feuler.bs.user.repository;

import cn.feuler.bs.user.dataobject.Bpttlr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Repository
public interface TlrRepository extends JpaRepository<Bpttlr,String> {

    Bpttlr findByTlrNo(String tlrNo);

    List<Bpttlr> findAllByLvlNoIn(List<String> lvlNo);

    List<Bpttlr> findAllByLvlNo(String lvlNo);

    Bpttlr findByTlrNoAndTlrPasswd(String TlrNo, String TlrPasswd);
}
