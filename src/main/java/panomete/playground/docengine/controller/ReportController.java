package panomete.playground.docengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import panomete.playground.docengine.service.ReportGenerateService;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportGenerateService reportGenerateService;

    @GetMapping("/report/person")
    public Mono<ResponseEntity<Void>> generatePersonReport() {
        return reportGenerateService.generatePersonReport()
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}
