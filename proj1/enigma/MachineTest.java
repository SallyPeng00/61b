package enigma;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class MachineTest {
    @Test
    public void intConvertTest() {
        AllRotors all1 = new AllRotors();
        all1.add(rotorI);
        all1.add(rotorII);
        all1.add(rotorIII);
        all1.add(rotorIV);
        all1.add(rotorV);
        all1.add(rotorVI);
        all1.add(rotorBeta);
        all1.add(reflectorB);
        String[] rotorString = new String[]
            {"Reflector B", "Rotor Beta", "Rotor III", "Rotor IV", "Rotor I"};
        Collection<Rotor> rotorList = all1.getAllRotors();
        Machine testMachine = new Machine(new Alphabet(), 5, 3, rotorList);
        testMachine.insertRotors(rotorString);
        testMachine.setRotors("AXLE");
        testMachine.setPlugboard(new Permutation("(YF) (ZH)", new Alphabet()));
        int actual = testMachine.convert(24);
        assertEquals(25, actual);
    }

    @Test
    public void stringConvertTest() {
        AllRotors all1 = new AllRotors();
        all1.add(rotorI);
        all1.add(rotorII);
        all1.add(rotorIII);
        all1.add(rotorIV);
        all1.add(rotorV);
        all1.add(rotorVI);
        all1.add(rotorBeta);
        all1.add(reflectorB);
        String[] rotorString = new String[]
            {"Reflector B", "Rotor Beta", "Rotor I", "Rotor II", "Rotor III"};
        Collection<Rotor> rotorList = all1.getAllRotors();
        Machine testMachine = new Machine(new Alphabet(), 5, 3, rotorList);
        testMachine.insertRotors(rotorString);

        String actual = "ILBDAAMTAZ";
        assertEquals(actual, testMachine.convert("HELLO WORLD"));

        testMachine.setRotors("AAAA");
        testMachine.setPlugboard(new Permutation("(AQ) (EP)", new Alphabet()));
        String actual1 = "IHBDQQMTQZ";
        assertEquals(actual1, testMachine.convert("HELLO WORLD"));

        testMachine.setRotors("AAAA");
        String actual2 = "HELLOWORLD";
        assertEquals(actual2, testMachine.convert("IHBDQ QMTQZ"));


    }

    MovingRotor rotorI = new MovingRotor("Rotor I",
            new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) "
                    + "(IV) (JZ) (S)", new Alphabet()), "Q");
    MovingRotor rotorII = new MovingRotor("Rotor II",
            new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
                    + "(GR) (NT) (A) (Q)",
                    new Alphabet()), "E");
    MovingRotor rotorIII = new MovingRotor("Rotor III",
            new Permutation("(ABDHPEJT) "
                    + "(CFLVMZOYQIRWUKXSG) (N)",
                    new Alphabet()), "V");
    MovingRotor rotorIV = new MovingRotor("Rotor IV",
            new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)",
                    new Alphabet()), "J");
    MovingRotor rotorV = new MovingRotor("Rotor V",
            new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)",
                    new Alphabet()), "Z");
    MovingRotor rotorVI = new MovingRotor("Rotor VI",
            new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)",
                    new Alphabet()), "ZM");
    FixedRotor rotorBeta = new FixedRotor("Rotor Beta",
            new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)",
                    new Alphabet()));
    Reflector reflectorB = new Reflector("Reflector B",
            new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) "
                    + "(LO) (MP) (RX) (SZ) (TV)", new Alphabet()));


}
