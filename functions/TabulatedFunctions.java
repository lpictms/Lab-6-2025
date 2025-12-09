package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (function == null)
            throw new IllegalArgumentException("функция null");
        if (pointsCount < 2) {
            throw new IllegalArgumentException("недостаточное количество точек");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("левая граница меньше правой");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("границы табулирования выходят за область определения");
        }

        // создаем массив для значений y
        double[] values = new double[pointsCount];
        double step = Math.abs(rightX - leftX) / (pointsCount - 1);

        // записываем значения в массив
        for (int i = 0; i < pointsCount; i++)
            values[i] = function.getFunctionValue(leftX + i * step);

        return new ArrayTabulatedFunction(leftX, rightX, values);
    }

    // работа с байтовым потоком
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        int pointsCount = function.getPointsCount();
        DataOutputStream outputData = new DataOutputStream(out);
        outputData.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            outputData.writeDouble(function.getPointX(i));
            outputData.writeDouble(function.getPointY(i));
        }
        outputData.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream inputData = new DataInputStream(in);
        int pointsCount = inputData.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                double x = inputData.readDouble();
                double y = inputData.readDouble();
                points[i] = new FunctionPoint(x, y);
            }

        return new ArrayTabulatedFunction(points);

    }

    // работа с символьным потоком
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        PrintWriter writer = new PrintWriter(out);
        int pointsCount = function.getPointsCount();
        writer.print(pointsCount);
        writer.print(" ");

        for (int i = 0; i < pointsCount; i++) {
            writer.print(function.getPointX(i));
            writer.print(" ");
            writer.print(function.getPointY(i));
            writer.print(" ");
        }
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return new ArrayTabulatedFunction(points);
    }

}
