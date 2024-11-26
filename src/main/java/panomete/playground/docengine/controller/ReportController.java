package panomete.playground.docengine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import panomete.playground.docengine.service.ReportGenerateService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportGenerateService reportGenerateService;

    // version 1 download directly after finish procedure
    @GetMapping("/api/v1/report/person")
    public Mono<ResponseEntity<Void>> generatePersonReport() {
        return reportGenerateService.generatePersonReport()
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    // version 2 download after finish procedure
    @GetMapping("/api/v2/report/person")
    public Mono<ResponseEntity<FileSystemResource>> getPersonReport() {
        return reportGenerateService.generatePersonReportV2().flatMap(
                pdfFile -> {
                    FileSystemResource fileSystemResource = new FileSystemResource(pdfFile);
                    HttpHeaders headers = new HttpHeaders();
                    String encodedFileName = URLEncoder.encode(pdfFile.getName(), StandardCharsets.UTF_8);
                    headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
                    headers.add("Content-Type", "application/pdf");
                    return Mono.just(
                            ResponseEntity.ok()
                                    .headers(headers)
                                    .body(fileSystemResource)
                    ).doFinally(signalType -> {
                        log.info(pdfFile.getAbsolutePath());
                        if (pdfFile.delete()) {
                            log.info("File {} deleted successfully", pdfFile.getName());
                        } else {
                            log.error("Failed to delete file {}", pdfFile.getName());
                        }
                    }) ;
                });
    }

    // version 3 show download after finish procedure
    @GetMapping("/api/v3/report/person")
    public Mono<ResponseEntity<Resource>> getPersonReportV3() {
        return Mono.zip(
                reportGenerateService.generatePersonReportV3(),
                getPDFFileName()
        ).flatMap(tuple -> {
            Resource resource = tuple.getT1();
            String fileName = tuple.getT2();
            HttpHeaders headers = new HttpHeaders();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            try {
                return Mono.just(
                        ResponseEntity.ok()
                                .headers(headers)
                                .contentLength(resource.contentLength())
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .body(resource)
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // version 4 show pdf in new tab before download
    @GetMapping("/api/v4/report/person")
    public Mono<ResponseEntity<Resource>> getPersonReportV4() {
        return Mono.zip(
                reportGenerateService.generatePersonReportV3(),
                getPDFFileName()
        ).flatMap(tuple -> {
            Resource resource = tuple.getT1();
            String fileName = tuple.getT2();
            HttpHeaders headers = new HttpHeaders();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            headers.add("Content-Disposition", "inline; filename*=UTF-8''" + encodedFileName);
            try {
                return Mono.just(
                        ResponseEntity.ok()
                                .headers(headers)
                                .contentLength(resource.contentLength())
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(resource)
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Mono<String> getPDFFileName() {
        return Mono.just("report-" + "นาคาชา" + "-" + System.currentTimeMillis() + ".pdf");
    }
}
