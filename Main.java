import functions.*;
import functions.basic.Exp;
import functions.basic.Log;
import threads.*;
import threads.Task;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        testIntegration();
        System.out.println("\nТестирование задания 2:\n");
        nonThread();
        System.out.println("\nТестирование задания 3:\n");
        simpleThreads();
        System.out.println("\nТестирование задания 4:\n");
        complicatedThreads();

    }

    public static void testIntegration(){
        double step =1;
        double theoreticalResult = Math.E - 1;
        double error;

        while (true){
            double result = Functions.integration(new Exp(), 0, 1, step);
            error = Math.abs(theoreticalResult-result);

            if (error < 1e-7)
                break;

            step /= 2;   // уменьшаем шаг
        }
        System.out.println("шаг = " + step);
    }

    public static void nonThread(){
        Task task = new Task(null, 0, 0, 0, 100);

        for (int i = 0; i < task.getTaskCount(); i++){
            // работа с рандом
            double base =  Math.random() * 9 + 1; // основание на отрезке от 1 до 10
            Function log = new Log(base);
            double leftB = Math.random() * 100; // левая граница от 0 до 100
            double rightB = Math.random() *100+100; // правая граница от 100 до 200
            double step = Math.random(); // шаг дискретизации от 0 до 1

            System.out.printf("Source %.2f %.2f %.2f", leftB, rightB, step);

            // устанавливаем значения
            task.setFunc(log);
            task.setLeftBorder(leftB);
            task.setRightBorder(rightB);
            task.setStep(step);

            // вычисляем интеграл
            double result = Functions.integration(log, leftB, rightB, step);
            System.out.printf("\nResult %.2f %.2f %.2f %.2f\n\n", leftB, rightB, step,result);

        }
    }

    public static void simpleThreads(){
        Task task = new Task(null, 0, 0, 0, 100);

        //создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));

        //устанавливаем приоритеты потокам
//        generatorThread.setPriority(Thread.MAX_PRIORITY);
//        integratorThread.setPriority(Thread.MIN_PRIORITY);

        //запускаем потоки
        generatorThread.start();
        integratorThread.start();

        try {
            // ждем завершения потоков
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            System.out.println("поток прерван");
        }
    }

    public static void complicatedThreads()  {
        Task task = new Task(null, 0, 0, 0, 100);

        Semaphore semaphoreGen = new Semaphore(1);
        Semaphore semaphoreInteg = new Semaphore(0);

        Generator generator = new Generator(task, semaphoreGen, semaphoreInteg);
        Integrator integrator = new Integrator(task, semaphoreGen, semaphoreInteg);

        generator.start();
        integrator.start();

        try {
            Thread.sleep(50);

        // Прерываем оба потока
        generator.interrupt();
        integrator.interrupt();

        // Ждём завершения
        generator.join();
        integrator.join();
        } catch (InterruptedException e) {
            System.out.println("работа прервана");
        }

    }

}