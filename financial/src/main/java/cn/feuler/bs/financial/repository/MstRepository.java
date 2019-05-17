package cn.feuler.bs.financial.repository;

        import cn.feuler.bs.financial.dataobject.Citmst;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by: Feuler
 * Date: 2019/3/27
 **/

public interface MstRepository extends JpaRepository<Citmst,String> {

    Citmst findByCiNo(String ciNo);

    Citmst findByCiNoAndCiPasswd(String ciNo,String ciPasswd);

    Citmst findByCiName(String ciName);
}
