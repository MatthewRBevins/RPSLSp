import java.nio.file.*;
import java.io.*;
import java.util.*;

//TODO:Finish programming record printing, catch errors in file and user input,
// change recursion to loops, make write file method, create method for statistic printing,
// remove most code from main, make class comment header, reduce line lengths to 100 chars,
// javadoc comments for each method and class constant

public class RPSLSp {
    //Choices is an array of strings storing all possible weapons
    static String[] choices = new String[0];
    //Lines is an array of strings storing all lines in the battles txt file
    static String[] lines;
    //Outcomes is a 2D array of ints storing all possible outcomes. Rows = user's weapon (based on the weapon's index in the choices array), column = computer's weapon, and if the user wins the outcome is stored as 0, if the computer wins it is stored as 1, and if it is a tie it is stored as 2.
    static int[][] outcomes;
    //Verbs is a 2D array of strings storing the verbs used in all possible outcomes. The format is the same as the outcomes array, except the values in the array will be the verb printed depending on the outcome.
    static String[][] verbs;
    //Record is a 2D array of ints storing the number of times each player used each weapon. The first row is the player and the second is the computer. The columns store the number of times each competitor used each weapon.
    static int[][] record;
    static int[] winLossTies = new int[3];
    //TODO: figure out where to store these
    static Random r = new Random();
    static String filePath = "./battles5.txt";
    public static void main(String[] args) {
        r.setSeed(5);
        Scanner input = new Scanner(System.in);
        readFile(filePath);
        initializeArrays();

        System.out.println("Welcome to " + Arrays.toString(choices).substring(1,Arrays.toString(choices).length()-1) + "!\n" +
                "You can choose either one of these options and the computer will choose a random option, \n" +
                "and the program will display and keep track of the winners. Have fun!");
        while (true) {
            System.out.println();
            //Prints all weapon options
            for (int i = 0; i < choices.length; i++) {
                System.out.println((i + 1) + ". " + choices[i]);
            }
            System.out.print("Choose your weapon (1-" + choices.length + "): ");
            String w = input.next();
            if ()
            int weapon = 0;
            if (weapon != 0) System.out.println(playBattle(weapon, r.nextInt(choices.length) + 1));
            System.out.print("Battle again (yes/no)? ");
            String choice = input.next();
            if (choice.toLowerCase().contains("n")) {
                //Print record
                printRecord();
                break;
            }
        }

    }
    static void printRecord() {
        System.out.println();
        System.out.print("         ");
        for (String s : choices) {
            System.out.printf("%8s ", s);
        }
        System.out.println();
        System.out.println("         -------- -------- -------- -------- --------");
        System.out.printf("%8s", "Computer");
        for (int i : record[1]) {
            System.out.printf("%8d ", i);
        }
        System.out.println();
        System.out.printf("%8s", "User");
        for (int i : record[0]) {
            System.out.printf("%8d ", i);
        }
        System.out.println("\n");
        System.out.println("The computer won " + winLossTies[1] + " time" + needsS(winLossTies[1]) + ",");
        System.out.println("the user won " + winLossTies[0] + " time" + needsS(winLossTies[0]) + ",");
        System.out.println("and they tied " + winLossTies[2] + " time" + needsS(winLossTies[2]) + ".");
    }
    static String needsS(int val) {
        if (val == 1) return "";
        return "s";
    }
    static String[] readFile(String path) {
        Path file = Paths.get(filePath);
        try {
            //The following code opens the battles file then goes through each line and inserts it into the lines array.
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
        }
        catch (IOException x) {
            System.err.println(x);
        }
        return lines;
    }
    static void initializeArrays() {
        //Creates the choices array using the separateLine method
        choices = separateLine(lines[1]);
        //Initializes the outcomes array
        outcomes = new int[choices.length][choices.length];
        //Initializes the verbs array
        verbs = new String[choices.length][choices.length];
        //Uses the findOutcomes method to fill the outcomes and verbs arrays.
        outcomes = findOutcomes(lines[2], 2, outcomes, verbs);
        //Initializes the record array.
        record = new int[2][choices.length];
    }
    static String playBattle(int user, int computer) {
        /*
        This function gets the winner of the current round and inserts the results into the record array
        Parameters: user: user's weapon choice (int), computer: computer's weapon choice (int)
        Returns: formatted string telling the user who won the battle
         */
        //Update player record
        record[0][user-1]++;
        //Update computer record
        record[1][computer-1]++;
        //Gets outcome using outcomes array and returns string based on the outcome
        switch (outcomes[user - 1][computer - 1]) {
            //User win
            case 0:
                winLossTies[0]++;
                return "User (" + choices[user - 1] + ") " + verbs[user - 1][computer - 1] + " Computer (" + choices[computer - 1] + ")";
            //Computer win
            case 1:
                winLossTies[1]++;
                return "Computer (" + choices[computer - 1] + ") " + verbs[user - 1][computer - 1] + " User (" + choices[user - 1] + ")";
            //Tie
            case 2:
                winLossTies[2]++;
                return "User (" + choices[user - 1] + ") ties Computer (" + choices[computer - 1] + ")";
            //This shouldn't happen
            default:
                return "error";
        }
    }
    static int[][] findOutcomes(String val, int index, int[][] finalArr, String[][] verbsArr) {
        /*
        This function uses recursion to calculate the outcomes of each scenario and puts the outcomes into a 2D array. It also puts the verbs into a separate 2D array.
        Parameters: val: the current line (string), index: the current line index (int), finalArr: 2D array storing possible outcomes
         */
        for (int i = 0; i < lines.length; i++) {
            String[] s = separateLine(val);
            //Puts the verb of the current scenario into the verbs array. The row is the player's choice and the column is the computer's choice.
            verbsArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = s[s.length-1];
            //If the player wins in the current scenario, set the
            if (Objects.equals(s[2], s[0])) {
                finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 0;
            }
            //If the computer wins in the current scenario
            else if (Objects.equals(s[2], s[1])) {
                finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 1;
            }
            //If the computer and player tie in the current scenario
            else {
                finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 2;
            }
        }
        String[] s = separateLine(val);
        //Puts the verb of the current scenario into the verbs array. The row is the player's choice and the column is the computer's choice.
        verbsArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = s[s.length-1];
        //If the player wins in the current scenario, set the
        if (Objects.equals(s[2], s[0])) {
            finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 0;
        }
        //If the computer wins in the current scenario
        else if (Objects.equals(s[2], s[1])) {
            finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 1;
        }
        //If the computer and player tie in the current scenario
        else {
            finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 2;
        }
        //If it has not calculated all possible scenarios yet, call the function again through a return statement with the updated outcomes array (finalArr) and verbs array and the next line and index.
        if (index+1 < lines.length) {
            return findOutcomes(lines[index + 1], index + 1, finalArr, verbsArr);
        }
        //If it has calculated all possible scenarios, return the outcomes array (finalArr) and set verbsArr to the global verbs array.
        else {
            verbs = verbsArr;
            return finalArr;
        }
    }
    static String[] separateLine(String val) {
        /*
        This functions separates words in a string (val) into an array of strings
        Parameters: val: line to be separated (string)
        Returns: array of strings storing each word in the line separately
         */
        ArrayList<String> finalArr = new ArrayList<String>();
        String c = "";
        for (int i = 0; i < val.length(); i++) {
            //If current character is a space or colon, the string 'c' is added to the array
            if (val.charAt(i) == ' ' || val.charAt(i) == ':') {
                finalArr.add(c);
                c = "";
            }
            //If the current character is not a space or colon, the current character is added on to the string 'c'
            else {
                c += val.charAt(i);
            }
        }
        //If c is not empty, add c to the final array
        if (!c.isEmpty()) finalArr.add(c);
        return finalArr.toArray(new String[0]);
    }
    static int getIndex(String[] arr, String val) {
        /*
        Gets index of a specified value in a specified array
        Parameters: arr: array to search in (array of strings), val: value to search for (string)
        Returns: index of specified value in specified array
         */
        for (int i = 0; i < arr.length; i++) {
            if (Objects.equals(arr[i], val)) return i;
        }
        //return -1 if val not found in arr
        return -1;
    }
}