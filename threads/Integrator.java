package threads;

import functions.Function;
import functions.Functions;

import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphoreGen;
    private Semaphore semaphoreInteg;

    public Integrator(Task task, Semaphore semGen, Semaphore semInteg){
        this.task = task;
        this.semaphoreGen = semGen;
        this.semaphoreInteg = semInteg;
    }

    @Override
    public  void run() {
        try {
            while (!isInterrupted()) {
                semaphoreInteg.acquire();
                try{

                    Function func = task.getFunc();
                    double leftB = task.getLeftBorder();
                    double rightB = task.getRightBorder();
                    double step = task.getStep();


                    // вычисляем интеграл
                    double result = Functions.integration(func, leftB, rightB, step);
                    System.out.printf("Result %.2f %.2f %.2f %.2f\n\n", leftB, rightB, step, result);

                }catch (Exception e){
                    System.out.println("ошибка при вычислении");
                }finally {
                    semaphoreGen.release();
                }
            }
        }catch (InterruptedException e){
            System.out.println("интегратор прерван");
            Thread.currentThread().interrupt();
        }
    }
}
