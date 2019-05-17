//package cn.feuler.bs.financial.server;
//
//import cn.feuler.bs.financial.dataobject.Citacr;
//import cn.feuler.bs.financial.service.AcrService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * Create by: Feuler
// * Date: 2019/4/9
// **/
//
//@RestController
//@RequestMapping("/financialserver")
//public class FinancialServer {
//
//    @Autowired
//    private AcrService acrService;
//
//    @PostMapping("/getCiAcList")
//    public List<Citacr> getCiAcList(@RequestBody String ciNo) {
//        return acrService.getAcList(ciNo);
//    }
//
//    @PostMapping("/getCiAc")
//    public Citacr getCiAc(@RequestBody Citacr citacr) {
//        return acrService.getCiAc(citacr.getCiNo(), citacr.getAcNo());
//    }
//
//    @PostMapping("/updateAc")
//    public Citacr updateAc(@RequestBody Citacr citacr) {
//        return acrService.updateAc(citacr);
//    }
//
//    @GetMapping("/getAcList")
//    public List<Citacr> getAcList() {
//        return acrService.getAcList();
//    }
//
//    @GetMapping("/getNewAcNo")
//    public String getNewAcNo() {
//        return acrService.getNewAcNo();
//    }
//
//    @PostMapping("/getAcInfo")
//    public Citacr getAcInfo(@RequestBody String acNo) {
//        return acrService.getAcInfo(acNo);
//    }
//
//    @PostMapping("/createNewAc")
//    public Citacr createNewAc(@RequestBody Citacr citacr) {
//        return acrService.create(citacr);
//    }
//
//    @PostMapping("/freezeAc")
//    public Citacr freezeAc(@RequestBody Citacr citacr) {
//        return acrService.freezeAc(citacr);
//    }
//
//}
