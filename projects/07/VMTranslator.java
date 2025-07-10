import java.io.IOException;

public class VMTranslator {
    public static void main(String[] args) {
        Parser parser = new Parser(args[0]);
        CodeWriter codeWriter = new CodeWriter(args[0]);

        while (parser.hasMoreLines()) {
            String command = parser.advance();
            if (command.isBlank()) {continue;}
            if (command.strip().charAt(0) == '/') { continue;}

            switch(parser.commandType(command)) {
                case CommandType.C_ARITHMETIC -> {
                    try {codeWriter.writeArithmetic(command);}
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case CommandType.C_PUSH, CommandType.C_POP -> {
                    try {codeWriter.WritePushPop(parser.commandType(command),
                                                parser.arg1(command),
                                                parser.arg2(command));}
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case null -> {

                }
            }
        }

        try {
            parser.close();
            codeWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
