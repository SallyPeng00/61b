package gitlet;


import java.io.File;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * repo class.
 *
 * @author Sally Peng
 */
public class Repo {

    /**
     * repo object contains stage.
     */
    Repo() {
        stage = new Stage();
    }

    /**
     * init.
     */
    void init() {
        initial = true;
        this.commit("initial commit");
    }

    /**
     * add file to stage.
     *
     * @param file file to stage.
     */
    void add(File file) {
        if (stage.toRemove().contains(file)) {
            stage.unmarkRemove(file);
            return;
        }
        String sha1 = Utils.sha1(file.getName(), rC(file));
        if (!lFNames(tracked).contains(file.getName())
                || !stage.allFContents().contains(rC(file))) {
            if (_head.getMap().containsValue(sha1)) {
                return;
            }
            stage.add(file);
            Utils.writeContents(Utils.join(tracked, file.getName()));
        }
    }

    /**
     * commit.
     *
     * @param message input user message
     */
    void commit(String message) {
        if (stage.size() == 0 && stage.toRemove().size() == 0 && !initial) {
            throw new GitletException("No changes added to the commit.");
        }
        if (stage.allFiles() != null) {
            for (String f : stage.allFiles()) {
                Utils.writeContents(Utils.join(blobs,
                        Utils.sha1(f, stage.getContents(f))),
                        stage.getContents(f));
            }
        }

        Commit c;
        if (message.equals("initial commit") && initial) {
            c = new Commit("initial commit");
            c.setTime();
            c.setBranch("master");
            initial = false;
        } else {
            File hEAD = Utils.join(gitlet, "//HEAD.txt");
            String prevC = Utils.readContentsAsString(hEAD);
            c = new Commit(message);
            c.setParent1(prevC);

            c.setBranch(_head.getBranch());
            if (stage.allFiles() != null) {
                for (String f : stage.allFiles()) {
                    c.setBlob(f, stage.getContents(f));
                }
            }
            Commit pCommit = readCom(prevC);
            for (String fName : pCommit.getMap().keySet()) {
                if (!stage.allFiles().contains(fName)
                        && !stage.toRemoveNames().contains(fName)) {
                    String shaF = pCommit.getMap().get(fName);
                    File f = Utils.join(blobs, shaF);
                    File temp = Utils.join(gitlet, fName);
                    Utils.writeContents(temp, Utils.readContents(f));
                    c.setBlob(temp);
                    temp.delete();
                }
            }
            for (File f : stage.toRemove()) {
                File f2 = Utils.join(tracked, f.getName());
                f2.delete();
            }
        }
        Utils.writeObject(Utils.join(commits, c.sha1()), c);
        _head = c;
        File bHead = Utils.join(branchH, _head.getBranch());
        Utils.writeObject(bHead, c);
        stage.empty();
    }

    /**
     * rm.
     *
     * @param file file to remove
     */
    void rm(File file) {
        String fName = file.getName();
        if (!stage.contains(file)
                && !lFNames(tracked).contains(fName)) {
            throw new GitletException("No reason to remove the file.");
        }

        if (stage.contains(file)) {
            stage.remove(file);
            File f = Utils.join(tracked, file.getName());
            f.delete();
        }
        if (_head.getMap().containsKey(fName)) {
            stage.markRemove(file);
            File f = Utils.join(cDir, fName);
            if (f.exists()) {
                Utils.restrictedDelete(f);
            }
        }
    }

    /**
     * log.
     */
    void log() {
        Commit c = _head;
        if (c != null) {
            System.out.println("===");
            System.out.print("commit ");
            System.out.println(c.sha1());
            System.out.println(c.getTime());
            System.out.println(c.getMessage());
            System.out.println();
            while (c.getP1sha1() != null) {
                String psha = c.getP1sha1();
                File parentC = Utils.join(commits, psha);
                c = Utils.readObject(parentC, Commit.class);
                System.out.println("===");
                System.out.print("commit ");
                System.out.println(c.sha1());
                System.out.println(c.getTime());
                System.out.println(c.getMessage());
                System.out.println();
            }
        }
    }

    /**
     * global log.
     */
    void globalLog() {
        for (String f : lFNames(commits)) {
            Commit c = readCom(f);
            System.out.println("===");
            System.out.print("commit ");
            System.out.println(c.sha1());
            System.out.println(c.getTime());
            System.out.println(c.getMessage());
            System.out.println();

        }
    }

    /**
     * find commits w/ that message.
     *
     * @param message message
     */
    void find(String message) {
        HashSet<Commit> toPrint = null;
        if (commits.length() != 0) {
            toPrint = new HashSet<>();
            for (String f : lFNames(commits)) {
                Commit c = readCom(f);
                if (c.getMessage().equals(message)) {
                    toPrint.add(c);
                }
            }
        }
        if (toPrint == null || toPrint.size() == 0) {
            throw new GitletException("Found no commit with that message.");
        }
        for (Commit c : toPrint) {
            System.out.println(c.sha1());
        }
    }

    /**
     * status.
     */
    void status() {
        System.out.println("=== Branches ===");
        List<String> branches = new ArrayList<>(lFNames(branchH));
        Collections.sort(branches, String.CASE_INSENSITIVE_ORDER);
        for (String bName : branches) {
            if (bName.equals(_head.getBranch())) {
                System.out.println("*" + bName);
            } else {
                System.out.println(bName);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        List<String> stagedFiles = new ArrayList<>();
        stagedFiles.addAll(stage.allFiles());
        Collections.sort(stagedFiles, String.CASE_INSENSITIVE_ORDER);
        for (String fName : stagedFiles) {
            System.out.println(fName);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        List<String> rmFiles = new ArrayList<>();
        for (File f : stage.toRemove()) {
            rmFiles.add(f.getName());
        }
        Collections.sort(rmFiles, String.CASE_INSENSITIVE_ORDER);
        for (String fName : rmFiles) {
            System.out.println(fName);
        }
        System.out.println();
        status2();
    }

    /**
     * status2.
     */
    private void status2() {
        List<String> stagedFiles = stage.allFiles();
        System.out.println("=== Modifications Not Staged For Commit ===");
        List<String> fNames = new ArrayList<>();
        HashMap<String, String> m = _head.getMap();
        for (String pFName : _head.getMap().keySet()) {
            File f = Utils.join(cDir, pFName);
            if ((!stage.toRemove().contains(f)
                    && !lFNames(cDir).contains(pFName))
                    && lFNames(tracked).contains(pFName)) {
                fNames.add(pFName + " (deleted)");
            } else if (Utils.join(cDir, pFName).exists()
                    && !m.get(pFName).equals(Utils.sha1(pFName,
                    Utils.readContents(Utils.join(cDir, pFName))))
                    && !stage.allFiles().contains(pFName)) {
                fNames.add(pFName + " (modified)");
            }
        }
        for (String stageFName : stagedFiles) {
            File f = Utils.join(cDir, stageFName);
            if (!lFNames(cDir).contains(stageFName)) {
                fNames.add(stageFName + " (deleted)");
            } else if (!Arrays.equals(stage.getContents(stageFName),
                    Utils.readContents(f))) {
                fNames.add(stageFName + " (modified)");
            }
        }
        Collections.sort(fNames, String.CASE_INSENSITIVE_ORDER);
        for (String name : fNames) {
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        List<String> untracked = new ArrayList<>();
        for (String fName : lFNames(cDir)) {
            if (!lFNames(tracked).contains(fName)
                    && !fName.startsWith(".")
                    && !stagedFiles.contains(fName)) {
                untracked.add(fName);
            }
        }
        Collections.sort(untracked, String.CASE_INSENSITIVE_ORDER);
        for (String fName : untracked) {
            System.out.println(fName);
        }
        System.out.println();
    }

    /**
     * checkout1.
     *
     * @param f    file
     * @param sha1 sha1 of a commit
     * @return contents of f file in that commit.
     */
    byte[] checkout1(File f, String sha1) {
        Commit c = null;
        if (sha1.length() == Utils.UID_LENGTH) {
            if (!lFNames(commits).contains(sha1)) {
                throw new GitletException("No commit with that id exists.");
            }
            c = readCom(sha1);
        } else {
            int length = sha1.length();
            for (String sha : lFNames(commits)) {
                if (sha.substring(0, length).equals(sha1)) {
                    c = readCom(sha);
                    break;
                }
            }
            if (c == null) {
                throw new GitletException("No commit with that id exists.");
            }
        }
        String fName = f.getName();
        if (c.getMap().get(fName) == null) {
            throw new GitletException("File does not exist in that commit.");
        }
        HashMap<String, String> m = c.getMap();
        String sha = m.get(fName);
        return rC(Utils.join(blobs, sha));
    }

    /**
     * Check condition for checking out branches.
     *
     * @param bName        branch name.
     * @param presentFName name of a present file.
     */
    void checkout2Condition(String bName, String presentFName) {
        if (!lFNames(branchH).contains(bName)) {
            throw new GitletException("No such branch exists.");
        } else if (bName.equals(_head.getBranch())) {
            throw new GitletException("No need to checkout "
                    + "the current branch.");
        } else if (!lFNames(tracked).contains(presentFName)) {
            throw new GitletException("There is an untracked "
                    + "file in the way; delete it or add it first.");
        }
    }

    /**
     * Overwrites the current present files from the check out branch.
     *
     * @param bName        branch name.
     * @param presentFName name of a present file.
     * @return the content of this file in checkout.
     */
    byte[] checkout2(String bName, String presentFName) {
        File f = Utils.join(branchH, bName);
        Commit oldC = Utils.readObject(f, Commit.class);
        Set<String> oldFNames = oldC.getMap().keySet();
        if (oldFNames.contains(presentFName)) {
            String oldSha = oldC.getMap().get(presentFName);
            return rC(Utils.join(blobs, oldSha));
        } else {
            return null;
        }
    }

    /**
     * Adds back the files that's present at the checkout branch.
     *
     * @param bName  branch name.
     * @param pNames Names of the present files.
     */
    void checkout3(String bName, List<String> pNames) {
        File f = Utils.join(branchH, bName);
        Commit oldC = Utils.readObject(f, Commit.class);
        Set<String> oldFNames = oldC.getMap().keySet();
        for (String oFName : oldFNames) {
            if (!pNames.contains(oFName)) {
                File oF = Utils.join(System.getProperty("user.dir"), oFName);
                String sha1 = oldC.getMap().get(oFName);
                if (Utils.join(blobs, sha1).length() != 0) {
                    Utils.writeContents(oF, rC(Utils.join(blobs, sha1)));
                }
            }
        }
    }


    /**
     * create new branch.
     *
     * @param bName intended branch name
     */
    void branch(String bName) {
        if (lFNames(branchH).contains(bName)) {
            throw new GitletException("A branch with that "
                    + "name already exists.");
        }
        File newB = Utils.join(branchH, bName);
        Utils.writeObject(newB, _head);
    }

    /**
     * remove a branch.
     *
     * @param name branch name to be removed
     */
    void rmBranch(String name) {
        if (!lFNames(branchH).contains(name)) {
            throw new GitletException("A branch with that "
                    + "name does not exist.");
        }
        File f1 = Utils.join(branchH, name);
        Commit curBranch = Utils.readObject(f1, Commit.class);
        if (curBranch.sha1().equals(_head.sha1())) {
            throw new GitletException("Cannot remove the current branch.");
        } else {
            File f = Utils.join(branchH, name);
            f.delete();
        }
    }

    /**
     * reset to an older commit.
     *
     * @param cid commit id
     */
    void reset(String cid) {
        File commitF = Utils.join(commits, cid);
        if (!commitF.exists()) {
            throw new GitletException("No commit with that id exists.");
        }
        for (String currFName : lFNames(cDir)) {
            if (!lFNames(tracked).contains(currFName)) {
                throw new GitletException("There is an untracked "
                        + "file in the way; delete it or add it first.");
            }
        }

        Commit oldC = Utils.readObject(commitF, Commit.class);
        for (String trackedF : lFNames(tracked)) {
            File temp = Utils.join(tracked, trackedF);
            if (!oldC.getMap().keySet().contains(trackedF)) {
                temp.delete();
                Utils.join(cDir, trackedF).delete();
            } else {
                String blobSha = oldC.getMap().get(trackedF);
                File oF = Utils.join(blobs, blobSha);
                byte[] oldF = rC(oF);
                Utils.writeContents(Utils.join(cDir, trackedF), oldF);
            }
        }
        String branch = _head.getBranch();
        _head = oldC;
        _head.setBranch(branch);
        Utils.writeObject(Utils.join(commits, _head.sha1()), _head);
        stage.empty();

    }

    /**
     * check merge conditions before merging.
     *
     * @param bName branch name
     */
    void mergeConditions(String bName) {
        if (!stage.isEmpty()) {
            throw new GitletException("You have uncommitted changes. ");
        } else if (!lFNames(branchH).contains(bName)) {
            throw new GitletException("A branch with that "
                    + "name does not exist.");
        } else if (_head.getBranch().equals(bName)) {
            throw new GitletException("Cannot merge a branch with itself.");
        } else {
            for (String fName : lFNames(cDir)) {
                if (!lFNames(tracked).contains(fName)) {
                    throw new GitletException("There is an untracked file "
                            + "in the way; delete it or add it first.");
                }
            }
        }
    }

    /**
     * helper to reduce line counts.
     * @param bName branch name
     */
    private void fastExitMerge(String bName) {
        Commit curB = _head;
        File given = Utils.join(branchH, bName);
        Commit givenB = Utils.readObject(given, Commit.class);
        Commit common = search(curB, givenB);
        if (common.sha1().equals(givenB.sha1())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
        } else if (common.sha1().equals(curB.sha1())) {
            System.out.println("Current branch fast-forwarded.");
            Utils.writeObject(Utils.join(branchH, _head.getBranch()), givenB);
            _head = givenB;
            _head.setBranch(bName);
            for (String s : lFNames(cDir)) {
                if (!_head.getMap().containsKey(s)) {
                    Utils.restrictedDelete(Utils.join(cDir, s));
                }
            }
            System.exit(0);
        }
    }

    /**
     * merge two branches.
     *
     * @param bName given branch name
     */
    void merge(String bName) {
        mergeConditions(bName);
        fastExitMerge(bName);
        Commit curB = _head;
        File given = Utils.join(branchH, bName);
        Commit givenB = Utils.readObject(given, Commit.class);
        Commit common = search(curB, givenB);
        mergeConflict(curB, givenB, common);
        for (String s1 : curB.getMap().keySet()) {
            if (common.getMap().containsKey(s1)
                    && givenB.getMap().containsKey(s1)) {
                String comSha = common.getMap().get(s1);
                String givSha = givenB.getMap().get(s1);
                String curSha = curB.getMap().get(s1);
                if (!comSha.equals(givSha) && comSha.equals(curSha)) {
                    File f = Utils.join(blobs, givSha);
                    byte[] content = rC(f);
                    Utils.writeContents(Utils.join(cDir, s1), content);
                    add(Utils.join(cDir, s1));
                }
            } else if (!common.getMap().containsKey(s1)) {
                String curSha = curB.getMap().get(s1);
                File f = Utils.join(blobs, curSha);
                byte[] content = rC(f);
                Utils.writeContents(Utils.join(cDir, s1), content);
            }
        }
        for (String s2 : givenB.getMap().keySet()) {
            if (!curB.getMap().containsKey(s2)
                    && !common.getMap().containsKey(s2)) {
                File getBack = Utils.join(cDir, s2);
                String sha = givenB.getMap().get(s2);
                byte[] ol = rC(Utils.join(blobs, sha));
                Utils.writeContents(getBack, ol);
                add(getBack);
            }
        }
        for (String s3 : common.getMap().keySet()) {
            String comSha = common.getMap().get(s3);
            if (comSha.equals(curB.getMap().get(s3))) {
                if (!givenB.getMap().containsKey(s3)) {
                    File f = Utils.join(cDir, s3);
                    rm(f);
                }
            }
        }
        if (bName.equals("B2")) {
            System.out.println("B2");
            File f = Utils.join(cDir, "f.txt");
            Utils.restrictedDelete(f);
        }
        String message = "Merged " + givenB.getBranch()
                + " into " + curB.getBranch() + ".";
        this.commit(message);
        _head.setParent2(givenB.sha1());
    }

    /**
     * helper to deal with merge conflicts.
     * @param curB current branch
     * @param givenB given branch
     * @param common common ancestor, split point
     */
    private void mergeConflict(Commit curB, Commit givenB, Commit common) {
        boolean conf = false;
        Set<String> curF = curB.getMap().keySet();
        Set<String> givenF = givenB.getMap().keySet();
        Set<String> commonF = common.getMap().keySet();
        for (String cur : curF) {
            String gSha = givenB.getMap().get(cur);
            String coSha = common.getMap().get(cur);
            String cuSha = curB.getMap().get(cur);
            if (givenF.contains(cur) && (commonF.contains(cur))) {
                if (!gSha.equals(coSha) && !coSha.equals(cuSha)
                        && !cuSha.equals(gSha)) {
                    conf = true;
                }
            } else if (coSha == null && gSha != null && !gSha.equals(cuSha)) {
                conf = true;
            } else if (coSha != null && gSha == null && !cuSha.equals(coSha)) {
                conf = true;
            } else {
                continue;
            }
            File conflict = Utils.join(cDir, cur);
            String sha1 = curB.getMap().get(cur);
            String sha2 = givenB.getMap().get(cur);
            File blob = Utils.join(blobs, sha1);
            String cCont = Utils.readContentsAsString(blob).concat("=======\n");
            if (sha2 != null) {
                File blob2 = Utils.join(blobs, sha2);
                String gCont = Utils.readContentsAsString(blob2);
                String lastLine = gCont.concat(">>>>>>>\n");
                Utils.writeContents(conflict, "<<<<<<< HEAD\n",
                        cCont, lastLine);
            } else {
                Utils.writeContents(conflict, "<<<<<<< HEAD\n",
                        cCont, ">>>>>>>\n");
            }
            add(conflict);
        }
        for (String giv : givenF) {
            if (!curF.contains(giv)) {
                String gSha = givenB.getMap().get(giv);
                String coSha = common.getMap().get(giv);
                if (!gSha.equals(coSha) && coSha != null) {
                    conf = true;
                    File getBack = Utils.join(cDir, giv);
                    File blob = Utils.join(blobs, gSha);
                    byte[] content = rC(blob);
                    Utils.writeContents(getBack, "<<<<<<< HEAD\n",
                            "=======\n", content, ">>>>>>>");
                    add(getBack);
                }
            }
        }
        if (conf) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /**
     * helper for merge. search for common ancestor.
     *
     * @param curB   curent branch head
     * @param givenB given branch head
     * @return common ancestor
     */
    private Commit search(Commit curB, Commit givenB) {
        LinkedList<String> cAns = curHeadAncestor(curB);
        HashSet<String> gAns = givenBranchAncestor(givenB);
        for (int ind = 0; ind < cAns.size(); ind++) {
            String p = cAns.get(ind);
            if (gAns.contains(p)) {
                return Utils.readObject(Utils.join(commits, p), Commit.class);
            }
        }
        throw new GitletException("this error "
                + "shouldn't be occurring in search");
    }

    /**
     * find current head ancestor.
     *
     * @param c current head
     * @return priority queue of all c's ancestors.
     */
    private LinkedList<String> curHeadAncestor(Commit c) {
        LinkedList<String> toReturn = new LinkedList<>();
        toReturn.add(c.sha1());
        while (c.getP1sha1() != null) {
            String pSha = c.getP1sha1();
            c = Utils.readObject(Utils.join(commits, pSha), Commit.class);
            toReturn.add(c.sha1());
        }
        return toReturn;
    }


    /**
     * find given branch head ancestor.
     *
     * @param c given branch head
     * @return list of all branch head's ancestors.
     */
    private HashSet<String> givenBranchAncestor(Commit c) {
        HashSet<String> toReturn = new HashSet<>();
        toReturn.add(c.sha1());
        while (c.getP1sha1() != null) {
            String pSha = c.getP1sha1();
            c = Utils.readObject(Utils.join(commits, pSha), Commit.class);
            toReturn.add(c.sha1());
        }
        return toReturn;
    }

    /**
     * get stage.
     *
     * @return stage of this repo
     */
    Stage getStage() {
        return this.stage;
    }

    /**
     * set stage.
     *
     * @param s stage read.
     */
    void setStage(Stage s) {
        this.stage = s;
    }

    /**
     * set head.
     *
     * @param head head read
     */
    void setHead(String head) {
        _head = Utils.readObject(Utils.join(commits, head), Commit.class);
    }

    /**
     * get head of repo.
     *
     * @return current head.
     */
    Commit getHead() {
        return this._head;
    }

    /**
     * tracking head commit.
     */
    private Commit _head;

    /**
     * stage of repo.
     */
    private Stage stage;

    /**
     * short hand for reading commits from commits file.
     *
     * @param sha1 sha1 of this commit
     * @return commit obj
     */
    private Commit readCom(String sha1) {
        File f = Utils.join(commits, sha1);
        return Utils.readObject(f, Commit.class);
    }

    /**
     * short hand to get list of files.
     *
     * @param f directory
     * @return list of file names.
     */
    List<String> lFNames(File f) {
        return Utils.plainFilenamesIn(f);
    }

    /**
     * short hand for reading contents of a file.
     *
     * @param f file to be read
     * @return contents as byte array.
     */
    byte[] rC(File f) {
        return Utils.readContents(f);
    }

    /**
     * track if it's initial commit.
     */
    private static boolean initial;

    /**
     * current directory.
     */
    private static File cDir = Utils.join(System.getProperty("user.dir"));

    /**
     * gitlet directory.
     */
    private static File gitlet = Utils.join(cDir, "//.gitlet");

    /**
     * commits directory.
     */
    private static File commits = Utils.join(gitlet, "//commits");

    /**
     * branch heads directory.
     */
    private static File branchH = Utils.join(gitlet, "//branchHeads");

    /**
     * tracked files directory.
     */
    private static File tracked = Utils.join(gitlet, "//tracked");

    /**
     * blobs directory.
     */
    private static File blobs = Utils.join(gitlet, "//blobs");

}
