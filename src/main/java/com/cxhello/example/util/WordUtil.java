package com.cxhello.example.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author caixiaohui
 * @date 2021/3/26
 */
public class WordUtil {

    private static final String ENCODING = "UTF-8";

    /**
     * 生成word文档
     * @param templateName
     * @param map
     * @return
     * @throws IOException
     */
    public static File generateWord(String templateName, Map map) throws IOException {
        Configuration configuration = getConfiguration();
        Template template = configuration.getTemplate(templateName);
        File file = createDoc(map, template);
        return file;
    }

    /**
     * 导出word文档
     * @param templateName
     * @param map
     * @param title
     * @param response
     * @throws IOException
     */
    public static void exportWord(String templateName, Map map, String title, HttpServletResponse response) throws IOException {
        Configuration configuration = getConfiguration();
        Template template = configuration.getTemplate(templateName);
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            // 调用工具类的createDoc方法生成Word文档
            file = createDoc(map, template);
            fin = new FileInputStream(file);

            response.setCharacterEncoding(ENCODING);
            response.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件名
            String fileName = title + ".doc";
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

            out = response.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (out != null) {
                out.close();
            }
            if (file != null) {
                file.delete(); // 删除临时文件
            }
        }
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding(ENCODING);
        configuration.setClassForTemplateLoading(WordUtil.class, "/templates/");
        return configuration;
    }

    private static File createDoc(Map<?, ?> dataMap, Template template) {
        String name = "sellPlan.doc";
        File f = new File(name);
        Template t = template;
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), ENCODING);
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }

}
