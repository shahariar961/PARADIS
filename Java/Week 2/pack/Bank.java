// Fahim Shahariar Nahin, 2023-03-08.

package pack;

import java.util.List;
import java.util.ArrayList;

class Bank {
	// Instance variables.
	private final List<Account> accounts = new ArrayList<Account>();
 
	
	// Instance methods.
	//We use synchronized here so that if multiple threads are creating accounts it is still kept threadsafe
	synchronized int newAccount(int balance) {
        
		
			int accountId = accounts.size();
			accounts.add(new Account(accountId, balance));
			return accountId;
		}

	synchronized Account getAccount(int accountId) {
		Account account = accounts.get(accountId);
		return account;
		}
	}

	
