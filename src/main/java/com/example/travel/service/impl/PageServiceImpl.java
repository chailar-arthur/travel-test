package com.example.travel.service.impl;

import com.example.travel.TravelApplication;
import com.example.travel.entity.Spot;
import com.example.travel.service.PageService;
import com.example.travel.utils.PageEventHandler;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.core.io.resource.ResourceUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;
@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private TemplateEngine templateEngine;
    @Override
    public void generatePdf(OutputStream outputStream, Map<String,Object> map) throws IOException {
        Context context = new Context();
        context.setVariable("spots", map.get("spots"));
        String pdf = templateEngine.process("pdf", context);

        // 生成 PDF, 并添加页码
        try (PdfWriter pdfWriter = new PdfWriter(outputStream); PdfDocument pdfDocument = new PdfDocument(pdfWriter)) {
            pdfDocument.setDefaultPageSize(PageSize.A4);
            pdfDocument.addEventHandler(PdfDocumentEvent.INSERT_PAGE, new PageEventHandler());

            ConverterProperties converterProperties = new ConverterProperties();
            FontProvider fontProvider = new DefaultFontProvider(true, true, false);
            byte[] bs = null;
            try (InputStream in = TravelApplication.class.getClassLoader().getResourceAsStream("static/fonts/SimHei.ttf")) {
                bs = IOUtils.toByteArray(in);
            }
            fontProvider.addFont(FontProgramFactory.createFont(bs));
            converterProperties.setFontProvider(fontProvider);
            HtmlConverter.convertToPdf(pdf, pdfDocument, converterProperties);
        } catch (IOException e) {
            throw e;
        }
    }
}
