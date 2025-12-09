package functions.basic;

import functions.Function;

public class Log implements Function{
    private double base;

    public Log (double b){
        if (b <= 0 || b == 1)
            throw new IllegalArgumentException("неверное значение для основания");
        this.base = b;
    }

    public double getLeftDomainBorder(){
        return 0;
    }
    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0)
            return Double.NaN;
        return Math.log(x) / Math.log(base);
    }
}


