# Springboot导出PDF文件

### 需求描述

最近项目有一个需求，需要导出 PDF 文件，由于之前并没有做过这方面的需求，从网上花了很长时间才找到相关的资料。最后采用了`aspose.words + freemarker`的方式解决的问题，制作 freemarker 模板，将 freemarker 模板转为 word 文件。然后再使用 aspose.words 将 word 文件转为 PDF文件。一开始也尝试过使用 docx4j 将 word 文件转为 PDF，但转完正文中文乱码、还有其他各种格式问题，最后并没有采用。

### 所需依赖

由于 aspose.words 是破解版，在互联网仓库无法下载，需手动将 jar 包引入本地库，如果公司有私服的话也可以上传到自己公司的私服，引入本地库命令如下：

```bash
mvn install:install-file -DgroupId=com.aspose -DartifactId=aspose.words -Dversion=15.8 -Dpackaging=jar -Dfile=aspose-words-15.8.0-jdk16.jar
```

```xml
<!-- freemarker模板引擎，用于定义代码生成模板 -->
<dependency>
	<groupId>org.freemarker</groupId>
	<artifactId>freemarker</artifactId>
	<version>2.3.28</version>
</dependency>

<dependency>
	<groupId>com.aspose</groupId>
	<artifactId>aspose.words</artifactId>
	<version>15.8</version>
</dependency>
```

### 制作freemarker模板

在 word 中编写你想要的文档，将 word 文档另存为 xml 格式，将需要注入数据的地方修改为 `${xxx}` 格式，并且记住你的字段名，如果有多条数据则添加 `<#list userList as user></#list> `标签，userList 是 Map 里面的 key。

![image-20210402142437815](https://cxhello.oss-cn-beijing.aliyuncs.com/image/image-20210402142437815.png)

![image-20210402142525047](https://cxhello.oss-cn-beijing.aliyuncs.com/image/image-20210402142525047.png)

![image-20210402142552739](https://cxhello.oss-cn-beijing.aliyuncs.com/image/image-20210402142552739.png)

编辑好之后另存为 `.ftl` 文件，这就是模板。可以存放到项目里的 resource 下的 `templates` 文件夹下

![image-20210402142741541](https://cxhello.oss-cn-beijing.aliyuncs.com/image/image-20210402142741541.png)

### word文档生成工具类

```java
package com.cxhello.example.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

/**
 * @author cxhello
 * @date 2021/4/2
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
```

### word转换PDF工具类

```java
package com.cxhello.example.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author cxhello
 * @date 2021/4/2
 */
public class PdfUtil {

    private static final Logger logger = LoggerFactory.getLogger(PdfUtil.class);

    public static void covertDocToPdf(File file, String fileName, HttpServletResponse response) {
        response.setContentType("application/pdf");
        try {
            if (!isWordLicense()) {
                logger.error("License验证不通过");
            }
            fileName = fileName + ".pdf";
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            FileInputStream fileInputStream = new FileInputStream(file);
            Document doc = new Document(fileInputStream);
            fileInputStream.close();
            doc.save(response.getOutputStream(), SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    public static boolean isWordLicense() {
        boolean result = false;
        try {
            InputStream inputStream = PdfUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License license = new License();
            license.setLicense(inputStream);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
```

### 业务类

```java
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
```

### Linux导出中文乱码问题

在 Linux 服务器使用 aspose.word 转换 word 文件为 pdf 的时候显示中文乱码，但是在 windows 服务器上使用可以正常转换。通过查资料分析后确认是由于 Linux 服务器缺少对应的字库导致文件转换出现乱码的。

安装字库，将 windows 机器的 `C:\Windows\fonts` 目录下的全部文件拷贝到生产服务器字体安装目录下，然后执行以下命令更新字体缓存。

```bash
# 查看linux目前的所有字体
fc-list
# 查看Linux目前的所有中文字体
fc-list :lang=zh
# 拷贝到linux下的字体目录
mkdir /usr/share/fonts/windows
cp /local/src/fonts/* /usr/share/fonts/windows
# 执行安装字体命令
cd /usr/share/fonts
sudo mkfontscale
sudo mkfontdir 
sudo fc-cache -fv
# 执行命令让字体生效
source /etc/profile
# 如果安装失败，可以考虑修改字体权限
chmod 755 *.ttf
```

### 代码地址

> https://github.com/cxhello/springboot-pdf

### 参考文章

> https://blog.csdn.net/tomcat_zhu/article/details/89397044

> https://blog.csdn.net/hunwanjie/article/details/97135633

> https://blog.csdn.net/weixin_44723536/article/details/91445959?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control&dist_request_id=1328741.37842.16169854640636425&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control

> https://cloud.tencent.com/developer/article/1639923

> https://www.jianshu.com/p/86716c7122ef

> https://www.cnblogs.com/stsinghua/p/13558544.html