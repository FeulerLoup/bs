package cn.feuler.bs.financial.controller;

import cn.feuler.bs.financial.VO.ResultVO;
import cn.feuler.bs.financial.convertor.FlowForm2FlowDTO;
import cn.feuler.bs.financial.dataobject.Citacr;
import cn.feuler.bs.financial.dataobject.Ddtflow;
import cn.feuler.bs.financial.dto.FlowDTO;
import cn.feuler.bs.financial.exception.FlowException;
import cn.feuler.bs.financial.form.FlowForm;
import cn.feuler.bs.financial.service.AcrService;
import cn.feuler.bs.financial.service.FlowService;
import cn.feuler.bs.financial.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by: Feuler
 * Date: 2019/3/24
 **/

@RestController
@Slf4j
public class FinancialController {

    @Autowired
    private AcrService acrService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping({"/deposit", "/withdrawal", "/transfer"})
    public ResultVO<Map<String, String>> create(@Valid @RequestBody FlowForm flowForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("操作失败，FlowForm={}", flowForm.toString());
            throw new FlowException(1, bindingResult.getFieldError().getDefaultMessage());
        }

        System.out.println(request.getServletPath().split("/"));
        if (!request.getServletPath().split("/")[1].toUpperCase().substring(0, 1).equals(flowForm.getAct())) {
            Map<String, String> map = new HashMap<>();
            map.put("result", "操作非法");
            return ResultVOUtil.fail(map);
        }

        FlowDTO flowDTO = FlowForm2FlowDTO.convert(flowForm);
        Map<String, String> result = flowService.create(flowDTO);

        if (result.get("code").equals("-1")) {
            Map<String, String> map = new HashMap<>();
            map.put("result", result.get("msg"));
            return ResultVOUtil.fail(map);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("result", result.get("msg"));
            return ResultVOUtil.success(map);
        }
    }

    @PostMapping("/AcBal")
    public BigDecimal AcBal(@RequestBody String acNo) {
        return acrService.getAcBal(acNo).getAcBal();
    }

    @PostMapping("/AcCiNo")
    public String AcCiNo(@RequestBody String acNo) {
        return acrService.getAcCiNo(acNo);
    }

    @PostMapping("/AcCcy")
    public String AcCcy(@RequestBody String acNo) {
        return acrService.getAcCcy(acNo);
    }

    @PostMapping("/CiAcList")
    public List<Citacr> CiAcList(@RequestBody String ciNo) {
        return acrService.getAcList(ciNo);
    }

    @PostMapping("/CiRecsList")
    public List<Ddtflow> CiRecsList(@RequestBody String ciNo) {
        return flowService.getRecsList(ciNo);
    }

    @PostMapping("/CiRecList")
    public List<Ddtflow> CiRecList(@RequestBody String acNo) {
        return flowService.getRecList(acNo);
    }

    @PostMapping("/getCiAcList")
    public List<Citacr> getCiAcList(@RequestBody String ciNo) {
        return acrService.getAcList(ciNo);
    }

    @PostMapping("/getCiAc")
    public Citacr getCiAc(@RequestBody Citacr citacr) {
        return acrService.getCiAc(citacr.getCiNo(), citacr.getAcNo());
    }

    @PostMapping("/updateAc")
    public Citacr updateAc(@RequestBody Citacr citacr) {
        return acrService.updateAc(citacr);
    }

    @GetMapping("/getAcList")
    public List<Citacr> getAcList() {
        return acrService.getAcList();
    }

    @GetMapping("/getNewAcNo")
    public String getNewAcNo() {
        return acrService.getNewAcNo();
    }

    @PostMapping("/getAcInfo")
    public Citacr getAcInfo(@RequestBody String acNo) {
        return acrService.getAcInfo(acNo);
    }

    @PostMapping("/createNewAc")
    public Citacr createNewAc(@RequestBody Citacr citacr) {
        return acrService.create(citacr);
    }

    @PostMapping("/freezeAc")
    public Citacr freezeAc(@RequestBody Citacr citacr) {
        return acrService.freezeAc(citacr);
    }

    @GetMapping("/getRecList")
    public List<Ddtflow> getRecList() {
        return flowService.getAllRecList();
    }
}