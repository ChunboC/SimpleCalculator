public interface Stack<T> {
	public void push(T data) throws Exception;

	public T pop() throws Exception;

	public T peek() throws Exception;

	public boolean isEmpty();

	public boolean isFull();

	public int size();
}
