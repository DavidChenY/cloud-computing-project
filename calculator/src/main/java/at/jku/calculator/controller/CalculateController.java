package at.jku.calculator.controller;

import at.jku.calculator.entity.Request;
import at.jku.calculator.entity.Result;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/")
public class CalculateController {

    private final Logger logger = LoggerFactory.getLogger(CalculateController.class);

    @Autowired
    public CalculateController() {
    }

    @GetMapping("random")
    public int getRandomNumber() {
        return new Random().nextInt();
    }

    @GetMapping("random/{count}")
    public List<Integer> getRandomNumbers(@PathParam("count") int count) {
        return new Random().ints(count).boxed().collect(Collectors.toList());
    }

    @PostMapping("calculate")
    public Result calculate(@RequestBody Request request) {
        float result = calculate(request.getFirstNumber(), request.getSecondNumber(), request.getOperation());
        return new Result(result);
    }

    private float calculate(int firstNumber, int secondNumber, String operation) {
        float result = 0;
        logger.info("calculate: {} {} {} ", firstNumber, operation, secondNumber);
        switch (operation) {
            case "+" -> result = firstNumber + secondNumber;
            case "-" -> result = firstNumber - secondNumber;
            case "*" -> {
                int count = 0;
                // run into very long loop with negative numbers
                while (count != secondNumber) {
                    result += firstNumber;
                    count++;
                }
            }
            case "/" -> result = firstNumber / secondNumber; // can throw divide by zero
            default -> {
                logger.error("about to crash");
                // crash the application
                System.exit(1);
                return -1;
            }
        }
        return result;
    }
}
