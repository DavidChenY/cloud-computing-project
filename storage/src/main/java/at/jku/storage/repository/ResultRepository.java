package at.jku.storage.repository;

import at.jku.storage.entity.Result;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ResultRepository {
    private final Map<Integer, Result> repository;

    public ResultRepository() {
        repository = new HashMap<>();
    }

    public void save(Result result) {
        repository.put(result.getId(), result);
    }

    public Optional<Result> load(int first, int second) {
        return repository.values().stream()
                .filter(result -> result.getFirst() == first && result.getSecond() == second)
                .findFirst();
    }
}
