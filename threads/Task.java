package threads;

import functions.Function;

public class Task {
    private double leftBorder;
    private double rightBorder;
    private double samplingStep;
    private Function function;
    private int countTask;

    public Task(Function func, double left, double right, double step, int count) {
        this.function = func;
        this.leftBorder = left;
        this.rightBorder = right;
        this.samplingStep = step;
        this.countTask = count;
    }

    public void setFunc(Function func){
        this.function = func;
    }

    public Function getFunc(){
        return function;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(double leftB) {
        this.leftBorder = leftB;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(double rightB) {
        this.rightBorder = rightB;
    }

    public double getStep() {
        return samplingStep;
    }

    public void setStep(double step) {
        this.samplingStep = step;
    }

    public int getTaskCount() {
        return countTask;
    }

    public void setCount(int count) {
        this.countTask = count;
    }
}
