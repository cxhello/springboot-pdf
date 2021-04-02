package com.cxhello.example.entity;

import lombok.Data;

/**
 * @author cxhello
 * @date 2021/4/2
 */
@Data
public class DeviceMeterWordData {

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 设备名称
     */
    private String deviceName;

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
