package at.jku.storage.controller;

import at.jku.storage.entity.Request;
import at.jku.storage.entity.Result;
import at.jku.storage.repository.ResultRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/storage")
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
            int result = callCalculator(request.getFirstNumber(), request.getSecondNumber());
            repository.save(new Result(request.getFirstNumber(), request.getSecondNumber(), result));
            return returnJSON("result", result);
        }
    }

    private int callCalculator(int first, int second) {
        String uri = "http://localhost:8080/storage/mock"; // TODO: change to real URL
        WebClient.ResponseSpec clientResponse = WebClient.builder().build()
                .get().uri(uri).retrieve();

        return clientResponse.bodyToMono(Integer.class).block();
    }

    private ResponseEntity<Object> returnJSON(String key, int value) {
        JSONObject entity = new JSONObject();
        entity.put(key, value);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }
}
