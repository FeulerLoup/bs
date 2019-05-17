package cn.feuler.bs.basic.utils;

import cn.feuler.bs.basic.VO.ResultVO;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

public class ResultVOUtils {

    public static ResultVO Success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }
}
