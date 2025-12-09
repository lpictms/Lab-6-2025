package functions;
import functions.meta.*;

public class Functions {

    private Functions()  {}

    public static double integration(Function func, double leftInteg, double rightInteg, double samplingStep) {
        // проверка
        double leftFunc = func.getLeftDomainBorder();
        double rightFunc = func.getRightDomainBorder();
        if (leftInteg < leftFunc || rightInteg > rightFunc)
            throw new IllegalArgumentException("область границы интегрирования выходит за область определения функции");

        // вычисление интеграла
        double current = leftInteg;
        double res = 0;
        while (current < rightInteg){
            double next = Math.min(rightInteg, current+samplingStep);

            double a = func.getFunctionValue(current);
            double b = func.getFunctionValue(next);
            double h = next - current;

            res+= ((a+b)*h)/2;

            current = next;
        }
        return res;

    }

    public static Function shift( Function f, double shiftX, double shiftY){
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power){
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2){
        return new Sum(f1,f2);
    }

    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2);
    }

}
