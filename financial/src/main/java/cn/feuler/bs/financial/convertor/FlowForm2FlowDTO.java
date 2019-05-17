package cn.feuler.bs.financial.convertor;

import cn.feuler.bs.financial.dto.FlowDTO;
import cn.feuler.bs.financial.form.FlowForm;

/**
 * Create by: Feuler
 * Date: 2019/3/24
 **/

public class FlowForm2FlowDTO {

    public static FlowDTO convert(FlowForm flowForm) {
        FlowDTO flowDTO = new FlowDTO();
        flowDTO.setAct(flowForm.getAct());
        flowDTO.setCiNo(flowForm.getCiNo());
        flowDTO.setAcNo(flowForm.getAcNo());
        flowDTO.setRecvCiNo(flowForm.getRecvCiNo());
        flowDTO.setRecvAcNo(flowForm.getRecvAcNo());
        flowDTO.setAmt(flowForm.getAmt());
        flowDTO.setOperator(flowForm.getOperator());
        return flowDTO;
    }
}
