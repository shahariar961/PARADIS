import pack.MyQueue;
import java.util.Random;

public class MyQueueDemo {
    public static void main(String[] args) {
        MyQueue<Integer> myQueue = new MyQueue<>();

        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    for (int i = 0; i < 10; i++) {
                        if (random.nextBoolean()) {
                            int value = random.nextInt(100);
                            myQueue.add(value);
                            System.out.println(Thread.currentThread().getName() + " added " + value);
                        } else {
                            Integer value = myQueue.remove();
                            if (value != null) {
                                System.out.println(Thread.currentThread().getName() + " removed " + value);
                            }
                        }
                        try {
                            Thread.sleep(random.nextInt(1000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
