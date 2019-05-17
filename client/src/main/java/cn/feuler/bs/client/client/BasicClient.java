package cn.feuler.bs.client.client;

import cn.feuler.bs.client.dataobject.Bptlvl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@FeignClient(name = "basic")
public interface BasicClient {

    @PostMapping("/getPermission")
    Bptlvl getPermission(@RequestBody String lvlNo);

}
