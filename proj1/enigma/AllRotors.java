package enigma;

import java.util.ArrayList;

/**
 * helper class for MachineTest.
 * @author Sally Peng
 */
public class AllRotors {
    /**
     * list of all possible rotors.
     */
    private ArrayList<Rotor> allRotors = new ArrayList<>();

    /**
     * add rotors to allRotors.
     * @param rotor rotors
     */
    public void add(Rotor rotor) {
        allRotors.add(rotor);
    }

    /**
     * get all possible rotors.
     * @return all possible rotors
     */
    public ArrayList<Rotor> getAllRotors() {
        return allRotors;
    }
}
