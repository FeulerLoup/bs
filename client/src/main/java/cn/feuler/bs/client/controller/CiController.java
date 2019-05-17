package cn.feuler.bs.client.controller;

import cn.feuler.bs.client.dataobject.Bptlvl;
import cn.feuler.bs.client.dataobject.Citacr;
import cn.feuler.bs.client.dataobject.Ddtflow;
import cn.feuler.bs.client.VO.ResultVO;
import cn.feuler.bs.client.client.BasicClient;
import cn.feuler.bs.client.client.FinancialClient;
import cn.feuler.bs.client.dataobject.Citmst;
import cn.feuler.bs.client.enums.Sts;
import cn.feuler.bs.client.form.FlowForm;
import cn.feuler.bs.client.service.MstService;
import cn.feuler.bs.client.utils.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;


/**
 * Create by: Feuler
 * Date: 2019/4/1
 **/

@Controller
public class CiController {

    @Autowired
    private FinancialClient financialClient;

    @Autowired
    private BasicClient basicClient;

    @Autowired
    private MstService mstService;

    @Autowired
    private CommUtil commUtil;


    @GetMapping("/index")
    public String index(ModelMap map, HttpSession httpSession) {

        Citmst citmst = (Citmst) httpSession.getAttribute("client");

        Bptlvl level = basicClient.getPermission(citmst.getLvlNo());
        httpSession.setAttribute("level", level);

        List<Citacr> acList = financialClient.getCiAcList(citmst.getCiNo());
        httpSession.setAttribute("acList", acList);


        return "index";
    }

    @GetMapping("/flows")
    public String flows(ModelMap map, HttpSession httpSession) {

        Citmst citmst = (Citmst) httpSession.getAttribute("client");

        List<Ddtflow> recsList = financialClient.CiRecsList(citmst.getCiNo());
        httpSession.setAttribute("recList", recsList);

        httpSession.setAttribute("dateTime", commUtil.getDateTime());
        return "flow";
    }

    @PostMapping("/flow")
    public String flow(ModelMap map,
                       HttpSession httpSession,
                       @RequestParam("acNo") String acNo) {

        List<Ddtflow> recList = financialClient.CiRecList(acNo);
        httpSession.setAttribute("recList", recList);

        httpSession.setAttribute("dateTime", commUtil.getDateTime());
        return "flow";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/forgot")
    public String forgot() {
        return "forgot";
    }

    @PostMapping("/doRegister")
    public String doRegister(ModelMap map,
                             HttpSession httpSession,
                             @RequestParam("ciNo") String ciNo,
                             @RequestParam("ciPasswd") String ciPasswd,
                             @RequestParam("ciPasswdConfirm") String ciPasswdConfirm,
                             @RequestParam("ciAddress") String ciAddress,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("ciPhone") String ciPhone) {
        if (!ciPasswd.equals(ciPasswdConfirm)) {
            map.addAttribute("msg", "注册失败，密码不一致");
            map.addAttribute("uri", "register");
            return "jump";
        }
        Citmst regInfo = new Citmst();
        regInfo.setCiNo(ciNo);
        regInfo.setCiPasswd(commUtil.md5Hex(ciPasswd));
        regInfo.setCiAddress(ciAddress);
        regInfo.setCiName(new StringBuffer(firstName).append(lastName).toString());
        regInfo.setCiPhone(ciPhone);
        regInfo.setDateTime(commUtil.getDateTime());

        if (mstService.getCiInfo(regInfo.getCiNo()) != null) {
            map.addAttribute("msg", "注册失败，ID已存在");
            map.addAttribute("uri", "register");
            return "jump";
        } else {
            Citmst result = mstService.create(regInfo);
            if (result != null) {
                map.addAttribute("msg", "注册成功!欢迎，" + result.getCiName());
                map.addAttribute("uri", "index");
                httpSession.setAttribute("client", result);
                return "jump";
            } else {
                map.addAttribute("msg", "注册失败，请联系管理员");
                map.addAttribute("uri", "register");
                return "jump";
            }
        }
    }

    @GetMapping("/login")
    public String login(HttpSession httpSession) {
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(ModelMap map,
                          HttpSession httpSession,
                          @RequestParam("ciNo") String ciNo,
                          @RequestParam("ciPasswd") String ciPasswd) {
        Citmst citmst = new Citmst();
        citmst.setCiNo(ciNo);
        citmst.setCiPasswd(commUtil.md5Hex(ciPasswd));
        Citmst result = mstService.login(citmst.getCiNo(), citmst.getCiPasswd());
        if (result != null && (result.getCiSts().equals("N") || result.getCiSts().equals("A"))) {
            map.addAttribute("msg", new StringBuffer("欢迎登陆，").append(result.getCiName()));
            map.addAttribute("uri", "index");
            httpSession.setAttribute("client", result);
            return "jump";
        } else if (result != null && result.getCiSts().equals("C")) {
            map.addAttribute("msg", "登陆失败，帐号已注销");
            map.addAttribute("uri", "login");
            return "jump";
        } else if (result != null && result.getCiSts().equals("R")) {
            map.addAttribute("msg", "登陆失败，账号申请重置密码中");
            map.addAttribute("uri", "login");
            return "jump";
        } else {
            map.addAttribute("msg", "登陆失败，帐号或密码错误");
            map.addAttribute("uri", "login");
            return "jump";
        }
    }

    @GetMapping("/logout")
    public String logout(ModelMap map, HttpSession httpSession) {

        httpSession.invalidate();
        map.addAttribute("msg", "已成功登出");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/reset")
    public String reset(ModelMap map, HttpSession httpSession, @RequestParam("ciNo") String ciNo) {
        if (mstService.reset(ciNo) != null) {
            map.addAttribute("msg", "成功通知管理员，请耐心等待处理");
            map.addAttribute("uri", "login");
            return "jump";
        }
        map.addAttribute("msg", "申请失败，用户不存在");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/info")
    public String info(ModelMap map, HttpSession httpSession) {
        return "info";
    }

    @PostMapping("/modInfo")
    public String modInfo(ModelMap map,
                          HttpSession httpSession,
                          @RequestParam("ciNo") String ciNo,
                          @RequestParam("ciPasswd") String ciPasswd,
                          @RequestParam("ciPasswdConfirm") String ciPasswdConfirm,
                          @RequestParam("ciAddress") String ciAddress,
                          @RequestParam("ciName") String ciName,
                          @RequestParam("ciPhone") String ciPhone) {

        if ((ciPasswd == null && ciPasswdConfirm != null)
                || (ciPasswd != null && ciPasswdConfirm == null)
                || (ciPasswd != null && ciPasswdConfirm != null && !ciPasswd.equals(ciPasswdConfirm))) {
            map.addAttribute("msg", "保存失败，密码不一致");
            map.addAttribute("uri", "info");
            return "jump";
        }

        Citmst ondInfo = (Citmst) httpSession.getAttribute("client");

        Citmst newInfo = new Citmst();
        newInfo.setCiNo(ciNo);
        newInfo.setCiAddress(ciAddress);
        newInfo.setCiName(ciName);
        newInfo.setCiPhone(ciPhone);
        if ((ciPasswd == null && ciPasswdConfirm == null) || (ciPasswd.equals("") && ciPasswdConfirm.equals(""))) {
            newInfo.setCiPasswd(ondInfo.getCiPasswd());
        } else {
            newInfo.setCiPasswd(commUtil.md5Hex(ciPasswd));
        }

        if (mstService.getCiInfo(newInfo.getCiNo()) == null) {
            map.addAttribute("msg", "修改失败，用户不存在");
            map.addAttribute("uri", "info");
            return "jump";
        } else {
            Citmst result = mstService.mod(newInfo);
            if (result != null) {
                map.addAttribute("msg", "修改成功,正在刷新");
                map.addAttribute("uri", "info");
                httpSession.setAttribute("client", result);
                return "jump";
            } else {
                map.addAttribute("msg", "修改失败，请联系管理员");
                map.addAttribute("uri", "info");
                return "jump";
            }
        }
    }

    @GetMapping("/trans")
    public String trans(ModelMap map, HttpSession httpSession) {
        return "transfer";
    }

    @PostMapping("/getAcBal")
    public String getAcBal(ModelMap map, HttpSession httpSession, @RequestParam("acNo") String acNo) {
        BigDecimal acBal = financialClient.AcBal(acNo);
        map.addAttribute("acBal", acBal);
        return "transfer::divAcBal";
    }

    @PostMapping("/transfer")
    public String transfer(ModelMap map,
                           HttpSession httpSession,
                           @RequestParam("recvName") String recvName,
                           @RequestParam("recvAcNo") String recvAcNo,
                           @RequestParam("acNo") String acNo,
                           @RequestParam("amt") String amt) {

        if (acNo.equals(recvAcNo)) {
            map.addAttribute("msg", "转账失败，转出账户与转入账户为同一账户");
            map.addAttribute("uri", "trans");
            return "jump";
        }
        String recvCiNo = financialClient.AcCiNo((recvAcNo));
        if (recvCiNo == null) {
            map.addAttribute("msg", "转账失败，账户不存在");
            map.addAttribute("uri", "trans");
            return "jump";
        }
        if (!mstService.getCiName(recvCiNo).equals(recvName)) {
            map.addAttribute("msg", "转账失败，收款人姓名与账户不匹配");
            map.addAttribute("uri", "trans");
            return "jump";
        }
        if (!financialClient.AcCcy(acNo).equals(financialClient.AcCcy(recvAcNo))) {
            map.addAttribute("msg", "转账失败，账户币种不一致");
            map.addAttribute("uri", "trans");
            return "jump";
        }

        Citmst operator = (Citmst) httpSession.getAttribute("client");

        BigDecimal Amt;
        try {
            Amt = new BigDecimal(amt);
        } catch (Exception e) {
            map.addAttribute("msg", "操作失败，金额异常");
            map.addAttribute("uri", "trans");
            return "jump";
        }

        FlowForm flowForm = new FlowForm();
        flowForm.setAct("T");
        flowForm.setCiNo(operator.getCiNo());
        flowForm.setAcNo(acNo);
        flowForm.setRecvCiNo(recvCiNo);
        flowForm.setRecvAcNo(recvAcNo);
        flowForm.setAmt(Amt);
        flowForm.setOperator(operator.getCiNo());
        System.out.println(flowForm.toString());
        ResultVO<HashMap> result = financialClient.transfer(flowForm);
        System.out.println(result);
        if (result.getCode().equals(0)) {
            map.addAttribute("msg", new StringBuffer("转账成功，流水号为").append(result.getData().get("result")));
            map.addAttribute("uri", "flows");
            return "jump";
        } else {
            map.addAttribute("msg", new StringBuffer("转账失败，").append(result.getData().get("result")));
            map.addAttribute("uri", "trans");
            return "jump";
        }
    }

    @GetMapping("/freeze")
    public String freeze(ModelMap map, HttpSession httpSession) {
        Citmst citmst = (Citmst) httpSession.getAttribute("client");

        List<Citacr> acList = financialClient.getCiAcList(citmst.getCiNo());
        httpSession.setAttribute("acList", acList);
        return "acFreeze";
    }

    @PostMapping("/freezeAc")
    public String freezeAc(ModelMap map,
                           HttpSession httpSession,
                           @RequestParam("acNo") String acNo
    ) {

        Citacr ac = financialClient.getAcInfo(acNo);
        httpSession.setAttribute("ac", ac);
        return "freeze";
    }

    @PostMapping("/doFreeze")
    public String doFreeze(ModelMap map,
                           HttpSession httpSession,
                           @RequestParam("ciNo") String ciNo,
                           @RequestParam("acNo") String acNo) {

        Citacr citacr = financialClient.getAcInfo(acNo);
        if (!citacr.getAcSts().equals(Sts.NORMAL.getCode())) {
            map.addAttribute("msg", "操作失败，账户状态为非正常状态");
            map.addAttribute("uri", "index");
            return "jump";
        }
        if (citacr.getCiNo().equals(ciNo)) {
            if (financialClient.freezeAc(citacr) != null) {
                map.addAttribute("msg", "操作成功");
                map.addAttribute("uri", "index");
                return "jump";
            }
            map.addAttribute("msg", "操作失败，请联系管理员");
            map.addAttribute("uri", "index");
            return "jump";
        }
        map.addAttribute("msg", "操作失败，信息不匹配");
        map.addAttribute("uri", "index");
        return "jump";
    }

}
