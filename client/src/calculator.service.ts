import httpCommon from "./http-common";
import ICalculation from "./Calculation";

class CalculatorDataService {
  calculate = (data: ICalculation) => {
    return httpCommon.post<ICalculation>("/calculate", data);
  }
}

export default new CalculatorDataService();