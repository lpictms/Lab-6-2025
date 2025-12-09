package threads;

import functions.Function;
import functions.basic.Log;

import java.util.concurrent.Semaphore;

public class Generator extends  Thread{
    private Task task;
    private Semaphore semaphoreGen;
    private Semaphore semaphoreInteg;

    public Generator(Task task, Semaphore semGen, Semaphore semInteg){
        this.task = task;
        this.semaphoreGen = semGen;
        this.semaphoreInteg = semInteg;
    }

    @Override
    public void run(){
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                if (Thread.currentThread().isInterrupted())
                    break;

                semaphoreGen.acquire();
                try{
                    double base = Math.random() * 9 + 1;
                    Function log = new Log(base);
                    double leftB = Math.random() * 100;
                    double rightB = Math.random() * 100 + 100;
                    double step = Math.random();

                    // устанавливаем параметры
                    task.setFunc(log);
                    task.setLeftBorder(leftB);
                    task.setRightBorder(rightB);
                    task.setStep(step);
                    System.out.printf("Source %.2f %.2f %.2f\n", leftB, rightB, step);


                } catch (Exception e) {
                    System.out.println("ошибка генерации задачи");
                }finally {
                    semaphoreInteg.release();
                }

            }
        }catch (InterruptedException e) {
            System.out.println("генератор прерван");
            Thread.currentThread().interrupt();
        }
    }
}
