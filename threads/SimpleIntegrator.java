package threads;

import functions.Function;
import functions.Functions;
import functions.basic.Log;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                synchronized (task) {
                    while (task.getFunc() == null)  // ждем пока появятся данные
                            task.wait();

                    Function func = task.getFunc();
                    double leftB = task.getLeftBorder();
                    double rightB = task.getRightBorder();
                    double step = task.getStep();
                    // вычисляем интеграл
                    double result = Functions.integration(func, leftB, rightB, step);
                    System.out.printf("Result %.2f %.2f %.2f %.2f\n\n", leftB, rightB, step, result);

                    task.setFunc(null); // сбрасываем задание
                    task.notifyAll(); // уведомляем ждущие потоки
                }
            }
        } catch (InterruptedException e) {
            System.out.println("итератор прерван");

        }
    }
}
