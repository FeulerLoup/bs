package cn.feuler.bs.basic.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@Data
public class LevelVO {

    @JsonProperty("name")
    private String tlrName;

    @JsonProperty("level")
    private String lvlNo;

    @JsonProperty("permissions")
    List<PermissionVO> permissionVOList;
}
