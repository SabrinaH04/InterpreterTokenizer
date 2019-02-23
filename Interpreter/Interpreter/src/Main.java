import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<String> inputVarsList = new LinkedList<String>(); //input vars, tokenized.
    //public static String outputFilename; //output file (corePrettyPrintFile)

    public static List<String> readInProgram(File coreProgram) throws IOException {
        //test files
        //coreProgram = "test program1";

        //line in
        String line = null;
        List<String> inputProgram = new LinkedList<String>();

        try {
            FileReader reader = new FileReader(coreProgram);

            BufferedReader buffer = new BufferedReader(reader);
            line = buffer.readLine();
            while (line != null) {
                //System.out.println(line);
                inputProgram.add(line);
                line = buffer.readLine();

            }
            buffer.close();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot open file " + coreProgram + "\n");
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return inputProgram;

    }

    public static void checkForNegVars(List<String> inputList) {
        int counts = 1;
        String x = " ";
        String y = " ";
        String xy = " ";
        while (inputList.size() > 2 && counts < inputList.size()) {//if there is a negative, there needs to be 2 tokens in the list
            x = inputList.get(counts - 1);
            y = inputList.get(counts);
            if (x.equals("-")) {//if the first token is a negative
                inputList.remove(counts);
                inputList.remove(counts - 1);
                xy = x + y;
                inputList.add(counts - 1, xy);//add -+int to list in place of - , int
                System.out.println("this is the xy added back to varList: " + xy);
            }

            counts++;
        }
    }
    //public static void writeToOutputFile(String filename, String output) throws IOException { //wrote this before I realized output to console
    //File outFile = new File(filename);
    //FileWriter filewriter = new FileWriter(outFile);
    //BufferedWriter writer = new BufferedWriter(filewriter);
    //writer.write(output);
    //writer.close();
    //filewriter.close();
    //}

    public static void main(String[] args) throws IOException {
        // Prints "Hello, World" to the terminal window.
        File file1 = null;
        File file2 = null;
        Scanner keyboard = new Scanner(System.in);
        //get file from args
        if (args.length > 0) {
            file1 = new File(args[0]);
            file2 = new File(args[1]);

        } else {
            System.out.println("Please enter two files to read: CORE program, then variable inputs.");
            //Scanner keyboard = new Scanner(System.in);
            file1 = new File(keyboard.nextLine());
            System.out.println("Second file: ");
            file2 = new File(keyboard.nextLine());
        }
        keyboard.close();
        List<String> inputProgram = new LinkedList<String>();
        List<String> inputVarsString = new LinkedList<String>();
        //read in the file

        inputProgram = readInProgram(file1);
        inputVarsString = readInProgram(file2);
        //tokenizing the input variable values- should work?
        Tokenizer.Tokenizing(inputVarsString);
        for (String tokens : Tokenizer.tokensList) { //for all tokens from tokensList
            inputVarsList.add(tokens); //transfer all tokens to inputVarsList
        }
        checkForNegVars(inputVarsList);//finds '-' tokens and adds them to the following 'int' token
        //for (String nums : inputVarsList) {
        //System.out.println("VARS FOUND: " + nums);
        //}
        Tokenizer.resetTokensList(); //resets tokensList, deletes all elements.
        //tokenize the file, should write over old tokensList
        Tokenizer.Tokenizing(inputProgram);
        //for(String tokens:Tokenizer.tokensList) {
        //System.out.println(tokens);

        //}
        //System.out.println("Please enter file path to write to:");
        //outputFilename = new String(keyboard.nextLine());
        //outputFilename = "C:\\Users\\sabri\\eclipse-workspace\\Interpreter\\src\\corePrettyPrintFile";
        ParsePrintExecute.startParsePrintExecute();

    }
}
