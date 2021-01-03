package gitlet;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Driver class for Gitlet, the tiny stupid version-control system.
 *
 * @author Sally Peng
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND> ....
     */
    public static void main(String... args) {
        _args = args;
        try {
            if (args.length < 1) {
                throw new GitletException("Please enter a command.");
            }
            if (args[0].equals("init")) {
                init();
                return;
            } else if (gitlet.length() == 0) {
                throw new GitletException("Not in an initialized "
                        + "Gitlet directory.");
            }
            _repo = new Repo();
            readRepo();

            if (args[0].equals("add")) {
                add();
            } else if (args[0].equals("commit")) {
                commit();
            } else if (args[0].equals("log")) {
                log();
            } else if (args[0].equals("checkout")) {
                checkout();
            } else if (args[0].equals("rm")) {
                rm();
            } else if (args[0].equals("global-log")) {
                globalLog();
            } else if (args[0].equals("find")) {
                find();
            } else if (args[0].equals("branch")) {
                branch();
            } else if (args[0].equals("status")) {
                status();
            } else if (args[0].equals("rm-branch")) {
                rmBranch();
            } else if (args[0].equals("reset")) {
                reset();
            } else if (args[0].equals("merge")) {
                merge();
            } else {
                throw new GitletException("No command with that name exists.");
            }
            writeRepo();
        } catch (GitletException e) {
            System.out.println(e.getMessage());
        }
    }

    /** read repo. */
    private static void readRepo() {
        Stage s = Utils.readObject(Utils.join(gitlet, "stage"), Stage.class);
        _repo.setStage(s);
        String h = Utils.readContentsAsString(Utils.join(gitlet, "HEAD.txt"));
        _repo.setHead(h);
    }

    /** write repo. */
    private static void writeRepo() {
        Utils.writeObject(Utils.join(gitlet, "stage"), _repo.getStage());
        Utils.join(gitlet, "//commits");
        File dir = Utils.join(gitlet, "//HEAD.txt");
        Utils.writeContents(dir, _repo.getHead().sha1());
    }

    /** init. */
    private static void init() {
        File dir = gitlet;
        if (dir.mkdir()) {
            Utils.join(gitlet, "//HEAD.txt");
            Utils.join(gitlet, "commits").mkdir();
            Utils.join(gitlet, "blobs").mkdir();
            Utils.join(gitlet, "branchHeads").mkdir();
            Utils.join(gitlet, "tracked").mkdir();
            _repo = new Repo();
            _repo.init();
            writeRepo();
        } else {
            throw new GitletException("A Gitlet version-control "
                    + "system already exists in the current directory.");
        }
    }

    /** add. */
    private static void add() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        }
        String fName = _args[1];
        File temp = new File(cDir + "/" + fName);
        if (!temp.exists()) {
            throw new GitletException("File does not exit.");
        }
        _repo.add(temp);
    }

    /** commit. */
    private static void commit() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        } else if (_args[1].equals("")) {
            throw new GitletException("Please enter a commit message.");
        }
        _repo.commit(_args[1]);
    }

    /** log. */
    private static void log() {
        if (_args.length != 1) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.log();
    }

    /** checkout. */
    private static void checkout() {
        if (_args.length == 1) {
            throw new GitletException("Incorrect operands.");
        } else if (_args[1].equals("--")) {
            if (_args.length != 3) {
                throw new GitletException("Incorrect operands.");
            }
            File f = Utils.join(cDir, _args[2]);
            byte[] oldF = _repo.checkout1(f, _repo.getHead().sha1());
            Utils.writeContents(f, oldF);
        } else if (_args.length == 4) {
            if (!_args[2].equals("--")) {
                throw new GitletException("Incorrect operands.");
            }
            String commit = _args[1];
            File f = Utils.join(cDir, _args[3]);
            byte[] oldF = _repo.checkout1(f, commit);
            Utils.writeContents(f, oldF);
        } else {
            if (_args.length != 2) {
                throw new GitletException("Incorrect operands.");
            }
            List<String> allF = Utils.plainFilenamesIn(cDir);
            if (allF != null) {
                Predicate<String> fNameFileter =
                    string -> !string.startsWith(".");
                var present = allF.stream().filter(fNameFileter);
                var presentF = present.collect(Collectors.toList());
                for (String fName : presentF) {
                    _repo.checkout2Condition(_args[1], fName);
                }
                for (String fName : presentF) {
                    File thisFile = Utils.join(cDir, fName);
                    byte[] oldF = _repo.checkout2(_args[1], fName);
                    if (oldF != null) {
                        Utils.writeContents(thisFile, oldF);
                    } else {
                        Utils.restrictedDelete(thisFile);
                    }
                }
                _repo.checkout3(_args[1], presentF);
            }
            checkoutHelper();
        }
    }

    /**
     * make length of checkout shorter.
     */
    private static void checkoutHelper() {
        _repo.getStage().empty();
        Commit branchHead =
                Utils.readObject(Utils.join(branchH, _args[1]), Commit.class);
        _repo.setHead(branchHead.sha1());
        _repo.getHead().setBranch(_args[1]);
        File f1 = Utils.join(commits, _repo.getHead().sha1());
        Utils.writeObject(f1, _repo.getHead());
        File f2 = Utils.join(branchH, _args[1]);
        Utils.writeObject(f2, _repo.getHead());
        for (String fName : Utils.plainFilenamesIn(tracked)) {
            File f = Utils.join(tracked, fName);
            f.delete();
        }
        Commit head = _repo.getHead();
        for (String newName : head.getMap().keySet()) {
            File f = Utils.join(tracked, newName);
            Utils.writeContents(f);
        }
    }

    /** rm. */
    private static void rm() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        }
        File f = Utils.join(cDir, _args[1]);
        _repo.rm(f);
    }

    /** global-log. */
    private static void globalLog() {
        if (_args.length != 1) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.globalLog();
    }

    /** find. */
    private static void find() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.find(_args[1]);
    }

    /** branch. */
    private static void branch() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.branch(_args[1]);
    }

    /** status. */
    private static void status() {
        if (_args.length != 1) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.status();
    }

    /** remove branch. */
    private static void rmBranch() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.rmBranch(_args[1]);

    }

    /** reset to a commit. */
    private static void reset() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.reset(_args[1]);
        File f = (Utils.join(branchH, _repo.getHead().getBranch()));
        Utils.writeObject(f, _repo.getHead());
    }

    /** merge. Unfinished. */
    private static void merge() {
        if (_args.length != 2) {
            throw new GitletException("Incorrect operands.");
        }
        _repo.merge(_args[1]);
    }

    /** repo. */
    private static Repo _repo;

    /** current directory. */
    private static File cDir = Utils.join(System.getProperty("user.dir"));

    /** gitlet directory. */
    private static File gitlet = Utils.join(cDir, "//.gitlet");

    /** commits directory. */
    private static File commits = Utils.join(gitlet, "//commits");

    /** branch heads directory. */
    private static File branchH = Utils.join(gitlet, "//branchHeads");

    /** tracked files directory. */
    private static File tracked = Utils.join(gitlet, "//tracked");

    /** command line input. */
    private static String[] _args;
}


