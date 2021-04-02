package com.cxhello.example.entity;

import lombok.Data;

/**
 * @author caixiaohui
 * @date 2021/3/24
 */
@Data
public class DeviceParameterReportData {

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
