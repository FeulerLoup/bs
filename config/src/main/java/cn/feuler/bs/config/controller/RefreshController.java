package cn.feuler.bs.config.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Create by: Feuler
 * Date: 2019/4/1
 **/

@RestController
public class RefreshController {
    @PostMapping("/actuator/fix-bus-refresh")
    @ResponseBody
    public Object fixBusRefresh(HttpServletRequest request, @RequestBody(required = false) String s) {
        System.out.println(s);
        return new ModelAndView("/actuator/bus-refresh");
    }
}
