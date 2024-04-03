import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
/**
 * <p>
 * Title: The calculator frame class
 * </p>
 * 
 * <p>
 * Description: creates and displays the calculator frame and buttons, provides backspace, clear, infix to postfix, and calculate functions
 * </p>
 * 
 * @author Chunbo Cheng
 */
@SuppressWarnings("serial")
class CalculatorFrame extends JFrame implements ActionListener {
	JTextField jtfInfix = new JTextField(21); // for infix
	JTextField jtfPostfix = new JTextField(); // for postix
	JTextField result = new JTextField("0"); // for result

	JButton[][] calcButton = new JButton[4][5];

	JPanel calcPanel = new JPanel();
	JPanel topPanel = new JPanel();

	public CalculatorFrame() {
		String[][] buttonText = new String[][] { { "7", "8", "9", "\u00F7", "C" }, { "4", "5", "6", "\u2217", "B" },
				{ "1", "2", "3", "-", "R" }, { "0", "(", ")", "+", "=" } };

		this.setTitle("CSC130 Calculator");
		this.setLayout(new BorderLayout(2, 1));

		jtfInfix.setHorizontalAlignment(JTextField.RIGHT);
		jtfPostfix.setHorizontalAlignment(JTextField.RIGHT);
		result.setHorizontalAlignment(JTextField.RIGHT);
		jtfPostfix.setEnabled(false);
		result.setEnabled(false);
		// jtfInfix.setEditable(false); // hide text caret

		// set the font size to 34 for the text fields
		Font textFieldFont = new Font(jtfPostfix.getFont().getName(), jtfPostfix.getFont().getStyle(), 24);
		jtfInfix.setFont(textFieldFont);
		jtfPostfix.setFont(textFieldFont);
		result.setFont(textFieldFont);

		topPanel.setLayout(new GridLayout(3, 1));
		topPanel.add(jtfInfix);
		topPanel.add(jtfPostfix);
		topPanel.add(result);

		calcPanel.setLayout(new GridLayout(4, 5, 3, 3));

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				calcButton[i][j] = new JButton("" + buttonText[i][j]);
				calcButton[i][j].setForeground(Color.blue);
				calcButton[i][j].setFont(new Font("sansserif", Font.BOLD, 42));
				calcButton[i][j].addActionListener(this);
				calcButton[i][j].setBorder(BorderFactory.createRaisedBevelBorder());
				calcPanel.add(calcButton[i][j]);
			}
		}
		this.add(topPanel, BorderLayout.NORTH);
		this.add(calcPanel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				if (e.getSource() == calcButton[i][j]) {
					// clear
					if (i == 0 && j == 4) {
						jtfInfix.setText(null);
						jtfPostfix.setText(null);
						result.setText("0");
					}
					// backspace
					else if (i == 1 && j == 4) {
						if (jtfInfix.getDocument().getLength() > 0)
							try {
								jtfInfix.setText(jtfInfix.getText(0, jtfInfix.getDocument().getLength() - 1));
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}

					}
					// number or operator
					else if (j < 4) {
						jtfInfix.setText(jtfInfix.getText() + calcButton[i][j].getText());
					}
					// = button pressed
					else if (i == 3 && j == 4) {
						// erase contents of the postfix textfield
						jtfPostfix.setText(null);
						// update the postfix textfield with the String returned
						try {
							jtfPostfix.setText(infixToPostfix());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// update the result textfield with the result of the computation
						try {
							result.setText("" + calculate());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * helper function to give each math operator a integer value for comparison
	 * 
	 * @param s - the input string
	 * @return 1 for plus and minus, 2 for multiply and divide, -1 for anything else
	 */
	public int evaluate(String s) {
		if (s.equals("+") || s.equals("-"))
			return 1;
		else if (s.equals("*") || s.equals("/") || s.equals("\u00F7") || s.equals("\u2217"))
			return 2;
		return -1;
	}

	/**
	 * convert an infix expression to postfix expression
	 * 
	 * @return the postfix expression as a string
	 * @throws Exception - stack full and stack empty exceptions
	 */
	public String infixToPostfix() throws Exception {
		String postFix = new String();
		LinkedStack<String> stack = new LinkedStack<String>();
		String expression = jtfInfix.getText();
		String delims = "+-() " + "\u00F7" + "\u2217"; // minus and plus sign in unicode
		StringTokenizer strToken = new StringTokenizer(expression, delims, true);
		while (strToken.hasMoreTokens()) {
			String token = strToken.nextToken();
			// next token is a number - append it to the result
			if (Character.isDigit(token.charAt(0)))
				postFix += Integer.parseInt(token) + " ";
			// next token is a left parenthesis - push it on the stack
			else if (token.equals("("))
				stack.push(token);
			// next token is a right parenthesis - pop everything from the stack and append
			// them to postFix until the left parenthesis is found
			else if (token.equals(")")) {
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					postFix += stack.pop() + " ";
				}
				// pop out the left parenthesis
				stack.pop();
			}
			// token must be an operator if it reaches else statement
			else {
				// pop the top item from the stack and append it to the result if it's an
				// operator and it has equal or higher priority comparing to the current
				// operator token
				while (!stack.isEmpty() && (evaluate(token) <= evaluate(stack.peek()))) {
					postFix += stack.pop() + " ";
				}
				// push the token onto the stack if it has higher priority than the top operator
				// on the stack
				stack.push(token);
			}
		}
		// pop every operator out to the end of postFix afterwards
		while (!stack.isEmpty()) {
			postFix += stack.pop() + " ";
		}
		return postFix;
	}

	/**
	 * evaluates a postfix expression and return the result as a string
	 * 
	 * @return the evaluation of a postfix expression as a string
	 * @throws Exception - stack full and stack empty exceptions
	 */
	public String calculate() throws Exception {
		String postFix = infixToPostfix();
		LinkedStack<String> stack = new LinkedStack<String>();
		StringTokenizer strToken = new StringTokenizer(postFix, " ", false); // separate tokens by space, and not
																				// including the space
		while (strToken.hasMoreTokens()) {
			String token = strToken.nextToken();
			// next token is a number - push it onto the stack
			if (Character.isDigit(token.charAt(0))) {
				stack.push(token);
			} else {
				// next token is an operator - pop out the top two numbers, perform the
				// operation based on the operator, then push the result back on the stack
				double right = Double.parseDouble(stack.pop());
				double left = Double.parseDouble(stack.pop());
				double res = 0;
				if (token.equals("+"))
					res = left + right;
				else if (token.equals("-"))
					res = left - right;
				else if (token.equals("\u2217"))
					res = left * right;
				else if (token.equals("\u00F7"))
					res = left / right;
				String strRes = res + "";
				stack.push(strRes);
			}
		}
		// once all operations are done, there is only one element left on the stack
		return stack.pop();
	}

	static final int MAX_WIDTH = 398, MAX_HEIGHT = 440;

	public static void main(String arg[]) {
		JFrame frame = new CalculatorFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(MAX_WIDTH, MAX_HEIGHT);
		frame.setBackground(Color.white);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}