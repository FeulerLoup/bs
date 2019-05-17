package cn.feuler.bs.user.controller;

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
        map.addAttribute("msg", "访问的路径不存在,请重新登录");
        map.addAttribute("uri", "/user/login");
        return getErrorPath();
    }

}
