	// Peter Idestam-Almquist, 2023-03-19.

	package pack;

	import java.util.function.Consumer;
	import java.util.Iterator;

	// Representing a queue with some methods.
	class MyQueueMulti<E> {
		
		// MyQueue instance variables.
		
		// Max number of elements is length - 1.
		private int length = 6; 
		private int first = 0;
		private int last = length - 1;
		private Object[] myArray;

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
				E element = (E)myArray[index];
				index = nextIndex(index);
				return element;
			}
		}
		
		// MyQueue constructor.
		public MyQueueMulti() {
			myArray = new Object[length];
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
			return nextIndex(last) == first;
		}
		
		// Check if the queue is full.
		public boolean isFull() {
			return nextIndex(nextIndex(last)) == first;
		}
				
		// Add an element last in the queue.
		public boolean add(E element) {
			if (isFull())
				return false;
			
			// Add new element.
			last = nextIndex(last);
			myArray[last] = element;
			return true;
		}
		
		// Remove and return the first element in the queue.
		@SuppressWarnings("unchecked")
		public E remove() {
			if (isEmpty())
				return null;
			
			Object element = myArray[first];
			first = nextIndex(first);
			return (E)element;
		}

		// Perform the action for each element.
		@SuppressWarnings("unchecked")
		public void forEach(Consumer<E> action) {
			int index = first;
			while (index != nextIndex(last)) {
				action.accept((E)myArray[index]);
				index = nextIndex(index);
			}
		}
		
		// Returns an iterator over the elements.
		public Iterator<E> iterator() {
			return new MyIterator<E>();
		}
		
		public static void main(String[] args) {
			MyQueueMulti<String> myQueue = new MyQueueMulti<String>();
			Thread t1 = new Thread(() -> {
                
                myQueue.add("Thread 1 - A");
                myQueue.add("Thread 1 - B");
                myQueue.add("Thread 1 - C");
                myQueue.add("Thread 1 - D");
                // myQueue.add("Thread 1 - E");
            
            });
            Thread t2 = new Thread(() -> {
                
                myQueue.add("Thread 2 - A");
                myQueue.add("Thread 2 - B");
                myQueue.add("Thread 2 - C");
                myQueue.add("Thread 2 - D");
                myQueue.add("Thread 2 - E");
              
                });
            t1.start();
            t2.start();
            try{
                t1.join();
                t2.join();

            }catch(InterruptedException e){
                e.printStackTrace();
            }
			System.out.println("myQueue: ");
			myQueue.forEach((x) -> System.out.println(x));
			
			Iterator<String> myIterator = myQueue.iterator();
			while (myIterator.hasNext())
				System.out.println(myIterator.next());

		}
	}