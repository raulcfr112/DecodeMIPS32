package br.ufms.main;

import java.util.Scanner;

//Autor: Raul Camargo

public class Decoder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite uma instrução MIPS de 32 bits (em formato decimal ou hexadecimal): ");
        String entrada = scanner.nextLine();

        decodificarInstrucao(entrada);
    }

    //Este método decodifica uma instrução com base em uma string de entrada, determinando o tipo da instrução com base no valor do opcode.
    public static void decodificarInstrucao(String entrada) {
        String instrucao;
        //Caso a entrada comece com "0x", a variável instrucao é substituida com um substring da entrada, tirando os dois primeiros caracteres.
        if (entrada.startsWith("0x")) {
            instrucao = entrada.substring(2);
        }
        //Se nao, a entrada é vista como um número decimal, a variavel entrada é convertida para um número inteiro usando o parseInt(), e depois é
        // convertido para uma representação hexadecimal usando o método toHexString().
        else {
            instrucao = Integer.toHexString(Integer.parseInt(entrada));
        }
        int instrucaoInt = Integer.parseUnsignedInt(instrucao, 16);

        //O valor de instrucaoInt é deslocado 26 bits para a direita. O resultado é atribuído à variável opcode
        int opcode = instrucaoInt >>> 26;

        //É feito um switch deste opcode, onde é chamado o determinado metodo que será daquela instrução.
        switch (opcode) {
            case 0 -> decodificarTipoR(instrucaoInt);
            case 2, 3 -> decodificarTipoJ(instrucaoInt);
            default -> decodificarTipoI(instrucaoInt);
        }
    }

    //O metodo decodificarTipoR() é usado para instruções do tipoR, onde a instrução é separada e tratada conforme instruções do tipo R exigem.
    public static void decodificarTipoR(int instrucao) {
        //Os valores rs, rt e rd são feitos com deslocamentos de bits e aplicando a mascara '0x1F' para obter os bits significativos.
        int rs = (instrucao >>> 21) & 0x1F;
        int rt = (instrucao >>> 16) & 0x1F;
        int rd = (instrucao >>> 11) & 0x1F;
        int funct = instrucao & 0x3F;

        //São criados vetores para armazenar os devidos operandos, e sinais de controle das instr. do tipo R.
        String mnemonico = getMnemonicoTipoR(funct);
        String[] operandos = getOperandosTipoR(rs, rt, rd);
        String[] sinaisControle = getSinaisControleTipoR();

        //E aqui é organizado e apresentado os dados no terminal.
        System.out.println("\nInstrução: " + mnemonico + " " + operandos[0] + ", " + operandos[1] + ", " + operandos[2]);
        System.out.println("Tipo: R\n");
        System.out.println("Operandos:\n" + operandos[0] + "\n" + operandos[1] + "\n" + operandos[2] + "\n");
        System.out.println("Sinais de Controle:\nRegDst=" + sinaisControle[0] +
                "\nBranch= " + sinaisControle[1] +
                "\nMemRead= " + sinaisControle[2] +
                "\nMemWrite= " + sinaisControle[3] +
                "\nMemtoReg= " + sinaisControle[4] +
                "\nALUSrc= " + sinaisControle[5] +
                "\nRegWrite= " + sinaisControle[6]);
    }

    //Segue a mesma logica do metodo decodificarTipoR().
    public static void decodificarTipoI(int instrucao) {
        int opcode = instrucao >>> 26;
        int rs = (instrucao >>> 21) & 0x1F;
        int rt = (instrucao >>> 16) & 0x1F;
        int imm = instrucao & 0xFFFF;

        String mnemonico = getMnemonicoTipoI(opcode);
        String[] operandos = getOperandosTipoI(rs, rt, imm, instrucao);
        String[] sinaisControle = getSinaisControleTipoI(mnemonico);

        System.out.println("\nInstrução: " + mnemonico + " " + operandos[0] + ", " + operandos[1] + ", " + operandos[2]);
        System.out.println("Tipo: I\n");
        System.out.println("Operandos:\n" + operandos[0] + "\n" + operandos[1] + "\n" + operandos[2] + "\n");
        System.out.println("Sinais de Controle:\nRegDst=" + sinaisControle[0] +
                "\nBranch= " + sinaisControle[1] +
                "\nMemRead= " + sinaisControle[2] +
                "\nMemWrite= " + sinaisControle[3] +
                "\nMemtoReg= " + sinaisControle[4] +
                "\nALUSrc= " + sinaisControle[5] +
                "\nRegWrite= " + sinaisControle[6]);
    }

    //Segue a mesma logica do metodo decodificarTipoR().
    public static void decodificarTipoJ(int instrucao) {
        int opcode = instrucao >>> 26;
        int addr = instrucao & 0x3FFFFFF;

        String mnemonico = getMnemonicoTipoJ(opcode);
        String[] operandos = getOperandosTipoJ(addr);
        String[] sinaisControle = getSinaisControleTipoJ();

        System.out.println("\nInstrução: " + mnemonico + " " + operandos[0] + "\n");
        System.out.println("Tipo: J\n");
        System.out.println("Operandos:\n" + operandos[0] + "\n");
        System.out.println("Sinais de Controle:\nRegDst=" + sinaisControle[0] +
                "\nBranch= " + sinaisControle[1] +
                "\nMemRead= " + sinaisControle[2] +
                "\nMemWrite= " + sinaisControle[3] +
                "\nMemtoReg= " + sinaisControle[4] +
                "\nALUSrc= " + sinaisControle[5] +
                "\nRegWrite= " + sinaisControle[6]);
    }

    //Metodo responsavel por verificar a varivel funct e retornar qual a Mnemonico daquela instrução.
    public static String getMnemonicoTipoR(int funct) {

        return switch (funct) {
            case 0x20 -> "add";
            case 0x21 -> "addu";
            case 0x24 -> "and";
            case 0x1A -> "div";
            case 0x1B -> "divu";
            case 0x08 -> "jr";
            case 0x18 -> "mult";
            case 0x19 -> "multu";
            case 0x27 -> "nor";
            case 0x25 -> "or";
            case 0x0 -> "sll";
            case 0x02 -> "srl";
            case 0x22 -> "sub";
            case 0x23 -> "subu";
            case 0x2A -> "slt";
            case 0x2B -> "sltu";
            default -> "";
        };
    }

    //Retorna os operandos das instruções do tipo R.
    public static String[] getOperandosTipoR(int rs, int rt, int rd) {
        String[] operandos = new String[3];

        operandos[0] = "RD: R" + rd;
        operandos[1] = "RS: R" + rs;
        operandos[2] = "RT: R" + rt;

        return operandos;
    }

    //Retorna os sinais de controle das instruções do tipo R.
    public static String[] getSinaisControleTipoR() {
        return new String[]{"1", "0", "0", "0", "0", "0", "1"};
    }

    public static String getMnemonicoTipoI(int opcode) {

        return switch (opcode) {
            case 0x08 -> "addi";
            case 0x09 -> "addiu";
            case 0x0C -> "andi";
            case 0x0D -> "ori";
            case 0x23 -> "lw";
            case 0x2B -> "sw";
            case 0x04 -> "beq";
            case 0x05 -> "bne";
            case 0x0A -> "slti";
            case 0x0B -> "sltiu";
            default -> "";
        };
    }

    public static String[] getOperandosTipoI(int rs, int rt, int imm, int instrucao) {
        String[] operandos = new String[3];

        int opcode = (instrucao >>> 26) & 0x3F;

        switch (opcode) { // addi
            // addiu
            // ori
            // ori
            // lw
            case 0x08, 0x09, 0x0C, 0x0D, 0x23, 0x2B, 0x0A, 0x0B -> { // sw
                operandos[0] = "RT: R" + rt;
                operandos[1] = "RS: R" + rs;
                operandos[2] = "Imediato " + imm;
            }
            case 0x0F -> { // lui
                operandos[0] = "RT: R" + rt;
                operandos[1] = "Imediato " + imm;
                operandos[2] = "";
            } // beq
            case 0x04, 0x05 -> { // bne
                operandos[0] = "RS: R" + rs;
                operandos[1] = "RT: R" + rt;
                operandos[2] = "Imediato " + imm;
            }
            default -> {
                operandos[0] = "";
                operandos[1] = "";
                operandos[2] = "";
            }
        }

        return operandos;
    }

    public static String[] getSinaisControleTipoI(String mnemonico) {

        return switch (mnemonico) {
            case "addiu","addi" -> new String[]{"0", "0", "0", "0", "0", "1", "1"};
            case "lw" -> new String[]{"0", "0", "1", "0", "1", "1", "1"};
            case "sw" -> new String[]{"x", "0", "0", "1", "x", "1", "0"};
            case "bne","beq" -> new String[]{"x", "1", "0", "0", "x", "0", "0"};
            default -> new String[]{"", "", "", "", "", "", ""};
        };
    }

    public static String getMnemonicoTipoJ(int opcode) {

        return switch (opcode) {
            case 0x02 -> "j";
            case 0x03 -> "jal";
            default -> "opcode inválido";
        };
    }

    public static String[] getOperandosTipoJ(int addr) {
        String[] operandos = new String[1];

        operandos[0] = "0x" + Integer.toHexString(addr);

        return operandos;
    }

    public static String[] getSinaisControleTipoJ() {
        return new String[]{"x", "x", "0", "0", "x", "x", "x"};
    }
}
