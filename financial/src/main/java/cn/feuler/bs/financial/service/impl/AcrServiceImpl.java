package cn.feuler.bs.financial.service.impl;

import cn.feuler.bs.client.enums.Sts;
import cn.feuler.bs.financial.dataobject.Citacr;
import cn.feuler.bs.financial.repository.AcrRepository;
import cn.feuler.bs.financial.service.AcrService;
import cn.feuler.bs.financial.utils.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/20
 **/

@Service
public class AcrServiceImpl implements AcrService {

    @Autowired
    private AcrRepository acrRepository;

    @Autowired
    private CommUtil commUtil;


    @Override
    public Citacr getAcBal(String acNo) {
        return acrRepository.findByAcNo(acNo);
    }

    @Override
    public List<Citacr> getAcList(String CiNo) {
        return acrRepository.findAllByCiNo(CiNo);
    }

    @Override
    public String getAcCiNo(String acNo) {
        Citacr citacr = acrRepository.findByAcNo(acNo);
        if (citacr != null) {
            return citacr.getCiNo();
        }
        return null;
    }

    @Override
    public String getAcCcy(String acNo) {
        Citacr citacr = acrRepository.findByAcNo(acNo);
        if (citacr != null) {
            return citacr.getCcy();
        }
        return null;
    }

    @Override
    public Citacr getCiAc(String ciNo, String acNo) {
        return acrRepository.findByciNoAndAcNo(ciNo, acNo);
    }

    @Override
    @Transactional
    public Citacr updateAc(Citacr citacr) {
        return acrRepository.saveAndFlush(citacr);
    }

    @Override
    @Transactional
    public Citacr create(Citacr citacr) {
        return acrRepository.saveAndFlush(citacr);
    }

    @Override
    public List<Citacr> getAcList() {
        return acrRepository.findAll();
    }

    @Override
    public String getNewAcNo() {
        String lastAcNo = acrRepository.findLastOne().getAcNo();

        return String.valueOf(new BigDecimal(lastAcNo).add(BigDecimal.ONE).toString());
    }

    @Override
    public Citacr getAcInfo(String acNo) {
        return acrRepository.findByAcNo(acNo);
    }

    @Override
    @Transactional
    public Citacr freezeAc(Citacr citacr) {
        Citacr ac=acrRepository.findByAcNo(citacr.getAcNo());
        ac.setAcSts(Sts.FREEZE.getCode());
        ac.setUpdTlr(citacr.getCiNo());
        ac.setDateTime(commUtil.getDateTime());
        return acrRepository.saveAndFlush(ac);
    }
}
