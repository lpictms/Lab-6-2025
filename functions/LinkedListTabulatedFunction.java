package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable{

    private static class FunctionNode{
        private FunctionNode prev;
        private FunctionNode next;
        private FunctionPoint point;

        public FunctionNode(FunctionPoint point){
           this.point=point;
           this.next = null;
           this.prev = null;
        }

        //геттеры и сеттеры
        public FunctionPoint getPoint (){
            return point;
        }

        public void setPoint(FunctionPoint point){
            this.point = point;
        }

        public FunctionNode getPrev(){
            return prev; // возвращаем предыдущий узел
        }

        public void setPrev(FunctionNode prev){
            this.prev = prev; // устанавливаем предыдущий узел
        }

        public FunctionNode getNext(){
            return next; // возвращаем следующий узел
        }

        public void setNext(FunctionNode next){
            this.next = next; // устанавливаем следующий узел
        }
    }

    private FunctionNode head = new FunctionNode(null); // list`s head
    private int pointsCount;


    private FunctionNode getNodeByIndex (int index){

        FunctionNode curr;
        if (index < pointsCount / 2) {
            // Двигаемся от головы вперед
            curr = head.getNext(); // начинаем с 1ого значащего элемента
            for(int i = 0; i < index; i++) {
                curr = curr.getNext();
            }
        }
        else {
            // Двигаемся от хвоста назад
            curr = head.getPrev(); // начинаем с последнего элемента
            for(int i = pointsCount - 1; i > index; i--) {
                curr = curr.getPrev();
            }
        }

        return curr;
    }

    private FunctionNode addNodeToTail(FunctionPoint point){

        FunctionNode tail = head.getPrev();
        FunctionNode newNode = new FunctionNode(point);

        tail.setNext(newNode);
        newNode.setPrev(tail);
        newNode.setNext (head);
        head.setPrev(newNode);

        pointsCount++;

        return newNode;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point){
        FunctionNode newNode = new FunctionNode(point);

        if(index == pointsCount)
            return addNodeToTail(point); // добавляем в конец

        FunctionNode currNode = getNodeByIndex(index);

        newNode.setPrev(currNode.getPrev());
        newNode.setNext (currNode);
        currNode.getPrev().setNext(newNode);
        currNode.setPrev(newNode);

        pointsCount++;

        return newNode;
    }

    private FunctionNode deleteNodeByIndex (int index){

        FunctionNode delNode = getNodeByIndex(index);

        delNode.getNext().setPrev(delNode.getPrev());
        delNode.getPrev().setNext(delNode.getNext());

        pointsCount--;

        return delNode;
    }

    public LinkedListTabulatedFunction() {
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        // записываем все точки
        FunctionNode curr = head.getNext();
        while (curr != head) {
            out.writeObject(curr.getPoint());
            curr = curr.getNext();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        pointsCount = in.readInt();
        this.head = new FunctionNode(null);
        this.head.setPrev(head);
        this.head.setNext(head);

        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = (FunctionPoint) in.readObject();
            addNodeToTail(point);
        }
    }


    @Override
    public String toString(){
        String result = "{";
        FunctionNode curr = head.getNext();
        while (curr!= head){
            result += String.format("(%.1f; %.1f)", curr.getPoint().getX(), curr.getPoint().getY());
            curr =  curr.getNext();
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

        if (o instanceof LinkedListTabulatedFunction) { //если объект тот же
            LinkedListTabulatedFunction func = (LinkedListTabulatedFunction) o;
            if (this.pointsCount != func.pointsCount) // сравниваем количество точек
                return false;

            FunctionNode currFirst = this.head.getNext();
            FunctionNode currSecond = func.head.getNext();
            while (currFirst != head ){ // сравниваем точки
                if (!currFirst.getPoint().equals(currSecond.getPoint()))
                    return false;
                currFirst = currFirst.getNext();
                currSecond = currSecond.getNext();
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

        FunctionNode curr = head.getNext();
        while (curr != head ){ // сравниваем точки
            hash ^= curr.getPoint().hashCode();
            curr = curr.getNext();
        }
        return hash;
    }

    @Override
    public Object clone(){
        FunctionPoint[] pointsCopy = new FunctionPoint[pointsCount];// создаем временный массив для хранения копий всех точек
        FunctionNode curr = head.getNext();
       for (int i = 0; curr!= head; i++) {
            pointsCopy[i] = new FunctionPoint(curr.getPoint());
            curr = curr.getNext();
       }
       return new LinkedListTabulatedFunction(pointsCopy);
    }


    //lab4 конструктор
    public LinkedListTabulatedFunction(FunctionPoint[] arrPoints) throws IllegalArgumentException {
        if (arrPoints.length <2)
            throw new IllegalArgumentException("Количество точек меньше двух");

        // инициализация
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        for (int i = 1; i< arrPoints.length; i++) {
            if ( arrPoints[i].getX() <= arrPoints[i - 1].getX())
                throw new IllegalArgumentException("Нарушена упорядоченность");
        }
        // создаем список добавляя последовательно точки в конец
        for (FunctionPoint arrPoint : arrPoints)
            addNodeToTail(new FunctionPoint(arrPoint));

    }

    // создание табулированной функции
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX>=rightX)
            throw new IllegalArgumentException("Левая граница больше правой");
        if (pointsCount <2)
            throw new IllegalArgumentException("Количество точек меньше двух");

        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = Math.abs(rightX-leftX)/(pointsCount-1);
        for (int i=0; i < pointsCount;i++) {
            FunctionPoint point = new FunctionPoint(leftX + i*step, 0);
            addNodeToTail(point);
        }
    }

    // создание табулированной функции с заданными значениями по оси ординат
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] value){
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (value.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }

        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = Math.abs(rightX-leftX)/(value.length-1);
        for (int i=0; i < value.length;i++) {
            FunctionPoint point = new FunctionPoint(leftX + i*step, value[i]);
            addNodeToTail(point);
        }
    }

    // возвращение левой границы области определения
    public double getLeftDomainBorder() {
        return head.getNext().getPoint().getX();
    }

    // возвращение правой границы области определения
    public double getRightDomainBorder() {
        return head.getPrev().getPoint().getX();
    }

    // возвращение у, если точка лежит в области определения функции
    public double getFunctionValue(double x){
        double leftX = getLeftDomainBorder();
        double rightX = getRightDomainBorder();

        if (x>= leftX && x <= rightX){

            FunctionNode curr = head.getNext();
            while (curr != head && curr.getNext() != head) {
                double x1 = curr.getPoint().getX();
                double x2 = curr.getNext().getPoint().getX();

                // совпадает ли х
                if (Math.abs(x - x1) < 1e-10)
                    return curr.getPoint().getY();
                if (Math.abs(x - x2) < 1e-10)
                    return curr.getNext().getPoint().getY();
                // попадает ли x в интервал
                if (x > x1 && x < x2) {
                    double y1 = curr.getPoint().getY();
                    double y2 = curr.getNext().getPoint().getY();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
                // переходим к следующему
                curr = curr.getNext();
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
        double x = getNodeByIndex(index).getPoint().getX();
        double y = getNodeByIndex(index).getPoint().getY();
        return new FunctionPoint(x, y);
    }

    // изменение указанной точки на заданную
    public void setPoint (int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if (( index != 0 && point.getX() <= getNodeByIndex(index-1).getPoint().getX()) ||
                (index != pointsCount-1 &&  point.getX() >= getNodeByIndex(index+1).getPoint().getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        getNodeByIndex(index).setPoint(new FunctionPoint(point));

    }

    // возвращение координаты х
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return getNodeByIndex(index).getPoint().getX();
    }
    // изменение значения х
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if ( ( index != 0 && x <= getNodeByIndex(index-1).getPoint().getX()) ||
                (index != pointsCount-1 &&  x >= getNodeByIndex(index+1).getPoint().getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        getNodeByIndex(index).getPoint().setX(x);
    }

    // возвращения координаты у
    public double getPointY (int index) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return getNodeByIndex(index).getPoint().getY();
    }

    // изменения значения у
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        getNodeByIndex(index).getPoint().setY(y);
    }

    // удаление точки
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index< 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if (pointsCount<3)
            throw new IllegalStateException("Недостаточное количество точек");

        deleteNodeByIndex(index);

    }

    // добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index= 0;

        double newX = point.getX();

        // находим позицию для вставки точки
        FunctionNode curr = head.getNext();
        while (curr != head && newX > curr.getPoint().getX()) {
            curr = curr.getNext();
            index++;
        }

        if (curr != head && Math.abs(curr.getPoint().getX() - newX) < 1e-10)
            throw new InappropriateFunctionPointException("Точка с таким Х уже существует");

        addNodeByIndex(index, point);

    }

    // вывод массива точек
    public void outFunction(){
        FunctionNode curr = head.getNext();
        while (curr != head) {
            System.out.printf("(%.2f, %.2f) ", curr.getPoint().getX(), curr.getPoint().getY());
            curr = curr.getNext();
        }
        System.out.println();
    }


}