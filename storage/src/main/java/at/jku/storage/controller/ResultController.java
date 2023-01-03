package at.jku.storage.controller;

import at.jku.storage.entity.Request;
import at.jku.storage.entity.Result;
import at.jku.storage.entity.Test;
import at.jku.storage.repository.ResultRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class ResultController {

    final ResultRepository repository;

    @Autowired
    public ResultController(ResultRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/health")
    public String test() {
        return "200 - OK";
    }

    @GetMapping("/mock")
    public int mock() {
        return 123;
    }

    @PostMapping("/calculate")
    @ResponseBody
    public ResponseEntity<Object> calculate(@RequestBody Request request) {
        Optional<Result> resultOptional = repository.load(request.getFirstNumber(), request.getSecondNumber());

        if(resultOptional.isPresent()) {
            return returnJSON("result", resultOptional.get().getResult());
        } else {
            float result = callCalculator(request.getFirstNumber(), request.getSecondNumber(), request.getOperation());
            repository.save(new Result(request.getFirstNumber(), request.getSecondNumber(), result));
            return returnJSON("result", result);
        }
    }

    private Float callCalculator(int first, int second, String operation) {
        String uri = "http://localhost:8081/calculate"; // TODO: change to real URL
        String body = getBody(first, second, operation);
        WebClient.ResponseSpec clientResponse = WebClient.builder().build()
                .post().uri(uri).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(body)).retrieve();

        return Objects.requireNonNull(clientResponse.bodyToMono(Test.class).block()).getResult();
    }

    private String getBody(int first, int second, String operation) {
        return "{\n" +
                "\"firstNumber\":"+first+",\n"+
                "\"secondNumber\":"+second+",\n"+
                "\"operation\":"+"\""+operation+"\"\n"+
                "}";
    }

    private ResponseEntity<Object> returnJSON(String key, float value) {
        JSONObject entity = new JSONObject();
        entity.put(key, value);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }
}
