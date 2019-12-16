import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Twenty19 {
    public Twenty19() {
    }
    
    public void run() throws Exception {
        day02();
    }
    
    void day01() throws IOException {
        
        List<Integer> numbers = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/2019-01.input"));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            numbers.add(Integer.parseInt(line));
        }
        br.close();
        int sum = 0;
        for (int n : numbers) {
            int fuel =  (n / 3) - 2;
            sum += fuel;
            int extra = fuel;
            do {
                extra = (extra / 3) - 2;
                if (extra > 0) {
                    sum += extra;
                }
            } while (extra > 0);
        }
        System.out.println("Sum is: " + sum);
    }
    
    private int intcode(List<Integer> numbers) throws Exception {
        int position = 0;
        while (numbers.get(position) != 99) {
           // System.out.println(Arrays.toString(numbers.toArray()));
            switch(numbers.get(position)) {
                case 1:
                    numbers.set(numbers.get(position + 3), numbers.get(numbers.get(position + 1)) + numbers.get(numbers.get(position + 2)));
                    position += 4;
                    break;
                case 2:
                    numbers.set(numbers.get(position + 3), numbers.get(numbers.get(position + 1)) * numbers.get(numbers.get(position + 2)));
                    position += 4;
                    break;
                default:
                    throw new Exception("Invalid opcode: " + numbers.get(position));
            }
        }
        return numbers.get(0);
    }
    
    private int nounverb(List<Integer> numbers, int noun, int verb) throws Exception {
       List<Integer> copy = new ArrayList<>(numbers);
       copy.set(1, noun);
       copy.set(2, verb);
       return intcode(copy);
    }
    
    void day02() throws Exception {
        List<Integer> numbers = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/2019-02.input"));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            numbers.addAll(Stream.of(line.split("\\D+")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }
        br.close();
  
        System.out.println("result is " + nounverb(numbers, 12, 2));
        
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                if (nounverb(numbers, noun, verb) == 19690720) {
                    System.out.println("Result 2 is " + (noun * 100 + verb));
                    return;
                }
            }
        }
    }
}
