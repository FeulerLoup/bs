package cn.feuler.bs.gateway.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@Controller
@RequestMapping("/error")
public class ErrController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "jump";
    }

    @RequestMapping
    public String errPage(ModelMap map) {
        map.addAttribute("msg", "网关错误，访问失败");
        map.addAttribute("uri", "client/login");
        return getErrorPath();
    }

}
