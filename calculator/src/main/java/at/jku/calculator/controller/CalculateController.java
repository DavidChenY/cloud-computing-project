package at.jku.calculator.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/")
public class CalculateController {
    @GetMapping("random")
    public int getRandomNumber() {
        return new Random().nextInt();
    }

    @GetMapping("random/{count}")
    public List<Integer> getRandomNumbers(@PathParam("count") int count) {
        return new Random().ints(count).boxed().collect(Collectors.toList());
    }
}
