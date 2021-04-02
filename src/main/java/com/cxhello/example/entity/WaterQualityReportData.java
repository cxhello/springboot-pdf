package com.cxhello.example.entity;

import lombok.Data;

/**
 * 水质水量
 * @author caixiaohui
 * @date 2021/3/24
 */
@Data
public class WaterQualityReportData {

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 计算方式
     */
    private String mode;

    /**
     * 数值
     */
    private String value;

}
