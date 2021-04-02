package com.cxhello.example.entity;

import lombok.Data;

/**
 * 工艺参数报警
 * @author caixiaohui
 * @date 2021/3/24
 */
@Data
public class ProcessParameterAlarmReportData {

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 报警次数
     */
    private Integer alarmCount;

}
