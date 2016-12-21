import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwentySixteen {
	public TwentySixteen() {
	}

	public void run() throws Exception {
		day14();
	}

	List<Character> findQuintuples(String hash) {
		List<Character> result = new ArrayList<Character>();
		int repeats = 0;
		for (int i = 1; i < hash.length(); i++) {
			if (hash.charAt(i - 1) == hash.charAt(i)) {
				repeats++;
				if (repeats == 4) {
					if (!result.contains(hash.charAt(i))) {
						result.add(hash.charAt(i));
					}
				}
			} else {
				repeats = 0;
			}
		}
		return result;
	}

	List<Character> findTriples(String hash) {
		List<Character> result = new ArrayList<Character>();
		int repeats = 0;
		for (int i = 1; i < hash.length(); i++) {
			if (hash.charAt(i - 1) == hash.charAt(i)) {
				repeats++;
				if (repeats == 2) {
					if (!result.contains(hash.charAt(i))) {
						result.add(hash.charAt(i));
						return result;
					}
				}
			} else {
				repeats = 0;
			}
		}
		return result;
	}

	void day14() throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		String input = "ahsbgdzn";
		int countKeys = 0;
		int index = 0;
		Map<Integer, List<Character>> triples = new HashMap<Integer, List<Character>>();
		
		SortedSet<Integer> keys = new TreeSet<Integer>();

		// we go beyond 64 to make sure we get the 64 first as we don't find them in strict order.
		while (countKeys < 80) {
			md.reset();
			String key = input + index;
			for (int i = 0; i < 2017; i++) {
				byte[] md5 = md.digest(key.getBytes("UTF-8"));
				BigInteger bi = new BigInteger(1, md5);
				key = String.format("%0" + (md5.length << 1) + "x", bi);
				if (index == 0)
				{
					System.out.println("hash iteration: " + i + " is " + key);
				}
			}

			List<Character> trips = findTriples(key);
			if (!trips.isEmpty()) {
				triples.put(index, trips);
			}
			List<Character> quints = findQuintuples(key);
			for (char c : quints) {

				for (int test = index - 1000; test < index; test++) {
					if (test >= 0) {
						List<Character> tripTest = triples.get(test);
						if (tripTest != null) {
							for (char t : tripTest) {
								if (t == c) {
									countKeys++;
									keys.add(test);
									System.out.println(
											"Found key at index: " + test + " count is: " + countKeys + " index is: " + index + " hash is " + key);
								}
							}
						}
					}
				}
			}
			index++;
		}
		int z = 0;
		for (int t : keys)
		{
			z++;
			System.out.println(z + " key is: " + t);

			if (z == 64)
			{
				break;
			}
		}
	}

	int[][] day13grid = new int[100][100];
	// part 1
	// int day13MinLength = 2000;
	// part 2
	int day13MinLength = 51;

	boolean canMove(int x, int y, int input) {
		if (day13grid[x][y] == 0) {
			int value = x * x + 3 * x + 2 * x * y + y + y * y + input;
			int countBits = 0;
			for (int i = 0; i < 32; i++) {
				if ((value & (1 << i)) != 0) {
					countBits++;
				}
			}
			if (countBits % 2 == 0) {
				day13grid[x][y] = 1; // space
			} else {
				day13grid[x][y] = 2; // wall
			}
		}
		return day13grid[x][y] == 1;
	}

	private void tryMove(int x, int y, int length, int input) {
		if (x < 0 || y < 0) {
			return;
		}
		if (length >= day13MinLength) {
			return;
		}
		if (x == 31 && y == 39) {
			if (length < day13MinLength) {
				System.out.println("Done, length is: " + length);
				day13MinLength = length;
			}
			return;
		}
		if (!canMove(x, y, input)) {
			return;
		}
		day13grid[x][y] = 3;
		tryMove(x - 1, y, length + 1, input);
		tryMove(x + 1, y, length + 1, input);
		tryMove(x, y - 1, length + 1, input);
		tryMove(x, y + 1, length + 1, input);
		day13grid[x][y] = 1;
	}

	public void day13() {
		int input = 1362;
		int x = 1;
		int y = 1;
		day13grid[x][y] = 1;
		tryMove(x, y, 0, input);

		int count = 0;
		for (int i = 0; i < day13grid.length; i++) {
			for (int j = 0; j < day13grid[0].length; j++) {
				if (day13grid[i][j] == 1) {
					count++;
				}
			}
		}
		System.out.println("Count is: " + count);
	}

	public void day12() throws IOException {
		int[] registers = new int[4];
		// part 2
		registers[2] = 1;
		List<String> instructions = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day12.input"));
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			instructions.add(line);
		}
		br.close();
		int place = 0;
		while (place < instructions.size()) {
			String instr = instructions.get(place);
			if (instr.startsWith("cpy")) {
				Pattern p = Pattern.compile("cpy ([a-d]|\\d+) ([a-d])");
				Matcher m = p.matcher(instr);
				m.find();
				String valueStr = m.group(1);
				String destination = m.group(2);
				int reg = valueStr.charAt(0) - 'a';
				int value;
				if (reg < 0 || reg > 3) {
					value = Integer.parseInt(valueStr);
				} else {
					value = registers[reg];
				}
				int dest = destination.charAt(0) - 'a';
				registers[dest] = value;
				place++;

			} else if (instr.startsWith("jnz")) {
				int reg = instr.charAt(4) - 'a';
				boolean isZero = false;
				if (reg >= 0 && reg <= 3) {
					if (registers[reg] == 0) {
						isZero = true;
					}
				} else if (instr.charAt(4) == '0') {
					isZero = true;
				}
				if (!isZero) {
					int jump = Integer.parseInt(instr.substring(6));
					place += jump;
				} else {
					place++;
				}
			} else if (instr.startsWith("inc")) {
				int reg = instr.charAt(4) - 'a';
				registers[reg]++;
				place++;
			} else if (instr.startsWith("dec")) {
				int reg = instr.charAt(4) - 'a';
				registers[reg]--;
				place++;
			}
		}
		System.out.println("a is: " + registers[0]);
	}

	Set<String> day11States = new HashSet<String>();
	// part 1
	/*
	 * String[] day11names = { "Pm", "Co", "Cm", "Ru", "Pu" }; int day11Num=5;
	 * boolean floor_generators[][] = { { true, false, false, false, false }, {
	 * false, true, true, true, true }, { false, false, false, false, false }, {
	 * false, false, false, false, false } }; boolean floor_chips[][] = { {
	 * true, false, false, false, false }, { false, false, false, false, false
	 * }, { false, true, true, true, true }, { false, false, false, false, false
	 * } };
	 */
	// part2
	String[] day11names = { "Pm", "Co", "Cm", "Ru", "Pu", "El", "Di" };
	int day11Num = 7;
	boolean floor_generators[][] = { { true, false, false, false, false, true, true },
			{ false, true, true, true, true, false, false }, { false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false } };
	boolean floor_chips[][] = { { true, false, false, false, false, true, true },
			{ false, false, false, false, false, false, false }, { false, true, true, true, true, false, false },
			{ false, false, false, false, false, false, false } };

	int[][] day11Combinations = generateCombinations(day11Num);

	int fact(int num) {
		int factorial = 1;
		for (int i = 2; i <= num; i++) {
			factorial = factorial * i;
		}
		return factorial;
	}

	int[][] generateCombinations(int num) {
		int[][] result = new int[fact(num)][num];

		return result;
	}

	// boolean floor_generators[][] = {
	// {false, false},
	// {true, false},
	// {false, true},
	// {false, false}};
	// boolean floor_chips[][] = {
	// {true, true},
	// {false, false},
	// {false, false},
	// {false, false}};
	private class Day11State {
		boolean[][] generators;
		boolean[][] chips;
		int elevator;
		int count;

		Day11State(int elevator, int count, boolean[][] generators, boolean[][] chips) {
			this.generators = generators;
			this.chips = chips;
			this.elevator = elevator;
			this.count = count;
		}

		Day11State(Day11State copy, int direction) {
			this.count = copy.count + 1;
			this.elevator = copy.elevator + direction;
			this.chips = copy_from(copy.chips);
			this.generators = copy_from(copy.generators);
		}

		boolean checkAll(Set<String> visited) {
			if (!checkValid()) {
				// System.out.println("State is invalid\n" + toString());
				return false;
			}
			if (checkDone()) {
				System.out.println("Done in: " + count);
				return false;
			}
			String mystate = toStringForCheck();
			if (visited.contains(mystate)) {
				return false;
			}
			visited.add(mystate);
			return true;
		}

		boolean checkValid() {
			for (int f = 0; f < 4; f++) {
				boolean hasGenerators = false;
				for (int g = 0; g < day11Num; g++) {
					if (generators[f][g]) {
						hasGenerators = true;
					}
				}
				if (hasGenerators) {
					for (int c = 0; c < day11Num; c++) {
						if (chips[f][c] && !generators[f][c]) {
							return false;
						}
					}
				}
			}
			return true;
		}

		boolean checkDone() {
			for (int i = 0; i < day11Num; i++) {
				if (!chips[3][i] || !generators[3][i]) {
					return false;
				}
			}
			return true;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			for (int i = 3; i >= 0; i--) {
				if (elevator == i) {
					sb.append("E ");
				} else {
					sb.append(". ");
				}
				for (int j = 0; j < day11Num; j++) {
					if (generators[i][j]) {
						sb.append(day11names[j]);
						sb.append("G ");
					} else {
						sb.append("... ");
					}
					if (chips[i][j]) {
						sb.append(day11names[j]);
						sb.append("C ");
					} else {
						sb.append("... ");
					}
				}
				sb.append("\n");
			}
			return sb.toString();
		}

		public String toStringForCheck() {
			int[] perm = new int[day11Num]; // store our permutation
			int permIndex = 0;
			for (int z = 0; z < day11Num; z++) {
				perm[z] = -1;
			}
			StringBuffer sb = new StringBuffer();
			/* first determine our permutation */
			for (int i = 3; i >= 0; i--) {

				for (int j = 0; j < day11Num; j++) {
					if (generators[i][j]) {
						perm[permIndex++] = j;

					}
				}
			}
			for (int i = 3; i >= 0; i--) {
				if (elevator == i) {
					sb.append("E ");
				} else {
					sb.append(". ");
				}
				for (int j = 0; j < day11Num; j++) {
					if (generators[i][perm[j]]) {
						sb.append(j);
					} else {
						sb.append(".");
					}
					if (chips[i][perm[j]]) {
						sb.append(j);
					} else {
						sb.append(".");
					}
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}

	boolean[][] copy_from(boolean[][] arr) {
		boolean[][] ret = new boolean[arr.length][arr[0].length];
		for (int x = 0; x < arr.length; x++) {
			for (int y = 0; y < arr[0].length; y++) {
				ret[x][y] = arr[x][y];
			}
		}
		return ret;
	}

	public void day11() {
		int[] dirs = { 1, -1 };
		// we need to do a breadth first search, not depth first.
		Day11State state = new Day11State(0, 0, copy_from(floor_generators), copy_from(floor_chips));
		day11States.add(state.toStringForCheck());
		Queue<Day11State> q = new ArrayDeque<Day11State>();
		q.add(state);
		int max = 1;
		while (true) {
			Day11State s = q.poll();
			if (s == null) {
				break;
			}
			if (s.count > max) {
				max = s.count;
				System.out.println("Size of queue is: " + q.size() + " size of states is: " + day11States.size()
						+ " count is: " + s.count);
			}

			// System.out.println("Processing state: \n" + s.toString());
			// add all 'next moves' to the queue.
			for (int dir : dirs) {
				if (s.elevator + dir < 0 || s.elevator + dir > 3) {
					continue;
				}
				// try moving generators
				for (int i = 0; i < day11Num; i++) {
					if (s.generators[s.elevator][i]) {
						Day11State next1 = new Day11State(s, dir);
						next1.generators[s.elevator][i] = false;
						next1.generators[s.elevator + dir][i] = true;
						if (next1.checkAll(day11States)) {
							q.add(next1);
						}
						// now try moving with another generator
						for (int j = i + 1; j < day11Num; j++) {
							if (s.generators[s.elevator][j]) {
								Day11State next2 = new Day11State(s, dir);
								next2.generators[s.elevator][i] = false;
								next2.generators[s.elevator + dir][i] = true;
								next2.generators[s.elevator][j] = false;
								next2.generators[s.elevator + dir][j] = true;
								if (next2.checkAll(day11States)) {
									q.add(next2);
								}
							}
						}
						// now try moving with another chip - must be the same
						// chip.

						if (s.chips[s.elevator][i]) {
							Day11State next3 = new Day11State(s, dir);
							next3.generators[s.elevator][i] = false;
							next3.generators[s.elevator + dir][i] = true;
							next3.chips[s.elevator][i] = false;
							next3.chips[s.elevator + dir][i] = true;
							if (next3.checkAll(day11States)) {
								q.add(next3);
							}
						}
					}
				}
				// now try moving chips
				for (int l = 0; l < day11Num; l++) {
					if (s.chips[s.elevator][l]) {
						Day11State next4 = new Day11State(s, dir);
						next4.chips[s.elevator][l] = false;
						next4.chips[s.elevator + dir][l] = true;
						if (next4.checkAll(day11States)) {
							q.add(next4);
						}
						// and try moving with another chip
						for (int m = l + 1; m < day11Num; m++) {
							if (s.chips[s.elevator][m]) {
								Day11State next5 = new Day11State(s, dir);
								next5.chips[s.elevator][l] = false;
								next5.chips[s.elevator + dir][l] = true;
								next5.chips[s.elevator][m] = false;
								next5.chips[s.elevator + dir][m] = true;
								if (next5.checkAll(day11States)) {
									q.add(next5);
								}
							}
						}
					}
				}
			}
		}
	}

	private class Bot {
		int num;
		int[] chips = new int[2];
		int numChips = 0;
		int lowTo = -999999;
		int highTo = -999999;

		Bot(int num) {
			this.num = num;
		}

		boolean check(int[] outputs, Map<Integer, Bot> bots) {
			if (numChips < 2 || lowTo == -999999) {
				return false;
			}
			int high;
			int low;
			if (chips[0] < chips[1]) {
				low = chips[0];
				high = chips[1];
			} else {
				low = chips[1];
				high = chips[0];
			}
			if (high == 61 && low == 17) {
				System.out.println("Found bot: " + num);
				// return true; comment out for part 2 as need to run to
				// completion.
			}
			if (lowTo >= 10000) {
				outputs[lowTo - 10000] = low;
			} else {
				Bot b = bots.get(lowTo);
				if (b == null) {
					b = new Bot(lowTo);
					bots.put(lowTo, b);

				}
				b.chips[b.numChips++] = low;
				if (b.check(outputs, bots)) {
					// return true;
				}
			}
			if (highTo >= 10000) {
				outputs[highTo - 10000] = high;
			} else {
				Bot b = bots.get(highTo);
				if (b == null) {
					b = new Bot(highTo);
					bots.put(highTo, b);

				}
				b.chips[b.numChips++] = high;
				if (b.check(outputs, bots)) {
					// return true;
				}
			}
			numChips = 0; // our chips have gone.
			return false;
		}
	}

	public void day10() throws IOException {
		Map<Integer, Bot> bots = new HashMap<Integer, Bot>();
		int[] outputs = new int[1000];
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day10.input"));
		String line = null;
		boolean done = false;
		while ((line = br.readLine()) != null && !done) {
			line = line.trim();
			if (line.startsWith("bot")) {
				Pattern p = Pattern
						.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int bot = Integer.parseInt(m.group(1));
				String lowDest = m.group(2);
				int low = Integer.parseInt(m.group(3));
				if (lowDest.equals("output")) {
					low = low + 10000;
				}
				String highDest = m.group(4);
				int high = Integer.parseInt(m.group(5));
				if (highDest.equals("output")) {
					high = high + 10000;
				}
				Bot b = bots.get(bot);
				if (b == null) {
					b = new Bot(bot);
					bots.put(bot, b);
				}
				b.lowTo = low;
				b.highTo = high;
				done = b.check(outputs, bots);
			} else if (line.startsWith("value")) {
				Pattern p = Pattern.compile("value (\\d+) goes to bot (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int value = Integer.parseInt(m.group(1));
				int bot = Integer.parseInt(m.group(2));
				Bot b = bots.get(bot);
				if (b == null) {
					b = new Bot(bot);
					bots.put(bot, b);
				}
				b.chips[b.numChips++] = value;
				done = b.check(outputs, bots);
			}
		}
		br.close();
		System.out.println("outputs values 0: " + outputs[0] + " 1: " + outputs[1] + " 2: " + outputs[2]);
	}

	private long decompressLength(String str) {
		long total = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '(') {
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
			} else {
				total++;
			}
		}
		return total;
	}

	public void day9() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day9.input"));
		String line = br.readLine();
		br.close();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '(') {
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
				for (int j = 0; j < reps; j++) {
					sb.append(repString);
				}
				i += length + 1 + marker.length();
			} else {
				sb.append(line.charAt(i));
			}
		}
		// System.out.println("String is: " + sb.toString());
		System.out.println("length is: " + sb.toString().length());
		System.out.println("length2 is: " + decompressLength(line));
	}

	private void printGrid(boolean[][] grid) {
		StringBuffer sb = new StringBuffer();
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 50; x++) {
				if (grid[y][x]) {
					sb.append('#');
				} else {
					sb.append('.');
				}
			}
			sb.append('\n');
		}
		System.out.println(sb.toString());
	}

	void day8() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day8.input"));
		String line = null;
		boolean[][] grid = new boolean[6][50];
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("rect")) {
				Pattern p = Pattern.compile("rect (\\d+)x(\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int rectX = Integer.parseInt(m.group(1));
				int rectY = Integer.parseInt(m.group(2));
				for (int x = 0; x < rectX; x++) {
					for (int y = 0; y < rectY; y++) {
						grid[y][x] = true;
					}
				}
			} else if (line.startsWith("rotate row")) {
				Pattern p = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int row = Integer.parseInt(m.group(1));
				int amount = Integer.parseInt(m.group(2));
				for (int i = 0; i < amount; i++) {
					boolean saved = grid[row][49];
					for (int x = 49; x > 0; x--) {
						grid[row][x] = grid[row][x - 1];
					}
					grid[row][0] = saved;
				}

			} else if (line.startsWith("rotate column")) {
				Pattern p = Pattern.compile("rotate column x=(\\d+) by (\\d+)");
				Matcher m = p.matcher(line);
				m.find();
				int col = Integer.parseInt(m.group(1));
				int amount = Integer.parseInt(m.group(2));
				for (int i = 0; i < amount; i++) {
					boolean saved = grid[5][col];
					for (int y = 5; y > 0; y--) {
						grid[y][col] = grid[y - 1][col];
					}
					grid[0][col] = saved;
				}
			}
			printGrid(grid);
		}
		br.close();
		int count = 0;
		for (int x = 0; x < 50; x++) {
			for (int y = 0; y < 6; y++) {
				if (grid[y][x]) {
					count++;
				}
			}
		}
		System.out.println("Count is " + count);
	}

	void day7() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day7.input"));
		String line = null;
		int count = 0;
		int count2 = 0;
		while ((line = br.readLine()) != null) {
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
			for (int i = 0; i < line.length() - 1; i++) {
				char thisChar = line.charAt(i);
				if (thisChar == '[') {
					inHypernet = true;
					lastChar = 0;
				} else if (thisChar == ']') {
					inHypernet = false;
					lastChar = 0;
				} else {
					if (lastChar != thisChar) {
						if (i < line.length() - 2 && thisChar == line.charAt(i + 1) && lastChar == line.charAt(i + 2)) {
							if (inHypernet) {
								invalid = true;
							} else {
								hasAbba = true;
							}
						}
						if (lastChar == line.charAt(i + 1)) {
							if (inHypernet) {
								babB[babCount] = lastChar;
								babA[babCount] = thisChar;
								babCount++;
							} else {
								abaA[abaCount] = lastChar;
								abaB[abaCount] = thisChar;
								abaCount++;
							}
						}
					}
					lastChar = thisChar;
				}
			}
			if (hasAbba && !invalid) {
				count++;
			}
			// System.out.println("abaA is: " + new String(abaA) + "abaB is: " +
			// new String(abaB));
			// System.out.println("babA is: " + new String(babA) + "babB is: " +
			// new String(babB));

			for (int j = 0; j < babCount; j++) {
				for (int k = 0; k < abaCount; k++) {
					if (babA[j] == abaA[k] && babB[j] == abaB[k]) {
						valid2 = true;
					}
				}
			}
			if (valid2) {
				count2++;
			}
		}
		br.close();
		System.out.println("Valid addresses count: " + count + " and valid2: " + count2);
	}

	void day6() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/16day6.input"));
		String line = null;
		int charCount[][] = new int[8][26];
		while ((line = br.readLine()) != null) {
			line = line.trim();
			for (int i = 0; i < 8; i++) {
				char c = line.charAt(i);
				charCount[i][c - 'a']++;
			}
		}
		br.close();
		String message = "";
		String message2 = "";
		for (int i = 0; i < 8; i++) {
			int max = 0;
			int min = 99999;
			char maxChar = 'X';
			char minChar = 'X';
			for (int k = 0; k < 26; k++) {
				if (charCount[i][k] > max) {
					max = charCount[i][k];
					maxChar = (char) ('a' + k);
				}
				if (charCount[i][k] < min) {
					min = charCount[i][k];
					minChar = (char) ('a' + k);
				}
			}
			message = message + Character.toString(maxChar);
			message2 = message2 + Character.toString(minChar);
		}
		System.out.println("Message is: " + message + " message2 is: " + message2);
	}

	void day5() throws IOException, NoSuchAlgorithmException {
		String input = "ojvtpuvg";
		MessageDigest md = MessageDigest.getInstance("MD5");
		int num = 0;
		int digitsFound = 0;
		int digitsFound2 = 0;
		String password = "";
		char[] password2 = new char[8];
		for (int i = 0; i < 8; i++) {
			password2[i] = '-';
		}
		while (digitsFound < 8 || digitsFound2 < 8) {
			String key = input + num;
			md.reset();
			byte[] md5 = md.digest(key.getBytes("UTF-8"));
			BigInteger bi = new BigInteger(1, md5);
			String hash = String.format("%0" + (md5.length << 1) + "X", bi);
			// System.out.println("Code is: " + key + " md5 is: " + hash);
			if (hash.startsWith("00000")) {
				if (digitsFound < 8) {
					System.out.println("Password char: " + hash.charAt(5));
					password = password + hash.charAt(5);
					digitsFound++;
				}
				if (digitsFound2 < 8) {
					if (Character.isDigit(hash.charAt(5))) {
						int pos = Integer.parseInt(hash.substring(5, 6));
						if (pos < 8 && password2[pos] == '-') {
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

	void day4() throws IOException {
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
			for (int i = 0; i < name.length(); i++) {
				char c = name.charAt(i);
				if (c != '-') {
					counts[c - 'a']++;
					if (counts[c - 'a'] > max) {
						max = counts[c - 'a'];
					}
					decoded = decoded + Character.toString((char) ('a' + ((c - 'a' + code) % 26)));
				} else {
					decoded = decoded + " ";
				}
			}
			String check = "";
			int checkFound = 0;
			while (checkFound < 5) {
				for (int maxcount = max; checkFound < 5; maxcount--) {
					for (int j = 0; j < 26; j++) {
						if (counts[j] == maxcount) {
							check = check + Character.toString((char) ('a' + j));
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

			if (decoded.contains("northpole")) {
				System.out.println("decoded name: " + decoded + " has id: " + code);
			}
		}
		System.out.println("Sum is: " + sum);
		br.close();
	}

	void day3() throws IOException {
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
			if ((x < y + z) && (y < x + z) && (z < x + y)) {
				count++;
			}
			if (triCount == 3) {
				triCount = 0;
				if ((tri1[0] < tri1[1] + tri1[2]) && (tri1[1] < tri1[0] + tri1[2]) && (tri1[2] < tri1[0] + tri1[1])) {
					count2++;
				}
				if ((tri2[0] < tri2[1] + tri2[2]) && (tri2[1] < tri2[0] + tri2[2]) && (tri2[2] < tri2[0] + tri2[1])) {
					count2++;
				}
				if ((tri3[0] < tri3[1] + tri3[2]) && (tri3[1] < tri3[0] + tri3[2]) && (tri3[2] < tri3[0] + tri3[1])) {
					count2++;
				}
			}
		}
		System.out.println("Count is: " + count + " count2 is: " + count2);
		br.close();
	}

	void day2() {
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
		char[][] keypad2 = { { 'x', 'x', 'D', 'x', 'x' }, { 'x', 'A', 'B', 'C', 'x' }, { '5', '6', '7', '8', '9' },
				{ 'x', '2', '3', '4', 'x' }, { 'x', 'x', '1', 'x', 'x' } };
		String[] lines = input.split(",");
		for (String line : lines) {
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				switch (c) {
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

	void day1() {
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
		for (String inp : inputs) {
			inp = inp.trim();
			if (inp.charAt(0) == 'R') {
				dir = (dir + 1) % 4;
			} else {
				dir = (dir + 3) % 4;
			}
			int amount = Integer.parseInt(inp.substring(1));
			for (int j = 0; j < amount; j++) {
				visited.add(x + "-" + y);
				x += dirs[dir][0];
				y += dirs[dir][1];
				if (!foundhq && visited.contains(x + "-" + y)) {
					foundhq = true;
					System.out.println("Found hq at " + x + "," + y + " distance: " + (Math.abs(x) + Math.abs(y)));
				}
			}
		}
		System.out.println("Final coords: " + x + "," + y + " distance: " + (Math.abs(x) + Math.abs(y)));
	}
}
