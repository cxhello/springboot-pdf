package com.cxhello.example.entity;

import lombok.Data;

import java.util.List;

/**
 * 设备仪表报警
 * @author caixiaohui
 * @date 2021/3/24
 */
@Data
public class DeviceMeterAlarmReportData {

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备参数
     */
    private List<DeviceParameterAlarmReportData> deviceParameterAlarmReportDataList;

}
