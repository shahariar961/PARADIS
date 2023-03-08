//Fahim Shahariar Nahin, 2023-03-08.
package pack;
//I import the Lock and ReentrantLock package.
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Operation implements Runnable {
	private final int ACCOUNT_ID;
	private final int AMOUNT;
	private final Account account;
    //Here I have defined a static variable so that every instance of this class run by different threads will share the same lock instance
    private static final Lock lock= new ReentrantLock();
	
	Operation(Bank bank, int accountId, int amount) {
		ACCOUNT_ID = accountId;
		AMOUNT = amount;
		account = bank.getAccount(ACCOUNT_ID);

	}
	
	int getAccountId() {
		return ACCOUNT_ID;
	}
	
	public void run() {
    // Here I try to get the lock and if successful we run the read and write commands then we finally release the lock.
        lock.lock();
        try{   
		 int balance = account.getBalance();
            balance = balance + AMOUNT;
            account.setBalance(balance);
        } finally {
            lock.unlock();
        }
		
		
	}
}	
