package com.cxhello.example.controller;

import com.alibaba.fastjson.JSON;
import com.cxhello.example.entity.StandardReportData;
import com.cxhello.example.util.PdfUtil;
import com.cxhello.example.util.WordUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cxhello
 * @date 2021/4/2
 */
@Controller
public class ExportController {

    @GetMapping("/exportPdf")
    public void exportPdf(HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String waterName = "测试水厂";
        map.put("waterName", waterName);
        map.put("time", "2021-03-23");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        map.put("date", date);
        File jsonFile = ResourceUtils.getFile("classpath:test.json");
        String json = FileUtils.readFileToString(jsonFile, "UTF-8");
        StandardReportData standardReportData = JSON.parseObject(json, StandardReportData.class);
        map.put("standardReportData", standardReportData);
        File file = WordUtil.generateWord("template.ftl", map);
        PdfUtil.covertDocToPdfSecond(file, waterName + "标准报告", response);
    }

}
