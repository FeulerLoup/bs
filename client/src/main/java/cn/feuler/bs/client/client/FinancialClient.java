package cn.feuler.bs.client.client;

import cn.feuler.bs.client.VO.ResultVO;
import cn.feuler.bs.client.dataobject.Citacr;
import cn.feuler.bs.client.dataobject.Ddtflow;
import cn.feuler.bs.client.form.FlowForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/27
 **/

@FeignClient(name = "financial")
public interface FinancialClient {


    @PostMapping("getCiAcList")
    List<Citacr> getCiAcList(@RequestBody String ciNo);

    @PostMapping("CiRecsList")
    List<Ddtflow> CiRecsList(@RequestBody String ciNo);

    @PostMapping("CiRecList")
    List<Ddtflow> CiRecList(@RequestBody String acNo);

    @PostMapping("AcBal")
    BigDecimal AcBal(@RequestBody String acNo);

    @PostMapping("AcCiNo")
    String AcCiNo(@RequestBody String acNo);

    @PostMapping("AcCcy")
    String AcCcy(@RequestBody String acNo);

    @PostMapping("transfer")
    ResultVO<HashMap> transfer(@RequestBody FlowForm flowForm);

    @PostMapping("getAcInfo")
    Citacr getAcInfo(@RequestBody String acNo);

    @PostMapping("freezeAc")
    Citacr freezeAc(@RequestBody Citacr citacr);
}
