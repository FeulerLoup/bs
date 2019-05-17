package cn.feuler.bs.user.client;

import cn.feuler.bs.user.VO.ResultVO;
import cn.feuler.bs.user.dataobject.Citacr;
import cn.feuler.bs.user.dataobject.Ddtflow;
import cn.feuler.bs.user.form.FlowForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@FeignClient(name = "financial")
public interface FinancialClient {

    @PostMapping("/getCiAcList")
    List<Citacr> getCiAcList(@RequestBody String ciNo);

    @PostMapping("/getCiAc")
    Citacr getCiAc(@RequestBody Citacr citacr);

    @PostMapping("/updateAc")
    Citacr updateAc(@RequestBody Citacr citacr);

    @GetMapping("/getAcList")
    List<Citacr> getAcList();

    @GetMapping("/getNewAcNo")
    String getNewAcNo();

    @PostMapping("/getAcInfo")
    Citacr getAcInfo(@RequestBody String acNo);

    @PostMapping("/createNewAc")
    Citacr createNewAc(@RequestBody Citacr citacr);

    @PostMapping("/CiRecList")
    List<Ddtflow> CiRecList(@RequestBody String acNo);

    @GetMapping("/getRecList")
    List<Ddtflow> getRecList();

    @PostMapping("/deposit")
    ResultVO<HashMap> deposit(@RequestBody FlowForm flowForm);

    @PostMapping("/withdrawal")
    ResultVO<HashMap> withdrawal(@RequestBody FlowForm flowForm);
}
