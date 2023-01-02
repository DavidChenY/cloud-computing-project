import React, { useState } from 'react';
import './App.css';
import ICalculation from './Calculation';
import calculatorService from './calculator.service';
import 'bootstrap/dist/css/bootstrap.min.css';

const App = () => {

  const [operation, setOperation] = useState('+');
  const [firstNumber, setFirstNumber] = useState(0);
  const [secondNumber, setSecondNumber] = useState(0);

  const calculate = () => {
    const data: ICalculation = {
      firstNumber: firstNumber,
      secondNumber: secondNumber,
      operation: operation
    };

    calculatorService.calculate(data)
      .then((response: any) => {
        console.log(response.data);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  }


  return (
    <div className="App">
      <header className="App-header">
          <div className='form-group'>
            <label htmlFor='firstNumber'>First Number:</label>
            <input type="number" name="firstNumber" id='firstNumber' className="form-control" value={firstNumber} onChange={e => setFirstNumber(e.target.valueAsNumber)} />
          </div>
          <div className="form-group">
            <label htmlFor='secondNumber'>Second Number:</label>
            <input type="number" name="secondNumber" id='secondNumber' className="form-control" value={secondNumber} onChange={e => setSecondNumber(e.target.valueAsNumber)} />
          </div>

          <div className='btn-group'>
            <input type="button" className="btn btn-secondary" value="+" onClick={() => setOperation('+')} />
            <input type="button" className="btn btn-secondary" value="-" onClick={() => setOperation('-')} />
            <input type="button" className="btn btn-secondary" value="*" onClick={() => setOperation('*')} />
            <input type="button" className="btn btn-secondary" value="/" onClick={() => setOperation('/')} />
          </div>
          <div>
            Selected operation: {operation}
          </div>
          <button type="submit" value="Submit" className='btn btn-primary' onClick={() => calculate()}>Calculate</button>
      </header>
    </div>
  );
}

export default App;
