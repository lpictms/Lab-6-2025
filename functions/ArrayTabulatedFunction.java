package functions;

import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable{
    private FunctionPoint[] points; // массив точек
    private int pointsCount; // количество точек

    public ArrayTabulatedFunction() {}


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.pointsCount = in.readInt();
        this.points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }


    @Override
    public String toString(){
        String result = "{";
        for (int i = 0; i<pointsCount; i++){
            result += String.format("(%.1f; %.1f)", points[i].getX(), points[i].getY());
        }
        result+= "}";
        return result;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof TabulatedFunction))
            return false;

        if (o instanceof ArrayTabulatedFunction) { //если объект тот же
            ArrayTabulatedFunction func = (ArrayTabulatedFunction) o;
            if (this.pointsCount != func.pointsCount) // сравниваем количество точек
                return false;

            for (int i = 0; i < pointsCount; i++){
                if (!this.points[i].equals(func.points[i])) //сравниваем точки с помощью equals
                    return false;

            }
        }

        else { // если объект другой реализации TabulatedFunction
            TabulatedFunction func = (TabulatedFunction) o;
            if (this.getPointsCount() != func.getPointsCount()) // сравниваем количество точек
                return false;

            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(func.getPoint(i))) // сравниваем точки
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        int hash = pointsCount; // начинаем с количества точек

        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        return hash;
    }

    @Override
    public Object clone(){
        FunctionPoint[] pointsClone = new FunctionPoint[pointsCount];
        for(int i =0; i < pointsCount; i++){
            pointsClone[i] = (FunctionPoint) points[i].clone();
        }
        return new ArrayTabulatedFunction(pointsClone);
    }

    public ArrayTabulatedFunction(FunctionPoint[] arrPoints) throws IllegalArgumentException {

        if (arrPoints.length <2)
            throw new IllegalArgumentException("Количество точек меньше двух");

        this.pointsCount = arrPoints.length;

        for (int i = 1; i < pointsCount; i++) {
            if (arrPoints[i].getX() <= arrPoints[i - 1].getX())
                throw new IllegalArgumentException("Нарушена упорядоченность");
        }

        // создаем массив точек
        this.points = new FunctionPoint[pointsCount];
        for (int i = 0; i<pointsCount; i++)
            this.points[i] = new FunctionPoint(arrPoints[i]);

    }

    // создание табулированной функции
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX>=rightX)
            throw new IllegalArgumentException("Левая граница больше правой");
        if (pointsCount <2)
            throw new IllegalArgumentException("Количество точек меньше двух");
        this.pointsCount = pointsCount;
        points = new FunctionPoint[pointsCount];
        double step = Math.abs(rightX-leftX)/(pointsCount-1);
        for (int i=0; i < pointsCount;i++) {
            points[i] = new FunctionPoint(leftX + i*step, 0);
        }
    }

    // создание табулированной функции с заданными значениями по оси ординат
    public ArrayTabulatedFunction(double leftX, double rightX, double[] value){
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (value.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }
        int pointsCount = value.length;
        this.pointsCount = pointsCount;
        points = new FunctionPoint[pointsCount];
        double step = Math.abs(rightX-leftX)/(pointsCount-1);
        for (int i=0; i < pointsCount;i++) {
            points[i] = new FunctionPoint(leftX + i*step, value[i]);
        }
    }

    // возвращение левой границы области определения
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    // возвращение правой границы области определения
    public double getRightDomainBorder() {
        return points[this.pointsCount - 1].getX();
    }

    // возвращение у, если точка лежит в области определения функции
    public double getFunctionValue(double x){
        double leftX = getLeftDomainBorder();
        double rightX = getRightDomainBorder();

        if (x>= leftX && x <= rightX){
            // ищем соседние точки
            for (int i = 0; i < this.pointsCount - 1; i++) {
                double x1 = points[i].getX();
                double x2 = points[i + 1].getX();

                if (Math.abs(x - x1) < 1e-10) return points[i].getY();
                if (Math.abs(x - x2) < 1e-10) return points[i + 1].getY();
                if (x > x1 && x < x2) {
                    double y1 = points[i].getY();
                    double y2 = points[i + 1].getY();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
            }

        }

        return Double.NaN;
    }

    // возвращения количества точек
    public int getPointsCount(){
        return pointsCount;
    }

    // возвращение точки
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return new FunctionPoint(points[index]);
    }

    // изменение указанной точки на заданную
    public void setPoint (int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if (( index != 0 && point.getX() <= points[index - 1].getX()) ||
                (index != pointsCount-1 &&  point.getX() >= points[index + 1].getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        points[index]= new FunctionPoint(point);

    }

    // возвращение координаты х
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return points[index].getX();
    }
    // изменение значения х
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if ( ( index != 0 && x <= points[index - 1].getX()) ||
                (index != pointsCount-1 &&  x >= points[index + 1].getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        points[index].setX(x);
    }

    // возвращения координаты у
    public double getPointY (int index) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return points[index].getY();
    }

    // изменения значения у
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        points[index].setY(y);
    }

    // удаление точки
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if (pointsCount<3)
            throw new IllegalStateException("Недостаточное количество точек");

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        points[--pointsCount] = null;

    }

    // добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index= 0;

        double newX = point.getX();


        // находим позицию для вставки точки
        while (index < pointsCount && newX > points[index].getX())
            index++;

        if ( Math.abs(points[index].getX() - newX)<1e-10)
            throw new InappropriateFunctionPointException("Точка с таким Х уже существует");
        // проверяем нужно ли увеличивать размер массива
        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        if (index < pointsCount)
            System.arraycopy(points, index, points, index + 1, pointsCount - index);

        points[index] = new FunctionPoint(point);
        pointsCount++;

    }

    // вывод массива точек
    public void outFunction(){
        for (int i = 0; i<pointsCount; i++)
            System.out.printf("(%.2f, %.2f) ", points[i].getX(), points[i].getY());

        System.out.println();
    }
}


