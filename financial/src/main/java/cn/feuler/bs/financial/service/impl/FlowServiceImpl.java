package cn.feuler.bs.financial.service.impl;

import cn.feuler.bs.financial.dataobject.Citacr;
import cn.feuler.bs.financial.dataobject.Ddtflow;
import cn.feuler.bs.financial.dto.FlowDTO;
import cn.feuler.bs.financial.repository.AcrRepository;
import cn.feuler.bs.financial.repository.FlowRepository;
import cn.feuler.bs.financial.repository.MstRepository;
import cn.feuler.bs.financial.service.FlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by: Feuler
 * Date: 2019/3/20
 **/

@Service
public class FlowServiceImpl implements FlowService {

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private AcrRepository acrRepository;

    @Autowired
    private MstRepository mstRepository;

    @Override
    public Ddtflow findByRecNo(Integer RecNo) {
        return flowRepository.findByRecNo(RecNo);
    }

    @Override
    public List<Ddtflow> findAllByAcNo(String AcNo) {
        return flowRepository.findAllByAcNo(AcNo);
    }

    @Override
    public List<Ddtflow> getRecsList(String CiNo) {
        return flowRepository.findAllByCiNo(CiNo);
    }

    @Override
    public List<Ddtflow> getRecList(String AcNo) {
        return flowRepository.findAllByAcNo(AcNo);
    }

    @Override
    public List<Ddtflow> getAllRecList() {
        return flowRepository.findAll();
    }

    @Override
    public List<Ddtflow> findAllByRecvAcNo(String RecvAcNo) {
        return flowRepository.findAllByRecvAcNo(RecvAcNo);
    }

    @Override
    public List<Ddtflow> findAllByRecvCiNo(String RecvCiNo) {
        return flowRepository.findAllByRecvCiNo(RecvCiNo);
    }

    @Override
    public List<Ddtflow> findAllByOperator(String Operator) {
        return flowRepository.findAllByOperator(Operator);
    }

    @Transactional
    @Override
    public Map<String, String> create(FlowDTO flowDTO) {

        Map<String, String> map = new HashMap<>();

        Ddtflow ddtflow = new Ddtflow();
        BeanUtils.copyProperties(flowDTO, ddtflow);

        switch (ddtflow.getAct()) {
            case "D": {
                if (mstRepository.findByCiNo(ddtflow.getCiNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "客户不存在");
                    return map;
                } else if (!mstRepository.findByCiNo(ddtflow.getCiNo()).getCiSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "客户状态为非正常状态");
                    return map;
                } else if (acrRepository.findByAcNo(ddtflow.getAcNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "帐号不存在");
                    return map;
                } else if (!acrRepository.findByAcNo(ddtflow.getAcNo()).getAcSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "帐号状态为非正常状态");
                    return map;
                }
                ddtflow.setRecvCiNo("");
                ddtflow.setRecvAcNo("");
                break;
            }
            case "W": {
                if (mstRepository.findByCiNo(ddtflow.getCiNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "客户不存在");
                    return map;
                } else if (!mstRepository.findByCiNo(ddtflow.getCiNo()).getCiSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "客户状态为非正常状态");
                    return map;
                } else if (acrRepository.findByAcNo(ddtflow.getAcNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "帐号不存在");
                    return map;
                } else if (!acrRepository.findByAcNo(ddtflow.getAcNo()).getAcSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "帐号状态为非正常状态");
                    return map;
                }else if (ddtflow.getAmt().compareTo(acrRepository.findByAcNo(ddtflow.getAcNo()).getAcBal()) > 0) {
                    map.put("code", "-1");
                    map.put("msg", "账户余额不足");
                    return map;
                }
                ddtflow.setRecvCiNo("");
                ddtflow.setRecvAcNo("");
                break;
            }
            case "T": {
                if (mstRepository.findByCiNo(ddtflow.getCiNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "转出客户不存在");
                    return map;
                } else if (!mstRepository.findByCiNo(ddtflow.getCiNo()).getCiSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "转出客户状态为非正常状态");
                    return map;
                } else if (acrRepository.findByAcNo(ddtflow.getAcNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "转出帐号不存在");
                    return map;
                } else if (!acrRepository.findByAcNo(ddtflow.getAcNo()).getAcSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "转出帐号状态为非正常状态");
                    return map;
                } else if (mstRepository.findByCiNo(ddtflow.getRecvCiNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "转入客户不存在");
                    return map;
                } else if (!mstRepository.findByCiNo(ddtflow.getRecvCiNo()).getCiSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "转入客户状态为非正常状态");
                    return map;
                } else if (acrRepository.findByAcNo(ddtflow.getRecvAcNo()) == null) {
                    map.put("code", "-1");
                    map.put("msg", "转入帐号不存在");
                    return map;
                } else if (!acrRepository.findByAcNo(ddtflow.getRecvAcNo()).getAcSts().equals("N")) {
                    map.put("code", "-1");
                    map.put("msg", "转入帐号状态为非正常状态");
                    return map;
                } else if (ddtflow.getAmt().compareTo(acrRepository.findByAcNo(ddtflow.getAcNo()).getAcBal()) > 0) {
                    map.put("code", "-1");
                    map.put("msg", "账户余额不足");
                    return map;
                }
                break;
            }
        }

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String datefmt = date.format(new Date());
        ddtflow.setDateTime(datefmt);

        flowRepository.saveAndFlush(ddtflow);

        switch (ddtflow.getAct()) {
            case "D": {
                Citacr citacr = acrRepository.findByAcNo(ddtflow.getAcNo());
                citacr.setAcBal(citacr.getAcBal().add(ddtflow.getAmt()));
                acrRepository.save(citacr);
                break;
            }
            case "W": {
                Citacr citacr = acrRepository.findByAcNo(ddtflow.getAcNo());
                citacr.setAcBal(citacr.getAcBal().subtract(ddtflow.getAmt()));
                acrRepository.save(citacr);
                break;
            }
            case "T": {
                Citacr transout = acrRepository.findByAcNo(ddtflow.getAcNo());
                transout.setAcBal(transout.getAcBal().subtract(ddtflow.getAmt()));
                acrRepository.save(transout);
                Citacr transin = acrRepository.findByAcNo(ddtflow.getRecvAcNo());
                transin.setAcBal(transin.getAcBal().add(ddtflow.getAmt()));
                acrRepository.save(transin);
                break;
            }
        }

        map.put("code", "0");
        map.put("msg", ddtflow.getRecNo().toString());
        return map;
    }
}
