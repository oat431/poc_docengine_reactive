package panomete.playground.docengine.config;

import lombok.extern.slf4j.Slf4j;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;

@Slf4j
@Component
public class ApplicationConfig {

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        registerFonts();
    }

    public void registerFonts() {
        try {
            log.info("Registering fonts");
            File fontFolder = new File("src/main/resources/font");
            File[] fontFiles = fontFolder.listFiles((dir, name) -> name.endsWith(".ttf") || name.endsWith(".otf"));
            if (fontFiles != null) {
                for (File fontFile : fontFiles) {
                    log.info("Registering font: {}", fontFile.getName());
                    PhysicalFonts.addPhysicalFont(fontFile.toURI());
                }
            }
            PhysicalFonts.discoverPhysicalFonts();
            log.info("Fonts registered successfully");
        } catch (Exception e) {
            log.error("Failed to register fonts", e);
        }
    }

    protected void configSimSunFont(WordprocessingMLPackage wordMLPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        wordMLPackage.setFontMapper(fontMapper);

        String fontFamily = "Sarabun";

        URL simsunUrl = this.getClass().getResource("/font/Sarabun-SemiBold.ttf");
        PhysicalFonts.addPhysicalFonts(fontFamily, simsunUrl.toURI());
        PhysicalFont simsunFont = PhysicalFonts.get(fontFamily);
        fontMapper.put(fontFamily, simsunFont);

        wordMLPackage.setFontMapper(fontMapper);

//        RFonts rfonts = Context.getWmlObjectFactory().createRFonts();
//        rfonts.setAsciiTheme(null);
//        rfonts.setAscii(fontFamily);
//        wordMLPackage.getMainDocumentPart().getPropertyResolver()
//                .getDocumentDefaultRPr().setRFonts(rfonts);
    }
}
