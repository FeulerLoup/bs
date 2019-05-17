package cn.feuler.bs.basic.repository;

import cn.feuler.bs.basic.dataobject.Bptlvl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Repository
public interface LvlRepository extends JpaRepository<Bptlvl, String> {

    Bptlvl findByLvlNo(String lvlNo);

    List<Bptlvl> findAll();

    @Query(nativeQuery = true, value = "SELECT * FROM bptlvl ORDER BY lvl_no DESC LIMIT 1;")
    Bptlvl findLastOne();
}
