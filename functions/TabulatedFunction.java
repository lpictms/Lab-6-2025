package functions;

public interface TabulatedFunction extends Function, Cloneable {

    public Object clone();

    // возвращения количества точек
    public int getPointsCount();

    // возвращение точки
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException ;

    // изменение указанной точки на заданную
    public void setPoint (int index, FunctionPoint point) throws InappropriateFunctionPointException ;

    // возвращение координаты х
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException ;

    // изменение значения х
    public void setPointX(int index, double x) throws InappropriateFunctionPointException ;

    // возвращения координаты у
    public double getPointY (int index) throws FunctionPointIndexOutOfBoundsException;

    // изменения значения у
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException ;

    // удаление точки
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException ;

    // добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    // вывод массива точек
    public void outFunction();
}

