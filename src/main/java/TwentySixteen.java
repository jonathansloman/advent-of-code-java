import java.util.HashSet;
import java.util.Set;

public class TwentySixteen {
	public TwentySixteen()
	{
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
