import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

enum CommandType {
    C_ARITHMETIC,
    C_PUSH,
    C_POP
}

public class Parser {

    private Scanner fileScanner;
    public Parser(String fileName) {
        try {
            fileScanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean hasMoreLines() {
        return fileScanner.hasNext();
    }

    public String advance() {
        return fileScanner.nextLine();
    }

    public CommandType commandType(String commandStr) {
        String[] strList = commandStr.strip().split(" ");
        if (strList.length == 1) {
            return CommandType.C_ARITHMETIC;
        } else {
            if (strList[0].equals("push")) {
                return CommandType.C_PUSH;
            } else if (strList[0].equals("pop")) {
                return CommandType.C_POP;
            }
        }
        return null;
    }

    public String arg1(String commandStr) {
        switch(commandType(commandStr)) {
            case CommandType.C_ARITHMETIC -> {return commandStr;}
            case CommandType.C_PUSH, CommandType.C_POP -> {return commandStr.strip().split(" ")[1];}
            default -> {return "";}
        }
    }

    public String arg2(String commandStr) {
        String[] strList = commandStr.strip().split(" ");
        if (commandType(commandStr) != CommandType.C_ARITHMETIC) {
            return strList[2];
        }

        return "";
    }

    public void close() {
        fileScanner.close();
    }
}
