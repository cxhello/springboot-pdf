package com.cxhello.example.entity;

import lombok.Data;

/**
 * @author cxhello
 * @date 2021/4/2
 */
@Data
public class DeviceMeterAlarmWordData {

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
     * 报警次数
     */
    private Integer alarmCount;

}
