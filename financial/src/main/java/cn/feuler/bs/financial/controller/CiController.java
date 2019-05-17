//package cn.feuler.bs.financial.controller;
//
//import cn.feuler.bs.financial.dataobject.Citmst;
//import cn.feuler.bs.financial.service.MstService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//
///**
// * Create by: Feuler
// * Date: 2019/3/27
// **/
//
//@RestController
//public class CiController {
//
//    @Autowired
//    private MstService mstService;
//
//
//    @PostMapping("/CiInfo")
//    public Citmst CiInfo(@RequestBody String ciNo) {
//        return mstService.getCiInfo(ciNo);
//    }
//
//
//
//    @PostMapping("/CiRecvCiNo")
//    public String getRecvCiNo(@RequestBody String recvName){
//        return mstService.getCiNo(recvName).toString();
//    }
//
//    @PostMapping("/CiLogin")
//    public Citmst CiLogin(@RequestBody Citmst citmst) {
//        return mstService.getCiInfo(citmst.getCiNo(), citmst.getCiPasswd());
//    }
//
//    @PostMapping("/CiRegister")
//    public Citmst Register(@RequestBody Citmst regInfo) {
//        if (mstService.getCiInfo(regInfo.getCiNo()) != null) {
//            return null;
//        } else {
//            return mstService.create(regInfo);
//        }
//    }
//
//    @PostMapping("/CiModInfo")
//    public Citmst modInfo(@RequestBody Citmst newInfo) {
//        if (mstService.getCiInfo(newInfo.getCiNo()) == null) {
//            return null;
//        } else {
//            return mstService.mod(newInfo);
//        }
//    }
//
//
//}
