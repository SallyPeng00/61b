package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.error;

/**
 * Enigma simulator.
 *
 * @author Sally Peng
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {

            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine enigma = readConfig();
        String msg = "";
        String setting = "";
        String nextLine = _input.nextLine();
        if (!nextLine.contains("*")) {
            throw new EnigmaException("Invalid input, must start with \'*\'");
        }

        while (nextLine != null) {
            if (nextLine.contains("*")) {
                setting = nextLine;
                setUp(enigma, setting);
            } else {
                msg = enigma.convert(nextLine);
                if (nextLine.equals("")) {
                    _output.println();
                } else {
                    printMessageLine(msg);
                }
            }
            if (_input.hasNextLine()) {
                nextLine = _input.nextLine();
            } else {
                break;
            }
        }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            ArrayList<Rotor> allRotors = new ArrayList<>();
            _config.nextLine();
            if (_config.hasNext()) {
                nextName = _config.next();
            } else {
                throw new EnigmaException("No rotors present");
            }
            while (_config.hasNext(".*")) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String name = nextName;
            String rotorType = _config.next();
            if (rotorType == null || rotorType.startsWith("(")) {
                throw new EnigmaException("No rotor type");
            }
            String cycles = "";

            String temp = _config.next();
            if (!temp.startsWith("(")) {
                nextName = temp;
            }

            while (temp.startsWith("(") && _config.hasNext()) {
                cycles += temp + " ";
                temp = _config.next();
                nextName = temp;
            }
            if (!_config.hasNext()) {
                cycles += temp;
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (rotorType.charAt(0) == 'M') {
                return new MovingRotor(name, perm, rotorType.substring(1));
            } else if (rotorType.charAt(0) == 'N') {
                return new FixedRotor(name, perm);
            } else {
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        String[] rotors = new String[M.numRotors()];
        Scanner setting = new Scanner(settings);
        setting.next();
        for (int i = 0; i < M.numRotors(); i++) {
            String newRotor = setting.next();
            rotors[i] = newRotor;
        }
        M.insertRotors(rotors);
        if (!setting.hasNext()) {
            throw new EnigmaException("no rotor setting available");
        }
        String setRotor = setting.next();
        if (setRotor.equals("") || setRotor.startsWith("(")) {
            throw new EnigmaException("no rotor setting available");
        }
        String next = "";
        String plugboard = "";
        if (setting.hasNext()) {
            next = setting.next();
        }
        if (!next.equals("") && !next.startsWith("(")) {
            setRotor = setRotor.concat(" " + next);
        } else if (!next.equals("")) {
            plugboard = plugboard.concat(next + " ");
        }
        while (setting.hasNext()) {
            plugboard = plugboard.concat(setting.next() + " ");
        }
        M.setRotors(setRotor);
        if (plugboard.length() != 0) {
            M.setPlugboard(new Permutation(plugboard, _alphabet));
        } else {
            M.setPlugboard(new Permutation("", _alphabet));
        }


    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            if (i + 5 < msg.length()) {
                _output.print(msg.substring(i, i + 5) + " ");
            } else {
                _output.println(msg.substring(i));
            }
        }
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;

    /**
     * Added: store the name of the next rotor, for readRotors.
     */
    private String nextName = "";
}
