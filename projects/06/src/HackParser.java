import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

enum instruction {
    A_INSTRUCTION,
    C_INSTRUCTION,
    L_INSTRUCTION
}

public class HackParser {
    private File file;
    private Scanner fileScanner;
    public String currentLine;

    public HackParser(String fileName) {
        file = new File(fileName);
        try {
            fileScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public HackParser(HackParser hackParser) {
        this.file = hackParser.file;
        this.fileScanner = hackParser.fileScanner;
        this.currentLine = "";
    }

    public boolean hasMoreLines() {
        return fileScanner.hasNext();
    }

    public void advance() {
        do {
            currentLine = fileScanner.nextLine().trim();
        }while (currentLine.isBlank() || currentLine.contains("//"));
    }

    public void reset() {
        fileScanner.reset();
    }

    public instruction instructionType() {
        char firstChar = currentLine.trim().charAt(0);

        if (firstChar == '@') {
            return instruction.A_INSTRUCTION;
        } else if (firstChar == '(') {
            return instruction.L_INSTRUCTION;
        }

        return instruction.C_INSTRUCTION;
    }

    public String symbol() {
        if (instructionType() == instruction.A_INSTRUCTION) {
            return currentLine.substring(1);
        } else if (instructionType() == instruction.L_INSTRUCTION) {
            return currentLine.substring(1, currentLine.length() - 1);
        }

        return "";
    }

    public String dest() {
        if (instructionType() == instruction.A_INSTRUCTION) {
            return "";
        }

        if (currentLine.indexOf('=') != -1) {
            return currentLine.split("=")[0];
        }
        return "";
    }

    public String comp() {
        if (instructionType() == instruction.A_INSTRUCTION) {
            return "";
        }

        if (!dest().isEmpty() && !jump().isEmpty()) {
            return currentLine.split("=")[1].split(";")[0];
        } else if (!dest().isEmpty()) {
            return currentLine.split("=")[1];
        } else if (!jump().isEmpty()) {
            return currentLine.split(";")[0];
        } else if (!currentLine.isEmpty()) {
            return currentLine;
        }

        return "";
    }

    public String jump() {
        if (instructionType() == instruction.A_INSTRUCTION) {
            return "";
        }

        if (currentLine.indexOf(';') != -1) {
            return currentLine.split(";")[1];
        }
        return "";
    }
}
