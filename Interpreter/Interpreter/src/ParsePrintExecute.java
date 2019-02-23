import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ParsePrintExecute {
    private ParsePrintExecute() {

    }

    //public static int Tposition = 0;
    public static void startParsePrintExecute() throws IOException { //Begin parse, print, execute of program
        progNode coreProgram = new progNode();
        coreProgram.parsing();
        coreProgram.printing();
        coreProgram.executing();

    }

}

class varHashMap {
    public static HashMap<String, Integer> var = new HashMap<String, Integer>();
}

//private static HashMap<String,Integer> variables = new HashMap<String,Integer>();
class progNode { //program node: 'program' declSeq 'begin' stmtSeq 'end'
    private declSeqNode DECLSEQ;
    private stmtSeqNode STMTSEQ;

    public void parsing() {
        //System.out.println("Parsing progNode!");
        Tokenizer.resetCursor(); //prog should only be called at the start of parsing, so cursor gets reset
        Tokenizer.checkToken("program");//makes sure first token is 'program'
        Tokenizer.skipToken(); //skips 'program' token
        this.DECLSEQ = new declSeqNode();
        this.DECLSEQ.parsing(); //cursor is at first token of DECLSEQ
        Tokenizer.checkToken("begin");
        Tokenizer.skipToken(); //skips 'begin' token
        this.STMTSEQ = new stmtSeqNode();
        this.STMTSEQ.parsing(); //cursor is at first token of STMTSEQ. returns with cursor at 'end'
        Tokenizer.checkToken("end");
        Tokenizer.skipToken();//skips 'end' token
        Tokenizer.checkToken("EOF");
        Tokenizer.skipToken();

    }

    public void printing() {
        System.out.print("program " + "\n");
        this.DECLSEQ.printing();
        System.out.print("begin" + "\n");
        this.STMTSEQ.printing();
        System.out.print("end");

    }

    public void executing() {
        System.out.println("\n" + "---------------CORE program output below: -----------------" + "\n");
        Tokenizer.resetCursor(); //reset cursor at start of execution
        Tokenizer.skipToken(); //skips 'program', no need to check, checked already in parser
        this.DECLSEQ.executing();
        Tokenizer.skipToken();//skip 'begin'
        this.STMTSEQ.executing();
        Tokenizer.skipToken();//skip 'end'
        Tokenizer.skipToken();//skip EOF

    }

}

class declSeqNode { //declSeq node: decl | decl declSeq
    private declSeqNode DECLSEQ;
    private declNode DECL;
    private int kind = 0; //assume the alternator is 0

    public void parsing() {
        //System.out.println("Parsing declSeqNode!");
        //System.out.println("Next token is now: "+Tokenizer.getToken());
        this.DECL = new declNode();
        this.DECL.parsing();
        if (!Tokenizer.getToken().equals("begin")) { //if the next token is not begin, there's a declSeq

            this.kind = 1; //kind is now 1, since it's decl declseq
            this.DECLSEQ = new declSeqNode();
            this.DECLSEQ.parsing();

        }

    }

    public void printing() {
        this.DECL.printing();
        if (this.kind == 1) { //if, in parsing, there's a declseq after...

            this.DECLSEQ.printing();
        }
    }

    public void executing() {
        //System.out.println("Executing declSeqNode!");
        this.DECL.executing();
        if (this.kind == 1) {
            this.DECLSEQ.executing();
        }

    }

}

class stmtSeqNode { //stmtSeq node: stmt | stmt stmtSeq
    private stmtNode STMT;
    private stmtSeqNode STMTSEQ;
    private int kind = 0;

    public void parsing() {
        //System.out.println("Parsing stmtSeqNode!");
        this.STMT = new stmtNode();
        this.STMT.parsing();
        if (!Tokenizer.getToken().equals("end") && !Tokenizer.getToken().equals("else")) { //stmt occurs before 'end'(prog, if, loop,) 'else'(if)
            this.kind = 1; //there's a stmtseq after the stmt
            this.STMTSEQ = new stmtSeqNode();
            this.STMTSEQ.parsing();
        }

    }

    public void printing() {
        if (this.kind == 0) {
            this.STMT.printing();
        } else {
            this.STMT.printing();
            this.STMTSEQ.printing();
        }

    }

    public void executing() {
        //System.out.println("Executing stmtSeqNode!");
        this.STMT.executing();
        if (this.kind == 1) {
            this.STMTSEQ.executing();
        }
    }

}

class declNode { //decl node: 'int' idList';'
    private idListNode IDLIST;

    public void parsing() {
        //System.out.println("Parsing declNode!");
        //System.out.println("Next token is now: "+Tokenizer.getToken());
        Tokenizer.checkToken("int"); //makes sure next token is 'int'
        Tokenizer.skipToken();
        //System.out.println("Next token is now: "+Tokenizer.getToken());
        this.IDLIST = new idListNode();
        this.IDLIST.parsing();
        Tokenizer.checkToken(";"); //next token is ';"
        Tokenizer.skipToken();

    }

    public void printing() {
        System.out.print("int ");
        this.IDLIST.printing();
        System.out.print("; " + "\n");
    }

    public void executing() {
        //System.out.println("Executing declNode!");
        Tokenizer.skipToken(); //skip 'int'
        this.IDLIST.executing(1); //1 means idlist came from decl, ids will be declared.
        Tokenizer.skipToken();//skip ";"

    }

}

class idListNode { //idList node: id | id ',' idList
    private idNode ID;
    private idListNode IDLIST;
    private int kind = 0;
    private List<String> idLinkedList = new LinkedList<String>();

    public void parsing() {
        //System.out.println("Parsing idListNode!");
        //System.out.println("Next token is now: "+Tokenizer.getToken());
        this.ID = new idNode();
        this.ID.parsing();
        this.idLinkedList.add(this.ID.getID()); //adds all IDs to a linked list, used in outNode and inNode
        if (Tokenizer.getToken().equals(",")) { //if the next token is a ',' then there are more IDS.
            this.kind = 1;
            Tokenizer.skipToken(); //skipping the comma
            this.IDLIST = new idListNode();
            this.IDLIST.parsing();

        }

    }

    public void printing() {
        if (this.kind == 0) {
            this.ID.printing();
        } else {
            this.ID.printing();
            System.out.print(", ");
            this.IDLIST.printing();
        }

    }

    public void executing(int x) {//x tells ID if it came from decl, so can be declared
        //System.out.println("Executing IDListNode!");
        this.ID.executing(x);
        if (this.kind == 1) {
            Tokenizer.skipToken(); //skip ","
            this.IDLIST.executing(x);
        }

    }

    public String getfirstID() {
        return this.ID.getID();
    }

    public List<String> getIDList() {
        return this.idLinkedList;
    }

    public int getKind() {
        return this.kind;
    }

}

class stmtNode { //stmt node: assign | if | loop | in | out
    private assignNode ASSIGN;
    private ifNode IF;
    private loopNode LOOP;
    private inNode IN;
    private outNode OUT;
    private int kind = 0; //start with assuming assign is the alternative

    public void parsing() {
        //System.out.println("Parsing stmtNode!");
        if (Tokenizer.isTokenID()) { //Next token is an ID, so must be 'assign'
            this.kind = 0;
            this.ASSIGN = new assignNode();
            this.ASSIGN.parsing();
        } else if (Tokenizer.getToken().equals("if")) { //next Token is if, so next node is if node
            this.kind = 1;
            this.IF = new ifNode();
            this.IF.parsing();
        } else if (Tokenizer.getToken().equals("while")) { //next node is loop
            this.kind = 2;
            this.LOOP = new loopNode();
            this.LOOP.parsing();
        } else if (Tokenizer.getToken().equals("read")) { //next node is in node
            this.kind = 3;
            this.IN = new inNode();
            this.IN.parsing();
        } else if (Tokenizer.getToken().equals("write")) { //next node is out node
            this.kind = 4;
            this.OUT = new outNode();
            this.OUT.parsing();
        } else { //next token isn't a stmt alternative
            System.out.println("Error in stmtNode, expected stmt alternative, got: " + Tokenizer.getToken());
            System.exit(1);
        }

    }

    public void printing() {
        if (this.kind == 0) { //assign
            this.ASSIGN.printing();
        } else if (this.kind == 1) { //if
            this.IF.printing();
        } else if (this.kind == 2) { //loop
            this.LOOP.printing();
        } else if (this.kind == 3) { //in
            this.IN.printing();
        } else { //out
            this.OUT.printing();
        }
    }

    public void executing() {
        //System.out.println("Executing stmtNode!");
        if (this.kind == 0) { //assign
            this.ASSIGN.executing();
        } else if (this.kind == 1) { //if
            this.IF.executing();
        } else if (this.kind == 2) { //loop
            this.LOOP.executing();
        } else if (this.kind == 3) { //in
            this.IN.executing();
        } else { //out
            this.OUT.executing();
        }

    }

}

class assignNode { //assign node: id '=' exp ';'
    private idNode ID;
    private expNode EXP;

    public void parsing() {
        //System.out.println("Parsing assignNode!");
        //Boolean isID = Tokenizer.isTokenID();
        if (Tokenizer.isTokenID()) { //next token should be ID
            this.ID = new idNode();
            this.ID.parsing();
            Tokenizer.checkToken("=");
            Tokenizer.skipToken();
            this.EXP = new expNode();
            this.EXP.parsing();
            Tokenizer.checkToken(";");
            Tokenizer.skipToken();
        } else {
            System.out.println("Error in assignNode, expected ID, got: " + Tokenizer.getToken());
            System.exit(1);
        }

    }

    public void printing() {
        this.ID.printing();
        System.out.print(" = ");
        this.EXP.printing();
        System.out.print("; " + "\n");
    }

    public void executing() {
        //System.out.println("Executing assignNode!");
        String ID2 = this.ID.getID();
        if (varHashMap.var.containsKey(ID2)) {//found ID2 in vars, good!
            this.EXP.executing();
            int assignVal = this.EXP.getExpVal();
            varHashMap.var.put(ID2, assignVal);

        } else { //ID2 not in vars, therefore not declared
            System.out.println("Error in assignNode, ID " + ID2 + " not declared");
            System.exit(1);
        }

    }

}

class ifNode { //if node: 'if' cond 'then' stmtSeq 'end'';'| 'if' cond 'then' stmtSeq 'else' stmtSeq 'end'';'
    private condNode COND;
    private stmtSeqNode STMTSEQ1;
    private stmtSeqNode STMTSEQ2;
    private int kind = 0;

    public void parsing() {
        //System.out.println("Parsing ifNode!");
        Tokenizer.checkToken("if"); //first token is if
        Tokenizer.skipToken();
        this.COND = new condNode();
        this.COND.parsing();
        Tokenizer.checkToken("then");
        Tokenizer.skipToken();
        this.STMTSEQ1 = new stmtSeqNode();
        this.STMTSEQ1.parsing();
        if (Tokenizer.getToken().equals("else")) { //alternative is if,then,else,end
            this.kind = 1; //alternative is if,then,else
            Tokenizer.skipToken();
            this.STMTSEQ2 = new stmtSeqNode();
            this.STMTSEQ2.parsing();
        }
        if (Tokenizer.getToken().equals("end")) {
            Tokenizer.skipToken(); //skips end token
            Tokenizer.checkToken(";");
            Tokenizer.skipToken();

        } else {
            System.out.println("Error in ifNode, expected 'else','end', got: " + Tokenizer.getToken());
            System.exit(1);

        }
    }

    public void printing() {
        if (this.kind == 0) { //if,then,end
            System.out.print("if ");
            this.COND.printing();
            System.out.print("then ");
            this.STMTSEQ1.printing();
        } else { //if,then,else,end
            System.out.print("if ");
            this.COND.printing();
            System.out.print("\n" + "then ");
            this.STMTSEQ1.printing();
            System.out.print("\n" + "else ");
            this.STMTSEQ2.printing();
            System.out.print("\n" + "end; " + "\n");
        }
    }

    public void executing() {
        //System.out.println("Executing ifNode!");
        Tokenizer.skipToken(); //skip 'if'
        this.COND.executing();
        Tokenizer.skipToken();//skip 'then'
        if (this.kind == 0) {
            if (this.COND.getCondVal()) {
                this.STMTSEQ1.executing();
            }

        } else {
            if (this.COND.getCondVal()) {
                this.STMTSEQ1.executing();
            } else {
                this.STMTSEQ2.executing();
            }
        }

    }

}

class loopNode { //loop node: 'while' cond 'loop' stmtseq 'end'';'
    private condNode COND;
    private stmtSeqNode STMTSEQ;

    public void parsing() {
        //System.out.println("Parsing loopNode!");
        Tokenizer.checkToken("while");
        Tokenizer.skipToken();
        this.COND = new condNode();
        this.COND.parsing();
        Tokenizer.checkToken("loop");
        Tokenizer.skipToken();
        this.STMTSEQ = new stmtSeqNode();
        this.STMTSEQ.parsing();
        Tokenizer.checkToken("end");
        Tokenizer.skipToken();
        Tokenizer.checkToken(";");
        Tokenizer.skipToken();

    }

    public void printing() {
        System.out.print("while ");
        this.COND.printing();
        System.out.print("\n" + "loop ");
        this.STMTSEQ.printing();
        System.out.print("end; " + "\n");

    }

    public void executing() {
        //System.out.println("Executing loopNode!");
        Boolean condVal1 = false;
        this.COND.executing();
        condVal1 = this.COND.getCondVal();
        while (condVal1) {

            //System.out.println("VALUE OF COND: " + this.COND.getCondVal());
            this.STMTSEQ.executing();
            this.COND.executing();
            condVal1 = this.COND.getCondVal();
        }

    }

}

class inNode { //in Node: 'read' idList ';'
    private idListNode IDLIST;
    private List<String> idLinkedList = new LinkedList<String>();

    public void parsing() {
        //System.out.println("Parsing inNode!");
        Tokenizer.checkToken("read");
        Tokenizer.skipToken();
        this.IDLIST = new idListNode();
        this.IDLIST.parsing();
        Tokenizer.checkToken(";");
        Tokenizer.skipToken();

    }

    public void printing() {
        System.out.print("read ");
        this.IDLIST.printing();
        System.out.print("; " + "\n");

    }

    public void executing() {
        //System.out.println("Executing inNode!");
        int counts = 0;
        this.idLinkedList = this.IDLIST.getIDList();
        while (counts == 0 || counts < this.idLinkedList.size()) {//while it hasn't looped once, or counter<number of IDs

            if (Main.inputVarsList.size() > 0) { //needs to be a value to read in
                if (varHashMap.var.containsKey(this.idLinkedList.get(counts))) {//ID must have been declared before
                    //System.out.println("This is the inputVar! ::" + Main.inputVarsList.get(counts));

                    int intVar = Integer.parseInt(Main.inputVarsList.get(counts));//sets string to int
                    varHashMap.var.put(this.idLinkedList.get(counts), intVar);
                } else {
                    System.out.println("Error in inNode, ID: " + this.IDLIST.getfirstID() + " not declared");
                    System.exit(1);
                }
            } else {
                System.out.println("Error in inNode, no input value for token " + this.IDLIST.getfirstID());
                System.exit(1);
            }
            counts++;
            Main.inputVarsList.remove(0); //removes the
        }
    }

}

class outNode { //out Node: 'write' idList ';'
    private idListNode IDLIST;
    private List<String> idLinkedList = new LinkedList<String>();

    public void parsing() {
        //System.out.println("Parsing outNode!");
        Tokenizer.checkToken("write");
        Tokenizer.skipToken();
        this.IDLIST = new idListNode();
        this.IDLIST.parsing();
        Tokenizer.checkToken(";");
        Tokenizer.skipToken();

    }

    public void printing() {
        System.out.print("write ");
        this.IDLIST.printing();
        System.out.print("; " + "\n");
    }

    public void executing() {
        //System.out.println("Executing outNode!");
        this.idLinkedList = this.IDLIST.getIDList();
        int counts = 0;
        while (counts == 0 || counts < this.idLinkedList.size()) {//while it hasn't looped once, or while there are still vars left in list

            if (varHashMap.var.containsKey(this.idLinkedList.get(counts))) { //if ID has been declared
                if (varHashMap.var.get(this.idLinkedList.get(counts)) != null) {//ID has a non-null value
                    System.out.print(this.idLinkedList.get(counts) + " = "
                            + varHashMap.var.get(this.idLinkedList.get(counts)) + "; " + "\n");

                } else {
                    System.out.println("Error in outNode, ID: " + this.idLinkedList.get(counts) + " has null value");
                    System.exit(1);
                }
            } else {
                System.out.println("Error in outNode, no ID " + this.idLinkedList.get(counts) + " declared");
                System.exit(1);
            }
            counts++;

        }
    }

}

class condNode { //cond Node: comp | '!'cond | '['cond'&&'cond']' | '['cond'or'cond']'
    private compNode COMP;
    private condNode COND1;
    private condNode COND2;
    private boolean condVal = false;
    private int kind = 0;

    public void parsing() {
        //System.out.println("Parsing condNode!");
        //System.out.println("Next token in condNode is: "+Tokenizer.getToken());
        String token = Tokenizer.getToken();
        if (token.equals("(")) { //comp, since comps are (...)

            this.kind = 0;
            this.COMP = new compNode();
            this.COMP.parsing();

        } else if (token.equals("!")) { // !cond
            this.kind = 1;
            Tokenizer.skipToken();
            this.COND1 = new condNode();
            this.COND1.parsing();
        } else if (token.equals("[")) { //&& or or node

            this.COND1 = new condNode();
            this.COND1.parsing();
            if (Tokenizer.getToken().equals("&&")) {
                this.kind = 2;
                Tokenizer.skipToken();
                this.COND2 = new condNode();
                this.COND2.parsing();
            } else if (Tokenizer.getToken().equals("or")) {
                this.kind = 3;
                Tokenizer.skipToken();
                this.COND2 = new condNode();
                this.COND2.parsing();
            } else {
                System.out.println("Error in condNode, expected 'or', '&&', got: " + Tokenizer.getToken());
                System.exit(1);
            }
        }

    }

    public void printing() {
        if (this.kind == 0) { //comp
            this.COMP.printing();
        } else if (this.kind == 1) {//!cond
            System.out.print(" !");
            this.COND1.printing();
        } else if (this.kind == 2) {// [cond&&cond]]
            System.out.print(" [");
            this.COND1.printing();
            System.out.print(" && ");
            this.COND2.printing();
            System.out.print("] ");
        } else {//[cond or cond]
            System.out.print(" [");
            this.COND1.printing();
            System.out.print(" or ");
            this.COND2.printing();
            System.out.print("] ");
        }
    }

    public void executing() {
        //System.out.println("Executing condNode!");
        if (this.kind == 0) {//comp
            this.COMP.executing();
            this.condVal = this.COMP.getCompVal();
        } else if (this.kind == 1) {//!
            this.COND1.executing();
            this.condVal = (!this.COND1.getCondVal());
        } else if (this.kind == 2) {//&&
            this.COND1.executing();
            this.COND2.executing();
            if (this.COND1.getCondVal() && this.COND2.getCondVal()) {
                this.condVal = true;
            } else {
                this.condVal = false;
            }

        } else {// or
            this.COND1.executing();
            this.COND2.executing();
            if (this.COND1.getCondVal() || this.COND2.getCondVal()) {
                this.condVal = true;
            } else {
                this.condVal = false;
            }

        }

    }

    public boolean getCondVal() {
        return this.condVal;
    }

}

class compNode { //comp Node: '(' op compOp op ')'
    private opNode OP1;
    private compOpNode COMPOP;
    private opNode OP2;
    private boolean compVal = false;

    public void parsing() {
        //System.out.println("Parsing compNode!");
        Tokenizer.checkToken("(");
        Tokenizer.skipToken();
        this.OP1 = new opNode();
        this.OP1.parsing();
        this.COMPOP = new compOpNode();
        this.COMPOP.parsing();
        this.OP2 = new opNode();
        this.OP2.parsing();
        Tokenizer.checkToken(")");
        Tokenizer.skipToken();

    }

    public void printing() {
        System.out.print(" (");
        this.OP1.printing();
        this.COMPOP.printing();
        this.OP2.printing();
        System.out.print(") ");

    }

    public void executing() {
        //System.out.println("Executing compNode!");
        this.OP1.executing();
        this.OP2.executing();
        this.COMPOP.executing();
        if (this.COMPOP.getCompOpVal().equals("=")) {
            if (this.OP1.getOpVal() == this.OP2.getOpVal()) {
                this.compVal = true;
            } else {
                this.compVal = false;
            }
        } else if (this.COMPOP.getCompOpVal().equals("!=")) {
            if (this.OP1.getOpVal() != this.OP2.getOpVal()) {
                this.compVal = true;
            } else {
                this.compVal = false;
            }
        } else if (this.COMPOP.getCompOpVal().equals("==")) {
            if (this.OP1.getOpVal() == this.OP2.getOpVal()) {
                this.compVal = true;
            } else {
                this.compVal = false;
            }
        } else if (this.COMPOP.getCompOpVal().equals("<")) {
            if (this.OP1.getOpVal() < this.OP2.getOpVal()) {
                this.compVal = true;
                //System.out.println("OP1: " + this.OP1.getOpVal() + "  OP2: " + this.OP2.getOpVal());
            } else {
                this.compVal = false;
            }
        } else if (this.COMPOP.getCompOpVal().equals(">")) {
            if (this.OP1.getOpVal() > this.OP2.getOpVal()) {
                this.compVal = true;
                //System.out.println("OP1: " + this.OP1.getOpVal() + "  OP2: " + this.OP2.getOpVal());
            } else {
                this.compVal = false;
            }
        } else if (this.COMPOP.getCompOpVal().equals("<=")) {
            if (this.OP1.getOpVal() <= this.OP2.getOpVal()) {
                this.compVal = true;
            } else {
                this.compVal = false;
            }
        } else if (this.COMPOP.getCompOpVal().equals(">=")) {
            if (this.OP1.getOpVal() >= this.OP2.getOpVal()) {
                this.compVal = true;
            } else {
                this.compVal = false;
            }
        }

    }

    public boolean getCompVal() {
        return this.compVal;
    }

}

class expNode { //exp Node: fac | fac '+' exp | fac '-' exp
    private facNode FAC;
    private expNode EXP;
    private int kind = 0;
    private int expVal = 0;

    public void parsing() {
        //System.out.println("Parsing expNode!");
        this.FAC = new facNode();
        this.FAC.parsing();
        if (Tokenizer.getToken().equals("+")) {
            Tokenizer.skipToken();
            this.kind = 1;
            this.EXP = new expNode();
            this.EXP.parsing();

        } else if (Tokenizer.getToken().equals("-")) {
            Tokenizer.skipToken();
            this.kind = 2;
            this.EXP = new expNode();
            this.EXP.parsing();
        }

    }

    public void printing() {
        if (this.kind == 0) {
            this.FAC.printing();
        } else if (this.kind == 1) {
            this.FAC.printing();
            System.out.print(" + ");
            this.EXP.printing();
        } else {
            this.FAC.printing();
            System.out.print(" - ");
            this.EXP.printing();
        }

    }

    public void executing() {
        //System.out.println("Executing expNode!");
        if (this.kind == 0) {
            this.FAC.executing();
            this.expVal = this.FAC.getFacVal();
        } else if (this.kind == 1) {
            this.FAC.executing();
            this.expVal = this.FAC.getFacVal();
            Tokenizer.skipToken();//skips '+'
            this.EXP.executing();
            this.expVal = this.expVal + this.EXP.getExpVal();

        } else if (this.kind == 2) {
            this.FAC.executing();
            this.expVal = this.FAC.getFacVal();
            Tokenizer.skipToken();//skips '-'
            this.EXP.executing();
            this.expVal = this.expVal - this.EXP.getExpVal();
        }

    }

    public int getExpVal() {
        return this.expVal;
    }

}

class facNode { //fac Node: op | op '*' fac
    private opNode OP;
    private facNode FAC;
    private int kind = 0;
    private int facVal = 0;

    public void parsing() {
        //System.out.println("Parsing facNode!");
        this.OP = new opNode();
        this.OP.parsing();
        if (Tokenizer.getToken().equals("*")) {
            Tokenizer.skipToken();
            this.kind = 1;
            this.FAC = new facNode();
            this.FAC.parsing();
        }

    }

    public void printing() {
        if (this.kind == 0) {
            this.OP.printing();
        } else {
            this.OP.printing();
            System.out.print(" * ");
            this.FAC.printing();
        }

    }

    public void executing() {
        //System.out.println("Executing facNode!");
        if (this.kind == 0) {
            this.OP.executing();
            this.facVal = this.OP.getOpVal();
        } else {
            this.OP.executing();
            this.facVal = this.OP.getOpVal();
            Tokenizer.skipToken();//skips '*'
            this.FAC.executing();
            this.facVal = this.facVal + this.FAC.getFacVal();
        }

    }

    public int getFacVal() {
        return this.facVal;
    }

}

class opNode { //op node: int | id | '('exp')'
    private intNode INT;
    private idNode ID;
    private expNode EXP;
    private int kind = 0;
    private int opVal = 0;

    public void parsing() {
        //System.out.println("Parsing opNode!");
        if (Tokenizer.isTokenID()) {
            this.kind = 1;
            this.ID = new idNode();
            this.ID.parsing();
        } else if (Tokenizer.getToken().equals("(")) {
            Tokenizer.skipToken();
            this.kind = 2;
            this.EXP = new expNode();
            this.EXP.parsing();
            Tokenizer.checkToken(")");
            Tokenizer.skipToken();
        } else {
            this.kind = 0;
            this.INT = new intNode();
            this.INT.parsing();

        }

    }

    public void printing() {
        if (this.kind == 0) {
            this.INT.printing();
        } else if (this.kind == 1) {
            this.ID.printing();
        } else {
            System.out.print(" (");
            this.EXP.printing();
            System.out.print(") ");
        }

    }

    public void executing() {
        //System.out.println("Executing opNode!");
        if (this.kind == 0) {
            this.INT.executing();
            this.opVal = this.INT.getInt();
        } else if (this.kind == 1) {
            this.ID.executing(0); //0, since ID did NOT come from decl
            this.opVal = varHashMap.var.get(this.ID.getID()); //get val if ID from hashmap
        } else if (this.kind == 2) {
            Tokenizer.skipToken();//skips '('
            this.EXP.executing();
            this.opVal = this.EXP.getExpVal();
            Tokenizer.skipToken(); //skip ")"
        }

    }

    public int getInt() {
        return this.INT.getInt();
    }

    public int getOpVal() {
        return this.opVal;
    }

}

class compOpNode { //compOp node: '=' | '!=' | '==' | '<' | '>' | '<=' | '>='
    private int kind = 0; //alternative type, start with '='
    private String compOpVal = " ";

    public void parsing() {
        //System.out.println("Parsing compOpNode!");
        String nextToken = Tokenizer.getToken();
        if (nextToken.equals("=")) {

        } else if (nextToken.equals("!=")) {
            this.kind = 1;
            Tokenizer.skipToken();
        } else if (nextToken.equals("==")) {
            this.kind = 2;
            Tokenizer.skipToken();
        } else if (nextToken.equals("<")) {
            this.kind = 3;
            Tokenizer.skipToken();
        } else if (nextToken.equals(">")) {
            this.kind = 4;
            Tokenizer.skipToken();
        } else if (nextToken.equals("<=")) {
            this.kind = 5;
            Tokenizer.skipToken();
        } else if (nextToken.equals(">=")) {
            this.kind = 6;
            Tokenizer.skipToken();
        } else {
            System.out.println("Error in compOpNode, expected comp op, got: " + Tokenizer.getToken());
            System.exit(1);
        }

    }

    public void printing() {
        if (this.kind == 0) {
            System.out.print(" = ");
        } else if (this.kind == 1) {
            System.out.print(" != ");
        } else if (this.kind == 2) {
            System.out.print(" == ");
        } else if (this.kind == 3) {
            System.out.print(" < ");
        } else if (this.kind == 4) {
            System.out.print(" > ");
        } else if (this.kind == 5) {
            System.out.print(" <= ");
        } else if (this.kind == 6) {
            System.out.print(" >= ");
        }

    }

    public void executing() {
        //System.out.println("Executing compOpNode!");
        if (this.kind == 0) {
            this.compOpVal = "=";
        } else if (this.kind == 1) {
            this.compOpVal = "!=";
        } else if (this.kind == 2) {
            this.compOpVal = "==";
        } else if (this.kind == 3) {
            this.compOpVal = "<";
        } else if (this.kind == 4) {
            this.compOpVal = ">";
        } else if (this.kind == 5) {
            this.compOpVal = "<=";
        } else if (this.kind == 6) {
            this.compOpVal = ">=";
        }
    }

    public String getCompOpVal() {
        return this.compOpVal;
    }

}

class idNode { //id Node: let | let id | let int
    private String ID1;

    public void parsing() {
        //System.out.println("Parsing idNode!");
        this.ID1 = Tokenizer.getToken();
        Tokenizer.skipToken();
        //System.out.println("ID found, it is: "+ID1);
    }

    public void printing() {
        System.out.print(this.ID1);

    }

    public void executing(int x) {
        //System.out.println("Executing idNode!");
        if (x == 1) {//ID came from a declstmt, so can initiate it.
            if (!varHashMap.var.containsKey(this.ID1)) { //if the var hasn't been declared
                varHashMap.var.put(this.ID1, -1); //-1 is default value, in place of NULL.

            } else {
                System.out.println("Error in idNode, tried to add " + this.ID1 + " to VARS, already exists.");
                System.exit(1);
            }
        } else {//ID came from elsewhere, CANNOT initiate, must check it is there.
            if (!varHashMap.var.containsKey(this.ID1)) {
                System.out.println("Error in idNode, var " + this.ID1 + " not declared.");
                System.exit(1);
            }
        }

    }

    public String getID() {
        return this.ID1;
    }

}

/*
 * //Not sure if I still need this// class letNode{ //let Node:
 * 'A'|'B'|.....|'Z'
 *
 * public void parsing() {
 *
 * } public void printing() {
 *
 * } public void executing() {
 *
 * }
 *
 *
 *
 * }
 */
class intNode { //int node: digit | digit int
    private int INT1;
    private String intString;

    public void parsing() {
        //System.out.println("Parsing intNode!");
        this.intString = Tokenizer.getToken();
        int x = 0;
        while (x < this.intString.length()) { //for every char in the intString
            if (!Character.isDigit(this.intString.charAt(x))) { //if the char is NOT a digit
                System.out.println("Error in intNode, expected int, got: " + Tokenizer.getToken());
                System.exit(1);
            } else {
                this.INT1 = Integer.parseInt(this.intString);
                //System.out.print("  "+INT1+"  ");
            }
            x++;
        }
        Tokenizer.skipToken(); //skips the int when finished.

    }

    public void printing() {
        System.out.print(this.INT1);
    }

    public void executing() {
        //System.out.println("Executing intNode!");

    }

    public int getInt() {
        return this.INT1;
    }

}
/*
 * class digitNode{ //digit node: 0|1|....|9
 *
 * public void parsing() {
 *
 * } public void printing() {
 *
 * } public void executing() {
 *
 * }
 *
 *
 *
 * }
 */
