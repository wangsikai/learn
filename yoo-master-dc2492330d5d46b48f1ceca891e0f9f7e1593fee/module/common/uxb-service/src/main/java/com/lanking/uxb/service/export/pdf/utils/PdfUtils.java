package com.lanking.uxb.service.export.pdf.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import httl.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

/**
 * pdf生成工具,依赖于itextpdf
 */
public class PdfUtils {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期

    private volatile static PdfUtils singleton;
    private PdfUtils (){}
    public static PdfUtils getSingleton() {
        if (singleton == null) {
            synchronized (PdfUtils.class) {
                if (singleton == null) {
                    singleton = new PdfUtils();
                }
            }
        }
        return singleton;
    }

    /**
     * 生成pdf 文件名称，格式为${yyyy-MM-dd}.pdf
     * @return
     */
    private String genPdfName(){
        String dateTime = dateFm.format(new java.util.Date());
        return dateTime+".pdf";
    }

    /**
     * 在指定目录下,创建pdf文档
     * @param imgList 图片列表
     * @param pdfPath 指定pdf 生成路径父目录
     * @return 生成pdf文件名(全路径),或者创建失败返回null
     * @throws Exception 输入图片文件名不存在或非法
     */
    public String buildPdfBy2Imgs(List<String> imgList, String pdfPath) throws Exception {
        if (imgList == null || imgList.size() < 1 || StringUtils.isBlank(pdfPath)) {
            throw new RuntimeException("build pdf,invalid input args");
        }
        Document document = null;
        PdfWriter pdfWriter = null;

        String dstPdf;
        if (pdfPath.endsWith("/")) {
            dstPdf = pdfPath + genPdfName();
        } else {
            dstPdf = pdfPath + "/" + genPdfName();
        }

        try {
            // 1 创建pdf document 对象
            document = new Document(PageSize.A4, 50, 50, 50, 50);

            // 2 创建pdfWriter对象，对document操作
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(dstPdf));

            // 3 打开pdf doc对象
            document.open();
            Iterator iter = imgList.iterator();
            Image img = Image.getInstance((String)iter.next());
            // 设置图片间距
            img.scaleAbsolute(495f, 742f);
            document.add(img);
            while (iter.hasNext()) {
                document.newPage();
                img = Image.getInstance((String)iter.next());
                // 设置图片间距
                img.scaleAbsolute(495f, 742f);
                document.add(img);
            }
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            } catch (Exception e) {
                logger.error("build pdf ocur exception: {}",e);
                File dstFile = new File(dstPdf);
                if (dstFile.exists()) {
                    dstFile.delete();
                }
                dstPdf = null;
            }
        }
        return dstPdf;
    }

}
