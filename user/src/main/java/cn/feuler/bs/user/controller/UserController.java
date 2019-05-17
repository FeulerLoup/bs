package cn.feuler.bs.user.controller;

import cn.feuler.bs.user.VO.ResultVO;
import cn.feuler.bs.user.client.BasicClient;
import cn.feuler.bs.user.client.CiClient;
import cn.feuler.bs.user.client.FinancialClient;
import cn.feuler.bs.user.dataobject.*;
import cn.feuler.bs.user.enums.ActSts;
import cn.feuler.bs.user.enums.ActType;
import cn.feuler.bs.user.enums.Sts;
import cn.feuler.bs.user.form.FlowForm;
import cn.feuler.bs.user.utils.CommUtil;
import cn.feuler.bs.user.service.TlrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Slf4j
@Controller
public class UserController {

    @Autowired
    private TlrService tlrService;

    @Autowired
    private CommUtil commUtil;

    @Autowired
    private BasicClient basicClient;

    @Autowired
    private CiClient ciClient;

    @Autowired
    private FinancialClient financialClient;


    @GetMapping("/login")
    public String login(HttpSession httpSession) {
        httpSession.invalidate();
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(ModelMap map,
                          HttpSession httpSession,
                          @RequestParam("tlrNo") String tlrNo,
                          @RequestParam("tlrPasswd") String tlrPasswd) {

        Bpttlr result = tlrService.tlrLogin(tlrNo, commUtil.md5Hex(tlrPasswd));

        if (result != null && (result.getTlrSts().equals(Sts.NORMAL.getCode()))) {
            map.addAttribute("msg", new StringBuffer("欢迎登陆，").append(result.getTlrName()));
            map.addAttribute("uri", "index");
            httpSession.setAttribute("self", result);
            return "jump";
        } else if (result != null && result.getTlrSts().equals(Sts.FREEZE.getCode())) {
            map.addAttribute("msg", "登陆失败，帐号已冻结");
            map.addAttribute("uri", "login");
            return "jump";
        } else if (result != null && result.getTlrSts().equals(Sts.CLOSED.getCode())) {
            map.addAttribute("msg", "登陆失败，帐号已注销");
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

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");
        basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.LOGOUT.getAct(), ActSts.NORMAL.getCode()));

        httpSession.invalidate();
        map.addAttribute("msg", "已成功登出");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/index")
    public String index(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");
        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {

            Bptlvl level = basicClient.getPermission(bpttlr.getLvlNo());
            httpSession.setAttribute("level", level);

            List<Citmst> clientList = ciClient.getCiList();
            List<Citmst> inactiveClientList = new ArrayList<>();
            List<Citmst> resetClientList = new ArrayList<>();
            int counter_1 = 0;
            int counter_2 = 0;
            for (Citmst citmst : clientList) {
                if (citmst.getCiSts().equals(Sts.ACTIVE.getCode())) {
                    inactiveClientList.add(citmst);
                    ++counter_1;
                }
                if (citmst.getCiSts().equals(Sts.RESET.getCode())) {
                    resetClientList.add(citmst);
                    ++counter_2;
                }
            }
            httpSession.setAttribute("inactiveCiNum", counter_1);
            httpSession.setAttribute("inactiveClientList", inactiveClientList);
            httpSession.setAttribute("resetCiNum", counter_2);
            httpSession.setAttribute("resetClientList", resetClientList);
            httpSession.setAttribute("clientList", clientList);
            if (level.getSuInq().equals("Y")) {

                List<Citacr> acList = financialClient.getAcList();
                List<Citacr> inactiveAcList = new ArrayList<>();
                int counter_3 = 0;
                for (Citacr citacr : acList) {
                    if (citacr.getAcSts().equals(Sts.ACTIVE.getCode())) {
                        inactiveAcList.add(citacr);
                        ++counter_3;
                    }
                }
                httpSession.setAttribute("inactiveAcNum", counter_3);
                httpSession.setAttribute("inactiveAcList", inactiveAcList);

                httpSession.setAttribute("acList", acList);
            } else {
                httpSession.setAttribute("inactiveAcNum", 0);
            }
            httpSession.setAttribute("dateTime", commUtil.getDateTime());

            return "index";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/info")
    public String info(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");
        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {

            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQTELLERINFO.getAct(), ActSts.NORMAL.getCode()));
            httpSession.setAttribute("teller", bpttlr);
            return "info";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/modInfo")
    public String modInfo(ModelMap map,
                          HttpSession httpSession,
                          @RequestParam("tlrNo") String tlrNo,
                          @RequestParam("tlrPasswd") String tlrPasswd,
                          @RequestParam("tlrPasswdConfirm") String tlrPasswdConfirm,
                          @RequestParam("tlrName") String tlrName) {

        Bpttlr oldInfo = (Bpttlr) httpSession.getAttribute("self");

        if (oldInfo != null && basicClient.getPermission(oldInfo.getLvlNo()).getTlrMod().equals("Y")) {
            if ((tlrPasswd == null && tlrPasswdConfirm != null)
                    || (tlrPasswd != null && tlrPasswdConfirm == null)
                    || (tlrPasswd != null && tlrPasswdConfirm != null && !tlrPasswd.equals(tlrPasswdConfirm))) {

                basicClient.createAct(new Bptact(oldInfo.getTlrNo(), ActType.MODTELLERPASSWD.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "保存失败，密码不一致");
                map.addAttribute("uri", "info");
                return "jump";
            }


            Bpttlr newInfo = new Bpttlr();
            BeanUtils.copyProperties(oldInfo, newInfo);
            newInfo.setTlrNo(tlrNo);
            newInfo.setTlrName(tlrName);
            newInfo.setUpdTlr(oldInfo.getTlrNo());
            newInfo.setDateTime(commUtil.getDateTime());
            if ("".equals(tlrPasswd) && "".equals(tlrPasswdConfirm)) {
                newInfo.setTlrPasswd(oldInfo.getTlrPasswd());
            } else {
                newInfo.setTlrPasswd(commUtil.md5Hex(tlrPasswd));
            }

            if (tlrService.getTlrInfo(newInfo.getTlrNo()) == null) {
                basicClient.createAct(new Bptact(oldInfo.getTlrNo(), ActType.MODTELLERPASSWD.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "修改失败，用户不存在");
                map.addAttribute("uri", "info");
                return "jump";
            } else {
                Bpttlr result = tlrService.mod(newInfo);
                if (result != null) {
                    basicClient.createAct(new Bptact(oldInfo.getTlrNo(), ActType.MODTELLERPASSWD.getAct(), ActSts.NORMAL.getCode()));
                    map.addAttribute("msg", "修改成功,正在刷新");
                    map.addAttribute("uri", "info");
                    httpSession.setAttribute("client", result);
                    return "jump";
                } else {
                    basicClient.createAct(new Bptact(oldInfo.getTlrNo(), ActType.MODTELLERPASSWD.getAct(), ActSts.ERROR.getCode()));
                    map.addAttribute("msg", "修改失败，请联系管理员");
                    map.addAttribute("uri", "info");
                    return "jump";
                }
            }
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/actList")
    public String actList(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEACCOUNT.getAct(), ActSts.NORMAL.getCode()));

            List<Bptact> actList = basicClient.getActList(bpttlr.getTlrNo());
            httpSession.setAttribute("actList", actList);

            httpSession.setAttribute("dateTime", commUtil.getDateTime());
            return "act";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/suActList")
    public String suActList(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEACTLIST.getAct(), ActSts.NORMAL.getCode()));

            List<Bptact> actList = basicClient.getSuActList();
            httpSession.setAttribute("actList", actList);

            httpSession.setAttribute("dateTime", commUtil.getDateTime());
            return "act";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/clientActive")
    public String clientActive(ModelMap map, HttpSession httpSession, @RequestParam("ciNo") String ciNo) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {

            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQCLIENTINFO.getAct(), ActSts.NORMAL.getCode()));
            httpSession.setAttribute("client", ciClient.getCiInfo(ciNo));
            return "clientActive";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/activeClient")
    public String activeClient(ModelMap map,
                               HttpSession httpSession,
                               @RequestParam("ciNo") String ciNo) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrAp().equals("Y")) {

            Citmst citmst = ciClient.getCiInfo(ciNo);
            if (citmst != null) {
                citmst.setCiSts(Sts.NORMAL.getCode());
                citmst.setUpdTlr(bpttlr.getTlrNo());
                citmst.setDateTime(commUtil.getDateTime());
                Citmst result = ciClient.updateCiInfo(citmst);
                if (result != null) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ACTIVECLIENT.getAct(), ActSts.NORMAL.getCode()));
                    map.addAttribute("msg", "操作完成");
                    map.addAttribute("uri", "index");
                    return "jump";
                }
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ACTIVECLIENT.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "操作失败，请联系管理员");
                map.addAttribute("uri", "index");
                return "jump";

            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ACTIVECLIENT.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "操作失败，用户不存在");
            map.addAttribute("uri", "index");
            return "jump";

        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/clientList")
    public String clientList(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSECLIENT.getAct(), ActSts.NORMAL.getCode()));

            List<Citmst> clientList = ciClient.getCiList();
            httpSession.setAttribute("clientList", clientList);

            httpSession.setAttribute("dateTime", commUtil.getDateTime());
            return "clientList";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/clientMod")
    public String clientMod(ModelMap map,
                            HttpSession httpSession,
                            @RequestParam("ciNo") String ciNo) {
        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {

            Citmst citmst = ciClient.getCiInfo(ciNo);
            if (citmst != null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQCLIENTINFO.getAct(), ActSts.NORMAL.getCode()));
                httpSession.setAttribute("client", citmst);
                if (basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {
                    return "clientInfoSu";
                }
                return "clientInfo";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQCLIENTINFO.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "用户不存在");
            map.addAttribute("uri", "clientList");
            return "jump";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/modClient")
    public String modClient(ModelMap map,
                            HttpSession httpSession,
                            @RequestParam("ciNo") String ciNo,
                            @RequestParam("ciLvl") String ciLvl,
                            @RequestParam("ciSts") String ciSts) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrMod().equals("Y")) {

            Citmst citmst = ciClient.getCiInfo(ciNo);
            if (citmst != null) {
                citmst.setCiLvl(ciLvl);
                citmst.setCiSts(ciSts);
                switch (ciSts) {
                    case "C": {
                        citmst.setLvlNo("0");
                    }
                    case "F": {
                        citmst.setLvlNo("1");
                    }
                    case "N": {
                        citmst.setLvlNo("2");
                    }

                }
                citmst.setUpdTlr(bpttlr.getTlrNo());
                citmst.setDateTime(commUtil.getDateTime());
                Citmst result = ciClient.updateCiInfo(citmst);
                if (result != null) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODCLIENTINFO.getAct(), ActSts.NORMAL.getCode()));
                    List<Citmst> clientList = ciClient.getCiList();
                    httpSession.setAttribute("clientList", clientList);

                    httpSession.setAttribute("dateTime", commUtil.getDateTime());
                    map.addAttribute("msg", "操作完成");
                    map.addAttribute("uri", "clientList");
                    return "jump";
                }
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODCLIENTINFO.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "操作失败，请联系管理员");
                map.addAttribute("uri", "clientList");
                return "jump";

            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODCLIENTINFO.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "操作失败，用户不存在");
            map.addAttribute("uri", "clientList");
            return "jump";

        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/modClientSu")
    public String modClientSu(ModelMap map,
                              HttpSession httpSession,
                              @RequestParam("ciNo") String ciNo,
                              @RequestParam("ciLvl") String ciLvl,
                              @RequestParam("ciSts") String ciSts,
                              @RequestParam("ciPasswd") String ciPasswd,
                              @RequestParam("ciPasswdConfirm") String ciPasswdConfirm) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrMod().equals("Y")) {

            if ((ciPasswd == null && ciPasswdConfirm != null)
                    || (ciPasswd != null && ciPasswdConfirm == null)
                    || (ciPasswd != null && ciPasswdConfirm != null && !ciPasswd.equals(ciPasswdConfirm))) {

                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODCLIENTINFO.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "保存失败，密码不一致");
                map.addAttribute("uri", "index");
                return "jump";
            }

            Citmst citmst = ciClient.getCiInfo(ciNo);
            if (citmst != null) {
                citmst.setCiLvl(ciLvl);
                citmst.setCiSts(ciSts);
                switch (ciSts) {
                    case "C": {
                        citmst.setLvlNo("0");
                    }
                    case "F": {
                        citmst.setLvlNo("1");
                    }
                    case "N": {
                        citmst.setLvlNo("2");
                    }

                }
                if ((ciPasswd == null && ciPasswdConfirm == null) || (ciPasswd.equals("") && ciPasswdConfirm.equals(""))) {
                    citmst.setCiPasswd(citmst.getCiPasswd());
                } else {
                    citmst.setCiPasswd(commUtil.md5Hex(ciPasswd));
                }
                citmst.setUpdTlr(bpttlr.getTlrNo());
                citmst.setDateTime(commUtil.getDateTime());
                Citmst result = ciClient.updateCiInfo(citmst);
                if (result != null) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODCLIENTINFO.getAct(), ActSts.NORMAL.getCode()));
                    List<Citmst> clientList = ciClient.getCiList();
                    httpSession.setAttribute("clientList", clientList);

                    httpSession.setAttribute("dateTime", commUtil.getDateTime());
                    map.addAttribute("msg", "操作完成");
                    map.addAttribute("uri", "index");
                    return "jump";
                }
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODCLIENTINFO.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "操作失败，请联系管理员");
                map.addAttribute("uri", "index");
                return "jump";

            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODCLIENTINFO.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "操作失败，用户不存在");
            map.addAttribute("uri", "index");
            return "jump";

        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/clientAcList")
    public String clientAcList(ModelMap map,
                               HttpSession httpSession,
                               @RequestParam("ciNo") String ciNo) {
        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {

            Citmst citmst = ciClient.getCiInfo(ciNo);

            if (citmst != null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEACCOUNT.getAct(), ActSts.NORMAL.getCode()));

                List<Citacr> acList = financialClient.getCiAcList(citmst.getCiNo());
                httpSession.setAttribute("acList", acList);
                return "clientAcList";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEACCOUNT.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "用户不存在");
            map.addAttribute("uri", "clientList");
            return "jump";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "clientList");
        return "jump";
    }

    @GetMapping("/acList")
    public String acList(ModelMap map,
                         HttpSession httpSession) {
        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEACCOUNT.getAct(), ActSts.NORMAL.getCode()));

            List<Citacr> acList = financialClient.getAcList();
            httpSession.setAttribute("acList", acList);
            return "clientAcList";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "clientList");
        return "jump";
    }

    @PostMapping("/clientAcMod")
    public String clientMod(ModelMap map,
                            HttpSession httpSession,
                            @RequestParam("ciNo") String ciNo,
                            @RequestParam("acNo") String acNo) {
        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {

            Citacr citacr = new Citacr();
            citacr.setAcNo(acNo);
            citacr.setCiNo(ciNo);
            Citacr ciAc = financialClient.getCiAc(citacr);
            if (ciAc != null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQACCOUNT.getAct(), ActSts.NORMAL.getCode()));

                httpSession.setAttribute("clientAc", ciAc);
                return "clientAcInfo";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQACCOUNT.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "用户不存在");
            map.addAttribute("uri", "clientAcMod");
            return "jump";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/modClientAc")
    public String modClientAc(ModelMap map,
                              HttpSession httpSession,
                              @RequestParam("acNo") String acNo,
                              @RequestParam("ciNo") String ciNo,
                              @RequestParam("acSts") String acSts) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrMod().equals("Y")) {

            Citacr citacr = new Citacr();
            citacr.setAcNo(acNo);
            citacr.setCiNo(ciNo);
            Citacr ciAc = financialClient.getCiAc(citacr);
            if (ciAc != null) {
                ciAc.setAcSts(acSts);
                Citacr result = financialClient.updateAc(ciAc);
                if (result != null) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODACCOUNTSTS.getAct(), ActSts.NORMAL.getCode()));

                    List<Citacr> acList = financialClient.getCiAcList(result.getCiNo());
                    httpSession.setAttribute("acList", acList);
                    map.addAttribute("msg", "修改成功");
                    map.addAttribute("uri", "index");
                    return "jump";
                }
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODACCOUNTSTS.getAct(), ActSts.ERROR.getCode()));
                httpSession.setAttribute("clientAc", citacr);
                map.addAttribute("msg", "修改失败，请联系管理员");
                map.addAttribute("uri", "clientAcInfo");
                return "jump";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODACCOUNTSTS.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "修改失败，信息有误");
            map.addAttribute("uri", "clientAcInfo");
            return "jump";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/tellerList")
    public String tellerList(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSETELLER.getAct(), ActSts.NORMAL.getCode()));

            List<Bpttlr> tellerList = tlrService.getTlrList();
            httpSession.setAttribute("tellerList", tellerList);
            httpSession.setAttribute("dateTime", commUtil.getDateTime());
            return "tellerList";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/tellerMod")
    public String tellerMod(ModelMap map,
                            HttpSession httpSession,
                            @RequestParam("tlrNo") String tlrNo) {
        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {

            Bpttlr teller = tlrService.getTlrInfo(tlrNo);
            if (teller != null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQTELLERINFO.getAct(), ActSts.NORMAL.getCode()));

                List<Bptlvl> permissionList = basicClient.getPermissionList();
                httpSession.setAttribute("permissionList", permissionList);
                httpSession.setAttribute("teller", teller);
                return "tellerInfo";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQTELLERINFO.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "用户不存在");
            map.addAttribute("uri", "tellerList");
            return "jump";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/modTeller")
    public String modTeller(ModelMap map,
                            HttpSession httpSession,
                            @RequestParam("tlrNo") String tlrNo,
                            @RequestParam("tlrName") String tlrName,
                            @RequestParam("lvlNo") String lvlNo,
                            @RequestParam("tlrSts") String tlrSts,
                            @RequestParam("tlrPasswd") String tlrPasswd,
                            @RequestParam("tlrPasswdConfirm") String tlrPasswdConfirm) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {

            if ((tlrPasswd == null && tlrPasswdConfirm != null)
                    || (tlrPasswd != null && tlrPasswdConfirm == null)
                    || (tlrPasswd != null && tlrPasswdConfirm != null && !tlrPasswd.equals(tlrPasswdConfirm))) {

                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODTELLERPASSWD.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "保存失败，密码不一致");
                map.addAttribute("uri", "tellerList");
                return "jump";
            }

            Bpttlr teller = tlrService.getTlrInfo(tlrNo);
            if (teller != null) {
                teller.setTlrName(tlrName);
                teller.setLvlNo(lvlNo);
                teller.setTlrSts(tlrSts);
                teller.setUpdTlr(bpttlr.getTlrNo());
                teller.setDateTime(commUtil.getDateTime());
                if ((tlrPasswd == null && tlrPasswdConfirm == null) || (tlrPasswd.equals("") && tlrPasswdConfirm.equals(""))) {
                    teller.setTlrPasswd(teller.getTlrPasswd());
                } else {
                    teller.setTlrPasswd(commUtil.md5Hex(tlrPasswd));
                }
                Bpttlr result = tlrService.mod(teller);
                if (result != null) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODTELLERINFO.getAct(), ActSts.NORMAL.getCode()));

                    List<Bpttlr> tellerList = tlrService.getTlrList();
                    httpSession.setAttribute("tellerList", tellerList);
                    httpSession.setAttribute("dateTime", commUtil.getDateTime());
                    map.addAttribute("msg", "操作完成");
                    map.addAttribute("uri", "tellerList");
                    return "jump";
                }
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODTELLERINFO.getAct(), ActSts.ERROR.getCode()));

                map.addAttribute("msg", "操作失败，请联系管理员");
                map.addAttribute("uri", "tellerList");
                return "jump";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODTELLERINFO.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "操作失败，用户不存在");
            map.addAttribute("uri", "tellerList");
            return "jump";

        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/newTeller")
    public String newTeller(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuMod().equals("Y")) {

            List<Bptlvl> permissionList = basicClient.getPermissionList();
            httpSession.setAttribute("permissionList", permissionList);
            return "newTeller";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/addTeller")
    public String addTeller(ModelMap map,
                            HttpSession httpSession,
                            @RequestParam("tlrNo") String tlrNo,
                            @RequestParam("tlrName") String tlrName,
                            @RequestParam("lvlNo") String lvlNo,
                            @RequestParam("tlrSts") String tlrSts,
                            @RequestParam("tlrPasswd") String tlrPasswd,
                            @RequestParam("tlrPasswdConfirm") String tlrPasswdConfirm) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuMod().equals("Y")) {

            if ("".equals(tlrPasswd) || "".equals(tlrPasswdConfirm) || !tlrPasswd.equals(tlrPasswdConfirm)) {

                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ADDTELLER.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "新增失败，密码不一致");
                map.addAttribute("uri", "newTeller");
                return "jump";
            }

            Bpttlr teller = tlrService.getTlrInfo(tlrNo);
            if (teller == null) {
                teller = new Bpttlr();
                teller.setTlrNo(tlrNo);
                teller.setTlrName(tlrName);
                teller.setLvlNo(lvlNo);
                teller.setTlrSts(tlrSts);
                teller.setUpdTlr(bpttlr.getTlrNo());
                teller.setDateTime(commUtil.getDateTime());
                teller.setTlrPasswd(commUtil.md5Hex(tlrPasswd));

                Bpttlr result = tlrService.mod(teller);
                if (result != null) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ADDTELLER.getAct(), ActSts.NORMAL.getCode()));

                    List<Bpttlr> tellerList = tlrService.getTlrList();
                    httpSession.setAttribute("tellerList", tellerList);
                    httpSession.setAttribute("dateTime", commUtil.getDateTime());
                    map.addAttribute("msg", "操作完成");
                    map.addAttribute("uri", "tellerList");
                    return "jump";
                }
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ADDTELLER.getAct(), ActSts.ERROR.getCode()));

                map.addAttribute("msg", "操作失败，请联系管理员");
                map.addAttribute("uri", "newTeller");
                return "jump";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ADDTELLER.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "操作失败，用户已存在");
            map.addAttribute("uri", "newTeller");
            return "jump";

        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/acOpen")
    public String acOpen(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrAp().equals("Y")) {

            String newAcNo = financialClient.getNewAcNo();
            httpSession.setAttribute("newAcNo", newAcNo);
            return "openAc";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/openClientAc")
    public String modClientAc(ModelMap map,
                              HttpSession httpSession,
                              @RequestParam("ciName") String ciName,
                              @RequestParam("ciNo") String ciNo,
                              @RequestParam("acNo") String acNo,
                              @RequestParam("acCcy") String acCcy
    ) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrMod().equals("Y")) {

            Citmst citmst = ciClient.getCiInfo(ciNo);
            if (citmst == null || !citmst.getCiName().equals(ciName)) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.OPENACCOUNT.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "开通失败，信息不匹配");
                map.addAttribute("uri", "index");
                return "jump";
            }
            if (financialClient.getAcInfo(acNo) != null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.OPENACCOUNT.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "开通失败，帐号已存在");
                map.addAttribute("uri", "index");
                return "jump";
            }
            Citacr citacr = new Citacr();
            citacr.setAcNo(acNo);
            citacr.setCiNo(ciNo);
            citacr.setAcSts(Sts.ACTIVE.getCode());
            citacr.setCcy(acCcy);
            citacr.setAcBal(BigDecimal.ZERO);
            citacr.setUpdTlr(bpttlr.getTlrNo());
            citacr.setDateTime(commUtil.getDateTime());
            Citacr result = financialClient.createNewAc(citacr);
            if (result != null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.OPENACCOUNT.getAct(), ActSts.NORMAL.getCode()));

                List<Citacr> acList = financialClient.getCiAcList(result.getCiNo());
                httpSession.setAttribute("acList", acList);
                map.addAttribute("msg", "开通成功，等待超级管理员复核");
                map.addAttribute("uri", "index");
                return "jump";
            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.OPENACCOUNT.getAct(), ActSts.ERROR.getCode()));
            httpSession.setAttribute("clientAc", citacr);
            map.addAttribute("msg", "开通失败，请联系管理员");
            map.addAttribute("uri", "clientAcInfo");
            return "jump";
        }

        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/acActive")
    public String acActive(ModelMap map, HttpSession httpSession, @RequestParam("acNo") String acNo) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {

            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.INQACCOUNT.getAct(), ActSts.NORMAL.getCode()));
            Citacr citacr = financialClient.getAcInfo(acNo);
            httpSession.setAttribute("ac", citacr);
            httpSession.setAttribute("client", ciClient.getCiInfo(citacr.getCiNo()));
            return "clientAcActive";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/activeAc")
    public String activeAc(ModelMap map,
                           HttpSession httpSession,
                           @RequestParam("acNo") String acNo) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuAp().equals("Y")) {

            Citacr citacr = financialClient.getAcInfo(acNo);
            if (citacr != null) {
                citacr.setAcSts(Sts.NORMAL.getCode());
                citacr.setUpdTlr(bpttlr.getTlrNo());
                citacr.setDateTime(commUtil.getDateTime());
                Citacr result = financialClient.updateAc(citacr);
                if (result != null) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ACTIVEACCOUNT.getAct(), ActSts.NORMAL.getCode()));
                    map.addAttribute("msg", "操作完成");
                    map.addAttribute("uri", "index");
                    return "jump";
                }
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ACTIVEACCOUNT.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "操作失败，请联系管理员");
                map.addAttribute("uri", "index");
                return "jump";

            }
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.ACTIVEACCOUNT.getAct(), ActSts.ERROR.getCode()));
            map.addAttribute("msg", "操作失败，用户不存在");
            map.addAttribute("uri", "index");
            return "jump";

        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/clientAcFlow")
    public String clientAcFlow(ModelMap map, HttpSession httpSession, @RequestParam("acNo") String acNo) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrInq().equals("Y")) {

            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEACCOUNTFLOW.getAct(), ActSts.NORMAL.getCode()));
            List<Ddtflow> recList = financialClient.CiRecList(acNo);
            httpSession.setAttribute("recList", recList);
            return "flowList";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/clientFlow")
    public String clientFlow(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuInq().equals("Y")) {

            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEFLOW.getAct(), ActSts.NORMAL.getCode()));
            List<Ddtflow> recList = financialClient.getRecList();
            httpSession.setAttribute("recList", recList);
            return "flowList";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/depositWithdrawal")
    public String depositWithdrawal(ModelMap map, HttpSession httpSession) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrMod().equals("Y")) {

            return "depositWithdrawal";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/doDepositWithdrawal")
    public String doDepositWithdrawal(ModelMap map,
                                      HttpSession httpSession,
                                      @RequestParam("ciNo") String ciNo,
                                      @RequestParam("acNo") String acNo,
                                      @RequestParam("act") String act,
                                      @RequestParam("ccy") String ccy,
                                      @RequestParam("amt") String amt,
                                      @RequestParam("operator") String operator) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getTlrMod().equals("Y")) {

            BigDecimal Amt;
            try {
                Amt = new BigDecimal(amt);
            } catch (Exception e) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.FINANCIAL.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "操作失败，金额异常");
                map.addAttribute("uri", "depositWithdrawal");
                return "jump";
            }
            FlowForm flowForm = new FlowForm();
            flowForm.setAct(act);
            flowForm.setCiNo(ciNo);
            flowForm.setAcNo(acNo);
            flowForm.setAmt(Amt);
            flowForm.setOperator(operator);

            Citacr citacr = financialClient.getAcInfo(flowForm.getAcNo());
            if (citacr == null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.FINANCIAL.getAct(), ActSts.NORMAL.getCode()));
                map.addAttribute("msg", "操作失败，账户不存在");
                map.addAttribute("uri", "depositWithdrawal");
                return "jump";
            }
            if (!citacr.getCiNo().equals(flowForm.getCiNo())) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.FINANCIAL.getAct(), ActSts.NORMAL.getCode()));
                map.addAttribute("msg", "操作失败，信息不匹配");
                map.addAttribute("uri", "depositWithdrawal");
                return "jump";
            }
            if (!"D".equals(flowForm.getAct()) && !"W".equals(flowForm.getAct())) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.FINANCIAL.getAct(), ActSts.NORMAL.getCode()));
                map.addAttribute("msg", "操作失败，操作非法");
                map.addAttribute("uri", "depositWithdrawal");
                return "jump";
            }
            if (!citacr.getCcy().equals(ccy)) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.FINANCIAL.getAct(), ActSts.NORMAL.getCode()));
                map.addAttribute("msg", "操作失败，币种与账户不匹配");
                map.addAttribute("uri", "depositWithdrawal");
                return "jump";
            }


            if (flowForm.getAct().equals("D")) {
                ResultVO<HashMap> result = financialClient.deposit(flowForm);
                if (result.getCode().equals(0)) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.DEPOSIT.getAct(), ActSts.NORMAL.getCode()));
                    map.addAttribute("msg", new StringBuffer("操作成功，流水号为").append(result.getData().get("result")));
                } else {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.DEPOSIT.getAct(), ActSts.ERROR.getCode()));
                    map.addAttribute("msg", new StringBuffer("操作失败，").append(result.getData().get("result")));
                }
            } else {
                ResultVO<HashMap> result = financialClient.withdrawal(flowForm);
                if (result.getCode().equals(0)) {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.WITHDRAW.getAct(), ActSts.NORMAL.getCode()));
                    map.addAttribute("msg", new StringBuffer("操作成功，流水号为").append(result.getData().get("result")));
                } else {
                    basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.WITHDRAW.getAct(), ActSts.ERROR.getCode()));
                    map.addAttribute("msg", new StringBuffer("操作失败，").append(result.getData().get("result")));
                }
            }
            map.addAttribute("uri", "depositWithdrawal");
            return "jump";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @GetMapping("/permissionList")
    public String permissionList(ModelMap map,
                                 HttpSession httpSession) {
        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuMod().equals("Y")) {
            basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.BROWSEACCOUNT.getAct(), ActSts.NORMAL.getCode()));

            List<Bptlvl> permissionList = basicClient.getPermissionList();
            httpSession.setAttribute("permissionList", permissionList);
            return "permissionList";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "clientList");
        return "jump";
    }

    @GetMapping("/newPermission")
    public String newPermission(ModelMap map,
                                HttpSession httpSession) {
        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuMod().equals("Y")) {

            String newLvlNo = basicClient.getNewLvlNo();
            httpSession.setAttribute("newLvlNo", newLvlNo);
            return "newPermission";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "clientList");
        return "jump";
    }

    @PostMapping("/permissionMod")
    public String permissionMod(ModelMap map, HttpSession httpSession, @RequestParam("lvlNo") String lvlNo) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuMod().equals("Y")) {

            Bptlvl permission = basicClient.getPermission(lvlNo);
            httpSession.setAttribute("permission", permission);
            return "permissionInfo";
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "login");
        return "jump";
    }

    @PostMapping("/ModPermission")
    public String ModPermission(ModelMap map,
                                HttpSession httpSession,
                                @RequestParam("lvlNo") String lvlNo,
                                @RequestParam("ciFinancial") String ciFinancial,
                                @RequestParam("ciInq") String ciInq,
                                @RequestParam("tlrInq") String tlrInq,
                                @RequestParam("suInq") String suInq,
                                @RequestParam("ciMod") String ciMod,
                                @RequestParam("tlrMod") String tlrMod,
                                @RequestParam("suMod") String suMod,
                                @RequestParam("tlrAp") String tlrAp,
                                @RequestParam("suAp") String suAp) {

        Bpttlr bpttlr = (Bpttlr) httpSession.getAttribute("self");

        if (bpttlr != null && basicClient.getPermission(bpttlr.getLvlNo()).getSuMod().equals("Y")) {

            if (!("Y".equals(ciFinancial) || "N".equals(ciFinancial))
                    && ("Y".equals(ciInq) || "N".equals(ciInq))
                    && ("Y".equals(tlrInq) || "N".equals(tlrInq))
                    && ("Y".equals(suInq) || "N".equals(suInq))
                    && ("Y".equals(ciMod) || "N".equals(ciMod))
                    && ("Y".equals(tlrMod) || "N".equals(tlrMod))
                    && ("Y".equals(suMod) || "N".equals(suMod))
                    && ("Y".equals(tlrAp) || "N".equals(tlrAp))
                    && ("Y".equals(suAp) || "N".equals(suAp))) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODPERMISSION.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "修改失败，参数错误");
                map.addAttribute("uri", "permissionList");
                return "jump";
            }

            Bptlvl check = basicClient.getPermission(lvlNo);
            if (check == null && !lvlNo.equals(basicClient.getNewLvlNo())) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODPERMISSION.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "修改失败，参数错误");
                map.addAttribute("uri", "permissionList");
                return "jump";
            }
            Bptlvl bptlvl = new Bptlvl();
            bptlvl.setLvlNo(lvlNo);
            bptlvl.setCiFinancial(ciFinancial);
            bptlvl.setCiInq(ciInq);
            bptlvl.setCiMod(ciMod);
            bptlvl.setTlrInq(tlrInq);
            bptlvl.setTlrMod(tlrMod);
            bptlvl.setTlrAp(tlrAp);
            bptlvl.setSuInq(suInq);
            bptlvl.setSuMod(suMod);
            bptlvl.setSuAp(suAp);
            bptlvl.setUpdTlr(bpttlr.getTlrNo());
            bptlvl.setDateTime(commUtil.getDateTime());

            Bptlvl result = basicClient.modPermission(bptlvl);
            if (result != null) {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODPERMISSION.getAct(), ActSts.NORMAL.getCode()));
                map.addAttribute("msg", "修改成功");
                map.addAttribute("uri", "permissionList");
                return "jump";
            } else {
                basicClient.createAct(new Bptact(bpttlr.getTlrNo(), ActType.MODPERMISSION.getAct(), ActSts.ERROR.getCode()));
                map.addAttribute("msg", "修改失败，参数错误");
                map.addAttribute("uri", "index");
                return "jump";
            }
        }
        map.addAttribute("msg", "非法访问，请重新登录");
        map.addAttribute("uri", "permissionList");
        return "jump";
    }
}
