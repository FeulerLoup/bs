package cn.feuler.bs.user.service;

import cn.feuler.bs.user.dataobject.Bpttlr;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

public interface TlrService {

    //查询所有普通用户
    List<Bpttlr> findByTLR();

    //根据等级查询
    List<Bpttlr> findByLevel(List<String> levelList);

    Bpttlr tlrLogin(String tlrNo, String tlrPasswd);

    Bpttlr getTlrInfo(String TlrNo);

    Bpttlr mod(Bpttlr bpttlr);

    List<Bpttlr> getTlrList();

}
