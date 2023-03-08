
package pack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Operation implements Runnable {
	private final int ACCOUNT_ID;
	private final int AMOUNT;
	private final Account account;
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
