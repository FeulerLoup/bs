package cn.feuler.bs.basic.service.impl;

import cn.feuler.bs.basic.dataobject.Bptact;
import cn.feuler.bs.basic.repository.ActRepository;
import cn.feuler.bs.basic.service.ActService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@Service
public class ActServiceImpl implements ActService {

    @Autowired
    private ActRepository actRepository;

    @Override
    public Bptact create(Bptact bptact) {
        return actRepository.saveAndFlush(bptact);
    }

    @Override
    public List<Bptact> getActList(String tlrNo) {
        return actRepository.findAllByTlrNo(tlrNo);
    }

    @Override
    public List<Bptact> getSuActList() {
        return actRepository.findAll();
    }

}
