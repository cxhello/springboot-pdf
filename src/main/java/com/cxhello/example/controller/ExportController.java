package com.cxhello.example.controller;

import com.alibaba.fastjson.JSON;
import com.cxhello.example.entity.StandardReportData;
import com.cxhello.example.util.PdfUtil;
import com.cxhello.example.util.WordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    private static final Logger logger = LoggerFactory.getLogger(ExportController.class);

    @GetMapping("/exportPdf")
    public void exportPdf(HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String waterName = "测试水厂";
        map.put("waterName", waterName);
        map.put("time", "2021-03-23");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        map.put("date", date);
        Resource resource = new ClassPathResource("test.json");
        InputStream is = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            is = resource.getInputStream();
            inputStreamReader = new InputStreamReader(is);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder str = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                str.append(s);
            }
            StandardReportData standardReportData = JSON.parseObject(str.toString(), StandardReportData.class);
            map.put("standardReportData", standardReportData);
            File file = WordUtil.generateWord("template.ftl", map);
            PdfUtil.covertDocToPdfSecond(file, waterName + "标准报告", response);
        } catch (IOException e) {
            logger.error("System error", e);
        } finally {
            try {
                is.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                logger.error("System error", e);
            }
        }
    }

}
