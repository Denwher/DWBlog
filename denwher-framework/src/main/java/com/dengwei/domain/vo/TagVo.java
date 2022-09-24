package com.dengwei.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Denwher
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagVo {

    //id
    private Long id;
    //标签名
    private String name;

    //备注
    private String remark;
}
