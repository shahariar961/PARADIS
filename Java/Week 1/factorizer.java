//Fahim Shahariar Nahin
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

class PrimeFinder implements Runnable {
    

    private BigInteger min;
    private BigInteger max;
    private int step;
    private BigInteger product;
    private BigInteger factor1;
    private BigInteger factor2;


    PrimeFinder(BigInteger min, BigInteger max, int step, BigInteger product) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.product = product;
        this.factor1 = BigInteger.ZERO;
        this.factor2 = BigInteger.ZERO;
    }

    public void run() {
        BigInteger number = min;
        while (number.compareTo(max) <= 0) {
            if (product.remainder(number).compareTo(BigInteger.ZERO) == 0) {
                BigInteger otherFactor = product.divide(number);
                    factor1 = number;
                    factor2 = otherFactor;
                    return;
                }
            number = number.add(BigInteger.valueOf(step));
        }
    }

    public static void main(String[] args) {
        try {
            // Read input.
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader consoleReader = new BufferedReader(streamReader);
            System.out.print("Input (numThreads,product)>");
            String input = consoleReader.readLine();
            String[] inputs = input.split(",");
            int numThreads = Integer.parseInt(inputs[0]);
            BigInteger product = new BigInteger(inputs[1]);

            // Start timing.
            long start = System.nanoTime();

            // Create threads.
            if (product.isProbablePrime(10)){
                System.out.println("Number is Prime, No Factorization is Possible");
            }
            else {
            Thread[] threads = new Thread[numThreads];
            PrimeFinder[] primeFinders = new PrimeFinder[numThreads];
            BigInteger tempMin= BigInteger.valueOf( 2);
            
            for (int i = 0; i < numThreads; i++) {
                primeFinders[i] = new PrimeFinder(tempMin, product, numThreads, product);
                threads[i] = new Thread(primeFinders[i]);
            }

            // Run threads.
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }
            for (int i = 0; i < numThreads; i++) {
                threads[i].join();
            }

            // Stop timing.
            long stop = System.nanoTime();

            // Output results.
            int numProcessors = Runtime.getRuntime().availableProcessors();
        
        if (primeFinders[0].factor1.isProbablePrime(10) && primeFinders[0].factor2.isProbablePrime(10)) {
            System.out.println("Prime factors of product " + product + ": " + primeFinders[0].factor1 + " and " + primeFinders[0].factor2);
        }
        else {
            System.out.println("Non-prime factors of product " + product + ": " + primeFinders[0].factor1 + " and " + primeFinders[0].factor2);
        }
        System.out.println("Available processors: " + numProcessors);
        System.out.println("Number of threads: " + numThreads);
        System.out.println("Execution time (seconds): " + (stop-start)/1.0E9);
        }
    }
        catch (Exception exception) {
            System.out.println(exception);
        }
    }
    
}