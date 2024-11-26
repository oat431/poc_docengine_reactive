package panomete.playground.docengine.utils;

import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

public class ReportUtils {
    public static InputStream streamResource(String reportTemplate) {
        try {
            return Files.newInputStream(Path.of("src/main/resources/template/".concat(reportTemplate)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource", e);
        }
    }

    public static OutputStream createOutputStreamPDF(String prefix) {
        try {
            Path outputPath = Paths.get(
                    "src/main/resources/output/",
                    format("%s-report-%s.pdf", prefix,System.currentTimeMillis())
            );
            return Files.newOutputStream(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create output stream", e);
        }
    }

    // version 1 download directly in to project output folder
    public static void convertToPDF(ByteArrayOutputStream byteArrayOutputStream, String preFix) {
        try {
            Mapper fontMapper = new IdentityPlusMapper();
            WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            wordprocessingMLPackage.setFontMapper(fontMapper);

            String fontFamily = "Sarabun";

            URI sarabunUri = new File("src/main/resources/font/Sarabun-SemiBold.ttf").toURI();
            PhysicalFonts.addPhysicalFonts(fontFamily, sarabunUri);
            PhysicalFont sarabunFont = PhysicalFonts.get(fontFamily);
            fontMapper.put(fontFamily, sarabunFont);
            wordprocessingMLPackage.setFontMapper(fontMapper);

            OutputStream out = createOutputStreamPDF(preFix);
            Docx4J.toPDF(wordprocessingMLPackage, out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // version 2 send file via api and delete after download, also download font from folder
    public static File convertToPDFFile(ByteArrayOutputStream byteArrayOutputStream, String preFix) {
        try {
            Mapper fontMapper = new IdentityPlusMapper();
            WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            wordprocessingMLPackage.setFontMapper(fontMapper);

            String fontFamily = "Sarabun";

            File fontFolder = new File("src/main/resources/font");
            File[] fontFiles = fontFolder.listFiles((dir, name) -> name.endsWith(".ttf") || name.endsWith(".otf"));
            PhysicalFont sarabunFont = PhysicalFonts.get(fontFamily);
            if (fontFiles != null) {
                for (File fontFile : fontFiles) {
                    PhysicalFonts.addPhysicalFonts(fontFamily,fontFile.toURI());
                    fontMapper.put(fontFamily, sarabunFont);
                    wordprocessingMLPackage.setFontMapper(fontMapper);
                }
            }

            File pdfFile = new File(fileName(preFix));
            OutputStream out = new FileOutputStream(pdfFile);
            Docx4J.toPDF(wordprocessingMLPackage, out);
            return pdfFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String fileName(String prefix) {
        return  "src/main/resources/output/" + format("%s-report-%s.pdf", prefix, System.currentTimeMillis());
    }

    public static byte[] convertToPDFStream(ByteArrayOutputStream byteArrayOutputStream) {
        try {
            WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Docx4J.toPDF(wordprocessingMLPackage, out);
            return out.toByteArray();
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
    }
}
