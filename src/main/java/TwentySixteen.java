import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwentySixteen {
	public TwentySixteen()
	{
	}
	
	void day4() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day4.input"));
		String line = null;
		int sum = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			Pattern p = Pattern.compile("([a-z-]+)-(\\d+)\\[([a-z]+)\\]");
			Matcher m = p.matcher(line);
			m.find();
			String name = m.group(1);
			int code = Integer.parseInt(m.group(2));
			String checksum = m.group(3);
			
			int counts[] = new int[26];
			int max = 0;
			String decoded = "";
			for (int i = 0; i < name.length(); i++)
			{
				char c = name.charAt(i);
				if (c != '-')
				{
					counts[c - 'a']++;
					if (counts[c - 'a'] > max)
					{
						max = counts[c - 'a'];
					}
					decoded = decoded + Character.toString((char)('a' + ((c - 'a' + code) % 26)));
				} else
				{
					decoded = decoded + " ";
				}
			}
			String check = "";
			int checkFound = 0;
			while (checkFound < 5)
			{
				for (int maxcount = max; checkFound < 5; maxcount--)
				{
					for (int j = 0; j < 26; j++)
					{
						if (counts[j] == maxcount)
						{
							check = check + Character.toString((char)('a' + j));
							checkFound++;
							if (checkFound == 5)
								break;
						}
					}
				}
			}
			
			if (check.equals(checksum)) {
				sum += code;
			}
			
			System.out.println("decoded name: " + decoded);
		}
		System.out.println("Sum is: " + sum );
		br.close();
	}
	
	void day3() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day3.input"));
		String line = null;
		int count = 0;
		int count2 = 0;
		int tri1[] = new int[3];
		int tri2[] = new int[3];
		int tri3[] = new int[3];
		int triCount = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			Pattern p = Pattern.compile("(\\d+)\\s*(\\d+)\\s*(\\d+)");
			Matcher m = p.matcher(line);
			m.find();
			int x = Integer.parseInt(m.group(1));
			int y = Integer.parseInt(m.group(2));
			int z = Integer.parseInt(m.group(3));
			tri1[triCount] = x;
			tri2[triCount] = y;
			tri3[triCount] = z;
			triCount++;
			if ((x < y + z) && (y < x + z) && (z < x + y))
			{
				count++;
			}
			if (triCount == 3)
			{
				triCount = 0;
				if ((tri1[0] < tri1[1] + tri1[2]) && (tri1[1] < tri1[0] + tri1[2]) && (tri1[2] < tri1[0] + tri1[1]))
				{
					count2++;
				}
				if ((tri2[0] < tri2[1] + tri2[2]) && (tri2[1] < tri2[0] + tri2[2]) && (tri2[2] < tri2[0] + tri2[1]))
				{
					count2++;
				}
				if ((tri3[0] < tri3[1] + tri3[2]) && (tri3[1] < tri3[0] + tri3[2]) && (tri3[2] < tri3[0] + tri3[1]))
				{
					count2++;
				}
			}
		}
		System.out.println("Count is: " + count + " count2 is: " + count2);
		br.close();
	}

	void day2()
	{
		String input = "URULLLLLRLDDUURRRULLLDURRDRDRDLURURURLDLLLLRUDDRRLUDDDDDDLRLRDDDUUDUDLDULUDLDURDULLRDDURLLLRRRLLRURLLUDRDLLRRLDDRUDULRRDDLUUUDRLDLURRRULURRDLLLDDDLUDURDDRLDDDLLRULDRUDDDLUDLURUDLLRURRUURUDLLLUUUUDDURDRDDDLDRRUDURDLLLULUDURURDUUULRULUDRUUUUDLRLUUUUUDDRRDDDURULLLRRLDURLDLDRDLLLUULLRRLLLLDRLRDRRDRRUDDLULUUDDDDRRUUDDLURLRDUUDRRLDUDLRRRLRRUUDURDRULULRDURDRRRDLDUUULRDDLRLRDLUUDDUDDRLRRULLLULULLDDDRRDUUUDDRURDDURDRLRDLDRDRULRLUURUDRLULRLURLRRULDRLRDUDLDURLLRLUDLUDDURDUURLUDRLUL," +
				"LLLUUURUULDDDULRRDLRLLLLLLLLRURRDLURLUDRRDDULDRRRRRRLDURRULDDULLDDDRUUDLUDULLDLRRLUULULRULURDURLLDULURDUDLRRLRLLDULLRLDURRUULDLDULLRDULULLLULDRLDLDLDLDDLULRLDUDRULUDDRDDRLRLURURRDULLUULLDRRDRRDLDLLRDLDDUUURLUULDDRRRUULDULDDRDDLULUDRURUULLUDRURDRULDRUULLRRDURUDDLDUULLDDRLRRDUDRLRRRLDRLRULDRDRRUDRLLLDDUDLULLURRURRLUURDRLLDLLDUDLUUURRLRDDUDRLUDLLRULLDUUURDLUUUDUDULRLDLDRUUDULRDRRUDLULRLRDLDRRDDDUDLDLDLRUURLDLLUURDLDLRDLDRUDDUURLLLRDRDRRULLRLRDULUDDDLUDURLDUDLLRULRDURDRDLLULRRDLLLDUURRDUDDLDDRULRRRRLRDDRURLLRRLLL,"  +
				"DRURLDDDDRLUDRDURUDDULLRRLLRLDDRLULURLDURRLDRRLRLUURDDRRDLRDLDLULDURUDRLRUDULRURURLRUDRLLDDUDDRDLDRLLDDLRRDRUUULDUUDRUULRLLDLLULLLRRDRURDLDDRRDDUDDULLDUUULDRUDLDLURLDRURUDLRDDDURRLRDDUDLLLRRUDRULRULRRLLUUULDRLRRRLLLDLLDUDDUUDRURLDLRRUUURLUDDDRRDDLDDDDLUURDDULDRLRURLULLURRDRLLURLLLURDURLDLUDUUDUULLRLDLLLLULRDDLDUDUDDDUULURRLULDLDRLRDRLULLUDDUUUUURDRURLDUULDRRDULUDUDLDDRDLUDDURUDURLDULRUDRRDLRLRDRRURLDLURLULULDDUUDLRLLLLURRURULDDRUUULLDULDRDULDDDLLLRLULDDUDLRUDUDUDURLURLDDLRULDLURD," +
				"DRUDRDURUURDLRLUUUUURUDLRDUURLLDUULDUULDLURDDUULDRDDRDULUDDDRRRRLDDUURLRDLLRLRURDRRRDURDULRLDRDURUDLLDDULRDUDULRRLLUDLLUUURDULRDDLURULRURDDLRLLULUDURDRRUDLULLRLDUDLURUDRUULDUDLRDUDRRDULDDLDRLRRULURULUURDULRRLDLDULULRUUUUULUURLURLRDLLRRRRLURRUDLRLDDDLDRDRURLULRDUDLRLURRDRRLRLLDLDDLLRRULRLRLRUDRUUULLDUULLDDRLUDDRURLRLDLULDURLLRRLDLLRDDDUDDUULLUDRUDURLLRDRUDLUDLLUDRUUDLRUURRRLLUULLUUURLLLRURUULLDLLDURUUUULDDDLRLURDRLRRRRRRUDLLLRUUULDRRDLRDLLDRDLDDLDLRDUDLDDRDDDDRULRRLRDULLDULULULRULLRRLLUURUUUDLDLUDUDDDLUUDDDDUDDDUURUUDRDURRLUULRRDUUDDUDRRRDLRDRLDLRRURUUDRRRUUDLDRLRDURD," +
				"DDDLRURUDRRRURUUDLRLRDULDRDUULRURRRUULUDULDDLRRLLRLDDLURLRUDRLRRLRDLRLLDDLULDLRRURDDRDLLDDRUDRRRURRDUDULUDDULRRDRLDUULDLLLDRLUDRDURDRRDLLLLRRLRLLULRURUUDDRULDLLRULDRDLUDLULDDDLLUULRRLDDUURDLULUULULRDDDLDUDDLLLRRLLLDULRDDLRRUDDRDDLLLLDLDLULRRRDUDURRLUUDLLLLDUUULDULRDRULLRDRUDULRUUDULULDRDLDUDRRLRRDRLDUDLULLUDDLURLUUUDRDUDRULULDRDLRDRRLDDRRLUURDRULDLRRLLRRLDLRRLDLDRULDDRLURDULRRUDURRUURDUUURULUUUDLRRLDRDLULDURUDUDLUDDDULULRULDRRRLRURLRLRLUDDLUUDRRRLUUUDURLDRLRRDRRDURLLL";
		int x = 1;
		int y = 1;
		int x1 = 0;
		int y1 = 2;
		char[][] keypad = {
				{'7','8','9'},
				{'4','5','6'},
				{'1','2','3'}};
		char[][] keypad2 = {
				{'x', 'x', 'D', 'x', 'x'},
				{'x', 'A', 'B', 'C', 'x'},
				{'5', '6', '7', '8', '9'},
				{'x', '2', '3', '4', 'x'},
				{'x', 'x', '1', 'x', 'x'}};
		String[] lines = input.split(",");
		for (String line : lines)
		{
			for (int i = 0; i < line.length(); i++)
			{
				char c = line.charAt(i);
				switch(c) 
				{
				case 'U':
					if (y < 2) y++;
					if (y1 < 4 && keypad2[y1 + 1][x1] != 'x') y1++;
					break;
				case 'R':
					if (x < 2) x++;
					if (x1 < 4 && keypad2[y1][x1 + 1] != 'x') x1++;
					break;
				case 'D':
					if (y > 0) y--;
					if (y1 > 0 && keypad2[y1 - 1][x1] != 'x') y1--;
					break;
				case 'L':
					if (x > 0) x--;
					if (x1 > 0 && keypad2[y1][x1 - 1] != 'x') x1--;
					break;
				}
			}
			System.out.println("Got code1: " + keypad[y][x] + " code2: " + keypad2[y1][x1]);
		}
	}
	
	void day1() 
	{
		//String input = "R8, R4, R4, R8";
		String input = "R5, R4, R2, L3, R1, R1, L4, L5, R3, L1, L1, R4, L2, R1, R4, R4, L2, L2, R4, L4, R1, R3, L3, L1, L2, R1, R5, L5, L1, L1, R3, R5, L1, R4, L5, R5, R1, L185, R4, L1, R51, R3, L2, R78, R1, L4, R188, R1, L5, R5, R2, R3, L5, R3, R4, L1, R2, R2, L4, L4, L5, R5, R4, L4, R2, L5, R2, L1, L4, R4, L4, R2, L3, L4, R2, L3, R3, R2, L2, L3, R4, R3, R1, L4, L2, L5, R4, R4, L1, R1, L5, L1, R3, R1, L2, R1, R1, R3, L4, L1, L3, R2, R4, R2, L2, R1, L5, R3, L3, R3, L1, R4, L3, L3, R4, L2, L1, L3, R2, R3, L2, L1, R4, L3, L5, L2, L4, R1, L4, L4, R3, R5, L4, L1, L1, R4, L2, R5, R1, R1, R2, R1, R5, L1, L3, L5, R2";
		String inputs[] = input.split(",");
		int x = 0;
		int y = 0;
		int dir = 0;
		int dirs[][] = {{0,1},{1,0},{0,-1},{-1,0}}; // 0 - north, 1 - east etc.
		Set<String> visited = new HashSet<String>();
		boolean foundhq = false;
		for (String inp : inputs)
		{
			inp = inp.trim();
			if (inp.charAt(0) == 'R')
			{
				dir = (dir + 1) % 4;
			} else 
			{
				dir = (dir + 3) % 4;
			}
			int amount = Integer.parseInt(inp.substring(1));
			for (int j = 0; j < amount; j++)
			{
				visited.add(x + "-" + y);
				x += dirs[dir][0];
				y += dirs[dir][1];
				if (!foundhq && visited.contains(x + "-" + y))
				{
					foundhq = true;
					System.out.println("Found hq at " + x + "," + y + " distance: " + (Math.abs(x) + Math.abs(y)));
				}
			}
		}
		System.out.println("Final coords: " + x + "," + y + " distance: " + (Math.abs(x) + Math.abs(y)));
	}
}
