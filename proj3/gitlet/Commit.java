package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Commit class.
 * @author Sally Peng
 */
public class Commit implements Serializable {

    /**
     * init.
     * @param message commit message
     */
    Commit(String message) {
        this._message = message;
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        time  = "Date: " + dateFormat.format(new Date());
        this.sha1 = Utils.sha1("commit", Utils.serialize(this));
        map = new HashMap<>();
    }

    /**
     * sha 1 of this commit.
     * @return sha1
     */
    String sha1() {
        return this.sha1;
    }

    /**
     * set a parent.
     * @param p1 parent commit
     */
    void setParent1(String p1) {
        this.p1sha1 = p1;
    }

    /**
     * set a parent.
     * @param p2 parent commit
     */
    void setParent2(String p2) {
        this.p2sha1 = p2;
    }

    /** set a time, only for initial commit.*/
    void setTime() {
        this.time = "Date: Wed Dec 31 16:00:00 1969 -0800";
    }

    /**
     * get time.
     * @return time stamp.
     */
    String getTime() {
        return this.time;
    }

    /**
     * parent sha.
     * @return parent sha
     */
    String getP1sha1() {
        return this.p1sha1;
    }

    /**
     * parent sha.
     * @return parent sha
     */
    String getP2sha1() {
        return this.p2sha1;
    }

    /**
     * set blob.
     * @param file tracked file in this commit.
     */
    void setBlob(File file) {
        byte[] contents = Utils.readContents(file);
        map.put(file.getName(), Utils.sha1(file.getName(), contents));
    }

    /**
     * set blob by passing in file info separately.
     * @param fName file name
     * @param contents contents, usually read from blobs
     */
    void setBlob(String fName, byte[] contents) {
        map.put(fName, Utils.sha1(fName, contents));
    }

    /**
     * get message for this commit.
     * @return message
     */
    String getMessage() {
        return this._message;
    }

    /**
     * maps between file name to blob sha1.
     * @return map
     */
    HashMap<String, String> getMap() {
        return this.map;
    }

    /**
     * branching.
     * @param bName branch name
     */
    void setBranch(String bName) {
        this.branch = bName;
    }

    /**
     * get branch of this commit.
     * @return branch name.
     */
    String getBranch() {
        return this.branch;
    }

    /**Maps the FILE_NAME to the SHA1 value of that file's content. */
    private HashMap<String, String> map;

    /** parent1 sha.*/
    private String p1sha1 = null;

    /** parent2 sha.*/
    private String p2sha1 = null;

    /** sha.*/
    private String sha1;

    /** time.*/
    private String time;

    /** message.*/
    private String _message;

    /**branch.*/
    private String branch;


}
