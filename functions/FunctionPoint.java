package functions;

public class FunctionPoint {
    private double x; // координата абсциссы
    private double y; // координата ординаты

    // конструктор объекта точки с заданными координатами
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    // конструктор объекта точки с коор что у указанной точки
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }

    // конструктор точки (0, 0)
    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }

    // геттеры
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // сеттеры
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    @Override
    public String toString(){
        return String.format("(%.1f; %.1f)", this.x, this.y);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) // если ссылки равны, то тот же объект
            return true;
        if (o == null || getClass() != o.getClass()) // проверка является ли объект точкой
            return false;

        double epsilon = 1e-10;
        return Math.abs(this.x - ((FunctionPoint) o).getX()) < epsilon && // совпадают ли координаты
                Math.abs(this.y - ((FunctionPoint) o).getY()) < epsilon;
    }

    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(this.x);
        long yBits = Double.doubleToLongBits(this.y);

        int xHash = (int) (xBits ^ (xBits >>> 32));
        int yHash = (int) (yBits ^ (yBits >>> 32));

        return xHash ^ yHash;
    }


    @Override
    public Object clone(){
        return new FunctionPoint(x, y);
    }
}

