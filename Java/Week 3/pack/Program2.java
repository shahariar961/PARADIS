// Fahim Shahariar, 2023-03-13.
// [Replace this comment with your own name.]

// [Do necessary modifications of this file.]

package paradis.assignment3;
import java.util.Arrays;

// [You are welcome to add some import statements.]

public class Program2 {
	final static int NUM_WEBPAGES = 40;
	private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];
	// [You are welcome to add some variables.]

	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void initialize() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
		}
	}
	
	// [Do modify this sequential part of the program.]
	private static void downloadWebPages(WebPage page) {
			page.download();
		
	}
	
	// [Do modify this sequential part of the program.]
	private static void analyzeWebPages(WebPage page) {
			page.analyze();
		
	}
	
	// [Do modify this sequential part of the program.]
	private static void categorizeWebPages(WebPage page) {
			page.categorize();
		
	}
	
	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void presentResult(WebPage page) {
		
			System.out.println(page);
	}
	
	public static void main(String[] args) {
		// Initialize the list of webpages.
		initialize();
		
		// Start timing.
		long start = System.nanoTime();

		// Do the work.
		Arrays.stream(webPages).parallel().forEach(Program2::downloadWebPages);
		Arrays.stream(webPages).parallel().forEach(Program2::analyzeWebPages);
		Arrays.stream(webPages).parallel().forEach(Program2::categorizeWebPages);
        Arrays.stream(webPages).forEach(Program2::presentResult);
		// Stop timing.
		long stop = System.nanoTime();

		// Present the result.
		
		// Present the execution time.
		System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);
	}
}
