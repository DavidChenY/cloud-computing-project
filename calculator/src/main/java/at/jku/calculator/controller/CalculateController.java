package at.jku.calculator.controller;

import at.jku.calculator.entity.Request;
import jakarta.websocket.server.PathParam;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("calculate")
    public ResponseEntity<Object> calculate(@RequestBody Request request) {
        float result = mysteriousFunction(request.getFirstNumber(), request.getSecondNumber(), request.getOperation());
        JSONObject entity = new JSONObject();
        entity.put("result", result);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    private float mysteriousFunction(int firstNumber, int secondNumber, String operation) {
        float result;
        if(firstNumber==0 || firstNumber==1)
            return 1f;

        if(secondNumber==0 || secondNumber==1)
            return 1f;

        switch(operation){
            case "+":
                result = mysteriousFunction(firstNumber-1, secondNumber-1, operation) * firstNumber + mysteriousFunction(firstNumber-1, secondNumber-1, operation) * secondNumber;
                break;
            case "-":
                result = mysteriousFunction(firstNumber-1, secondNumber-1, operation) * firstNumber - mysteriousFunction(firstNumber-1, secondNumber-1, operation) * secondNumber;
                break;
            case "*":
                result = mysteriousFunction(firstNumber-1, secondNumber-1, operation) * firstNumber * mysteriousFunction(firstNumber-1, secondNumber-1, operation) * secondNumber;
                break;
            case "/":
                result = mysteriousFunction(firstNumber-1, secondNumber-1, operation) * firstNumber / mysteriousFunction(firstNumber-1, secondNumber-1, operation) * secondNumber;
                break;
            default: return -1;
        }
        return result;
    }
}
