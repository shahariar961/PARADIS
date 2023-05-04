	// Peter Idestam-Almquist, 2023-03-19.

	package pack;

	import java.util.function.Consumer;
	import java.util.Iterator;
	import java.util.concurrent.ArrayBlockingQueue; //NEW		
	;
	// Representing a queue with some methods.
	class MyQueue<E> {
		
		// MyQueue instance variables.
		
		// Max number of elements is length - 1.
		private int length = 6; 
		private int first = 0;
		private int last = length - 1;
		private ArrayBlockingQueue<E> myArray = new ArrayBlockingQueue<>(length); // NEW
		//private Object[] myArray;

		// Inner class MyIterator.
		class MyIterator<E> implements Iterator<E> {
			// MyIterator instance variables.
			private int index;
			// MyIterator constructor.
			private MyIterator() {
				index = first;
			}	
			// MyIterator instance methods.
			public boolean hasNext() {

				return index != nextIndex(last);
			}
			@SuppressWarnings("unchecked")
			public E next() {
				//E element = (E)myArray[index];
				E element = (E)myArray.toArray()[index]; // NEW
				index = nextIndex(index);
				return element;
			}
		}
		// MyQueue constructor.
		public MyQueue() {
			//myArray = new Object[length];
		}
		// MyQueue instance methods.
		// Next position when regarding the array as a circle.
		private int nextIndex(int index) {
			if (index == length - 1)
				return 0;
			else
				return index + 1;
		}
		// Check if the queue is empty.
		public boolean isEmpty() {
			//return nextIndex(last) == first;
			return myArray.isEmpty(); //NEW
		}
		// Check if the queue is full.
		public boolean isFull() {
			//return nextIndex(nextIndex(last)) == first;
			return myArray.remainingCapacity() == 0 ; //NEW
		}		
		// Add an element last in the queue.
		public boolean add(E element) {
			//if (isFull())
			//	return false;
			// Add new element.
			//last = nextIndex(last);
			//myArray[last] = element;
			try { //NEW
				myArray.put(element); // NEW
			} catch (InterruptedException e) {  // NEW
 				return false ;  // NEW
			}
			return true;
		}
		// Remove and return the first element in the queue.
		@SuppressWarnings("unchecked")
		public E remove() {
			//if (isEmpty())
			try{	//NEW
				return myArray.take(); //NEW
			} catch (InterruptedException e) { // NEW

			
				return null;
			}
			//bject element = myArray[first];
			//first = nextIndex(first);
			//return (E)element;
		}
		// Perform the action for each element.
		@SuppressWarnings("unchecked")
		// public void forEach(Consumer<E> action) {
		// 	int index = first;
		// 	while (index != nextIndex(last)) {
		// 		//action.accept((E)myArray[index]);
		// 		action.accept(myArray.toArray((E[]) new Object[myArray.size()])[index]); // NEW
		// 		index = nextIndex(index);
		// 	}
		// }

		public void forEach(Consumer<E> action) {
			Iterator<E> iterator = myArray.iterator();
			while (iterator.hasNext()) {
				action.accept(iterator.next());
			}
		}
		// Returns an iterator over the elements.
		public Iterator<E> iterator() {
			return new MyIterator<E>();
		}
		public static void main(String[] args) {
			MyQueue<String> myQueue = new MyQueue<String>();
			
			myQueue.add("A");
			myQueue.add("B");
			myQueue.add("C");
			myQueue.add("D");
			myQueue.add("E");
			 
 			System.out.println("myQueue: ");
			myQueue.forEach((x) -> System.out.println(x));
			
			Iterator<String> myIterator = myQueue.iterator();
			while (myIterator.hasNext())
				System.out.println(myIterator.next());

		}
	}