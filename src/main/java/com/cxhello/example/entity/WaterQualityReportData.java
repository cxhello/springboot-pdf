package com.cxhello.example.entity;

import lombok.Data;

/**
 * 水质水量
 * @author cxhello
 * @date 2021/4/2
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
