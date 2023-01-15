package at.jku.storage.controller;

import at.jku.storage.domain.dto.Calculation;
import at.jku.storage.entity.Request;
import at.jku.storage.entity.Result;
import at.jku.storage.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
@RestController
public class CalculationController {

    @Value("${calculator-url}")
    private String calculatorURL;

    private final CalculationRepository calculationRepository;

    @Autowired
    public CalculationController(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @PostMapping("/calculate")
    @ResponseBody
    public Result calculate(@RequestBody Request request) {
        Calculation calculation = calculationRepository.findByFirstNumberAndSecondNumberAndOperation(request.getFirstNumber(), request.getSecondNumber(), request.getOperation());

        if (calculation != null) {
            return new Result(calculation.getResult());
        } else {
            float result = callCalculator(request.getFirstNumber(), request.getSecondNumber(), request.getOperation());
            calculationRepository.save(new Calculation(request.getFirstNumber(), request.getSecondNumber(), request.getOperation(), result));
            return new Result(result);
        }
    }

    private Float callCalculator(int first, int second, String operation) {
        String uri = calculatorURL + "/calculate";
        Request request = new Request(first, second, operation);
        WebClient.ResponseSpec clientResponse = WebClient.builder().build()
                .post().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request)).retrieve();

        return Objects.requireNonNull(clientResponse.bodyToMono(Result.class).block()).getResult();
    }
}
