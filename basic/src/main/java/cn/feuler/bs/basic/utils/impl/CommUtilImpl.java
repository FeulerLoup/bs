package cn.feuler.bs.basic.utils.impl;

import cn.feuler.bs.basic.utils.CommUtil;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by: Feuler
 * Date: 2019/4/9
 **/

@Component
public class CommUtilImpl implements CommUtil {


    @Override
    public String getDateTime() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String datefmt = date.format(new Date());
        return datefmt;
    }


}
