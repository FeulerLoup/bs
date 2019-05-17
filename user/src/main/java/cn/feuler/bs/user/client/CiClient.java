package cn.feuler.bs.user.client;

import cn.feuler.bs.user.dataobject.Citmst;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@FeignClient(name = "client")
public interface CiClient {

    @GetMapping("/ciserver/getCiList")
    List<Citmst> getCiList();

    @PostMapping("/ciserver/getCiInfo")
    Citmst getCiInfo(@RequestBody String ciNo);

    @PostMapping("/ciserver/updateCiInfo")
    Citmst updateCiInfo(@RequestBody Citmst citmst);


}
