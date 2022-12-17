package br.com.erudio.restspringbootjavaerudio.math;

public class SimpleMath {

    public Double sum(Double numOne, Double numTwo) {
        return numOne + numTwo;
    }

    public Double sub(Double numOne, Double numTwo) {
        return numOne - numTwo;
    }

    public Double multi(Double numOne, Double numTwo) {
        return numOne * numTwo;
    }

    public Double div(Double numOne, Double numTwo) {
        return numOne / numTwo;
    }

    public Double med(Double numOne, Double numTwo) {
        return (numOne + numTwo) / 2;
    }

    public Double raiz2(Double numOne) {
        return Math.sqrt(numOne);
    }

}
