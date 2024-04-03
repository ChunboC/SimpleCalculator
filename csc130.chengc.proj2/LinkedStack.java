/**
 * <p>
 * Title: The LinkedStack class
 * </p>
 * 
 * <p>
 * Description: a stack class based on linked list
 * </p>
 * 
 * @author Chunbo Cheng
 */
public class LinkedStack<T> implements Stack<T> {
	// helper class - linked list with three constructors
	private class Node<E> {
		private E data;
		private Node<E> next;

		public Node() {
			data = null;
			next = null;
		}

		public Node(E data) {
			this.data = data;
			next = null;
		}

		public Node(E data, Node<E> next) {
			this.data = data;
			this.next = next;
		}
	}

	private Node<T> head;
	private int numItems;

	@Override
	public void push(T data) throws Exception {
		if (isFull())
			throw new StackFullException("Stack full Exception...");
		head = new Node<T>(data, head);
		numItems++;
	}

	@Override
	public T pop() throws Exception {
		if (isEmpty())
			throw new StackEmptyException("Stack empty Exception...");
		T retVal = head.data;
		head = head.next;
		numItems--;
		return retVal;
	}

	@Override
	public T peek() throws Exception {
		if (isEmpty())
			throw new StackEmptyException("Stack empty Exception...");
		return head.data;
	}

	@Override
	public boolean isEmpty() {
		return numItems == 0;
	}

	@Override
	public boolean isFull() {
		return false; // a linked list is never full;
	}

	@Override
	public int size() {
		return numItems;
	}
	
	/**
	 * toString() - returns the information of a stack
	 * 
	 * @return all nodes' value in the stack in a string
	 */
	public String toString() {
		String str = new String();
		Node<T> trav = head;
		while (!isEmpty()) {
			str += trav.data + " ";
			trav = trav.next;
		}
		return str;
	}
}
