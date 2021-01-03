import java.util.LinkedList;
import java.util.List;


/** A set of String values.
 *  @author Sally Peng
 */
class ECHashStringSet implements StringSet {


    public ECHashStringSet() {
        size = 0;
        chain = new LinkedList[1];
    }

    @Override
    public void put(String s) {
        if (size/chain.length > 5) {
            resize();
        }
        int hash = s.hashCode() & 0x7fffffff % chain.length;
        if (chain[hash] == null) {
            chain[hash] = new LinkedList<>();
        }
        LinkedList<String> atHash = chain[hash];
        atHash.add(s);
        size += 1;
    }

    @Override
    public boolean contains(String s) {
        // FIXME

        int hash = s.hashCode() & 0x7fffffff % chain.length;

        if (chain[hash]!= null) {
            for (String stored : chain[hash]) {
                if (stored.equals(s)) {
                    return true;
                }
            }
        }

//
//
//        for (String stored : chain[hash]) {
//            if (stored != null && stored.equals(s)) {
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public List<String> asList() {
        return null; // FIXME
    }


    private void resize() {
        LinkedList<String>[] oldChain = chain;
        chain = new LinkedList[oldChain.length * 2];
        size = 0;
        for (LinkedList<String> string : oldChain) {
            if (string != null) {
                for (String st : string) {
                    put(st);
                }
            }
        }
    }

    private LinkedList<String>[] chain;
    private int size;
}
