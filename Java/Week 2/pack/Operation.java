package pack;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Operation implements Runnable {
    private final int ACCOUNT_ID;
    private final int AMOUNT;
    private final Account account;
    private final Lock writeLock;
    private final Lock readLock;

    Operation(Bank bank, int accountId, int amount) {
        ACCOUNT_ID = accountId;
        AMOUNT = amount;
        account = bank.getAccount(ACCOUNT_ID);
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        writeLock = rwLock.writeLock();
        readLock = rwLock.readLock();
    }

    int getAccountId() {
        return ACCOUNT_ID;
    }

    public void run() {
        // Acquire the write lock before modifying the balance.
        writeLock.lock();
        try {
            int balance = account.getBalance();
            balance = balance + AMOUNT;
            account.setBalance(balance);
        } finally {
            // Release the write lock after modifying the balance.
            writeLock.unlock();
        }
    }

    // This method allows other threads to read the account balance.
    int getBalance() {
        // Acquire the read lock before reading the balance.
        readLock.lock();
        try {
            return account.getBalance();
        } finally {
            // Release the read lock after reading the balance.
            readLock.unlock();
        }
    }
}// // Peter Idestam-Almquist, 2023-02-26.

// package pack;

// class Operation implements Runnable {
// 	private final int ACCOUNT_ID;
// 	private final int AMOUNT;
// 	private final Account account;
	
// 	Operation(Bank bank, int accountId, int amount) {
// 		ACCOUNT_ID = accountId;
// 		AMOUNT = amount;
// 		account = bank.getAccount(ACCOUNT_ID);
// 	}
	
// 	int getAccountId() {
// 		return ACCOUNT_ID;
// 	}
	
// 	public void run() {

// 		synchronized(account){

// 		int balance = account.getBalance();
// 		balance = balance + AMOUNT;
// 		account.setBalance(balance);
// 		}
// 	}
// }	
