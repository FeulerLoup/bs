package cn.feuler.bs.basic.controller;

import cn.feuler.bs.basic.dataobject.Bptact;
import cn.feuler.bs.basic.dataobject.Bptlvl;
import cn.feuler.bs.basic.service.ActService;
import cn.feuler.bs.basic.service.LvlService;
import cn.feuler.bs.basic.utils.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Create by: Feuler
 * Date: 2019/4/1
 **/

@RestController
public class BasicController {

    @Autowired
    private LvlService lvlService;

    @Autowired
    private ActService actService;

    @Autowired
    private CommUtil commUtil;

    @PostMapping("/getPermission")
    public Bptlvl getPermission(@RequestBody String lvlNo) {
        return lvlService.getPermission(lvlNo);
    }

    @PostMapping("/createAct")
    public void createAct(@RequestBody Bptact bptact) {
        bptact.setDateTime(commUtil.getDateTime());
        actService.create(bptact);
    }

    @GetMapping("/getSuActList")
    public List<Bptact> getSuActList() {
        return actService.getSuActList();
    }

    @PostMapping("/getActList")
    public List<Bptact> getActList(@RequestBody String tlrNo) {
        return actService.getActList(tlrNo);
    }

    @GetMapping("/getPermissionList")
    public List<Bptlvl> getPermissionList() {
        return lvlService.getPermissionList();
    }

    @GetMapping("/getNewLvlNo")
    public String getNewLvlNo() {
        return lvlService.getNewLvlNo();
    }

    @PostMapping("/modPermission")
    public Bptlvl modPermission(@RequestBody Bptlvl bptlvl) {
        return lvlService.modPermission(bptlvl);
    }
}
