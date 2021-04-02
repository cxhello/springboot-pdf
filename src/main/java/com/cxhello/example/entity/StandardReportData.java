package com.cxhello.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author caixiaohui
 * @date 2021/3/24
 */
@Data
public class StandardReportData {

    /**
     * 工艺单元报告数据
     */
    private List<ProcessUnitReportData> processUnitReportDataList;

    /**
     * 处理效率报告数据
     */
    private List<ProcessEfficiencyReportData> processEfficiencyReportDataList;

    /**
     * 排放标准分析报告数据
     */
    private List<EmissionStandardReportData> emissionStandardReportDataList;

    /**
     * 综合性结论
     */
    private String conclusion;

    /**
     * 处理效率word数据
     */
    @JsonIgnore
    private List<ProcessEfficiencyWordData> processEfficiencyWordDataList;

    public List<ProcessEfficiencyWordData> getProcessEfficiencyWordDataList() {
        List<ProcessEfficiencyWordData> processEfficiencyWordDataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.processEfficiencyReportDataList)) {
            for (ProcessEfficiencyReportData processEfficiencyReportData : this.processEfficiencyReportDataList) {
                String uuid = UUID.randomUUID().toString();
                if (CollectionUtils.isNotEmpty(processEfficiencyReportData.getProcessEfficiencyParameterList())) {
                    for (ProcessEfficiencyParameter processEfficiencyParameter : processEfficiencyReportData.getProcessEfficiencyParameterList()) {
                        ProcessEfficiencyWordData processEfficiencyWordData = new ProcessEfficiencyWordData();
                        processEfficiencyWordData.setUuid(uuid);
                        processEfficiencyWordData.setProcessSectionName(processEfficiencyReportData.getProcessSectionName());
                        processEfficiencyWordData.setName(processEfficiencyParameter.getName());
                        processEfficiencyWordData.setValue(processEfficiencyParameter.getValue());
                        processEfficiencyWordDataList.add(processEfficiencyWordData);
                    }
                }
            }
        }
        return processEfficiencyWordDataList;
    }
}
