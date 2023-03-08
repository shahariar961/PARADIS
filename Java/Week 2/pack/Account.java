// Peter Idestam-Almquist, 2023-02-26.
package pack;
import java.util.concurrent.atomic.AtomicInteger;



// class Account {
//     // Instance variables.
//     private final int ID;
//     private final AtomicInteger balance;

//     // Constructor.
//     Account(int id, int balance) {
//         ID = id;
//         this.balance = new AtomicInteger(balance);
//     }

//     // Instance methods.

//     int getId() {
//         return ID;
//     }

//     int getBalance() {
//         return balance.get();
//     }

//     void setBalance(int balance) {
//         this.balance.set(balance);
//     }
// }

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
