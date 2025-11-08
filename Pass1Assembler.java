package Pass_1_assembler;

import java.util.*;

class Symbol {
    String name;
    int address;
    Symbol(String n, int a) {
        name = n;
        address = a;
    }
}

class Literal {
    String literal;
    int address;
    Literal(String l, int a) {
        literal = l;
        address = a;
    }
}

public class Pass1Assembler {
    static Map<String, String> MOT = new HashMap<>();
    static Map<String, Integer> REG = new HashMap<>();
    static List<Symbol> SYMTAB = new ArrayList<>();
    static List<Literal> LITTAB = new ArrayList<>();
    static List<Integer> POOLTAB = new ArrayList<>();
    static List<String> IC = new ArrayList<>();
    static int LC = 0;

    static void init() {
        MOT.put("STOP", "IS,00");
        MOT.put("ADD", "IS,01");
        MOT.put("SUB", "IS,02");
        MOT.put("MULT", "IS,03");
        MOT.put("MOVER", "IS,04");
        MOT.put("MOVEM", "IS,05");
        MOT.put("BC", "IS,07");
        MOT.put("START", "AD,01");
        MOT.put("END", "AD,02");
        MOT.put("ORIGIN", "AD,03");
        MOT.put("EQU", "AD,04");
        MOT.put("LTORG", "AD,05");
        MOT.put("DS", "DL,01");
        MOT.put("DC", "DL,02");
        REG.put("AREG", 1);
        REG.put("BREG", 2);
        REG.put("CREG", 3);
        POOLTAB.add(1);
    }

    static int getSymIndex(String sym) {
        for (int i = 0; i < SYMTAB.size(); i++)
            if (SYMTAB.get(i).name.equals(sym)) return i + 1;
        SYMTAB.add(new Symbol(sym, -1));
        return SYMTAB.size();
    }

    static int getLitIndex(String lit) {
        for (int i = 0; i < LITTAB.size(); i++)
            if (LITTAB.get(i).literal.equals(lit)) return i + 1;
        LITTAB.add(new Literal(lit, -1));
        return LITTAB.size();
    }

    public static void main(String[] args) {
        init();
        String[] code = {
            "START 200",
            "MOVER AREG, ='5'",
            "MOVEM AREG, X",
            "L1 MOVER BREG, ='2'",
            "ORIGIN L1+3",
            "LTORG",
            "NEXT ADD AREG, ='1'",
            "SUB BREG, ='2'",
            "BC LT, BACK",
            "LTORG",
            "BACK EQU L1",
            "ORIGIN NEXT+5",
            "MULT CREG, ='4'",
            "STOP",
            "X DS 1",
            "END"
        };

        for (String line : code) {
            String[] parts = line.trim().split("[ ,]+");
            String label = "", mnemonic = "", op1 = "", op2 = "";
            if (MOT.containsKey(parts[0]) || parts[0].equals("END"))
                mnemonic = parts[0];
            else {
                label = parts[0];
                if (parts.length > 1) mnemonic = parts[1];
                if (parts.length > 2) op1 = parts[2];
                if (parts.length > 3) op2 = parts[3];
            }
            if (mnemonic.isEmpty() && parts.length > 0) mnemonic = parts[0];

            switch (mnemonic) {
                case "START":
                    LC = Integer.parseInt(parts[1]);
                    IC.add("(AD,01)\t(C," + LC + ")");
                    break;
                case "MOVER":
                case "MOVEM":
                case "ADD":
                case "SUB":
                case "MULT":
                case "BC":
                case "STOP": {
                    if (!label.isEmpty())
                        SYMTAB.add(new Symbol(label, LC));
                    String mcode = MOT.get(mnemonic);
                    String ic = "(" + mcode + ")";
                    if (mnemonic.equals("STOP")) {
                        IC.add(LC + "\t" + ic);
                        LC++;
                        break;
                    }
                    String reg = "", op = "";
                    if (mnemonic.equals("BC")) {
                        reg = "(" + op1 + ")";
                        op = "(S," + getSymIndex(parts[2]) + ")";
                    } else {
                        reg = "(R," + REG.get(parts[1]) + ")";
                        if (parts[2].startsWith("='"))
                            op = "(L," + getLitIndex(parts[2]) + ")";
                        else
                            op = "(S," + getSymIndex(parts[2]) + ")";
                    }
                    IC.add(LC + "\t" + ic + "\t" + reg + "\t" + op);
                    LC++;
                    break;
                }
                case "ORIGIN": {
                    String[] split = parts[1].split("\\+");
                    String sym = split[0];
                    int add = Integer.parseInt(split[1]);
                    for (Symbol s : SYMTAB)
                        if (s.name.equals(sym))
                            LC = s.address + add;
                    IC.add("(AD,03)\t(S," + getSymIndex(sym) + ")\t(C," + add + ")");
                    break;
                }
                case "LTORG":
                case "END": {
                    IC.add("(AD,05)");
                    for (Literal l : LITTAB)
                        if (l.address == -1) {
                            l.address = LC;
                            IC.add(LC + "\t(DL,02)\t(C," + l.literal.substring(2, 3) + ")");
                            LC++;
                        }
                    POOLTAB.add(LITTAB.size() + 1);
                    break;
                }
                case "EQU": {
                    String sym1 = label, sym2 = parts[2];
                    int addr = 0;
                    for (Symbol s : SYMTAB)
                        if (s.name.equals(sym2)) addr = s.address;
                    SYMTAB.add(new Symbol(sym1, addr));
                    IC.add("(AD,04)\t(S," + getSymIndex(sym1) + ")\t(S," + getSymIndex(sym2) + ")");
                    break;
                }
                case "DS": {
                    SYMTAB.add(new Symbol(label, LC));
                    IC.add(LC + "\t(DL,01)\t(C," + parts[2] + ")");
                    LC += Integer.parseInt(parts[2]);
                    break;
                }
            }
        }

        System.out.println("\nINTERMEDIATE CODE:");
        for (String s : IC) System.out.println(s);

        System.out.println("\nSYMBOL TABLE:");
        for (int i = 0; i < SYMTAB.size(); i++)
            System.out.println((i + 1) + "\t" + SYMTAB.get(i).name + "\t" + SYMTAB.get(i).address);

        System.out.println("\nLITERAL TABLE:");
        for (int i = 0; i < LITTAB.size(); i++)
            System.out.println((i + 1) + "\t" + LITTAB.get(i).literal + "\t" + LITTAB.get(i).address);

        System.out.println("\nPOOL TABLE:");
        for (int i = 0; i < POOLTAB.size(); i++)
            System.out.println("#" + (i + 1) + "\t" + POOLTAB.get(i));
    }
}
