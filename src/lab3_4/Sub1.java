package lab3_4;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Sub1 {
    public synchronized void run() throws InterruptedException {
        // Варіант 18, 1) MА= min(D+ В)*MD*MT+MX*ME;

        System.out.println("Starting sub1...");
        long startTime = System.nanoTime();

        Vector D = new Vector("./sub1/D.txt").filledWithRandomValues();
        Vector B = new Vector("./sub1/B.txt").filledWithRandomValues();

        Matrix MD = new Matrix("./sub1/MD.txt").filledWithRandomValues();
        Matrix MT = new Matrix("./sub1/MT.txt").filledWithRandomValues();
        Matrix MX = new Matrix("./sub1/MX.txt").filledWithRandomValues();
        Matrix ME = new Matrix("./sub1/ME.txt").filledWithRandomValues();

        double[] min_D_B = new double[1];

        ExecutorService service = Executors.newFixedThreadPool(4);
        ReentrantLock locker = new ReentrantLock();

        Runnable task1 = () -> {
            locker.lock();
            min_D_B[0] = D.sumWithVector(B).findMin();
            locker.unlock();
        };

        Runnable task2 = () -> {
            locker.lock();
            MX.multiplyWithMatrix(ME);
            locker.unlock();
        };

        Runnable task3 = () -> {
            locker.lock();
            MD.multiplyWithMatrix(MT);
            locker.unlock();
        };

        Callable<Matrix> task4 = () -> {
            locker.lock();
            MX.multiplyWithMatrix(MD).multiplyWithDouble(min_D_B[0]).saveToFile("./sub1/MA.txt");
            locker.lock();

            return MX;
        };

        service.execute(task1);
        service.execute(task2);
        service.execute(task3);

        try {
            System.out.println("MA:");
            service.submit(task4).get().printToConsole();

            long elapsedTime = System.nanoTime() - startTime;
            System.out.println("Total execution time sub1 in millis: " + elapsedTime / 1000000);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
