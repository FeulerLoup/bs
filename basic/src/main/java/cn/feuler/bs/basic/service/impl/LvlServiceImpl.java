package cn.feuler.bs.basic.service.impl;

import cn.feuler.bs.basic.dataobject.Bptlvl;
import cn.feuler.bs.basic.repository.LvlRepository;
import cn.feuler.bs.basic.service.LvlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Service
public class LvlServiceImpl implements LvlService {

    @Autowired
    private LvlRepository lvlRepository;

    @Override
    public Bptlvl getPermission(String lvlNo) {
        return lvlRepository.findByLvlNo(lvlNo);
    }

    @Override
    public Bptlvl modPermission(Bptlvl bptlvl) {
        return lvlRepository.saveAndFlush(bptlvl);
    }

    @Override
    public List<Bptlvl> getPermissionList() {
        return lvlRepository.findAll();
    }

    @Override
    public String getNewLvlNo() {
        String lastLvlNo = lvlRepository.findLastOne().getLvlNo();

        return String.valueOf(new BigDecimal(lastLvlNo).add(BigDecimal.ONE).toString());
    }
}
