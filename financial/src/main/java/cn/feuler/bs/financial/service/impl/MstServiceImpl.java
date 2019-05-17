//package cn.feuler.bs.financial.service.impl;
//
//import cn.feuler.bs.financial.dataobject.Citmst;
//import cn.feuler.bs.financial.repository.MstRepository;
//import cn.feuler.bs.financial.service.MstService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Create by: Feuler
// * Date: 2019/3/27
// **/
//
//@Service
//public class MstServiceImpl implements MstService {
//
//    @Autowired
//    private MstRepository mstRepository;
//
//    @Override
//    public Citmst getCiInfo(String ciNo) {
//        return mstRepository.findByCiNo(ciNo);
//    }
//
//    @Override
//    public Citmst getCiNo(String ciName) {
//        return mstRepository.findByCiName(ciName);
//    }
//
//    @Override
//    public Citmst getCiInfo(String ciNo, String ciPasswd) {
//        return mstRepository.findByCiNoAndCiPasswd(ciNo, ciPasswd);
//    }
//
//    @Override
//    public Citmst create(Citmst citmst) {
//        citmst.setCiSts("A");
//        citmst.setLvl("1");
//        citmst.setCiLvl("PT");
//        citmst.setUpdTlr(citmst.getCiNo());
//        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        String datefmt = date.format(new Date());
//        citmst.setDateTime(datefmt);
//        return mstRepository.saveAndFlush(citmst);
//    }
//
//    @Override
//    public Citmst mod(Citmst citmst) {
//        Citmst oldInfo = mstRepository.findByCiNo(citmst.getCiNo());
//        citmst.setCiSts(oldInfo.getCiSts());
//        citmst.setLvl(oldInfo.getLvl());
//        citmst.setCiLvl(oldInfo.getCiLvl());
//        citmst.setUpdTlr(citmst.getCiNo());
//        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        String datefmt = date.format(new Date());
//        citmst.setDateTime(datefmt);
//        return mstRepository.saveAndFlush(citmst);
//    }
//
//
//}
