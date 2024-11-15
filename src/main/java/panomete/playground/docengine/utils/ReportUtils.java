package panomete.playground.docengine.utils;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;
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

    public static void convertToPDF(ByteArrayOutputStream byteArrayOutputStream, String preFix) {
        try {
                WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                OutputStream out = createOutputStreamPDF(preFix);
                Docx4J.toPDF(wordprocessingMLPackage, out);
            } catch (Docx4JException e) {
                throw new RuntimeException(e);
            }
    }

    public static File convertToPDFFile(ByteArrayOutputStream byteArrayOutputStream, String preFix) {
        try {
            WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            File pdfFile = new File(fileName(preFix));
            OutputStream out = new FileOutputStream(pdfFile);
            Docx4J.toPDF(wordprocessingMLPackage, out);
            return pdfFile;
        } catch (FileNotFoundException | Docx4JException e) {
            throw new RuntimeException(e);
        }
    }

    private static String fileName(String prefix) {
        return  "src/main/resources/output/" + format("%s-report-%s.pdf", prefix, System.currentTimeMillis());
    }
}
