import java.nio.file.*;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class RPSLSp {
    static String[] choices = new String[0];
    static String[] lines;
    static int[][] outcomes;
    static String[][] verbs;
    static int[][] record;
    static Scanner input = new Scanner(System.in);
    static Random r = new Random();
    public static void main(String[] args) {
        String filePath = "./battles5.txt";
        Path file = Paths.get(filePath);
        try {
            InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            ArrayList<String> linesA = new ArrayList<String>();
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                linesA.add(line);
            }
            lines = linesA.toArray(new String[0]);
            choices = separateLine(lines[1]);
            int[][] finalArr = new int[choices.length][choices.length];
            for (int i = 0; i < finalArr.length; i++) {
                Arrays.fill(finalArr[i], -1);
            }
            verbs = new String[choices.length][choices.length];
            outcomes = findOutcomes(lines[2], 2, finalArr, verbs);
            record = new int[2][choices.length];
        }
        catch (IOException x) {
            System.err.println(x);
        }

        //GAME:
        //TODO: INTRO
        System.out.println("Welcome to " + Arrays.toString(choices).substring(1,Arrays.toString(choices).length()-1) + "!");
        while (true) {
            System.out.println();
            for (int i = 0; i < choices.length; i++) {
                System.out.println((i + 1) + ". " + choices[i]);
            }
            System.out.print("Choose your weapon (1-" + choices.length + "): ");
            int weapon = input.nextInt();
            System.out.println(getWinner(weapon, r.nextInt(3) + 1));
            System.out.print("Battle again (yes/no)? ");
            String choice = input.next();
            if (choice.equals("n")) {
                System.out.printf("         %8s %8s %8s %8s %8s\n", "rock", "paper", "scissors", "lizard", "spock");
                System.out.println("         -------- -------- -------- -------- --------");
                System.out.printf("%8s %8d %8d %8d %8d %8d\n", "Computer", 0,0,0,0,0);
                System.out.printf("%8s %8d %8d %8d %8d %8d\n", "User", 0,0,0,0,0);
                System.out.println("The computer won ___ times,");
                System.out.println("the user won ___ times,");
                System.out.println("and they tied ___ times.");
                break;
            }
        }

    }
    public static String getWinner(int user, int computer) {
        //player
        record[0][user-1]++;
        //computer
        record[1][computer-1]++;
        switch (outcomes[user - 1][computer - 1]) {
            case 0:
                return "User (" + choices[user - 1] + ") " + verbs[user - 1][computer - 1] + " Computer (" + choices[computer - 1] + ")";
            case 1:
                return "Computer (" + choices[computer - 1] + ") " + verbs[user - 1][computer - 1] + " User (" + choices[user - 1] + ")";
            case 2:
                return "User (" + choices[user - 1] + ") ties Computer (" + choices[computer - 1] + ")";
            default:
                return "error";
        }
    }
    public static int[][] findOutcomes(String val, int index, int[][] finalArr, String[][] verbsArr) {
        String[] s = separateLine(val);
        verbsArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = s[s.length-1];
        //first wins
        if (Objects.equals(s[2], s[0])) {
            finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 0;
        }
        //second wins
        else if (Objects.equals(s[2], s[1])) {
            finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 1;
        }
        //tie
        else {
            finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 2;
        }
        if (index+1 < lines.length) {
            return findOutcomes(lines[index + 1], index + 1, finalArr, verbsArr);
        }
        else {
            verbs = verbsArr;
            return finalArr;
        }
    }
    public static String[] separateLine(String val) {
        ArrayList<String> finalArr = new ArrayList<String>();
        String c = "";
        for (int i = 0; i < val.length(); i++) {
            if (val.charAt(i) == ' ' || val.charAt(i) == ':') {
                finalArr.add(c);
                c = "";
            }
            else {
                c += val.charAt(i);
            }
        }
        if (!c.isEmpty()) finalArr.add(c);
        return finalArr.toArray(new String[0]);
    }
    public static int getIndex(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (Objects.equals(arr[i], val)) return i;
        }
        return -1;
    }
}