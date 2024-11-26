package panomete.playground.docengine.service;

import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

import java.io.File;

public interface ReportGenerateService {
    Mono<Void> generatePersonReport();
    Mono<File> generatePersonReportV2();
    Mono<Resource> generatePersonReportV3();
}
