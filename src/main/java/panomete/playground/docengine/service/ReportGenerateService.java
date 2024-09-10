package panomete.playground.docengine.service;

import reactor.core.publisher.Mono;

public interface ReportGenerateService {
    Mono<Void> generatePersonReport();
}
