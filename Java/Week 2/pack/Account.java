// Fahim Shahariar Nahin, 2023-03-08.
package pack;
//We do not use locks in the account class because both Operations and Bank class has locks on it so we avoid putting locks in here to decrease the chance of deadlock
class Account {
	// Instance variables.
	private final int ID;
	private int balance;
	
	// Constructor.
	Account(int id, int balance) {
		ID = id;
		this.balance = balance;
	}
	
	// Instance methods.
	
	int getId() {
		return ID;
	}
	
	int getBalance() {
		return balance;
	}
	
	void setBalance(int balance) {
		this.balance = balance;
	}
}
