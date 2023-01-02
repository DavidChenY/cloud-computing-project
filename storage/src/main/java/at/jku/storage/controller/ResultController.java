package at.jku.storage.controller;

import at.jku.storage.entity.Result;
import at.jku.storage.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
    public int calculate(@RequestParam int first, @RequestParam int second) {
        Optional<Result> resultOptional = repository.load(first, second);

        if(resultOptional.isPresent()) {
            return resultOptional.get().getResult();
        }

        int result = callCalculator(first, second);
        repository.save(new Result(first, second, result));
        return result;
    }

    private int callCalculator(int first, int second) {
        String uri = "http://localhost:8080/storage/mock"; // TODO: change to real URL
        WebClient.ResponseSpec clientResponse = WebClient.builder().build()
                .get().uri(uri).retrieve();

        return clientResponse.bodyToMono(Integer.class).block();
    }
}
