
import java.io.*;
import java.util.*;

class tm{
	int nextstate;
	int writealpha;
	char move;
	tm(int n, int w, char m)
	{
		nextstate = n;
		writealpha = w;
		move = m;
	}
}

public class Main {
	public static void main(String[] args)
	{
		tm[][] Turing = new tm[10][10];
		BufferedReader br = null;
		String currline = null;
		String target = "3";
		int pointer = 0;
		int currstate = 0;
		int result = 0;
		Scanner c;
		try {
			br = new BufferedReader(new FileReader(args[0]));
			while(true)
			{
				currline = br.readLine();
				if(currline.equals("end")) break;
				else
				{
					int i = currline.charAt(0) - 48;
					int j = currline.charAt(1) - 48;
					Turing[i][j] = new tm(currline.charAt(2) - 48, currline.charAt(3) - 48, currline.charAt(4));
				}
			}
			c = new Scanner(System.in);
			target = target + c.nextLine() + target;
			while(true)
			{
				tm x = null;
				x = Turing[currstate][target.charAt(pointer) - 48];
				currstate = x.nextstate;
				target = target.substring(0, pointer) + String.valueOf(x.writealpha) + target.substring(pointer+1,target.length());
				if(x.move == 'L')
				{
					pointer --;
				}else if(x.move == 'R')
				{
					pointer ++;
					if(pointer >= target.length())
					{
						target = target + "3";
					}
				}
				else
				{
					break;
				}
			}
			for(int i = 0; i<target.length(); i++)
			{
				if(target.charAt(i) == '1') result ++;
			}
			System.out.println(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
