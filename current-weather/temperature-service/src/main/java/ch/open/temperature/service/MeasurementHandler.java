package ch.open.temperature.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@Slf4j
public class MeasurementHandler {

    private final MeasurementRepository repository;

    private long count;

    public MeasurementHandler(MeasurementRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getMeasurementsAsStream(ServerRequest request) {
        count++;
        log.info("get measurements as stream");
        return ok()
                .contentType(APPLICATION_STREAM_JSON)
                .body(repository
                        .findAllByTimeGreaterThan(LocalDateTime.now())
                        .log("egress-"+count), Measurement.class);
    }

    public Mono<ServerResponse> getMeasurements(ServerRequest request) {
        log.info("get measurements");
        return ok().body(repository.findAll().log("egress-list"), Measurement.class);
    }

}
