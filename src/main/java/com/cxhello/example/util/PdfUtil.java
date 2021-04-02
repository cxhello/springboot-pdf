package com.cxhello.example.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author caixiaohui
 * @date 2021/3/26
 */
public class PdfUtil {

    private static final Logger logger = LoggerFactory.getLogger(PdfUtil.class);

    public static void covertDocToPdf(File file, String fileName, HttpServletResponse response) {
        response.setContentType("application/pdf");
        try {
            fileName = fileName + ".pdf";
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(file);
            setFontMapper(wordprocessingMLPackage);
            Docx4J.toPDF(wordprocessingMLPackage, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file.delete(); // 删除临时文件
            }
        }
    }

    public static void covertDocToPdfSecond(File file, String fileName, HttpServletResponse response) {
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

    private static void setFontMapper(WordprocessingMLPackage wordprocessingMLPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
        fontMapper.put("等线", PhysicalFonts.get("SimSun"));
        fontMapper.put("等线 Light", PhysicalFonts.get("SimSun"));
        fontMapper.put("华文琥珀", PhysicalFonts.get("STHupo"));
        fontMapper.put("华文隶书", PhysicalFonts.get("STLiti"));
        fontMapper.put("华文新魏", PhysicalFonts.get("STXinwei"));
        fontMapper.put("华文彩云", PhysicalFonts.get("STCaiyun"));
        fontMapper.put("方正姚体", PhysicalFonts.get("FZYaoti"));
        fontMapper.put("方正舒体", PhysicalFonts.get("FZShuTi"));
        fontMapper.put("华文细黑", PhysicalFonts.get("STXihei"));
        fontMapper.put("宋体扩展",PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋_GB2312",PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("新細明體",PhysicalFonts.get("SimSun"));
        wordprocessingMLPackage.setFontMapper(fontMapper);
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
