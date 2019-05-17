package cn.feuler.bs.user.utils.impl;

import cn.feuler.bs.user.utils.CommUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

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

    @Override
    public String md5Hex(String passwd) {
        return DigestUtils.md5DigestAsHex(passwd.getBytes());
    }


}
