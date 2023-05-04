/* Here we replaced the normal Array with an Array blocking queue´, and we have modified the methods next(), isEmpty(), isFull() add(), remove() to use the methods provided by the Array Blocking Queue to conduct thread safe atomic operations on the array. We have also changed the forEach method since we cannot navigate a blocking queue using an index, thus we have created a iterator instance that iterates through the queue. We have modified the inner class MyIterator to use the blocking queue method toArray() to create a copy of the array then iterate through that.*/
// Peter Idestam-Almquist, 2023-03-19.
	package paradis.exam230320.task3;

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
		//private Object[] myArray;
		private ArrayBlockingQueue<E> myArray = new ArrayBlockingQueue<>(length); // NEW
		

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
				// return null;
			//Object element = myArray[first];
			//first = nextIndex(first);
			//return (E)element;
			try{	//NEW
				return myArray.take(); //NEW
			} catch (InterruptedException e) { // NEW

			
				return null;  // NEW
			}
			
		}
		// Perform the action for each element.
		@SuppressWarnings("unchecked")
		public void forEach(Consumer<E> action) {
			//int index = first;
			Iterator<E> iterator = myArray.iterator(); // NEW
			//while (index != nextIndex(last)) {
			while (iterator.hasNext()) {  // NEW
				//action.accept((E)myArray[index]);
				//index = nextIndex(index);
				action.accept(iterator.next()); // NEW
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