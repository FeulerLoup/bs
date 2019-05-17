package cn.feuler.bs.user.service.impl;

import cn.feuler.bs.user.client.BasicClient;
import cn.feuler.bs.user.client.CiClient;
import cn.feuler.bs.user.client.FinancialClient;
import cn.feuler.bs.user.dataobject.Bptact;
import cn.feuler.bs.user.dataobject.Bpttlr;
import cn.feuler.bs.user.enums.ActSts;
import cn.feuler.bs.user.enums.ActType;
import cn.feuler.bs.user.enums.Sts;
import cn.feuler.bs.user.repository.TlrRepository;
import cn.feuler.bs.user.service.TlrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Service
public class TlrServiceImpl implements TlrService {

    @Autowired
    private TlrRepository tlrRepository;

    @Autowired
    private BasicClient basicClient;

    @Autowired
    private CiClient ciClient;

    @Autowired
    private FinancialClient financialClient;


    @Override
    public List<Bpttlr> findByTLR() {
        return tlrRepository.findAllByLvlNo("2");
    }

    @Override
    public List<Bpttlr> findByLevel(List<String> levelList) {
        return tlrRepository.findAllByLvlNoIn(levelList);
    }

    @Override
    public Bpttlr tlrLogin(String tlrNo, String tlrPasswd) {
        Bpttlr result = tlrRepository.findByTlrNoAndTlrPasswd(tlrNo, tlrPasswd);
        if (result != null && (result.getTlrSts().equals(Sts.NORMAL.getCode()))) {
            basicClient.createAct(new Bptact(tlrNo, ActType.LOGIN.getAct(), ActSts.NORMAL.getCode()));
            return result;
        } else if (result != null && result.getTlrSts().equals(Sts.FREEZE.getCode())) {
            basicClient.createAct(new Bptact(tlrNo, ActType.LOGIN.getAct(), ActSts.ERROR.getCode()));
            return result;
        } else if (result != null && result.getTlrSts().equals(Sts.CLOSED.getCode())) {
            basicClient.createAct(new Bptact(tlrNo, ActType.LOGIN.getAct(), ActSts.ERROR.getCode()));
            return result;
        } else {
            basicClient.createAct(new Bptact(tlrNo, ActType.LOGIN.getAct(), ActSts.ERROR.getCode()));
            return null;
        }
    }

    @Override
    public Bpttlr getTlrInfo(String TlrNo) {
        return tlrRepository.findByTlrNo(TlrNo);
    }

    @Override
    @Transactional
    public Bpttlr mod(Bpttlr bpttlr) {
        return tlrRepository.saveAndFlush(bpttlr);
    }

    @Override
    public List<Bpttlr> getTlrList() {
        return tlrRepository.findAll();
    }
}
