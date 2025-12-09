package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function inFunction;
    private Function outFunction;

    public Composition(Function inFunc, Function outFunc){
        if(inFunc == null || outFunc == null)
            throw new IllegalArgumentException("функции null");

        this.inFunction=inFunc;
        this.outFunction=outFunc;
    }

    public double getLeftDomainBorder() {
        return inFunction.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return inFunction.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return outFunction.getFunctionValue(inFunction.getFunctionValue(x));
    }
}
