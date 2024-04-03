
/**
 * <p>
 * Title: The StackFullException Class
 * </p>
 * 
 * <p>
 * Description: Defines the specific exception when the stack is full
 * </p>
 * 
 * @author Chunbo Cheng
 */

public class StackFullException extends RuntimeException {
	/**
	 * Constructs a new StackFullException with a default error message string.
	 */
	public StackFullException() {
		super("Exception : Stack is full");
	}

	/**
	 * Constructs a new StackFullException with the parameter as the error message
	 * string.
	 * 
	 * @param msg The string passed as the error message string.
	 */
	public StackFullException(String msg) {
		super(msg);
	}
}