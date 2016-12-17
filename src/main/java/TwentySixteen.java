import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwentySixteen
{
	public TwentySixteen()
	{
	}

	public void run() throws Exception
	{
		day11();
	}

	private boolean checkValid(boolean[][] floor_generators, boolean[][] floor_chips)
	{
		for (int f = 0; f < 4; f++)
		{
			boolean hasGenerators = false;
			for (int g = 0; g < 5; g++)
			{
				if (floor_generators[f][g])
				{
					hasGenerators = true;
				}
			}
			if (hasGenerators)
			{
				for (int c = 0; c < 5; c++)
				{
					if (floor_chips[f][c] && !floor_generators[f][c])
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkDone(boolean[][] floor_generators, boolean[][] floor_chips)
	{
		for (int i = 0; i < 5; i++)
		{
			if (!floor_chips[3][i] || !floor_generators[3][i])
			{
				return false;
			}
		}
		return true;
	}

	private void makeMove(int elevator,
			int count)
	{
		String state = day11toString(floor_generators, floor_chips, elevator);

		if (count >= day11min)
		{
		//	System.out.println("count exceeded\n" + state);
			return;
		}
		if (checkDone(floor_generators, floor_chips))
		{
			System.out.println("Done in " + count + " moves, state is\n" + 
					day11toString(floor_generators, floor_chips, elevator));
			day11min = count;
			return;
		}
		if (!checkValid(floor_generators, floor_chips))
		{
		//	System.out.println("Invalid:\n" + state);
			return;
		}
		for (String s : day11States)
		{
			if (s.equals(state))
			{
		//		System.out.println("Already been here");
				return;
			}
		}
		day11States.add(state);
		//System.out.println("New state, count: " + count + "\n" + state);
		count++;
		if (elevator < 3) // try moving up first
		{
			// try moving generators
			for (int i = 0; i < 5; i++)
			{
				if (floor_generators[elevator][i])
				{
					floor_generators[elevator][i] = false;
					floor_generators[elevator + 1][i] = true;

					makeMove(elevator + 1, count);

					// now try moving with another generator
					for (int j = i + 1; j < 5; j++)
					{
						if (floor_generators[elevator][j])
						{
							floor_generators[elevator][j] = false;
							floor_generators[elevator + 1][j] = true;

							makeMove(elevator + 1, count);

							// and undo it
							floor_generators[elevator][j] = true;
							floor_generators[elevator + 1][j] = false;
						}
					}
					// now try moving with another chip
					for (int k = 0; k < 5; k++)
					{
						if (floor_chips[elevator][k])
						{
							floor_chips[elevator][k] = false;
							floor_chips[elevator + 1][k] = true;

							makeMove(elevator + 1, count);

							// and undo it
							floor_chips[elevator][k] = true;
							floor_chips[elevator + 1][k] = false;
						}
					}
					// and undo it
					floor_generators[elevator][i] = true;
					floor_generators[elevator + 1][i] = false;
				}
			}
			// now try moving chips
			for (int l = 0; l < 5; l++)
			{
				if (floor_chips[elevator][l])
				{
					floor_chips[elevator][l] = false;
					floor_chips[elevator + 1][l] = true;

					makeMove(elevator + 1, count);
					// and try moving with another chip
					for (int m = l + 1; m < 5; m++)
					{
						if (floor_chips[elevator][m])
						{
							floor_chips[elevator][m] = false;
							floor_chips[elevator + 1][m] = true;
							makeMove(elevator + 1, count);
							// and undo it
							floor_chips[elevator][m] = true;
							floor_chips[elevator + 1][m] = false;
						}
					}
					// and undo it
					floor_chips[elevator][l] = true;
					floor_chips[elevator + 1][l] = false;
				}
			}
		}
		// try moving back down, but let's try only taking chips down, never
		// generators
		if (elevator > 0)
		{
			// try moving generators
			for (int i = 0; i < 5; i++)
			{
				if (floor_generators[elevator][i])
				{
					floor_generators[elevator][i] = false;
					floor_generators[elevator - 1][i] = true;
					makeMove(elevator - 1, count);
					// now try moving with another generator
					for (int j = i + 1; j < 5; j++)
					{
						if (floor_generators[elevator][j])
						{
							floor_generators[elevator][j] = false;
							floor_generators[elevator - 1][j] = true;
							makeMove(elevator - 1, count);
							// and undo it
							floor_generators[elevator][j] = true;
							floor_generators[elevator - 1][j] = false;
						}
					}
					// now try moving with another chip
					for (int k = 0; k < 5; k++)
					{
						if (floor_chips[elevator][k])
						{
							floor_chips[elevator][k] = false;
							floor_chips[elevator - 1][k] = true;
							makeMove(elevator - 1, count);
							// and undo it
							floor_chips[elevator][k] = true;
							floor_chips[elevator - 1][k] = false;
						}
					}
					// and undo it
					floor_generators[elevator][i] = true;
					floor_generators[elevator - 1][i] = false;
				}
			}
			// now try moving chips
			for (int l = 0; l < 5; l++)
			{
				if (floor_chips[elevator][l])
				{
					floor_chips[elevator][l] = false;
					floor_chips[elevator - 1][l] = true;
					makeMove(elevator - 1, count);
					// and try moving with another chip
					for (int m = l + 1; m < 5; m++)
					{
						if (floor_chips[elevator][m])
						{
							floor_chips[elevator][m] = false;
							floor_chips[elevator - 1][m] = true;

							makeMove(elevator - 1, count);
							// and undo it
							floor_chips[elevator][m] = true;
							floor_chips[elevator - 1][m] = false;
						}
					}
					// and undo it
					floor_chips[elevator][l] = true;
					floor_chips[elevator - 1][l] = false;
				}
			}
		}
		//System.out.println("backtracking");
	}

	private String day11toString(boolean[][] floor_generators,
			boolean[][] floor_chips, int elevator)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 3; i >= 0; i--)
		{
			if (elevator == i)
			{
				sb.append("E ");
			} else
			{
				sb.append(". ");
			}
			for (int j = 0; j < 5; j++)
			{
				if (floor_generators[i][j])
				{
					sb.append(day11names[j]);
					sb.append("G ");
				} else
				{
					sb.append("... ");
				}
				if (floor_chips[i][j])
				{
					sb.append(day11names[j]);
					sb.append("C ");
				} else
				{
					sb.append("... ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	List<String> day11States = new ArrayList<String>();
	String[] day11names = { "Pm", "Co", "Cm", "Ru", "Pu" };
	int day11min = 120;
	boolean floor_generators[][] = { { true, false, false, false, false },
			{ false, true, true, true, true }, { false, false, false, false, false },
			{ false, false, false, false, false } };
	boolean floor_chips[][] = { { true, false, false, false, false },
			{ false, false, false, false, false }, { false, true, true, true, true },
			{ false, false, false, false, false } };

	public void day11()
	{
		
		int elevator = 0;
		int count = 0;
		System.out.println(
				"Start: \n" + day11toString(floor_generators, floor_chips, elevator));
		makeMove(elevator, count);
	}

	private class Bot
	{
		int num;
		int[] chips = new int[2];
		int numChips = 0;
		int lowTo = -999999;
		int highTo = -999999;

		Bot(int num)
		{
			this.num = num;
		}

		boolean check(int[] outputs, Map<Integer, Bot> bots)
		{
			if (numChips < 2 || lowTo == -999999)
			{
				return false;
			}
			int high;
			int low;
			if (chips[0] < chips[1])
			{
				low = chips[0];
				high = chips[1];
			} else
			{
				low = chips[1];
				high = chips[0];
			}
			if (high == 61 && low == 17)
			{
				System.out.println("Found bot: " + num);
				// return true; comment out for part 2 as need to run to
				// completion.
			}
			if (lowTo >= 10000)
			{
				outputs[lowTo - 10000] = low;
			} else
			{
				Bot b = bots.get(lowTo);
				if (b == null)
				{
					b = new Bot(lowTo);
					bots.put(lowTo, b);

				}
				b.chips[b.numChips++] = low;
				if (b.check(outputs, bots))
				{
					// return true;
				}
			}
			if (highTo >= 10000)
			{
				outputs[highTo - 10000] = high;
			} else
			{
				Bot b = bots.get(highTo);
				if (b == null)
				{
					b = new Bot(highTo);
					bots.put(highTo, b);

				}
				b.chips[b.numChips++] = high;
				if (b.check(outputs, bots))
				{
					// return true;
				}
			}
			numChips = 0; // our chips have gone.
			return false;
		}
	}

	public void day10() throws IOException
	{
		Map<Integer, Bot> bots = new HashMap<Integer, Bot>();
		int[] outputs = new int[1000];
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day10.input"));
		String line = null;
		boolean done = false;
		while ((line = br.readLine()) != null && !done)
		{
			line = line.trim();
			if (line.startsWith("bot"))
			{
				Pattern p = Pattern.compile(
						"bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int bot = Integer.parseInt(m.group(1));
				String lowDest = m.group(2);
				int low = Integer.parseInt(m.group(3));
				if (lowDest.equals("output"))
				{
					low = low + 10000;
				}
				String highDest = m.group(4);
				int high = Integer.parseInt(m.group(5));
				if (highDest.equals("output"))
				{
					high = high + 10000;
				}
				Bot b = bots.get(bot);
				if (b == null)
				{
					b = new Bot(bot);
					bots.put(bot, b);
				}
				b.lowTo = low;
				b.highTo = high;
				done = b.check(outputs, bots);
			} else if (line.startsWith("value"))
			{
				Pattern p = Pattern.compile("value (\\d+) goes to bot (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int value = Integer.parseInt(m.group(1));
				int bot = Integer.parseInt(m.group(2));
				Bot b = bots.get(bot);
				if (b == null)
				{
					b = new Bot(bot);
					bots.put(bot, b);
				}
				b.chips[b.numChips++] = value;
				done = b.check(outputs, bots);
			}
		}
		br.close();
		System.out.println(
				"outputs values 0: " + outputs[0] + " 1: " + outputs[1] + " 2: " + outputs[2]);
	}

	private long decompressLength(String str)
	{
		long total = 0;
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == '(')
			{
				int end = str.indexOf(')', i);
				String marker = str.substring(i + 1, end);
				Pattern p = Pattern.compile("(\\d+)x(\\d+)");
				Matcher m = p.matcher(marker);
				m.find();
				int length = Integer.parseInt(m.group(1));
				int reps = Integer.parseInt(m.group(2));
				String repString = str.substring(end + 1, end + 1 + length);
				total += reps * decompressLength(repString);
				i += length + 1 + marker.length();
			} else
			{
				total++;
			}
		}
		return total;
	}

	public void day9() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day9.input"));
		String line = br.readLine();
		br.close();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < line.length(); i++)
		{
			if (line.charAt(i) == '(')
			{
				int end = line.indexOf(')', i);
				String marker = line.substring(i + 1, end);
				Pattern p = Pattern.compile("(\\d+)x(\\d+)");
				Matcher m = p.matcher(marker);
				m.find();
				int length = Integer.parseInt(m.group(1));
				int reps = Integer.parseInt(m.group(2));
				String repString = line.substring(end + 1, end + 1 + length);
				// System.out.println("Got marker: " + marker + "length: " +
				// length + " reps: " + reps + " and repString: " + repString);
				for (int j = 0; j < reps; j++)
				{
					sb.append(repString);
				}
				i += length + 1 + marker.length();
			} else
			{
				sb.append(line.charAt(i));
			}
		}
		// System.out.println("String is: " + sb.toString());
		System.out.println("length is: " + sb.toString().length());
		System.out.println("length2 is: " + decompressLength(line));
	}

	private void printGrid(boolean[][] grid)
	{
		StringBuffer sb = new StringBuffer();
		for (int y = 0; y < 6; y++)
		{
			for (int x = 0; x < 50; x++)
			{
				if (grid[y][x])
				{
					sb.append('#');
				} else
				{
					sb.append('.');
				}
			}
			sb.append('\n');
		}
		System.out.println(sb.toString());
	}

	void day8() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day8.input"));
		String line = null;
		boolean[][] grid = new boolean[6][50];
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			if (line.startsWith("rect"))
			{
				Pattern p = Pattern.compile("rect (\\d+)x(\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int rectX = Integer.parseInt(m.group(1));
				int rectY = Integer.parseInt(m.group(2));
				for (int x = 0; x < rectX; x++)
				{
					for (int y = 0; y < rectY; y++)
					{
						grid[y][x] = true;
					}
				}
			} else if (line.startsWith("rotate row"))
			{
				Pattern p = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int row = Integer.parseInt(m.group(1));
				int amount = Integer.parseInt(m.group(2));
				for (int i = 0; i < amount; i++)
				{
					boolean saved = grid[row][49];
					for (int x = 49; x > 0; x--)
					{
						grid[row][x] = grid[row][x - 1];
					}
					grid[row][0] = saved;
				}

			} else if (line.startsWith("rotate column"))
			{
				Pattern p = Pattern.compile("rotate column x=(\\d+) by (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int col = Integer.parseInt(m.group(1));
				int amount = Integer.parseInt(m.group(2));
				for (int i = 0; i < amount; i++)
				{
					boolean saved = grid[5][col];
					for (int y = 5; y > 0; y--)
					{
						grid[y][col] = grid[y - 1][col];
					}
					grid[0][col] = saved;
				}
			}
			printGrid(grid);
		}
		br.close();
		int count = 0;
		for (int x = 0; x < 50; x++)
		{
			for (int y = 0; y < 6; y++)
			{
				if (grid[y][x])
				{
					count++;
				}
			}
		}
		System.out.println("Count is " + count);
	}

	void day7() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day7.input"));
		String line = null;
		int count = 0;
		int count2 = 0;
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			boolean inHypernet = false;
			char lastChar = 0;
			boolean hasAbba = false;
			boolean invalid = false;
			boolean valid2 = false;
			char[] abaA = new char[100];
			char[] abaB = new char[100];
			int abaCount = 0;
			char[] babA = new char[100];
			char[] babB = new char[100];
			int babCount = 0;
			for (int i = 0; i < line.length() - 1; i++)
			{
				char thisChar = line.charAt(i);
				if (thisChar == '[')
				{
					inHypernet = true;
					lastChar = 0;
				} else if (thisChar == ']')
				{
					inHypernet = false;
					lastChar = 0;
				} else
				{
					if (lastChar != thisChar)
					{
						if (i < line.length() - 2 && thisChar == line.charAt(i + 1)
								&& lastChar == line.charAt(i + 2))
						{
							if (inHypernet)
							{
								invalid = true;
							} else
							{
								hasAbba = true;
							}
						}
						if (lastChar == line.charAt(i + 1))
						{
							if (inHypernet)
							{
								babB[babCount] = lastChar;
								babA[babCount] = thisChar;
								babCount++;
							} else
							{
								abaA[abaCount] = lastChar;
								abaB[abaCount] = thisChar;
								abaCount++;
							}
						}
					}
					lastChar = thisChar;
				}
			}
			if (hasAbba && !invalid)
			{
				count++;
			}
			// System.out.println("abaA is: " + new String(abaA) + "abaB is: " +
			// new String(abaB));
			// System.out.println("babA is: " + new String(babA) + "babB is: " +
			// new String(babB));

			for (int j = 0; j < babCount; j++)
			{
				for (int k = 0; k < abaCount; k++)
				{
					if (babA[j] == abaA[k] && babB[j] == abaB[k])
					{
						valid2 = true;
					}
				}
			}
			if (valid2)
			{
				count2++;
			}
		}
		br.close();
		System.out.println("Valid addresses count: " + count + " and valid2: " + count2);
	}

	void day6() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day6.input"));
		String line = null;
		int charCount[][] = new int[8][26];
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			for (int i = 0; i < 8; i++)
			{
				char c = line.charAt(i);
				charCount[i][c - 'a']++;
			}
		}
		br.close();
		String message = "";
		String message2 = "";
		for (int i = 0; i < 8; i++)
		{
			int max = 0;
			int min = 99999;
			char maxChar = 'X';
			char minChar = 'X';
			for (int k = 0; k < 26; k++)
			{
				if (charCount[i][k] > max)
				{
					max = charCount[i][k];
					maxChar = (char) ('a' + k);
				}
				if (charCount[i][k] < min)
				{
					min = charCount[i][k];
					minChar = (char) ('a' + k);
				}
			}
			message = message + Character.toString(maxChar);
			message2 = message2 + Character.toString(minChar);
		}
		System.out.println("Message is: " + message + " message2 is: " + message2);
	}

	void day5() throws IOException, NoSuchAlgorithmException
	{
		String input = "ojvtpuvg";
		MessageDigest md = MessageDigest.getInstance("MD5");
		int num = 0;
		int digitsFound = 0;
		int digitsFound2 = 0;
		String password = "";
		char[] password2 = new char[8];
		for (int i = 0; i < 8; i++)
		{
			password2[i] = '-';
		}
		while (digitsFound < 8 || digitsFound2 < 8)
		{
			String key = input + num;
			md.reset();
			byte[] md5 = md.digest(key.getBytes("UTF-8"));
			BigInteger bi = new BigInteger(1, md5);
			String hash = String.format("%0" + (md5.length << 1) + "X", bi);
			// System.out.println("Code is: " + key + " md5 is: " + hash);
			if (hash.startsWith("00000"))
			{
				if (digitsFound < 8)
				{
					System.out.println("Password char: " + hash.charAt(5));
					password = password + hash.charAt(5);
					digitsFound++;
				}
				if (digitsFound2 < 8)
				{
					if (Character.isDigit(hash.charAt(5)))
					{
						int pos = Integer.parseInt(hash.substring(5, 6));
						if (pos < 8 && password2[pos] == '-')
						{
							password2[pos] = hash.charAt(6);
							digitsFound2++;
							System.out.println("Second password is: " + new String(password2));
						}
					}
				}
			}
			num++;
		}
		System.out.println("password is: " + password);
	}

	void day4() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day4.input"));
		String line = null;
		int sum = 0;
		while ((line = br.readLine()) != null)
		{
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
					decoded = decoded + Character.toString((char) ('a' + ((c - 'a' + code) % 26)));
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
							check = check + Character.toString((char) ('a' + j));
							checkFound++;
							if (checkFound == 5)
								break;
						}
					}
				}
			}

			if (check.equals(checksum))
			{
				sum += code;
			}

			if (decoded.contains("northpole"))
			{
				System.out.println("decoded name: " + decoded + " has id: " + code);
			}
		}
		System.out.println("Sum is: " + sum);
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
		while ((line = br.readLine()) != null)
		{
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
				if ((tri1[0] < tri1[1] + tri1[2]) && (tri1[1] < tri1[0] + tri1[2])
						&& (tri1[2] < tri1[0] + tri1[1]))
				{
					count2++;
				}
				if ((tri2[0] < tri2[1] + tri2[2]) && (tri2[1] < tri2[0] + tri2[2])
						&& (tri2[2] < tri2[0] + tri2[1]))
				{
					count2++;
				}
				if ((tri3[0] < tri3[1] + tri3[2]) && (tri3[1] < tri3[0] + tri3[2])
						&& (tri3[2] < tri3[0] + tri3[1]))
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
		String input = "URULLLLLRLDDUURRRULLLDURRDRDRDLURURURLDLLLLRUDDRRLUDDDDDDLRLRDDDUUDUDLDULUDLDURDULLRDDURLLLRRRLLRURLLUDRDLLRRLDDRUDULRRDDLUUUDRLDLURRRULURRDLLLDDDLUDURDDRLDDDLLRULDRUDDDLUDLURUDLLRURRUURUDLLLUUUUDDURDRDDDLDRRUDURDLLLULUDURURDUUULRULUDRUUUUDLRLUUUUUDDRRDDDURULLLRRLDURLDLDRDLLLUULLRRLLLLDRLRDRRDRRUDDLULUUDDDDRRUUDDLURLRDUUDRRLDUDLRRRLRRUUDURDRULULRDURDRRRDLDUUULRDDLRLRDLUUDDUDDRLRRULLLULULLDDDRRDUUUDDRURDDURDRLRDLDRDRULRLUURUDRLULRLURLRRULDRLRDUDLDURLLRLUDLUDDURDUURLUDRLUL,"
				+ "LLLUUURUULDDDULRRDLRLLLLLLLLRURRDLURLUDRRDDULDRRRRRRLDURRULDDULLDDDRUUDLUDULLDLRRLUULULRULURDURLLDULURDUDLRRLRLLDULLRLDURRUULDLDULLRDULULLLULDRLDLDLDLDDLULRLDUDRULUDDRDDRLRLURURRDULLUULLDRRDRRDLDLLRDLDDUUURLUULDDRRRUULDULDDRDDLULUDRURUULLUDRURDRULDRUULLRRDURUDDLDUULLDDRLRRDUDRLRRRLDRLRULDRDRRUDRLLLDDUDLULLURRURRLUURDRLLDLLDUDLUUURRLRDDUDRLUDLLRULLDUUURDLUUUDUDULRLDLDRUUDULRDRRUDLULRLRDLDRRDDDUDLDLDLRUURLDLLUURDLDLRDLDRUDDUURLLLRDRDRRULLRLRDULUDDDLUDURLDUDLLRULRDURDRDLLULRRDLLLDUURRDUDDLDDRULRRRRLRDDRURLLRRLLL,"
				+ "DRURLDDDDRLUDRDURUDDULLRRLLRLDDRLULURLDURRLDRRLRLUURDDRRDLRDLDLULDURUDRLRUDULRURURLRUDRLLDDUDDRDLDRLLDDLRRDRUUULDUUDRUULRLLDLLULLLRRDRURDLDDRRDDUDDULLDUUULDRUDLDLURLDRURUDLRDDDURRLRDDUDLLLRRUDRULRULRRLLUUULDRLRRRLLLDLLDUDDUUDRURLDLRRUUURLUDDDRRDDLDDDDLUURDDULDRLRURLULLURRDRLLURLLLURDURLDLUDUUDUULLRLDLLLLULRDDLDUDUDDDUULURRLULDLDRLRDRLULLUDDUUUUURDRURLDUULDRRDULUDUDLDDRDLUDDURUDURLDULRUDRRDLRLRDRRURLDLURLULULDDUUDLRLLLLURRURULDDRUUULLDULDRDULDDDLLLRLULDDUDLRUDUDUDURLURLDDLRULDLURD,"
				+ "DRUDRDURUURDLRLUUUUURUDLRDUURLLDUULDUULDLURDDUULDRDDRDULUDDDRRRRLDDUURLRDLLRLRURDRRRDURDULRLDRDURUDLLDDULRDUDULRRLLUDLLUUURDULRDDLURULRURDDLRLLULUDURDRRUDLULLRLDUDLURUDRUULDUDLRDUDRRDULDDLDRLRRULURULUURDULRRLDLDULULRUUUUULUURLURLRDLLRRRRLURRUDLRLDDDLDRDRURLULRDUDLRLURRDRRLRLLDLDDLLRRULRLRLRUDRUUULLDUULLDDRLUDDRURLRLDLULDURLLRRLDLLRDDDUDDUULLUDRUDURLLRDRUDLUDLLUDRUUDLRUURRRLLUULLUUURLLLRURUULLDLLDURUUUULDDDLRLURDRLRRRRRRUDLLLRUUULDRRDLRDLLDRDLDDLDLRDUDLDDRDDDDRULRRLRDULLDULULULRULLRRLLUURUUUDLDLUDUDDDLUUDDDDUDDDUURUUDRDURRLUULRRDUUDDUDRRRDLRDRLDLRRURUUDRRRUUDLDRLRDURD,"
				+ "DDDLRURUDRRRURUUDLRLRDULDRDUULRURRRUULUDULDDLRRLLRLDDLURLRUDRLRRLRDLRLLDDLULDLRRURDDRDLLDDRUDRRRURRDUDULUDDULRRDRLDUULDLLLDRLUDRDURDRRDLLLLRRLRLLULRURUUDDRULDLLRULDRDLUDLULDDDLLUULRRLDDUURDLULUULULRDDDLDUDDLLLRRLLLDULRDDLRRUDDRDDLLLLDLDLULRRRDUDURRLUUDLLLLDUUULDULRDRULLRDRUDULRUUDULULDRDLDUDRRLRRDRLDUDLULLUDDLURLUUUDRDUDRULULDRDLRDRRLDDRRLUURDRULDLRRLLRRLDLRRLDLDRULDDRLURDULRRUDURRUURDUUURULUUUDLRRLDRDLULDURUDUDLUDDDULULRULDRRRLRURLRLRLUDDLUUDRRRLUUUDURLDRLRRDRRDURLLL";
		int x = 1;
		int y = 1;
		int x1 = 0;
		int y1 = 2;
		char[][] keypad = { { '7', '8', '9' }, { '4', '5', '6' }, { '1', '2', '3' } };
		char[][] keypad2 = { { 'x', 'x', 'D', 'x', 'x' }, { 'x', 'A', 'B', 'C', 'x' },
				{ '5', '6', '7', '8', '9' }, { 'x', '2', '3', '4', 'x' },
				{ 'x', 'x', '1', 'x', 'x' } };
		String[] lines = input.split(",");
		for (String line : lines)
		{
			for (int i = 0; i < line.length(); i++)
			{
				char c = line.charAt(i);
				switch (c)
				{
				case 'U':
					if (y < 2)
						y++;
					if (y1 < 4 && keypad2[y1 + 1][x1] != 'x')
						y1++;
					break;
				case 'R':
					if (x < 2)
						x++;
					if (x1 < 4 && keypad2[y1][x1 + 1] != 'x')
						x1++;
					break;
				case 'D':
					if (y > 0)
						y--;
					if (y1 > 0 && keypad2[y1 - 1][x1] != 'x')
						y1--;
					break;
				case 'L':
					if (x > 0)
						x--;
					if (x1 > 0 && keypad2[y1][x1 - 1] != 'x')
						x1--;
					break;
				}
			}
			System.out.println("Got code1: " + keypad[y][x] + " code2: " + keypad2[y1][x1]);
		}
	}

	void day1()
	{
		// String input = "R8, R4, R4, R8";
		String input = "R5, R4, R2, L3, R1, R1, L4, L5, R3, L1, L1, R4, L2, R1, R4, R4, L2, L2, R4, L4, R1, R3, L3, L1, L2, R1, R5, L5, L1, L1, R3, R5, L1, R4, L5, R5, R1, L185, R4, L1, R51, R3, L2, R78, R1, L4, R188, R1, L5, R5, R2, R3, L5, R3, R4, L1, R2, R2, L4, L4, L5, R5, R4, L4, R2, L5, R2, L1, L4, R4, L4, R2, L3, L4, R2, L3, R3, R2, L2, L3, R4, R3, R1, L4, L2, L5, R4, R4, L1, R1, L5, L1, R3, R1, L2, R1, R1, R3, L4, L1, L3, R2, R4, R2, L2, R1, L5, R3, L3, R3, L1, R4, L3, L3, R4, L2, L1, L3, R2, R3, L2, L1, R4, L3, L5, L2, L4, R1, L4, L4, R3, R5, L4, L1, L1, R4, L2, R5, R1, R1, R2, R1, R5, L1, L3, L5, R2";
		String inputs[] = input.split(",");
		int x = 0;
		int y = 0;
		int dir = 0;
		int dirs[][] = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // 0 -
																		// north,
																		// 1 -
																		// east
																		// etc.
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
					System.out.println("Found hq at " + x + "," + y + " distance: "
							+ (Math.abs(x) + Math.abs(y)));
				}
			}
		}
		System.out.println(
				"Final coords: " + x + "," + y + " distance: " + (Math.abs(x) + Math.abs(y)));
	}
}
