package panomete.playground.docengine.service;

import reactor.core.publisher.Mono;

import java.io.File;

public interface ReportGenerateService {
    Mono<Void> generatePersonReport();
    Mono<File> generatePersonReportV2();
}
