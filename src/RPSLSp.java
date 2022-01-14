import java.nio.file.*;
import java.io.*;
import java.util.*;

/**
 * RPSLSp is a program that allows you to play rock-paper-scissors-lizard spock against a computer.
 *
 * @author Matthew Bevins
 */
public class RPSLSp {
    /**
     * Choices is an array of strings storing all possible weapons
     */
    static String[] choices = new String[0];
    /**
     * Lines is an array of strings storing all lines in the battles txt file
     */
    static String[] lines;
    /**
     * Outcomes is a 2D array of ints storing all possible outcomes. Rows = user's weapon (based on the weapon's index in the choices array), column = computer's weapon, and if the user wins the outcome is stored as 0, if the computer wins it is stored as 1, and if it is a tie it is stored as 2.
     */
    static int[][] outcomes;
    /**
     * Verbs is a 2D array of strings storing the verbs used in all possible outcomes. The format is the same as the outcomes array, except the values in the array will be the verb printed depending on the outcome.
     */
    static String[][] verbs;
    /**
     * Record is a 2D array of ints storing the number of times each player used each weapon. The first row is the player and the second is the computer. The columns store the number of times each competitor used each weapon.
     */
    static int[][] record;
    /**
     * WinLossTies stores the number of wins for the user in index 0, the number of wins for the computer in index 1, and the number of ties in index 2.
     */
    static int[] winLossTies = new int[3];
    /**
     * Path to battles file
     */
    static String filePath = "battles5.txt";
    /**
     * Random number generator
     */
    static Random r = new Random();
    public static void main(String[] args) {
        r.setSeed(5);
        Scanner input = new Scanner(System.in).useDelimiter("\n");
        lines = readFile(filePath);
        switch(lines[0]) {
            case "ERROR:FILENOTFOUND":
                while (Objects.equals(readFile(filePath)[0], "ERROR:FILENOTFOUND")) {
                    System.out.print("File not found. Try again: ");
                    filePath = input.next();
                }
                lines = readFile(filePath);
                break;
            case "ERROR:MISSINGCHOICES":
                errorMessage();
        }
        initializeArrays();

        //GAME CODE
        System.out.println("Welcome to " +
                Arrays.toString(choices).substring(1,Arrays.toString(choices).length()-1)
                + "!\n" + "You can choose either one of these options " +
                "and the computer will choose a random " +
                "option, \n" + "and the program will display and " +
                "keep track of the winners. Have fun!");
        String output = "";
        while (true) {
            if (! output.contains("not valid")) {
                //Prints all weapon options
                System.out.println();
                for (int i = 0; i < choices.length; i++) {
                    System.out.println((i + 1) + ". " + choices[i]);
                }
            }
            System.out.print("Choose your weapon (1-" + choices.length + "): ");
            String weaponInput = input.next();
            output = checkUserInput(weaponInput);
            System.out.println(output);
            if (! output.contains("not valid")) {
                System.out.print("Battle again (yes/no)? ");
                String choice = input.next();
                if (choice.toLowerCase().charAt(0) == 'n') {
                    //Print record
                    printRecord();
                    break;
                }
            }
        }

    }

    static String checkUserInput(String input) {
        int weapon = 0;
        //checks if user's input is a number
        try {
            weapon = Integer.parseInt(input);
        }
        catch(NumberFormatException e) {
            return "Input is not valid, you need to enter a number.";
        }
        if (weapon >= 1 && weapon <= choices.length)
            return playBattle(weapon, r.nextInt(choices.length) + 1);
        return "Input is not valid, you need to enter a number between 1 and " + choices.length + ".";
    }

    /**
     * Finds the winner of a given battle and updates the record.
     * @param user The user's weapon choice.
     * @param computer The computer's weapon choice.
     * @return A formatted string telling the user who won and with what weapon.
     */
    static String playBattle(int user, int computer) {
        //Update player record
        record[0][user-1]++;
        //Update computer record
        record[1][computer-1]++;
        //Gets outcome using outcomes array and returns string based on the outcome
        switch (outcomes[user - 1][computer - 1]) {
            //User win
            case 0:
                winLossTies[0]++;
                return "User (" + choices[user - 1] + ") " + verbs[user - 1][computer - 1] +
                        " Computer (" + choices[computer - 1] + ")";
            //Computer win
            case 1:
                winLossTies[1]++;
                return "Computer (" + choices[computer - 1] + ") " + verbs[user - 1][computer - 1] +
                        " User (" + choices[user - 1] + ")";
            //Tie
            case 2:
                winLossTies[2]++;
                return "User (" + choices[user - 1] + ") ties Computer (" + choices[computer - 1] + ")";
            //This shouldn't happen
            default:
                return "error";
        }
    }

    /**
     * Formats and prints the records of the user and computer.
     */
    static void printRecord() {
        System.out.println();
        System.out.print("         ");
        for (String s : choices) {
            System.out.printf("%8s ", s);
        }
        System.out.println();
        System.out.println("         -------- -------- -------- -------- --------");
        System.out.printf("%8s ", "Computer");
        for (int i : record[1]) {
            System.out.printf("%8d ", i);
        }
        System.out.println();
        System.out.printf("%8s ", "User");
        for (int i : record[0]) {
            System.out.printf("%8d ", i);
        }
        System.out.println("\n");
        System.out.println("The computer won " + winLossTies[1] + " time" + needsS(winLossTies[1]) + ",");
        System.out.println("the user won " + winLossTies[0] + " time" + needsS(winLossTies[0]) + ",");
        System.out.println("and they tied " + winLossTies[2] + " time" + needsS(winLossTies[2]) + ".");
    }

    /**
     * Runs the necessary methods to initialize the arrays needed for the game to function.
     */
    static void initializeArrays() {
        //Creates the choices array using the separateLine method
        choices = separateLine(lines[1]);
        if (Math.sqrt(lines.length-2) != choices.length) {
            System.out.println("Missing some battle information in the file.");
            errorMessage();
        }
        if (choices.length < Integer.parseInt(lines[0])) {
            System.out.println("Just FYI, there are too many choices in the file.\n");
        }
        if (choices.length > Integer.parseInt(lines[0])) {
            System.out.println("There are not enough choices in the file.");
            errorMessage();
        }
        //Uses the findOutcomes method to fill the outcomes and verbs arrays.
        findOutcomes();
        //Initializes the record array.
        record = new int[2][choices.length];
    }

    /**
     * Prints the file error message and ends the game.
     */
    static void errorMessage() {
        System.out.println("There is an error in " + filePath + ", ending game.");
        System.exit(1);
    }

    /**
     * Reads a file based on the given path and separates each line into an array.
     * @param path Path to file.
     * @return Array where each index is a different line in the given file.
     */
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
            try {
                Integer.parseInt(linesA.get(0));
            }
            catch(Exception e) {
                if (e.toString().contains("NumberFormatException")) {
                    System.out.println("Missing number of choices.");
                    return new String[]{"ERROR:MISSINGCHOICES"};
                }
                else {
                    return new String[]{"ERROR:UNKNOWN"};
                }
            }
            lines = linesA.toArray(new String[0]);
        }
        catch (IOException x) {
            return new String[]{"ERROR:FILENOTFOUND"};
        }
        return lines;
    }

    /**
     * Goes through every possible outcome and puts it in the outcomes array and the verb associated with that scenario in the verbs array.
     */
    static void findOutcomes() {
        int[][] finalArr = new int[choices.length][choices.length];
        String[][] verbsArr = new String[choices.length][choices.length];
        for (int i = 2; i < lines.length; i++) {
            String[] s = separateLine(lines[i]);
            if (getIndex(choices, s[0]) == -1 || getIndex(choices, s[1]) == -1 ||
                    (getIndex(choices, s[2]) == -1 && !Objects.equals(s[2], "ties")) ||
                    (!Objects.equals(s[2], "ties") && s.length == 3)) {
                System.out.println("Missing some battle information in the file.");
                errorMessage();
            }
            //Puts the verb of the current scenario into the verbs array. The row is the player's choice and the column is the computer's choice.
            verbsArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = s[s.length-1];
            //If the player wins in the current scenario, set the scenario's value in the 2D array to 0
            if (Objects.equals(s[2], s[0])) {
                finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 0;
            }
            //If the computer wins in the current scenario, set the scenario's value in the 2D array to 1
            else if (Objects.equals(s[2], s[1])) {
                finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 1;
            }
            //If the computer and player tie in the current scenario, set the scenario's value in the 2D array to 2
            else {
                finalArr[getIndex(choices, s[0])][getIndex(choices, s[1])] = 2;
            }
        }
        verbs = verbsArr;
        outcomes = finalArr;
    }

    /**
     * Separates words in a string into an array of strings.
     * @param val String to be separated.
     * @return An array of strings (each index contains a different word).
     */
    static String[] separateLine(String val) {
        ArrayList<String> finalArr = new ArrayList<String>();
        String c = "";
        for (int i = 0; i < val.length(); i++) {
            //If current character is a space or colon, the string 'c' is added to the array
            if (val.charAt(i) == ' ' || val.charAt(i) == ':') {
                finalArr.add(c);
                c = "";
            }
            //If the current character is a dash, add a space to the string 'c'
            else if (val.charAt(i) == '-'){
                c += " ";
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

    /**
     * Checks whether an integer would need the letter "s" at the end of a word.
     * @param val Integer to check.
     * @return String containing either 's' or nothing.
     */
    static String needsS(int val) {
        if (val == 1) return "";
        return "s";
    }

    /**
     * Finds the index of a given value in a given array.
     * @param arr Array to search in.
     * @param val Value to search for.
     * @return Integer index of val in arr.
     */
    static int getIndex(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (Objects.equals(arr[i], val)) return i;
        }
        //return -1 if val not found in arr
        return -1;
    }
}