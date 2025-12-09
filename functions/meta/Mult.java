package functions.meta;

import functions.Function;

public class Mult implements Function {
    private Function firstFunction;
    private Function secondFunction;

    public Mult (Function firstFunc, Function secondFunc){
        if(firstFunc == null || secondFunc == null)
            throw new IllegalArgumentException("функции null");
        this.firstFunction = firstFunc;
        this.secondFunction = secondFunc;
    }

    public double getLeftDomainBorder() {
        return Math.max(firstFunction.getLeftDomainBorder(), secondFunction.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        return Math.max(firstFunction.getRightDomainBorder(), secondFunction.getRightDomainBorder());
    }

    // проверку
    public double getFunctionValue(double x) {
        return firstFunction.getFunctionValue(x)*secondFunction.getFunctionValue(x);
    }
}
