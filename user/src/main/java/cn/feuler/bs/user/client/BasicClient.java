package cn.feuler.bs.user.client;

import cn.feuler.bs.user.dataobject.Bptact;
import cn.feuler.bs.user.dataobject.Bptlvl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@FeignClient(name = "basic")
public interface BasicClient {

    @PostMapping("/getPermission")
    Bptlvl getPermission(@RequestBody String lvlNo);

    @PostMapping("/createAct")
    void createAct(@RequestBody Bptact bptact);

    @GetMapping("/getSuActList")
    List<Bptact> getSuActList();

    @PostMapping("/getActList")
    List<Bptact> getActList(@RequestBody String tlrNo);

    @GetMapping("/getPermissionList")
    List<Bptlvl> getPermissionList();

    @GetMapping("/getNewLvlNo")
    String getNewLvlNo();

    @PostMapping("/modPermission")
    Bptlvl modPermission(@RequestBody Bptlvl bptlvl);
}
