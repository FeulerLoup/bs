package cn.feuler.bs.client.service.impl;

import cn.feuler.bs.client.dataobject.Citmst;
import cn.feuler.bs.client.enums.Sts;
import cn.feuler.bs.client.repository.MstRepository;
import cn.feuler.bs.client.service.MstService;
import cn.feuler.bs.client.utils.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Create by: Feuler
 * Date: 2019/3/27
 **/

@Service
public class MstServiceImpl implements MstService {

    @Autowired
    private MstRepository mstRepository;

    @Autowired
    private CommUtil commUtil;

    @Override
    public Citmst getCiInfo(String ciNo) {
        return mstRepository.findByCiNo(ciNo);
    }

    @Override
    public String getCiNo(String ciName) {
        return mstRepository.findByCiName(ciName).getCiNo();
    }

    @Override
    public Citmst getCiInfo(String ciNo, String ciPasswd) {
        return mstRepository.findByCiNoAndCiPasswd(ciNo, ciPasswd);
    }

    @Override
    public Citmst login(String ciNo, String ciPasswd) {
        return mstRepository.findByCiNoAndCiPasswd(ciNo, ciPasswd);
    }

    @Override
    @Transactional
    public Citmst create(Citmst citmst) {
        citmst.setCiSts("A");
        citmst.setLvlNo("1");
        citmst.setCiLvl("PT");
        citmst.setUpdTlr(citmst.getCiNo());
        citmst.setDateTime(commUtil.getDateTime());
        return mstRepository.saveAndFlush(citmst);
    }

    @Override
    @Transactional
    public Citmst mod(Citmst citmst) {
        Citmst oldInfo = mstRepository.findByCiNo(citmst.getCiNo());
        citmst.setCiSts(oldInfo.getCiSts());
        citmst.setLvlNo(oldInfo.getLvlNo());
        citmst.setCiLvl(oldInfo.getCiLvl());
        citmst.setUpdTlr(citmst.getCiNo());
        citmst.setDateTime(commUtil.getDateTime());
        return mstRepository.saveAndFlush(citmst);
    }

    @Override
    public String getCiName(String ciNo) {
        return mstRepository.findByCiNo(ciNo).getCiName();
    }

    @Override
    public List<Citmst> getAll() {
        return mstRepository.findAll();
    }

    @Override
    @Transactional
    public Citmst updateCiInfo(Citmst citmst) {
        return mstRepository.saveAndFlush(citmst);
    }

    @Override
    @Transactional
    public Citmst reset(String ciNo) {
        Citmst citmst = mstRepository.findByCiNo(ciNo);
        if (citmst != null) {
            citmst.setCiSts(Sts.RESET.getCode());
            return mstRepository.saveAndFlush(citmst);
        }
        return null;
    }


}
