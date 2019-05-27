
import java.io.*;
import java.util.*;

public class Main {
	static String dpatostr(Stack<Character> p, int n, String i)
	{
		// dpa to string conversion
		@SuppressWarnings("unchecked") //use to prevent warning in p.clone();
		Stack<Character> r = (Stack<Character>) p.clone(); // may not change p
		String retstr = "=> ";
		for(int a = 0; a<n; a++)
		{
			retstr = retstr + i.charAt(a); // already transformed string
		}
		while(true)
		{
			char x = r.pop();
			if(x != '#') retstr = retstr + x; // characters remaining in stack
			else break;
		}
		return retstr;
	}
	public static void main(String[] args)
	{
		char state;
		boolean end = false;
		boolean reject = false;
		String inputstr;
		Stack<Character> dpa = new Stack<Character>();
		ArrayList<String> forout = new ArrayList<String>(); // saving leftmost derivation, print it when dpa accepts input string
		int num = 0; // number of characters read
		Scanner a = new Scanner(System.in); // scanner input
		try {
			inputstr = a.nextLine();
			state = 'p'; // initial state
			while(!end)
			{
				char x;
				switch(state)
				{
				case 'p':
					dpa.push('#');
					dpa.push('E');
					forout.add("E"); // initial output when dpa accept string
					state = 'q'; //push E# and change state to q
					break;
				case 'q': //center state
					char cur;
					if(num < inputstr.length()) cur = inputstr.charAt(num); // get char
					else cur = '\0';  // input end
					switch(cur) // change state by input
					{
					case '+':
					case '-':
						state = '+';
						break;
					case '*':
					case '/':
						state = '*';
						break;
					case '(':
						state = '(';
						break;
					case ')':
						state = ')';
						break;
					case '\0':
						state = '#';
						break;
					default:
						state = 'i';
					}			
					break;
				case 'i': // state id(numbers,alphabets)
					x = dpa.pop(); //check stack and match with parsing table
					switch(x)
					{
					case 'A':
						state = 'q';
						num++;
						break;
					case 'E':
						dpa.push('B');
						dpa.push('T');
						break;
					case 'F':
						dpa.push('A');
						break;
					case 'T':
						dpa.push('C');
						dpa.push('F');
						break;
					default:
						end = true; // program end
						reject = true; // wrong state -> not accept
						break;
					}
					if(!end) forout.add(dpatostr(dpa,num,inputstr)); //save leftmost derivation data
					break;
				case '+': // state for + and -
					if(dpa.isEmpty())
					{
						end = true;
						reject = true;
						break;
					}
					x = dpa.pop();
					switch(x)
					{
					case 'B':
						dpa.push('B');
						dpa.push('T');
						state = 'q';
						num++;
						break;
					case 'C':
						break;							
					default:
						end = true;
						reject = true;
						break;
					}
					if(!end) forout.add(dpatostr(dpa,num,inputstr));
					break;
				case '*': // state for * and /
					x = dpa.pop();
					switch(x)
					{
					case 'B':
						break;
					case 'C':
						dpa.push('C');
						dpa.push('F');
						state = 'q';
						num++;
						break;
					default:
						end = true;
						reject = true;
						break;
					}
					if(!end) forout.add(dpatostr(dpa,num,inputstr));
					break;
				case '(': // state for left bracket
					x = dpa.pop();
					switch(x)
					{
					case 'B':
						break;
					case 'C':
						break;
					case 'F':
						dpa.push('R');
						dpa.push('E');
						state = 'q';
						num++;
						break;
					case 'T':
						dpa.push('C');
						dpa.push('F');
						break;
					case 'E':
						dpa.push('B');
						dpa.push('T');
						break;
					default:
						end = true;
						reject = true;
						break;
					}
					if(!end) forout.add(dpatostr(dpa,num,inputstr));
					break;
				case ')': // state for right bracket
					x = dpa.pop();
					switch(x)
					{
					case 'B':
						break;
					case 'C':
						break;
					case 'R':
						state = 'q';
						num++;
						break;
					default:
						end = true;
						reject = true;
						break;
					}
					if(!end) forout.add(dpatostr(dpa,num,inputstr));
					break;
				case '#': // state when input string is all read
					x = dpa.pop();
					switch(x)
					{
					case 'B':
						break;
					case 'C':
						break;
					case '#':
						end = true;
						break;
					default:
						end = true;
						reject = true;
						break;
					}
					if(!end) forout.add(dpatostr(dpa,num,inputstr));
					break;
				}
			}
			if(!reject) // if accepted
			{
				System.out.println("Yes"); // print Yes
				while(forout.size() != 0)
				{
					System.out.println(forout.get(0)); // print saved data
					forout.remove(0);
				}
			}
			else System.out.println("No"); // print No
			a.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
	}
}
