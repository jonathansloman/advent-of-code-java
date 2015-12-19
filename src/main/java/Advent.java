import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Advent
{
	static long startTime;

	public Advent()
	{
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println("dir is: " + System.getProperty("user.dir"));
		System.out.println("Started: ");
		Advent a = new Advent();
		startTime = System.currentTimeMillis();
		// a.day1();
		// a.day2();
		// a.day3();
		// a.day4();
		// a.day5();
		// a.day6();
		// a.day7();
		// a.day8();
		// a.day9();
		// a.day10();
		// a.day11();
		// a.day12();
		// a.day13();
		//a.day14();
		//a.day15();
		//a.day16();
		//a.day17();
		//a.day18();
		a.day19();
		System.out.println("Completed in: " + (System.currentTimeMillis() - startTime));
	}

	public void day1() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day1.input"));
		String input = br.readLine();
		br.close();
		int floor = 0;
		int basement = -1;
		for (int i = 0; i < input.length(); i++)
		{
			if (input.charAt(i) == '(')
			{
				floor++;
			} else if (input.charAt(i) == ')')
			{
				floor--;
			}
			if (basement == -1 && floor == -1)
			{
				basement = i + 1;
			}
		}
		System.out.println("floor is: " + floor + " basement reached at: " + basement);
	}

	public void day2() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day2.input"));
		String line = null;
		int total = 0;
		int ribbon = 0;
		while ((line = br.readLine()) != null)
		{
			String[] parts = line.split("x");
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			int z = Integer.parseInt(parts[2]);
			total += 2 * x * y + 2 * x * z + 2 * y * z;
			ribbon += x * y * z;
			if (x >= y && x >= z)
			{
				total += y * z;
				ribbon += 2 * y + 2 * z;
			} else if (y >= x && y >= z)
			{
				total += x * z;
				ribbon += 2 * x + 2 * z;
			} else
			{
				total += x * y;
				ribbon += 2 * x + 2 * y;
			}
		}
		br.close();
		System.out.println("total for day 2: " + total + " with ribbon: " + ribbon);
	}

	public void day3() throws IOException
	{
		Map<String, Integer> locations = new HashMap<String, Integer>();
		Map<String, Integer> locations2 = new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day3.input"));
		String line = null;
		int x = 0;
		int y = 0;
		int santax = 0;
		int santay = 0;
		int robox = 0;
		int roboy = 0;
		boolean santaturn = true;
		locations.put(x + "x" + y, 1);
		locations2.put(santax + "x" + santay, 1);
		while ((line = br.readLine()) != null)
		{
			for (int i = 0; i < line.length(); i++)
			{
				if (line.charAt(i) == '>')
				{
					x++;
					if (santaturn)
					{
						santax++;
					} else
					{
						robox++;
					}
				} else if (line.charAt(i) == '<')
				{
					x--;
					if (santaturn)
					{
						santax--;
					} else
					{
						robox--;
					}
				} else if (line.charAt(i) == '^')
				{
					y++;
					if (santaturn)
					{
						santay++;
					} else
					{
						roboy++;
					}
				} else if (line.charAt(i) == 'v')
				{
					y--;
					if (santaturn)
					{
						santay--;
					} else
					{
						roboy--;
					}
				} else
					continue;
				String loc = x + "x" + y;
				if (locations.get(loc) == null)
				{
					locations.put(loc, 1);
				} else
				{
					int oldval = locations.get(loc);
					locations.put(loc, oldval + 1);
				}
				String loc2;
				if (santaturn)
				{
					loc2 = santax + "x" + santay;
					santaturn = false;
				} else
				{
					loc2 = robox + "x" + roboy;
					santaturn = true;
				}
				if (locations2.get(loc2) == null)
				{
					locations2.put(loc2, 1);
				} else
				{
					int oldval = locations2.get(loc2);
					locations2.put(loc2, oldval + 1);
				}
			}
		}
		br.close();
		System.out.println("Total houses is: " + locations.keySet().size() + " and for next year: "
				+ locations2.keySet().size());
	}

	public void day4() throws Exception
	{
		String keystart = "ckczppom";
		MessageDigest md = MessageDigest.getInstance("MD5");
		int num = 0;
		boolean found5 = false;
		while (true)
		{
			String key = keystart + num;
			md.reset();
			byte[] md5 = md.digest(key.getBytes("UTF-8"));
			BigInteger bi = new BigInteger(1, md5);
			String hash = String.format("%0" + (md5.length << 1) + "X", bi);
			if (!found5 && hash.startsWith("00000"))
			{
				System.out.println("num for 00000 is: " + num);
				found5 = true;
			}
			if (hash.startsWith("000000"))
			{
				System.out.println("num for 000000 is: " + num);
				break;
			}
			num++;
		}
	}

	public void day5() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day5.input"));
		String line = null;
		int total = 0;
		int total2 = 0;
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			int vowels = 0;
			boolean doubleletter = false;
			boolean badstring = false;

			boolean repeatbetween = false;
			boolean repeatpair = false;
			for (int i = 0; i < line.length(); i++)
			{
				char c = line.charAt(i);
				if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
				{
					vowels++;
				}
				if (i > 0 && c == line.charAt(i - 1))
				{
					doubleletter = true;
				}
				if (i > 1 && c == line.charAt(i - 2))
				{
					repeatbetween = true;
				}
				if (i > 0)
				{
					String pair = line.substring(i - 1, i + 1);
					if (i > 2)
					{
						String prepart = line.substring(0, i - 2);
						if (prepart.indexOf(pair) != -1)
						{
							repeatpair = true;
						}
					}
					if (i < line.length() - 2)
					{
						String postpart = line.substring(i + 1, line.length());
						if (postpart.indexOf(pair) != -1)
						{
							repeatpair = true;
						}
					}
				}
			}
			if (line.indexOf("ab") != -1 || line.indexOf("cd") != -1 || line.indexOf("xy") != -1
					|| line.indexOf("pq") != -1)
			{
				badstring = true;
			}

			if (vowels >= 3 && doubleletter && !badstring)
			{
				total++;
			}
			if (repeatbetween && repeatpair)
			{
				total2++;
			}
		}
		br.close();
		System.out.println("nice strings: " + total + " and second: " + total2);

	}

	public void day6() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day6.input"));
		String line = null;
		boolean[][] grid = new boolean[1000][1000];
		int[][] grid2 = new int[1000][1000];
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			int action = 0;
			String rest;
			if (line.startsWith("toggle"))
			{
				action = 1;
				rest = line.substring(7);
			} else if (line.startsWith("turn on"))
			{
				action = 2;
				rest = line.substring(8);
			} else if (line.startsWith("turn off"))
			{
				action = 3;
				rest = line.substring(9);
			} else
			{
				System.out.println("Bad line: " + line);
				continue;
			}
			Pattern p = Pattern.compile("(\\d+),(\\d+) through (\\d+),(\\d+)");
			Matcher m = p.matcher(rest);
			m.find();
			int x1 = Integer.parseInt(m.group(1));
			int y1 = Integer.parseInt(m.group(2));
			int x2 = Integer.parseInt(m.group(3));
			int y2 = Integer.parseInt(m.group(4));
			for (int x = x1; x <= x2; x++)
			{
				for (int y = y1; y <= y2; y++)
				{
					if (action == 1)
					{
						grid[x][y] = !grid[x][y];
						grid2[x][y] += 2;
					} else if (action == 2)
					{
						grid[x][y] = true;
						grid2[x][y]++;
					} else if (action == 3)
					{
						grid[x][y] = false;
						if (grid2[x][y] > 0)
						{
							grid2[x][y]--;
						}
					}
				}
			}

		}
		int total = 0;
		int total2 = 0;
		for (int x = 0; x < 1000; x++)
		{
			for (int y = 0; y < 1000; y++)
			{
				if (grid[x][y])
				{
					total++;
				}
				total2 += grid2[x][y];
			}
		}
		br.close();
		System.out.println("Total is: " + total + " and second: " + total2);
	}

	class Operation
	{
		int operation;
		String input1;
		String input2;
		String output;
	}

	char findValue(Map<String, Operation> operations, String wire)
	{
		if (Character.isDigit(wire.charAt(0)))
		{
			return (char) Integer.parseInt(wire);
		}
		Operation o = operations.get(wire);
		if (o == null)
		{
			System.out.println("Missing operation for: " + wire);
			return (char) 0;
		}
		char newval;
		switch (o.operation)
		{
		case 1: // NOT
			newval = (char) ~(findValue(operations, o.input1));
			o.operation = 6;
			o.input1 = Integer.toString((int) newval);
			return newval;
		case 2: // LSHIFT
			newval = (char) (findValue(operations, o.input1) << (findValue(operations, o.input2)));
			o.operation = 6;
			o.input1 = Integer.toString((int) newval);
			return newval;
		case 3: // RSHIFT
			newval = (char) (findValue(operations, o.input1) >> (findValue(operations, o.input2)));
			o.operation = 6;
			o.input1 = Integer.toString((int) newval);
			return newval;
		case 4: // AND
			newval = (char) (findValue(operations, o.input1) & (findValue(operations, o.input2)));
			o.operation = 6;
			o.input1 = Integer.toString((int) newval);
			return newval;
		case 5: // OR
			newval = (char) (findValue(operations, o.input1) | (findValue(operations, o.input2)));
			o.operation = 6;
			o.input1 = Integer.toString((int) newval);
			return newval;
		case 6: // ASSIGN
			return findValue(operations, o.input1);
		default:
			System.out.println("ERROR!");
			return 0;
		}
	}

	public void day7() throws Exception
	{
		// change b in input to output for a for second part
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day7.input"));
		String line = null;
		Map<String, Operation> operations = new HashMap<String, Operation>();
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			Operation o = new Operation();
			try
			{
				if (line.startsWith("NOT"))
				{
					Pattern p = Pattern.compile("NOT ([a-z]+) -> ([a-z]+)");
					Matcher m = p.matcher(line);
					m.find();
					o.operation = 1;
					o.input1 = m.group(1);
					o.output = m.group(2);
				} else if (line.indexOf("LSHIFT") != -1)
				{
					Pattern p = Pattern.compile("([a-z]+) LSHIFT (\\d+) -> ([a-z]+)");
					Matcher m = p.matcher(line);
					m.find();
					o.operation = 2;
					o.input1 = m.group(1);
					o.input2 = m.group(2);
					o.output = m.group(3);
				} else if (line.indexOf("RSHIFT") != -1)
				{
					Pattern p = Pattern.compile("([a-z]+) RSHIFT (\\d+) -> ([a-z]+)");
					Matcher m = p.matcher(line);
					m.find();
					o.operation = 3;
					o.input1 = m.group(1);
					o.input2 = m.group(2);
					o.output = m.group(3);
				} else if (line.indexOf("AND") != -1)
				{
					Pattern p = Pattern.compile("([a-z0-9]+) AND ([a-z]+) -> ([a-z]+)");
					Matcher m = p.matcher(line);
					m.find();
					o.operation = 4;
					o.input1 = m.group(1);
					o.input2 = m.group(2);
					o.output = m.group(3);
				} else if (line.indexOf("OR") != -1)
				{
					Pattern p = Pattern.compile("([a-z]+) OR ([a-z]+) -> ([a-z]+)");
					Matcher m = p.matcher(line);
					m.find();
					o.operation = 5;
					o.input1 = m.group(1);
					o.input2 = m.group(2);
					o.output = m.group(3);
				} else if (line.indexOf("->") != -1)
				{
					Pattern p = Pattern.compile("([a-z0-9]+) -> ([a-z]+)");
					Matcher m = p.matcher(line);
					m.find();
					o.operation = 6;
					o.input1 = m.group(1);
					o.output = m.group(2);
				} else
				{
					System.out.println("Unknown line: " + line);
				}
				operations.put(o.output, o);
			} catch (Exception e)
			{
				System.out.println("Exception on line: " + line);
			}
		}
		br.close();
		System.out.println("Value of a: " + (int) findValue(operations, "a"));
	}

	public void day8() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day8.input"));
		String line = null;
		int literaltotal = 0;
		int memorytotal = 0;
		int encodedtotal = 0;
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			literaltotal += line.length();
			int count = 0;
			int count2 = 0;
			for (int i = 0; i < line.length(); i++)
			{
				if (line.charAt(i) == '\\')
				{
					if (line.charAt(i + 1) == 'x')
					{
						i += 3;
					} else
					{
						i++;
					}
				}
				count++;
			}
			for (int i = 0; i < line.length(); i++)
			{
				if (line.charAt(i) == '\\')
				{
					count2++;
				} else if (line.charAt(i) == '"')
				{
					count2++;
				}
				count2++;
			}
			count -= 2; // for start/end quotes;
			count2 += 2;
			memorytotal += count;
			encodedtotal += count2;
			// System.out.println(line + " " + line.length() + " " + count);
		}
		br.close();
		System.out.println("Difference is: " + (literaltotal - memorytotal) + " and part 2: "
				+ (encodedtotal - literaltotal));
	}

	private int findShortest(Set<String> places, Map<String, Integer> distances, String from,
			int distance, int shortest, boolean findLongest)
	{
		if (places.size() == 0)
		{
			if (findLongest)
			{
				if (shortest < distance)
				{
					return distance;
				} else
				{
					return shortest;
				}
			} else
			{
				if (shortest == -1 || distance < shortest)
				{
					return distance;
				} else
				{
					return shortest;
				}
			}
		}
		for (String place : places)
		{
			int newdistance = distance;
			if (from != null)
			{
				newdistance = distance + distances.get(from + "-" + place);
			}
			Set<String> remainingPlaces = new HashSet<String>(places);
			remainingPlaces.remove(place);
			shortest = findShortest(remainingPlaces, distances, place, newdistance, shortest,
					findLongest);
		}
		return shortest;
	}

	public void day9() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day9.input"));
		String line = null;
		Map<String, Integer> distances = new HashMap<String, Integer>();
		Set<String> places = new HashSet<String>();
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			Pattern p = Pattern.compile("([A-Za-z]+) to ([A-Za-z]+) = (\\d+)");
			Matcher m = p.matcher(line);
			m.find();
			String place1 = m.group(1);
			String place2 = m.group(2);
			int distance = Integer.parseInt(m.group(3));
			places.add(place1);
			places.add(place2);
			distances.put(place1 + "-" + place2, distance);
			distances.put(place2 + "-" + place1, distance);
		}
		br.close();
		System.out.println("Shortest is: " + findShortest(places, distances, null, 0, -1, false)
				+ " longest is: " + findShortest(places, distances, null, 0, -1, true));
	}

	public void day10()
	{
		String input = "1321131112";
		for (int i = 0; i < 50; i++)
		{ // 40 for part 1. 50 for part 2
			StringBuffer next = new StringBuffer();
			int count = 0;
			char lastdigit = 'z';
			for (int j = 0; j < input.length(); j++)
			{
				char thisdigit = input.charAt(j);
				if (thisdigit == lastdigit)
				{
					count++;
				} else
				{
					if (lastdigit != 'z')
					{
						next.append(Integer.toString(count));
						next.append(lastdigit);
					}
					lastdigit = thisdigit;
					count = 1;
				}
			}
			next.append(Integer.toString(count));
			next.append(lastdigit);
			input = next.toString();
			// System.out.println("next is: " + input);
		}
		System.out.println("Size is: " + input.length());
	}

	private char[] incrementChar(char[] list, int position)
	{
		char c = list[position];
		if (c == 'z')
		{
			list = incrementChar(list, position - 1);
			c = 'a';
		} else
		{
			c = (char) ((int) c + 1);
		}
		if (c == 'i' || c == 'o' || c == 'l')
		{
			c = (char) ((int) c + 1);
		}
		list[position] = c;
		return list;
	}

	public void day11()
	{
		String input = "hepxxyzz";
		int length = input.length();
		char[] list = input.toCharArray();
		boolean valid = false;
		while (!valid)
		{
			list = incrementChar(list, length - 1);
			// System.out.println("password is now: " + new String(list));
			boolean hasstraight = false;
			int pairs = 0;
			int pos = 0;
			boolean justhadpair = false;
			while (pos < length - 1 && (!hasstraight || pairs < 2))
			{
				if (!justhadpair && list[pos] == list[pos + 1])
				{
					pairs++;
					justhadpair = true;
				} else
				{
					justhadpair = false;
				}
				if (pos < length - 2 && list[pos] + 1 == list[pos + 1]
						&& list[pos + 1] + 1 == list[pos + 2])
				{
					hasstraight = true;
				}
				pos++;
			}
			// System.out.println("pwd: " + new String(list) + " pairs: " +
			// pairs + " str: " + hasstraight);
			if (hasstraight && pairs >= 2)
			{
				valid = true;
			}
		}
		System.out.println("Next password is: " + new String(list));
	}

	private int jsonCounter(JsonElement element)
	{
		int total = 0;
		if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
		{
			total += element.getAsJsonPrimitive().getAsInt();
		} else if (element.isJsonArray())
		{
			for (JsonElement e : element.getAsJsonArray())
			{
				total += jsonCounter(e);
			}
		} else if (element.isJsonObject())
		{
			JsonObject o = element.getAsJsonObject();
			boolean hasRed = false;
			for (Map.Entry<String, JsonElement> e : o.entrySet())
			{
				if (e.getValue().isJsonPrimitive()
						&& e.getValue().getAsJsonPrimitive().getAsString().equals("red"))
				{
					hasRed = true; // comment out for part 1
				} else
				{
					total += jsonCounter(e.getValue());
				}
			}
			if (hasRed)
			{
				total = 0;
			}
		}
		return total;
	}

	public void day12() throws Exception
	{
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(new FileReader("src/main/resources/day12.input"));
		int total = jsonCounter(element);

		System.out.println("Total is: " + total);
	}

	private int findBest(Set<String> names, Map<String, Integer> happy, String firstName,
			String prevName)
	{
		if (names.size() == 1)
		{
			String name = names.iterator().next();
			return happy.get(name + "-" + prevName) + happy.get(name + "-" + firstName)
					+ happy.get(prevName + "-" + name) + happy.get(firstName + "-" + name);
		}
		int best = -9999999;
		for (String name : names)
		{
			String nextFirst;
			if (firstName == null)
			{
				nextFirst = name;
			} else
			{
				nextFirst = firstName;
			}
			int h = 0;
			if (prevName != null)
			{
				h = happy.get(name + "-" + prevName) + happy.get(prevName + "-" + name);
			}
			Set<String> remaining = new HashSet<String>(names);
			remaining.remove(name);
			h += findBest(remaining, happy, nextFirst, name);
			if (h > best)
			{
				best = h;
			}
		}
		return best;
	}

	public void day13() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day13.input"));
		String line = null;
		Map<String, Integer> happy = new HashMap<String, Integer>();
		Set<String> names = new HashSet<String>();
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			Pattern p = Pattern
					.compile("([A-Za-z]+) would (gain|lose) (\\d+) happiness units by sitting next to ([A-Za-z]+).");
			Matcher m = p.matcher(line);
			m.find();
			String name1 = m.group(1);
			int sign = m.group(2).equals("gain") ? 1 : -1;
			int amount = Integer.parseInt(m.group(3)) * sign;
			String name2 = m.group(4);
			names.add(name1);
			names.add(name2);
			happy.put(name1 + "-" + name2, amount);
		}
		br.close();

		System.out.println("best seating is: " + findBest(names, happy, null, null));
		for (String name : names)
		{
			happy.put("Me-" + name, 0);
			happy.put(name + "-Me", 0);
		}
		names.add("Me");
		System.out.println("best seating2 is: " + findBest(names, happy, null, null));
	}

	class Reindeer
	{
		String name;
		int speed;
		int flytime;
		int resttime;
		int points = 0;
		int currentdistance = 0;

		public int distanceAt(int time)
		{
			int fullcycles = time / (flytime + resttime);
			int remainder = time % (flytime + resttime);
			if (remainder > flytime)
			{
				remainder = flytime;
			}
			currentdistance = (flytime * fullcycles + remainder) * speed;
			return currentdistance;
		}
	}

	public void day14() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day14.input"));
		String line = null;
		int furthest = 0;
		int totaltime = 2503;
		List<Reindeer> reindeer = new ArrayList<Reindeer>();
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			Pattern p = Pattern
					.compile("([A-Za-z]+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");
			Matcher m = p.matcher(line);
			m.find();
			Reindeer r = new Reindeer();
			r.name = m.group(1);
			r.speed = Integer.parseInt(m.group(2));
			r.flytime = Integer.parseInt(m.group(3));
			r.resttime = Integer.parseInt(m.group(4));
			reindeer.add(r);
			if (r.distanceAt(totaltime) > furthest)
			{
				furthest = r.distanceAt(totaltime);
			}
		}
		br.close();
		for (int time = 1; time <= totaltime; time++)
		{
			int leaddistance = 0;
			for (Reindeer r : reindeer)
			{
				int dist = r.distanceAt(time);
				if (dist > leaddistance)
				{
					leaddistance = dist;
				}
			}
			for (Reindeer r : reindeer)
			{
				if (r.currentdistance == leaddistance)
				{
					r.points++;
				}
			}
		}
		int maxpoints = 0;
		for (Reindeer r : reindeer)
		{
			if (r.points > maxpoints)
			{
				maxpoints = r.points;
			}
		}
		System.out.println("Furthest is: " + furthest + " max points is: " + maxpoints);
	}

	class Ingredient
	{
		String name;
		int capacity;
		int durability;
		int flavour;
		int texture;
		int calories;
	}

	int findBest(List<Ingredient> ingredients, int index, int amountleft, List<Integer> amounts)
	{
		int best = 0;
		if (index < ingredients.size() - 1)
		{
			for (int i = 0; i <= amountleft; i++)
			{
				amounts.add(index, i);
				int innerbest = findBest(ingredients, index + 1, amountleft - i, amounts);
				if (innerbest > best)
				{
					best = innerbest;
				}
			}
			return best;
		} else
		{
			amounts.add(index, amountleft);
			int cap = 0;
			int dur = 0;
			int fla = 0;
			int tex = 0;
			int cal = 0;
			for (int i = 0; i < ingredients.size(); i++) 
			{
				cap += ingredients.get(i).capacity * amounts.get(i);
				dur += ingredients.get(i).durability * amounts.get(i);
				fla += ingredients.get(i).flavour * amounts.get(i);
				tex += ingredients.get(i).texture * amounts.get(i);
				cal += ingredients.get(i).calories * amounts.get(i);
			}
			if (cap <= 0 || dur <= 0 || fla <= 0 || tex <= 0 || cal != 500)
			{
				return 0;
			} else
			{
				return cap * dur * fla * tex;
			}
		}
	}

	public void day15() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day15.input"));
		String line = null;
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			Pattern p = Pattern
					.compile("([A-Za-z]+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");
			Matcher m = p.matcher(line);
			m.find();
			Ingredient ing = new Ingredient();
			ing.name = m.group(1);
			ing.capacity = Integer.parseInt(m.group(2));
			ing.durability = Integer.parseInt(m.group(3));
			ing.flavour = Integer.parseInt(m.group(4));
			ing.texture = Integer.parseInt(m.group(5));
			ing.calories = Integer.parseInt(m.group(6));
			ingredients.add(ing);
		}
		br.close();
		System.out.println("bestScore: "
				+ findBest(ingredients, 0, 100, new ArrayList<Integer>(ingredients.size())));
	}
	
	public class Sue {
		int num;
		int children = -1;
		int cats = -1;
		int samoyeds = -1;
		int pomeranians = -1;
		int akitas = -1;
		int vizslas = -1;
		int goldfish = -1;
		int trees = -1;
		int cars = -1;
		int perfumes = -1;
		
		boolean matches(Sue s)
		{
			if (children != -1 && s.children != -1 && s.children != children) return false;
			if (cats != -1 && s.cats != -1 && s.cats != cats) return false;
			if (samoyeds != -1 && s.samoyeds != -1 && s.samoyeds != samoyeds) return false;
			if (akitas != -1 && s.akitas != -1 && s.akitas != akitas) return false;
			if (vizslas != -1 && s.vizslas != -1 && s.vizslas != vizslas) return false;
			if (goldfish != -1 && s.goldfish != -1 && s.goldfish != goldfish) return false;
			if (trees != -1 && s.trees != -1 && s.trees != trees) return false;
			if (cars != -1 && s.cars != -1 && s.cars != cars) return false;
			if (perfumes != -1 && s.perfumes != -1 && s.perfumes != perfumes) return false;
			if (pomeranians != -1 && s.pomeranians != -1 && s.pomeranians != pomeranians) return false;
			return true;
		}
		boolean matches2(Sue s)
		{
			if (children != -1 && s.children != -1 && s.children != children) return false;
			if (cats != -1 && s.cats != -1 && s.cats <= cats) return false;
			if (samoyeds != -1 && s.samoyeds != -1 && s.samoyeds != samoyeds) return false;
			if (akitas != -1 && s.akitas != -1 && s.akitas != akitas) return false;
			if (vizslas != -1 && s.vizslas != -1 && s.vizslas != vizslas) return false;
			if (goldfish != -1 && s.goldfish != -1 && s.goldfish >= goldfish) return false;
			if (trees != -1 && s.trees != -1 && s.trees <= trees) return false;
			if (cars != -1 && s.cars != -1 && s.cars != cars) return false;
			if (perfumes != -1 && s.perfumes != -1 && s.perfumes != perfumes) return false;
			if (pomeranians != -1 && s.pomeranians != -1 && s.pomeranians >= pomeranians) return false;
			return true;
		}
	}
	
	public void day16() throws Exception
	{
		Sue target = new Sue();
		target.children = 3;
		target.cats = 7;
		target.samoyeds = 2;
		target.pomeranians = 3;
		target.akitas = 0;
		target.vizslas = 0;
		target.goldfish = 5;
		target.trees = 3;
		target.cars = 2;
		target.perfumes = 1;
		
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day16.input"));
		String line = null;
		List<Sue> sues = new ArrayList<Sue>();
		while ((line = br.readLine()) != null)
		{
			line = line.trim();
			Pattern p = Pattern
					.compile("Sue (\\d+):( ([a-z]+): (\\d+),?)?( ([a-z]+): (\\d+),?)?( ([a-z]+): (\\d+),?)?( ([a-z]+): (\\d+),?)?");
			Matcher m = p.matcher(line);
			m.find();
			Sue s = new Sue();
			s.num = Integer.parseInt(m.group(1));
			int counter = 2;
			while (m.group(counter) != null)
			{
				String thing = m.group(counter + 1);
				int amount = Integer.parseInt(m.group(counter + 2));
				counter += 3;
				if (thing.equals("children")) s.children = amount;
				if (thing.equals("cats")) s.cats = amount;
				if (thing.equals("samoyeds")) s.samoyeds = amount;
				if (thing.equals("pomeranians")) s.pomeranians = amount;
				if (thing.equals("akitas")) s.akitas = amount;
				if (thing.equals("vizslas")) s.vizslas = amount;
				if (thing.equals("goldfish")) s.goldfish = amount;
				if (thing.equals("trees")) s.trees = amount;
				if (thing.equals("cars")) s.cars = amount;
				if (thing.equals("perfumes")) s.perfumes = amount;
			}
			sues.add(s);
			if (target.matches(s))
			{
				System.out.println("Got match: " + s.num);
			}
			if (target.matches2(s))
			{
				System.out.println("Got match2: " + s.num);
			}
		}
		br.close();
		System.out.println("Sues: " + sues.size());
	}
	
	int minContainers = 99999;
	int minCount = 0;
	private int findContainers(int amount, List<Integer> containers, int count)
	{
		// System.out.println("Amount is: " + amount + " size is: " + containers.size());
		if (amount == 0) {
			if (count < minContainers)
			{
				minContainers = count;
				minCount = 1;
			} else if (count == minContainers)
			{
				minCount++;
			}
			return 1;
		}
		if (amount < 0)
		{
			return 0;
		}
		if (containers.size() == 0)
		{
			return 0;
		}
		int total = 0;
		for (int i = 0; i < containers.size(); i++)
		{
			total += findContainers(amount - containers.get(i), containers.subList(i + 1, containers.size()), count + 1);
		}
		return total;
	}
	
	public void day17() throws Exception
	{
		List<Integer> containers = Arrays.asList(new Integer[]{11,30,47,31,32,36,3,1,5,3,32,36,15,11,46,26,28,1,19,3});
		int total = findContainers(150, containers, 0);
		System.out.println("Total is: " + total + " mincount is: " + minCount);
	}
	
	int countNeighbours(boolean[][]grid, int x, int y)
	{
		int count = 0;
		for (int i = x - 1; i <= x + 1; i++)
		{
			for (int j = y - 1; j <= y + 1; j++)
			{
				if (i >= 0 && i < 100 && j >= 0 && j < 100 && (i != x || j != y))
				{
					if (grid[i][j]) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	private void doStep(boolean[][] source, boolean[][] dest)
	{
		for (int x = 0; x < source.length; x++)
		{
			for (int y = 0; y < source[x].length; y++)
			{
				int neighbours = countNeighbours(source, x, y);
				if (neighbours == 3) 
				{
					dest[x][y] = true;
				} else if (neighbours == 2 && source[x][y]) 
				{
					dest[x][y] = true;
				} else
				{
					dest[x][y] = false;
				}
			}
		}
	}
	
	private void cornerOn(boolean[][]grid)
	{
		grid[0][0] = true;
		grid[99][0] = true;
		grid[99][99] = true;
		grid[0][99] = true;
	}
	
	public void day18() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day18.input"));
		String line = null;
		boolean[][] state = new boolean[100][100];
		boolean[][] state2 = new boolean[100][100];
		int y = 0;
		while ((line = br.readLine()) != null)
		{
			for (int x = 0; x < 100; x++)
			{
				if (line.charAt(x) == '#') {
					state[y][x] = true;
				} else {
					state[y][x] = false;
				}
			}
			y++;
		}
		br.close();
		cornerOn(state);
		for (int steps = 0; steps < 100; steps += 2)
		{
			doStep(state, state2);
			cornerOn(state2);
			doStep(state2, state);
			cornerOn(state);
		}
		int count = 0;
		for (y = 0; y < 100; y++)
		{
			for (int x = 0; x < 100; x++)
			{
				if (state[y][x]) {
					count++;
					System.out.print('#');
				} else
				{
					System.out.print('.');
				}
			}
			System.out.print('\n');
		}
		System.out.println("Count is: " + count);
	}
	
	public void day19() throws Exception
	{
		String molecule = "CRnCaCaCaSiRnBPTiMgArSiRnSiRnMgArSiRnCaFArTiTiBSiThFYCaFArCaCaSiThCaPBSiThSiThCaCaPTiRnPBSiThRnFArArCaCaSiThCaSiThSiRnMgArCaPTiBPRnFArSiThCaSiRnFArBCaSiRnCaPRnFArPMgYCaFArCaPTiTiTiBPBSiThCaPTiBPBSiRnFArBPBSiRnCaFArBPRnSiRnFArRnSiRnBFArCaFArCaCaCaSiThSiThCaCaPBPTiTiRnFArCaPTiBSiAlArPBCaCaCaCaCaSiRnMgArCaSiThFArThCaSiThCaSiRnCaFYCaSiRnFYFArFArCaSiRnFYFArCaSiRnBPMgArSiThPRnFArCaSiRnFArTiRnSiRnFYFArCaSiRnBFArCaSiRnTiMgArSiThCaSiThCaFArPRnFArSiRnFArTiTiTiTiBCaCaSiRnCaCaFYFArSiThCaPTiBPTiBCaSiThSiRnMgArCaF";
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day19.input"));
		String line = null;
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		Set<String> molecules = new HashSet<String>();
		while ((line = br.readLine()) != null)
		{
			Pattern p = Pattern.compile("([A-Za-z]+) => ([A-Za-z]+)");
			Matcher m = p.matcher(line);
			m.find();
			String key = m.group(1);
			String value = m.group(2);
			keys.add(key);
			values.add(value);
			for (int i = 0; i < molecule.length(); i++)
			{
				if (molecule.substring(i).startsWith(key))
				{
					String before = "";
					String after = "";
					if (i > 0)
					{
						before = molecule.substring(0,  i);
					}
					if (i + key.length() < molecule.length())
					{
						after = molecule.substring(i + key.length());
					}
					molecules.add(before + value + after);
				}
			}
		}
		br.close();
		System.out.println("Molecules is: " + molecules.size());
	}
}
