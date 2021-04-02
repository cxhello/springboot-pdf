package com.cxhello.example.entity;

import lombok.Data;

/**
 * @author caixiaohui
 * @date 2021/3/29
 */
@Data
public class ProcessEfficiencyWordData {

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 工艺段名称
     */
    private String processSectionName;

    /**
     * 名称
     */
    private String name;

    /**
     * 数值
     */
    private String value;

}
