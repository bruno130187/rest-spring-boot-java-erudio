package br.com.erudio.restspringbootjavaerudio.controllers;

import br.com.erudio.restspringbootjavaerudio.exceptions.UnsupportedMathOperationException;
import br.com.erudio.restspringbootjavaerudio.math.SimpleMath;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicLong;

import static br.com.erudio.restspringbootjavaerudio.converters.NumberConverter.convertToDouble;
import static br.com.erudio.restspringbootjavaerudio.converters.NumberConverter.isNumeric;

@RestController
public class MathController {

    private final AtomicLong counter = new AtomicLong();
    private SimpleMath simpleMath = new SimpleMath();

    @RequestMapping(value = "/sum/{numOne}/{numTwo}", method = RequestMethod.GET)
    public Double sum(
            @PathVariable(value = "numOne") String numOne,
            @PathVariable(value = "numTwo") String numTwo
    ) throws Exception{
        if (!isNumeric(numOne) || !isNumeric(numTwo)) {
            throw new UnsupportedMathOperationException("Please set a integer ou double value.");
        }
        return simpleMath.sum(convertToDouble(numOne), convertToDouble(numTwo));
    }

    @RequestMapping(value = "/sub/{numOne}/{numTwo}", method = RequestMethod.GET)
    public Double sub(
            @PathVariable(value = "numOne") String numOne,
            @PathVariable(value = "numTwo") String numTwo
    ) throws Exception{
        if (!isNumeric(numOne) || !isNumeric(numTwo)) {
            throw new UnsupportedMathOperationException("Please set a integer ou double value.");
        }
        return simpleMath.sub(convertToDouble(numOne), convertToDouble(numTwo));
    }

    @RequestMapping(value = "/multi/{numOne}/{numTwo}", method = RequestMethod.GET)
    public Double multi(
            @PathVariable(value = "numOne") String numOne,
            @PathVariable(value = "numTwo") String numTwo
    ) throws Exception{
        if (!isNumeric(numOne) || !isNumeric(numTwo)) {
            throw new UnsupportedMathOperationException("Please set a integer ou double value.");
        }
        return simpleMath.multi(convertToDouble(numOne), convertToDouble(numTwo));
    }

    @RequestMapping(value = "/div/{numOne}/{numTwo}", method = RequestMethod.GET)
    public Double div(
            @PathVariable(value = "numOne") String numOne,
            @PathVariable(value = "numTwo") String numTwo
    ) throws Exception{
        if (!isNumeric(numOne) || !isNumeric(numTwo)) {
            throw new UnsupportedMathOperationException("Please set a integer ou double value.");
        }
        return simpleMath.div(convertToDouble(numOne), convertToDouble(numTwo));
    }

    @RequestMapping(value = "/med/{numOne}/{numTwo}", method = RequestMethod.GET)
    public Double med(
            @PathVariable(value = "numOne") String numOne,
            @PathVariable(value = "numTwo") String numTwo
    ) throws Exception{
        if (!isNumeric(numOne) || !isNumeric(numTwo)) {
            throw new UnsupportedMathOperationException("Please set a integer ou double value.");
        }
        return simpleMath.med(convertToDouble(numOne), convertToDouble(numTwo));
    }

    @RequestMapping(value = "/raiz2/{numOne}", method = RequestMethod.GET)
    public Double raiz2(
            @PathVariable(value = "numOne") String numOne
    ) throws Exception{
        if (!isNumeric(numOne)) {
            throw new UnsupportedMathOperationException("Please set a integer ou double value.");
        }
        return simpleMath.raiz2(convertToDouble(numOne));
    }

}
