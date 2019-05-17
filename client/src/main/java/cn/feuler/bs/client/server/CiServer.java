package cn.feuler.bs.client.server;

import cn.feuler.bs.client.dataobject.Citmst;
import cn.feuler.bs.client.service.MstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@RestController
@RequestMapping("/ciserver")
public class CiServer {

    @Autowired
    private MstService mstService;

    @GetMapping("/getCiList")
    public List<Citmst> getCiList() {
        return mstService.getAll();
    }

    @PostMapping("/getCiInfo")
    public Citmst getCiInfo(@RequestBody String ciNo) {
        return mstService.getCiInfo(ciNo);
    }

    @PostMapping("/updateCiInfo")
    public Citmst updateCiInfo(@RequestBody Citmst citmst) {
        return mstService.updateCiInfo(citmst);
    }

}
