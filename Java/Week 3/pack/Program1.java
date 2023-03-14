// Fahim Shahariar Nahin, 2023-03-13.
// [Replace this comment with your own name.]

// [Do necessary modifications of this file.]

package paradis.assignment3;
import java.util.concurrent.*;
// [You are welcome to add some import statements.]

public class Program1 {
	final static int NUM_WEBPAGES = 40;
	private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];
    private static BlockingQueue<WebPage> queue = new ArrayBlockingQueue<>(10);
    private static BlockingQueue<WebPage> analyzeQueue = new ArrayBlockingQueue<>(10);

	// [You are welcome to add some variables.]

	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void initialize() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
		}
	}
	
	// [Do modify this sequential part of the program.]
	private static void downloadWebPages() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			webPages[i].download();
		}
	}
	
	// [Do modify this sequential part of the program.]
	private static void analyzeWebPages() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			webPages[i].analyze();
		}
	}
	
	// [Do modify this sequential part of the program.]
	private static void categorizeWebPages() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			webPages[i].categorize();
		}
	}
	
	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void presentResult() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			System.out.println(webPages[i]);
		}
	}
	
	public static void main(String[] args) {
		// Initialize the list of webpages.
		long start = System.nanoTime();

		initialize();
		ForkJoinPool pool = ForkJoinPool.commonPool();
        for(int i=0;i<webPages.length;i++){
        // WebPage page=webPages[i];
        pool.execute( new Producer(queue,webPages[i]));
        pool.execute( new Consumer(queue,analyzeQueue));
        pool.execute(new Consumer2(analyzeQueue));

        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		// Start timing.

		// Do the work.
		// downloadWebPages();
		// analyzeWebPages();
		// categorizeWebPages();
		
		// Stop timing.
		long stop = System.nanoTime();

		// Present the result.
		presentResult();
		
		// Present the execution time.
		System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);
	}
}

class Producer implements Runnable {
	private BlockingQueue<WebPage> queue;
    private WebPage page;


	Producer(BlockingQueue<WebPage> queue,WebPage page) { 
		this.queue = queue; 
        this.page=page;
        
	}
	
	public void run() {
		
			try {
				WebPage task = produce();
				queue.put(task);
			}
			catch (Exception exception) {
				System.out.println(exception);
			}
		
	}
	
	WebPage produce() {
		// It takes some time to produce a task.
		try {    
                           
                page.download();
                return page;
                
            
		}       
		catch (Exception exception) {
			System.out.println(exception);
            return new WebPage(0,null);
		}
    
	}
}

class Consumer implements Runnable {
	private BlockingQueue<WebPage> queue1;
	private BlockingQueue<WebPage> queue2;


	Consumer(BlockingQueue<WebPage> queue1,BlockingQueue<WebPage> queue2) { 
		this.queue1 = queue1;
        this.queue2=queue2;
	}
	
	public void run() {
		
			try {
				WebPage task = queue1.take();
				consume(task);
                queue2.put(task);
			}
			catch (Exception exception) {
				System.out.println(exception);
			}
		
	}
	
	WebPage consume(WebPage task) {
		// It takes some time to consume a task.
		try {

			task.analyze();
            return task;
        
		}
		catch (Exception exception) {
			System.out.println(exception);
            return new WebPage(0, null);
		}
		
	}
}
class Consumer2 implements Runnable {
	private BlockingQueue<WebPage> queue;

	Consumer2(BlockingQueue<WebPage> queue) { 
		this.queue = queue; 
	}
	
	public void run() {
		
			try {
				WebPage task = queue.take();
				consume(task);
			}
			catch (Exception exception) {
				System.out.println(exception);
			}
		
	}
	
	WebPage consume(WebPage task) {
		// It takes some time to consume a task.
		try {		
            task.categorize();
            return task;
        
		}
		catch (Exception exception) {
			System.out.println(exception);
            return new WebPage(0,null);
		}
		
	}
}