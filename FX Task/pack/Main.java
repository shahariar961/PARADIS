package pack;

public class Main {
    public static void main(String[] args) {
        MyQueue<String> myQueue = new MyQueue<>();

        // Create three threads that add elements to the queue
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                myQueue.add("Thread 1 - Element " + i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                myQueue.add("Thread 2 - Element " + i);
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                myQueue.add("Thread 3 - Element " + i);
            }
        });

        // Start the threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for the threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print the contents of the queue
        System.out.println("myQueue: ");
        myQueue.forEach((x) -> System.out.println(x));
    }
}

