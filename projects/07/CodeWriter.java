import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
    private FileWriter fileWriter;
    private int compCount = 0;
    public CodeWriter(String fileName) {
        try {
            fileWriter = new FileWriter(fileName.strip().split("\\.")[0] + ".asm");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeArithmetic(String command) throws IOException {
        fileWriter.write("// " + command + "\n");
        fileWriter.write("@SP\n");
        fileWriter.write("M=M-1\n");
        fileWriter.write("A=M\n");
        fileWriter.write("D=M\n");

        fileWriter.write("@SP\n");
        fileWriter.write("M=M-1\n");
        fileWriter.write("A=M\n");

        // handle operations here
        if (command.equalsIgnoreCase("add")) {

            fileWriter.write("D=D+M\n");

        } else if (command.equalsIgnoreCase("sub")) {

            fileWriter.write("D=M-D\n");

        } else if (command.equalsIgnoreCase("neg")) {
            fileWriter.write("@SP\n");
            fileWriter.write("M=M+1\n");

            fileWriter.write("D=-D\n");

        } else if (command.equalsIgnoreCase("eq")) {
            fileWriter.write("D=M-D\n");

            fileWriter.write("@comp" + compCount + ".true\n");
            fileWriter.write("D;JEQ\n");
            fileWriter.write("@comp" + compCount + ".false\n");
            fileWriter.write("D=0\n");
            fileWriter.write("0;JMP\n");
            fileWriter.write("(comp" + compCount + ".true)\n");
            fileWriter.write("D=-1\n");
            fileWriter.write("(comp" + compCount + ".false)\n");

            ++compCount;
        } else if (command.equalsIgnoreCase("gt")) {
            fileWriter.write("D=M-D\n");

            fileWriter.write("@comp" + compCount + ".true\n");
            fileWriter.write("D;JGT\n");
            fileWriter.write("@comp" + compCount + ".false\n");
            fileWriter.write("D=0\n");
            fileWriter.write("0;JMP\n");
            fileWriter.write("(comp" + compCount + ".true)\n");
            fileWriter.write("D=-1\n");
            fileWriter.write("(comp" + compCount + ".false)\n");

            ++compCount;
        } else if (command.equalsIgnoreCase("lt")) {
            fileWriter.write("D=M-D\n");

            fileWriter.write("@comp" + compCount + ".true\n");
            fileWriter.write("D;JLT\n");
            fileWriter.write("@comp" + compCount + ".false\n");
            fileWriter.write("D=0\n");
            fileWriter.write("0;JMP\n");
            fileWriter.write("(comp" + compCount + ".true)\n");
            fileWriter.write("D=-1\n");
            fileWriter.write("(comp" + compCount + ".false)\n");

            ++compCount;
        } else if (command.equalsIgnoreCase("and")) {
            fileWriter.write("D=D&M\n");
        } else if (command.equalsIgnoreCase("or")) {
            fileWriter.write("D=D|M\n");
        } else if (command.equalsIgnoreCase("not")) {
            fileWriter.write("@SP\n");
            fileWriter.write("M=M+1\n");

            fileWriter.write("D=!D\n");

        }

        fileWriter.write("@SP\n");
        fileWriter.write("A=M\n");
        fileWriter.write("M=D\n");
        fileWriter.write("@SP\n");
        fileWriter.write("M=M+1\n");
    }

    public void WritePushPop(CommandType commandType, String segment, String index) throws IOException {
        fileWriter.write("// " + commandType.toString() + " " + segment + " " + index + "\n");

        if (segment.equalsIgnoreCase("constant")) {
            if (commandType == CommandType.C_POP) return;

            fileWriter.write("@" + index + "\n");
            fileWriter.write("D=A\n");

            fileWriter.write("@SP\n");
            fileWriter.write("A=M\n");
            fileWriter.write("M=D\n");

            fileWriter.write("@SP\n");
            fileWriter.write("M=M+1\n");

            return;
        }

        String symbol = "";
        switch(segment) {
            case "local" -> symbol = "LCL";
            case "argument" -> symbol = "ARG";
            case "static" -> {
                if (commandType == CommandType.C_PUSH) {
                    fileWriter.write("@16\n");
                    fileWriter.write("D=A\n");
                    fileWriter.write("@" + index + "\n");
                    fileWriter.write("D=D+A\n");
                    fileWriter.write("A=D\n");
                    fileWriter.write("D=M\n");

                    fileWriter.write("@SP\n");
                    fileWriter.write("A=M\n");
                    fileWriter.write("M=D\n");
                    fileWriter.write("@SP\n");
                    fileWriter.write("M=M+1\n");

                } else if (commandType == CommandType.C_POP) {
                    fileWriter.write("@16\n");
                    fileWriter.write("D=A\n");
                    fileWriter.write("@" + index + "\n");
                    fileWriter.write("D=D+A\n");

                    fileWriter.write("@R13\n");
                    fileWriter.write("M=D\n");

                    fileWriter.write("@SP\n");
                    fileWriter.write("M=M-1\n");
                    fileWriter.write("A=M\n");
                    fileWriter.write("D=M\n");

                    fileWriter.write("@R13\n");
                    fileWriter.write("A=M\n");

                    fileWriter.write("M=D\n");
                }

                return;
            }
            case "pointer" -> {
                if (index.equals("0")) {
                    symbol = "THIS";
                } else {
                    symbol = "THAT";
                    index = "0";
                }

                if (commandType == CommandType.C_PUSH) {
                    fileWriter.write("@" + symbol + "\n");
                    fileWriter.write("D=A\n");
                    fileWriter.write("@" + index + "\n");
                    fileWriter.write("D=D+A\n");
                    fileWriter.write("A=D\n");
                    fileWriter.write("D=M\n");

                    fileWriter.write("@SP\n");
                    fileWriter.write("A=M\n");
                    fileWriter.write("M=D\n");
                    fileWriter.write("@SP\n");
                    fileWriter.write("M=M+1\n");

                } else if (commandType == CommandType.C_POP) {
                    fileWriter.write("@" + symbol + "\n");
                    fileWriter.write("D=A\n");
                    fileWriter.write("@" + index + "\n");
                    fileWriter.write("D=D+A\n");

                    fileWriter.write("@R13\n");
                    fileWriter.write("M=D\n");

                    fileWriter.write("@SP\n");
                    fileWriter.write("M=M-1\n");
                    fileWriter.write("A=M\n");
                    fileWriter.write("D=M\n");

                    fileWriter.write("@R13\n");
                    fileWriter.write("A=M\n");

                    fileWriter.write("M=D\n");
                }

                return;
            }
            case "this" -> symbol = "THIS";
            case "that" -> symbol = "THAT";
            case "temp" -> {
                if (commandType == CommandType.C_PUSH) {
                    fileWriter.write("@5\n");
                    fileWriter.write("D=A\n");
                    fileWriter.write("@" + index + "\n");
                    fileWriter.write("D=D+A\n");
                    fileWriter.write("A=D\n");
                    fileWriter.write("D=M\n");

                    fileWriter.write("@SP\n");
                    fileWriter.write("A=M\n");
                    fileWriter.write("M=D\n");
                    fileWriter.write("@SP\n");
                    fileWriter.write("M=M+1\n");

                } else if (commandType == CommandType.C_POP) {
                    fileWriter.write("@5\n");
                    fileWriter.write("D=A\n");
                    fileWriter.write("@" + index + "\n");
                    fileWriter.write("D=D+A\n");

                    fileWriter.write("@R13\n");
                    fileWriter.write("M=D\n");

                    fileWriter.write("@SP\n");
                    fileWriter.write("M=M-1\n");
                    fileWriter.write("A=M\n");
                    fileWriter.write("D=M\n");

                    fileWriter.write("@R13\n");
                    fileWriter.write("A=M\n");

                    fileWriter.write("M=D\n");
                }
                return;
            }
        }

        if (commandType == CommandType.C_PUSH) {
            fileWriter.write("@" + symbol + "\n");
            fileWriter.write("D=M\n");
            fileWriter.write("@" + index + "\n");
            fileWriter.write("D=D+A\n");
            fileWriter.write("A=D\n");
            fileWriter.write("D=M\n");

            fileWriter.write("@SP\n");
            fileWriter.write("A=M\n");
            fileWriter.write("M=D\n");

            fileWriter.write("@SP\n");
            fileWriter.write("M=M+1\n");
        } else if (commandType == CommandType.C_POP) {
            fileWriter.write("@" + symbol + "\n");
            fileWriter.write("D=M\n");

            fileWriter.write("@" + index + "\n");
            fileWriter.write("D=D+A\n");
            fileWriter.write("@R13\n");
            fileWriter.write("M=D\n");

            fileWriter.write("@SP\n");
            fileWriter.write("M=M-1\n");
            fileWriter.write("A=M\n");
            fileWriter.write("D=M\n");

            fileWriter.write("@R13\n");
            fileWriter.write("A=M\n");

            fileWriter.write("M=D\n");
        }

    }

    public void close() throws IOException {
        fileWriter.close();
    }
}
