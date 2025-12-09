package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function function;
    private double power;

    public Power (Function func, double pow){
        if(func == null )
            throw new IllegalArgumentException("функция null");
        this.power = pow;
        this.function = func;
    }

    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder();
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder();
    }

    public double getFunctionValue(double x){
        return Math.pow( function.getFunctionValue(x), power);
    }
}
