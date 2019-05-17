package cn.feuler.bs.financial.utils;

import cn.feuler.bs.financial.VO.ResultVO;

/**
 * Create by: Feuler
 * Date: 2019/3/25
 **/

public class ResultVOUtil {
    public static ResultVO success(Object object){
        ResultVO resultVO= new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }
    public static ResultVO fail(Object object){
        ResultVO resultVO= new ResultVO();
        resultVO.setCode(-1);
        resultVO.setMsg("失败");
        resultVO.setData(object);
        return resultVO;
    }
}
