
import java.util.*;
import java.io.*;

public class Main {
	static String psfix(String a) //make postfix expression
	{
		Stack<String> x = new Stack<String>(); //number stack
		Stack<String> y = new Stack<String>(); //symbol stack
		for(int i=0;i<a.length();i++)
		{
			char b = a.charAt(i);
			switch(b) {
			case '+': 
			case '*': //fall through
			case '.':
				y.push("" + b); // if string b is symbol > push to symbol stack
				break;
			case ')':
				String c = y.pop(); //found ) > pop symbol from symbol stack
				String a1,a2;
				if(c.equals("+") || c.equals(".")) //requires two arguments
				{
					a1 = x.pop();
					a2 = x.pop(); //pop two number
 					x.push(a2 + a1 + c); //push with right order
				}
				else if(c.equals("*")) //requires one argument
				{
					a1 = x.pop(); //pop one number
					x.push(a1 + c);
				}
			case '(':
				break;
			default:
				x.push("" + b); //if number > push to number stack
			}
		}
		return x.pop();
	}
	
	static ArrayList<Integer> fepsilon(ArrayList<Integer> eplist, ArrayList<Integer> strlist, Map<Integer,state> NFA)
	{
		int n;
		ArrayList<Integer> tmplist;
		while(!strlist.isEmpty())
		{
			n = strlist.get(0); 
			strlist.remove(0);//get one element
			eplist.add(n);//visit 
			tmplist = NFA.get(n).map.get(' ');//elements connected with epsilon 
			if(tmplist != null)
			{
				for(int j=0;j<tmplist.size();j++)
				{
					if(!eplist.contains(tmplist.get(j))) //if not visited
					{
						eplist.add(tmplist.get(j)); //visit
						strlist.add(tmplist.get(j)); //recursion
					}
				}
			}
		}
		return eplist;
	}
	static ArrayList<Integer> fstring(ArrayList<Integer> eplist, ArrayList<Integer> strlist, Map<Integer,state> NFA, char c)
	{
		ArrayList<Integer> tmplist;
		while(!eplist.isEmpty())
		{
			tmplist = NFA.get(eplist.get(0)).map.get(c); //elements connected with c
			eplist.remove(0);
			if(tmplist != null)
			{
				for(int j=0;j<tmplist.size();j++)
				{
					if(!strlist.contains(tmplist.get(j)))
					{
						strlist.add(tmplist.get(j)); //visit
					}
				}
			}
		}
		return strlist;
	}
	public static void main(String[] args)
	{
		String line;
		int num = 0;
		String[] a = new String[3];
		BufferedReader br = null;
		PrintWriter bw = null;
		try {
			br = new BufferedReader(new FileReader("input.txt"));
			bw = new PrintWriter("2016_11464.out"); //i/o ready
			try {
				num = Integer.parseInt(br.readLine()); //number of lines
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} 
			for(int l=0;l<num;l++)
			{
				line = br.readLine(); //read one line
				a = line.split(" "); //split by whitespace
				a[1] = psfix(a[1]); //make postfix
				//System.out.println(a[1]); - for test
				Map <Integer,state> FinalNFA = new HashMap<Integer,state>(); //final NFA(state number, state)
				Stack<Integer> InitStates = new Stack<Integer>(); //initial states of NFAs
				Stack<Integer> FinalStates = new Stack<Integer>(); //final states of NFAs
				int currnum = 0; //used number until current time
				int deletedstate = 0; //number of deleted states
				int initstate, finalstate; //initial,final state of final NFA
				ArrayList <Integer> newone; //temporary list
				int f1, f2, i1;
				state s1, s2, st1, st2; //temporary state, variables
				for(int i=0;i<a[1].length();i++)
				{
					char c = a[1].charAt(i); //read postfix string
					switch(c)
					{
					case '+':
						s1 = new state(); 
						s2 = new state(); //make new initial, final state
						currnum = FinalNFA.size()+deletedstate;
					    newone = new ArrayList<Integer>();
					    newone.add(InitStates.pop());
					    newone.add(InitStates.pop()); 
						s1.map.put(' ',newone); //connect from new initial state to two NFAs by epsilon
						f2 = FinalStates.pop();
						f1 = FinalStates.pop();
						st1 = FinalNFA.get(f1);
						st2 = FinalNFA.get(f2);
						if(st1.map.containsKey(' '))
						{
							newone = st1.map.get(' ');
						}
						else
						{
							newone = new ArrayList<Integer>();
						}
						newone.add(currnum + 2);
						st1.map.put(' ', newone); //connect from first NFA to new final state by epsilon
						if(st2.map.containsKey(' '))
						{
							newone = st2.map.get(' ');
						}
						else
						{
							newone = new ArrayList<Integer>();
						}
						newone.add(currnum + 2);
						st2.map.put(' ', newone); //connect from second NFA to new final state by epsilon
						FinalNFA.put(f1, st1);
						FinalNFA.put(f2, st2);
						FinalNFA.put(currnum + 1, s1);
						FinalNFA.put(currnum + 2, s2);
						InitStates.push(currnum + 1);
						FinalStates.push(currnum + 2);
						break;
					case '.':
					    i1 = InitStates.pop();
						f2 = FinalStates.pop();
						f1 = FinalStates.pop();
						st1 = FinalNFA.get(i1); 
						FinalNFA.put(f1, st1); //update first NFA's final state by second NFA's initial state
						FinalNFA.remove(i1); //delete second NFA's initial state
						FinalStates.push(f2); //push second NFA's final state (previously poped)
						deletedstate ++;
						break;
					case '*':
						s1 = new state();
						s2 = new state(); //two new states
						currnum = FinalNFA.size()+deletedstate;
					    i1 = InitStates.pop();
						f1 = FinalStates.pop();
						st1 = FinalNFA.get(f1);
						if(st1.map.containsKey(' '))
						{
							newone = st1.map.get(' ');
						}
						else
						{
							newone = new ArrayList<Integer>();
						}
						newone.add(i1);
						newone.add(currnum + 2); 
						st1.map.put(' ', newone); //connect from current final state to current initial state, new final state by epsilon
						newone = new ArrayList<Integer>();
						newone.add(i1);
						newone.add(currnum + 2);
						s1.map.put(' ', newone); //connect from new initial state to current initial state, new final state by epslion
						FinalNFA.put(currnum + 1, s1);
						FinalNFA.put(currnum + 2, s2);
						InitStates.push(currnum + 1);
						FinalStates.push(currnum + 2); //push new states
						break;
					default: //number as an input
						s1 = new state();
						s2 = new state(); //make new NFA
						currnum = FinalNFA.size()+deletedstate;
						newone = new ArrayList<Integer>();
						newone.add(currnum + 2);
						s1.map.put(c,newone); //connect from new initial state to new final state by input
						FinalNFA.put(currnum + 1, s1);
						FinalNFA.put(currnum + 2, s2);
						InitStates.push(currnum + 1);
						FinalStates.push(currnum + 2);
						break;
					}
				}
				initstate = InitStates.pop();
				finalstate = FinalStates.pop();
				/*
				for(int i=1;i<=FinalNFA.size()+deletedstate;i++)
				{
					if(FinalNFA.get(i) != null)
					{
						System.out.println("STATE "+ i + ":");
						ArrayList<Integer> temp = FinalNFA.get(i).map.get(' ');
						System.out.print("state connected with epsilon : ");
						if(temp != null)
							for(int j=0;j<temp.size();j++)
								System.out.print(temp.get(j) + " ");
						System.out.println();
						temp = FinalNFA.get(i).map.get('0');
						System.out.print("state connected with 0 : ");
						if(temp != null)
							for(int j=0;j<temp.size();j++)
								System.out.print(temp.get(j) + " ");
						System.out.println();
						temp = FinalNFA.get(i).map.get('1');
						System.out.print("state connected with 1 : ");
						if(temp != null)
							for(int j=0;j<temp.size();j++)
								System.out.print(temp.get(j) + " ");
						System.out.println();
					}
				}
				-fortest */
				//System.out.println("Init state : " + initstate + " Final state : " + finalstate); - for test
				ArrayList<Integer> eplist = new ArrayList<Integer>(); //epsilon move list
				ArrayList<Integer> strlist = new ArrayList<Integer>(); //character move list
				strlist.add(initstate);
				char c;
				for(int i = 0; i < a[2].length();i++)
				{
					c = a[2].charAt(i);
					eplist = fepsilon(eplist,strlist,FinalNFA); //epsilon move possible states
					strlist = fstring(eplist,strlist,FinalNFA,c); //number move possible states
				}
				eplist = fepsilon(eplist,strlist,FinalNFA);
				
				if(eplist.contains(finalstate)) //if the string can go to final state? 
					bw.println("yes");
				else
					bw.println("no");
			}
			br.close();
			bw.close(); //close i/o buffers
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
