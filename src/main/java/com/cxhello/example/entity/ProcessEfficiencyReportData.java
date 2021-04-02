package com.cxhello.example.entity;

import lombok.Data;

import java.util.List;

/**
 * @author cxhello
 * @date 2021/4/2
 */
@Data
public class ProcessEfficiencyReportData {

    /**
     * 工艺段名称
     */
    private String processSectionName;

    /**
     * 参数名称
     */
    private List<ProcessEfficiencyParameter> processEfficiencyParameterList;

}
