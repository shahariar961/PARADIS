
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Incrementer6 implements Runnable {
	
	private int myInt = 0;
	private Lock lock = new ReentrantLock();

	public void run() {
		for (int i = 0; i < 100000; i++) {
			lock.lock();
			try {
				myInt++;
			}
			finally {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		Incrementer6 incrementer = new Incrementer6();
		Incrementer6 incrementer2 = new Incrementer6();

        
		Thread thread1 = new Thread(incrementer);
		Thread thread2 = new Thread(incrementer2);
		try { 
			thread1.start(); 
			thread2.start();
			thread1.join(); 
			thread2.join(); 
		}
		catch (InterruptedException e) { 
			System.out.println(e);
		}
		System.out.println("myInt = " + incrementer.myInt);
    }
}