package com.dengwei.domain.dto;

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
public class TagListDto {
    //主键
    private Long id;
    //标签名
    private String name;

    //备注
    private String remark;
}
