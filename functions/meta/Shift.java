package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function function;
    private double shiftX;
    private double shiftY;

    public Shift(Function func, double shiftX, double shiftY) throws IllegalArgumentException{
        if(func == null )
            throw new IllegalArgumentException("функция null");
        this.function = func;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder(){
        return function.getLeftDomainBorder() + shiftX;
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder() + shiftX;
    }

    public double getFunctionValue(double x){
        return function.getFunctionValue(x+ shiftX)+ shiftY;
    }
}