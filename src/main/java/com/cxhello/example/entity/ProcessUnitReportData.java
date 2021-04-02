package com.cxhello.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 工艺单元
 * @author caixiaohui
 * @date 2021/3/24
 */
@Data
public class ProcessUnitReportData {

    /**
     * 工艺单元名称
     */
    private String processUnitName;

    /**
     * 水质水量报告数据
     */
    private List<WaterQualityReportData> waterQualityReportDataList;

    /**
     * 设备仪表报告数据
     */
    private List<DeviceMeterReportData> deviceMeterReportDataList;

    /**
     * 工艺参数报警报告数据
     */
    private List<ProcessParameterAlarmReportData> processParameterAlarmReportDataList;

    /**
     * 设备仪表报警报告数据
     */
    private List<DeviceMeterAlarmReportData> deviceMeterAlarmReportDataList;

    /**
     * 设备仪表word数据
     */
    @JsonIgnore
    private List<DeviceMeterWordData> deviceMeterWordDataList;

    /**
     * 设备仪表报警word数据
     */
    @JsonIgnore
    private List<DeviceMeterAlarmWordData> deviceMeterAlarmWordDataList;

    public List<DeviceMeterWordData> getDeviceMeterWordDataList() {
        List<DeviceMeterWordData> deviceMeterWordDataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.deviceMeterReportDataList)) {
            for (DeviceMeterReportData deviceMeterReportData : this.deviceMeterReportDataList) {
                String uuid = UUID.randomUUID().toString();
                if (CollectionUtils.isNotEmpty(deviceMeterReportData.getDeviceParameterReportDataList())) {
                    for (DeviceParameterReportData deviceParameterReportData : deviceMeterReportData.getDeviceParameterReportDataList()) {
                        DeviceMeterWordData deviceMeterWordData = new DeviceMeterWordData();
                        deviceMeterWordData.setUuid(uuid);
                        deviceMeterWordData.setDeviceName(deviceMeterReportData.getDeviceName());
                        deviceMeterWordData.setParameterName(deviceParameterReportData.getParameterName());
                        deviceMeterWordData.setMode(deviceParameterReportData.getMode());
                        deviceMeterWordData.setValue(deviceParameterReportData.getValue());
                        deviceMeterWordDataList.add(deviceMeterWordData);
                    }
                }

            }
        }
        return deviceMeterWordDataList;
    }

    public List<DeviceMeterAlarmWordData> getDeviceMeterAlarmWordDataList() {
        List<DeviceMeterAlarmWordData> deviceMeterAlarmWordDataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.deviceMeterAlarmReportDataList)) {
            for (DeviceMeterAlarmReportData deviceMeterAlarmReportData : this.deviceMeterAlarmReportDataList) {
                String uuid = UUID.randomUUID().toString();
                if (CollectionUtils.isNotEmpty(deviceMeterAlarmReportData.getDeviceParameterAlarmReportDataList())) {
                    for (DeviceParameterAlarmReportData deviceParameterAlarmReportData : deviceMeterAlarmReportData.getDeviceParameterAlarmReportDataList()) {
                        DeviceMeterAlarmWordData deviceMeterAlarmWordData = new DeviceMeterAlarmWordData();
                        deviceMeterAlarmWordData.setUuid(uuid);
                        deviceMeterAlarmWordData.setDeviceName(deviceMeterAlarmReportData.getDeviceName());
                        deviceMeterAlarmWordData.setParameterName(deviceParameterAlarmReportData.getParameterName());
                        deviceMeterAlarmWordData.setAlarmCount(deviceParameterAlarmReportData.getAlarmCount());
                        deviceMeterAlarmWordDataList.add(deviceMeterAlarmWordData);
                    }
                }
            }
        }
        return deviceMeterAlarmWordDataList;
    }

}
