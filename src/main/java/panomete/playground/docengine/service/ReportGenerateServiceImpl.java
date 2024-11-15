package panomete.playground.docengine.service;

import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import panomete.playground.docengine.entity.Person;
import panomete.playground.docengine.utils.MockServiceUtil;
import panomete.playground.docengine.utils.ReportUtils;
import pro.verron.officestamper.api.OfficeStamperConfiguration;
import pro.verron.officestamper.api.StreamStamper;
import pro.verron.officestamper.preset.OfficeStampers;
import reactor.core.publisher.Mono;

import java.io.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static panomete.playground.docengine.utils.EvaluationContexts.enableMapAccess;
import static pro.verron.officestamper.preset.OfficeStamperConfigurations.standard;


@Service
@RequiredArgsConstructor
public class ReportGenerateServiceImpl implements ReportGenerateService {
    private final MockServiceUtil mockServiceUtil;
    @Override
    public Mono<Void> generatePersonReport() {
        return mockServiceUtil.mockPersonAsync()
                .flatMap(this::reportMapper);
    }

    @Override
    public Mono<File> generatePersonReportV2() {
        return mockServiceUtil.mockPersonAsync()
                .flatMap(this::reportMapperFile);
    }

    private Mono<Void> reportMapper(Person person) {
        return Mono.defer(() -> {
            // setup report generator
            OfficeStamperConfiguration configuration = standard().setEvaluationContextConfigurer(enableMapAccess());
            StreamStamper<WordprocessingMLPackage> stamper = OfficeStampers.docxStamper(configuration);
            InputStream template = ReportUtils.streamResource("report.docx");

            String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            // map context from the template
            Map<String, Object> context = new HashMap<>();
            context.put("reportDate", now);
            context.put("reportUser", person.firstName().concat(" ").concat(person.lastName()));
            context.put("nationalId", person.nationalId());
            context.put("dateOfBirth", person.dateOfBirth().toString());
            context.put("address1", person.address().addressLine1());
            context.put("address2", person.address().addressLine2());
            context.put("male", person.isMale());
            context.put("female", person.isFemale());
            context.put("other", person.isOther());
            context.put("young", person.isYoung());
            context.put("adult", person.isAdult());
            context.put("old", person.isOld());
            context.put("todos", person.todos());


            // generate docx
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            stamper.stamp(template, context, outputStream);

            // convert to pdf
            ReportUtils.convertToPDF(outputStream, person.firstName());

            return Mono.empty();
        });
    }

    private Mono<File> reportMapperFile(Person person) {
        return Mono.defer(() -> {
            // setup report generator
            OfficeStamperConfiguration configuration = standard().setEvaluationContextConfigurer(enableMapAccess());
            StreamStamper<WordprocessingMLPackage> stamper = OfficeStampers.docxStamper(configuration);
            InputStream template = ReportUtils.streamResource("report.docx");

            String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            // map context from the template
            Map<String, Object> context = new HashMap<>();
            context.put("reportDate", now);
            context.put("reportUser", person.firstName().concat(" ").concat(person.lastName()));
            context.put("nationalId", person.nationalId());
            context.put("dateOfBirth", person.dateOfBirth().toString());
            context.put("address1", person.address().addressLine1());
            context.put("address2", person.address().addressLine2());
            context.put("male", person.isMale());
            context.put("female", person.isFemale());
            context.put("other", person.isOther());
            context.put("young", person.isYoung());
            context.put("adult", person.isAdult());
            context.put("old", person.isOld());
            context.put("todos", person.todos());


            // generate docx
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            stamper.stamp(template, context, outputStream);

            // convert to pdf
            File outputFile = ReportUtils.convertToPDFFile(outputStream, person.firstName());

            return Mono.just(outputFile);
        });
    }
}
