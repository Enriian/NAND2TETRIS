import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HackAssembler {
    public static void main(String[] args) {
        HackParser parser = new HackParser(args[0]);
        SymbolTable symbolTable = new SymbolTable();
        int currentLineAddress = 0;
        int variableAddress = 16;

        List<String> addressVariablesList = new ArrayList<>();

        while(parser.hasMoreLines()) {
            parser.advance();
            if (parser.instructionType() == instruction.L_INSTRUCTION) {
                symbolTable.addEntry(parser.symbol(), currentLineAddress);
                currentLineAddress--;
            } else if (parser.instructionType() == instruction.A_INSTRUCTION && !parser.symbol().matches("\\d+")) {
                addressVariablesList.add(parser.symbol());
            }
            currentLineAddress++;
        }

        for (String x : addressVariablesList) {
            if (!symbolTable.contains(x)) {
                symbolTable.addEntry(x, variableAddress);
                variableAddress++;
            }
        }

        HackParser secondParse = new HackParser(args[0]);
        String outputFileName = args[0].substring(0, args[0].indexOf("."));
        String instructionLine;
        try (FileWriter outputFile = new FileWriter(outputFileName + ".hack");) {
            while (secondParse.hasMoreLines()) {
                secondParse.advance();
                if (secondParse.instructionType() == instruction.A_INSTRUCTION) {
                    int num;
                    if (symbolTable.contains(secondParse.symbol())) {
                        num = symbolTable.getAddress(secondParse.symbol());
                    } else {
                        num = Integer.parseInt(secondParse.symbol());
                    }
                    instructionLine = Integer.toBinaryString(num);
                    outputFile.write(String.format("%16s", instructionLine).replace(' ', '0'));
                    outputFile.write("\n");
                } else if (secondParse.instructionType() == instruction.C_INSTRUCTION) {
                    instructionLine = "111" + BinaryCode.comp(secondParse.comp())
                            + BinaryCode.dest(secondParse.dest())
                            + BinaryCode.jump(secondParse.jump());
                    outputFile.write(instructionLine);
                    outputFile.write("\n");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
