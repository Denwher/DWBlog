package com.dengwei.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Denwher
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {
    //menu的id
    private Long id;

    //menu父id
    private Long parentId;

    //menu名称
    private String label;

    //子menu合集
    private List<MenuTreeVo> children;

}
