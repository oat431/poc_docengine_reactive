package panomete.playground.docengine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import panomete.playground.docengine.service.ReportGenerateService;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportGenerateService reportGenerateService;

    @GetMapping("/api/v1/report/person")
    public Mono<ResponseEntity<Void>> generatePersonReport() {
        return reportGenerateService.generatePersonReport()
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping("/api/v2/report/person")
    public Mono<ResponseEntity<FileSystemResource>> getPersonReport() {
        return reportGenerateService.generatePersonReportV2().flatMap(
                pdfFile -> {
                    FileSystemResource fileSystemResource = new FileSystemResource(pdfFile);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Disposition", "attachment; filename=" + pdfFile.getName());
                    headers.add("Content-Type", "application/pdf");
                    return Mono.just(
                            ResponseEntity.ok()
                                    .headers(headers)
                                    .body(fileSystemResource)
                    ).doFinally(signalType -> {
                        boolean isDelete = pdfFile.delete() ;
                        if (!isDelete) {
                            log.error("Failed to delete file {}", pdfFile.getName());
                        } else {
                            log.info("File {} deleted successfully", pdfFile.getName());
                        }
                    }) ;
                });
    }

}
