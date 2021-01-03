package tablut;

import org.junit.Test;
import ucb.junit.textui;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Junit tests for our Tablut Board class.
 *
 * @author Vivant Sakore
 */
public class TablutTests {

    /**
     * Run the JUnit tests in this package.
     */
    public static void main(String[] ignored) {
        textui.runClasses(TablutTests.class);
    }

    /**
     * Tests legalMoves for white pieces to make sure it returns
     * all legal Moves.
     * This method needs to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalWhiteMoves() {
        Board b = new Board();
        List<Move> movesList = b.legalMoves(Piece.WHITE);

        assertEquals(56, movesList.size());

        assertFalse(movesList.contains(Move.mv("e7-8")));
        assertFalse(movesList.contains(Move.mv("e8-f")));

        assertTrue(movesList.contains(Move.mv("e6-f")));
        assertTrue(movesList.contains(Move.mv("f5-8")));

    }

    /**
     * Tests legalMoves for black pieces to make sure it returns
     * all legal Moves.
     * This method needs to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalBlackMoves() {
        Board b = new Board();
        List<Move> movesList = b.legalMoves(Piece.BLACK);

        assertEquals(80, movesList.size());

        assertFalse(movesList.contains(Move.mv("e8-7")));
        assertFalse(movesList.contains(Move.mv("e7-8")));

        assertTrue(movesList.contains(Move.mv("f9-i")));
        assertTrue(movesList.contains(Move.mv("h5-1")));

    }


    @Test
    public void testMakeMove() {
        Board b = new Board();
        b.makeMove(Move.mv("i4-f"));
        assertEquals("Black", b.get(5, 3).toName());
        assertEquals(null, b.get(8, 3).toName());

        try {
            b.makeMove(Move.mv("i5-4"));
        } catch (AssertionError e) {
            System.out.println("illegal move caught");
        }

        b.makeMove(Move.mv("g5-4"));
        assertEquals("White", b.get(6, 3).toName());
        assertEquals(null, b.get(5, 3).toName());
        assertEquals(null, b.get(6, 4).toName());
    }

    @Test
    public void kingCapture() {
        Board a = new Board();
        a.makeMove(Move.mv("f1-4"));
        a.makeMove(Move.mv("e4-c"));
        a.makeMove(Move.mv("a6-d"));
        a.makeMove(Move.mv("d5-2"));
        a.makeMove(Move.mv("d6-5"));
        a.makeMove(Move.mv("d2-a"));
        a.makeMove(Move.mv("d1-4"));
        a.makeMove(Move.mv("f5-8"));
        a.makeMove(Move.mv("f4-5"));
        a.makeMove(Move.mv("e6-b"));
        a.makeMove(Move.mv("d4-e"));
        a.makeMove(Move.mv("b6-7"));
        a.makeMove(Move.mv("i6-e"));
        assertTrue(a.winner() == Piece.BLACK);

        Board d = new Board();
        d.makeMove(Move.mv("i4-1"));
        d.makeMove(Move.mv("f5-2"));
        d.makeMove(Move.mv("f9-6"));
        d.makeMove(Move.mv("e5-f"));
        d.makeMove(Move.mv("i1-4"));
        d.makeMove(Move.mv("g5-9"));
        d.makeMove(Move.mv("h5-g"));
        d.makeMove(Move.mv("g9-i"));
        d.makeMove(Move.mv("i4-f"));
        assertTrue(d.winner() == Piece.BLACK);


        Board c = new Board();
        c.makeMove(Move.mv("f1-4"));
        c.makeMove(Move.mv("e4-b"));
        c.makeMove(Move.mv("d1-4"));
        c.makeMove(Move.mv("e5-4"));
        c.makeMove(Move.mv("i6-7"));
        c.makeMove(Move.mv("e3-i"));
        c.makeMove(Move.mv("e2-3"));
        assertTrue(c.winner() == Piece.BLACK);


        Board e = new Board();
        e.makeMove(Move.mv("f1-g"));
        e.makeMove(Move.mv("f5-8"));
        e.makeMove(Move.mv("i6-7"));
        e.makeMove(Move.mv("e5-f"));
        e.makeMove(Move.mv("e2-a"));
        e.makeMove(Move.mv("f5-4"));
        e.makeMove(Move.mv("d1-c"));
        e.makeMove(Move.mv("f4-h"));
        e.makeMove(Move.mv("g1-4"));
        assertTrue(e.winner() == Piece.BLACK);

    }

    @Test
    public void regCapture() {
        Board b = new Board();
        b.makeMove(Move.mv("i4-f"));
        b.makeMove(Move.mv("g5-4"));
        assertEquals("White", b.get(6, 3).toName());
        assertEquals(null, b.get(5, 3).toName());
        assertEquals(null, b.get(6, 4).toName());

        Board outThroneVertically = new Board();
        outThroneVertically.makeMove(Move.mv("i4-1"));
        outThroneVertically.makeMove(Move.mv("g5-1"));
        outThroneVertically.makeMove(Move.mv("i6-g"));
        outThroneVertically.makeMove(Move.mv("f5-2"));
        outThroneVertically.makeMove(Move.mv("i1-4"));
        outThroneVertically.makeMove(Move.mv("e5-g"));
        outThroneVertically.makeMove(Move.mv("i4-g"));
        assertTrue(outThroneVertically.winner() == Piece.BLACK);
    }

    @Test
    public void testUndo() {
        Board b = new Board();
        b.makeMove(Move.mv("i4-f"));
        b.makeMove(Move.mv("g5-4"));
        b.makeMove(Move.mv("d1-4"));
        b.makeMove(Move.mv("g4-5"));
        b.makeMove(Move.mv("f1-4"));
        System.out.println(b);
        b.undo();
        System.out.println(b);
        b.undo();
        System.out.println(b);
    }

    @Test
    public void testRepeated() {
        Board b = new Board();
        b.makeMove(Move.mv("d1-a"));
        b.makeMove(Move.mv("d5-6"));
        b.makeMove(Move.mv("a1-b"));
        b.makeMove(Move.mv("e5-d"));
        b.makeMove(Move.mv("a4-d"));
        b.makeMove(Move.mv("c5-4"));
        b.makeMove(Move.mv("e2-d"));
        b.makeMove(Move.mv("d5-3"));
        b.makeMove(Move.mv("b1-3"));
        b.makeMove(Move.mv("c4-b"));
        b.makeMove(Move.mv("b5-c"));
        b.makeMove(Move.mv("b4-7"));
        b.makeMove(Move.mv("e1-d"));
        b.makeMove(Move.mv("e3-f"));
        b.makeMove(Move.mv("d1-a"));
        b.makeMove(Move.mv("d3-e"));
        b.makeMove(Move.mv("d2-e"));
        b.makeMove(Move.mv("e3-c"));
        b.makeMove(Move.mv("e2-c"));
        b.makeMove(Move.mv("c3-d"));
        b.makeMove(Move.mv("c2-d"));
        b.makeMove(Move.mv("d3-e"));
        System.out.println(b);
    }

    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - 1 - row][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;
    static final Piece W = Piece.WHITE;
    static final Piece B = Piece.BLACK;
    static final Piece K = Piece.KING;

    static final Piece[][] INITIAL_BOARDSTATE = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, W, E, E, E, E},
            {B, E, E, E, W, E, E, E, B},
            {B, B, W, W, K, W, W, B, B},
            {B, E, E, E, W, E, E, E, B},
            {E, E, E, E, W, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, B, B, B, E, E, E},
    };
}

