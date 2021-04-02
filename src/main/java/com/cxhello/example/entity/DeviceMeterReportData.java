package com.cxhello.example.entity;

import lombok.Data;

import java.util.List;

/**
 * 设备仪表
 * @author cxhello
 * @date 2021/4/2
 */
@Data
public class DeviceMeterReportData {

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备参数
     */
    private List<DeviceParameterReportData> deviceParameterReportDataList;

}
