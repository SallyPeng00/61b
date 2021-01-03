package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * stage.
 *
 * @author Sally Peng
 */
public class Stage implements Serializable {

    /** init. */
    Stage() {
        a = new HashMap<>();
        r = new HashSet<>();
    }

    /**
     * add.
     * @param f file to be staged.
     */
    void add(File f) {
        a.entrySet()
                .removeIf(
                    entry -> (f.equals(entry.getValue())));
        a.put(f.getName(), Utils.readContents(f));
        r.remove(f);
    }

    /**
     * remove add.
     * @param f file to be unstaged.
     */
    void remove(File f) {
        a.remove(f.getName());
    }

    /**
     * remove.
     * @param f file to be deleted.
     */
    void markRemove(File f) {
        r.add(f);
    }

    /**
     * remove.
     * @param f file to be un-deleted.
     */
    void unmarkRemove(File f) {
        r.remove(f);
    }

    /**
     * list of all files to be added.
     * @return list
     */
    List<String> allFiles() {
        List<String> toReturn = new ArrayList<>(a.keySet());
        return toReturn;
    }

    /**
     * list of all file contents.
     * @return list
     */
    List<byte[]> allFContents() {
        List<byte[]> toReturn = new ArrayList<>();
        for (String f : allFiles()) {
            toReturn.add(a.get(f));
        }
        return toReturn;
    }

    /**
     * list of all files to be removed.
     * @return list
     */
    List<File> toRemove() {
        List<File> toReturn = new ArrayList<>(r);
        return toReturn;
    }

    /**
     * list of files to remove.
     * @return file names staged for removal.
     */
    List<String> toRemoveNames() {
        List<File> toRemove = toRemove();
        List<String> names = new ArrayList<>();
        for (File f : toRemove) {
            names.add(f.getName());
        }
        return names;
    }

    /** dump all files, clears stage.*/
    void empty() {
        a.clear();
        r.clear();
    }

    /**
     * check if stage is empty.
     * @return empty
     */
    boolean isEmpty() {
        return a.isEmpty() && r.isEmpty();
    }

    /**
     * size of to be added.
     * @return size.
     */
    int size() {
        return a.size();
    }

    /**
     * Returns if a file is already in staging area.
     * @param file file name
     * @return true/false
     */
    boolean contains(File file) {
        if (a.keySet().contains(file.getName())) {
            return true;
        }
        return false;
    }

    /**
     * return the contents of a file in stage.
     * @param fName file name
     * @return the content of that file
     */
    byte[] getContents(String fName) {
        return a.get(fName);
    }

    /**
     * all files waiting to be added.
     * @return all staged files.
     */
    HashMap<String, byte[]> stageFiles() {
        return a;
    }

    /** Maps FILE name contents to FILE.*/
    private HashMap<String, byte[]> a;

    /** Files waiting to be removed.*/
    private HashSet<File> r;

}
