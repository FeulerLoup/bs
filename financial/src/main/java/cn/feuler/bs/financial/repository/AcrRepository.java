package cn.feuler.bs.financial.repository;

import cn.feuler.bs.financial.dataobject.Citacr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/20
 **/

public interface AcrRepository extends JpaRepository<Citacr, String> {

    Citacr findByAcNo(String AcNo);

    Citacr findByCiNo(String CiNo);

    Citacr findByciNoAndAcNo(String CiNo, String acNo);

    List<Citacr> findAllByAcSts(String STS);

    List<Citacr> findAllByCiNo(String CiNO);

    @Query(nativeQuery = true, value = "SELECT * FROM citacr ORDER BY ac_no DESC LIMIT 1;")
    Citacr findLastOne();
}
