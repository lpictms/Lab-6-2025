package functions.meta;

import functions.Function;

public class Scale implements Function{
    private Function function;
    private double scaleX;
    private double scaleY;

    public Scale(Function func, double scaleX, double scaleY){
        if(func == null )
            throw new IllegalArgumentException("функция null");
        this.function = func;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder()*scaleX;
    }

    public double getRightDomainBorder() {
        return function.getRightDomainBorder()*scaleY;
    }

    public double getFunctionValue(double x) {
        return function.getFunctionValue(x*scaleX)*scaleY;
    }
}
