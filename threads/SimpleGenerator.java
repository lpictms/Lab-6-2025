package threads;

import functions.*;
import functions.basic.*;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator(Task task){
        this.task= task;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                double base = Math.random() * 9 + 1;
                Function log = new Log(base);
                double leftB = Math.random() * 100;
                double rightB = Math.random() * 100 + 100;
                double step = Math.random();

                synchronized (task) {
                    while (task.getFunc() != null) //  ждем пока не обработается предыдущее задание
                        task.wait();
                    // устанавливаем параметры
                    task.setFunc(log);
                    task.setLeftBorder(leftB);
                    task.setRightBorder(rightB);
                    task.setStep(step);
                    System.out.printf("Source %.2f %.2f %.2f\n", leftB, rightB, step);

                    task.notifyAll();// уведомляем все ожидающие потоки о появлении данных
                }
            }
        }catch (InterruptedException e) {
            System.out.println("генератор прерван");
        }
    }
}

