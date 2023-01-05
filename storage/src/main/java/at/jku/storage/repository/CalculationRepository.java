package at.jku.storage.repository;

import at.jku.storage.domain.dto.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {

    <S extends Calculation> Calculation findByFirstNumberAndSecondNumberAndOperation(int firstNumber, int secondNumber, String operation);
}
