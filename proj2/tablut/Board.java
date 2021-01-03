package tablut;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Formatter;

import static tablut.Move.mv;
import static tablut.Piece.*;
import static tablut.Square.SQUARE_LIST;
import static tablut.Square.sq;


/**
 * The state of a Tablut Game.
 *
 * @author Sally Peng
 */
class Board {

    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 9;

    /**
     * The throne (or castle) square and its four surrounding squares..
     */
    static final Square THRONE = sq(4, 4),
            NTHRONE = sq(4, 5),
            STHRONE = sq(4, 3),
            WTHRONE = sq(3, 4),
            ETHRONE = sq(5, 4);

    /**
     * Initial positions of attackers.
     */
    static final Square[] INITIAL_ATTACKERS = {
            sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
            sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
            sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
            sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /**
     * Initial positions of defenders of the king.
     */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    /**
     * Initializes a game board with SIZE squares on a side in the
     * initial position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }


    /**
     * Copies MODEL into me.
     */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();
        for (String b : model._allBoards) {
            _allBoards.add(b);
        }
        this._bStack = model._bStack;
        this._turn = model._turn;
        this._winner = model._winner;
        this._moveCount = model._moveCount;
        this._repeated = model._repeated;
        this._moveLimit = model._moveLimit;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Piece p = model.get(sq(i, j));
                if (p != null) {
                    this.put(p, sq(i, j));
                }
            }
        }
    }

    /**
     * Clears the board to the initial position.
     */
    void init() {
        _board = new Piece[9][9];
        for (Square sq : SQUARE_LIST) {
            this.put(EMPTY, sq);
        }
        for (Square def : INITIAL_DEFENDERS) {
            this.put(WHITE, def);
        }
        for (Square att : INITIAL_ATTACKERS) {
            this.put(BLACK, att);
        }
        this.put(KING, sq(4, 4));
        _turn = BLACK;
        _winner = null;
        _repeated = false;
        _bStack = new Stack<>();
        _bStack.push(null);
        _allBoards = new HashSet<>();
        _allBoards.add(this.encodedBoard());
        _moveCount = 0;
        _moveLimit = 100;
    }

    /**
     * set Move limit.
     *
     * @param n moveLimit
     */
    void setMoveLimit(int n) {
        _moveLimit = n;
        assert (n * 2 > moveCount());
    }

    /**
     * Return a Piece representing whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return the winner in the current position, or null if there is no winner
     * yet.
     */
    Piece winner() {
        return _winner;
    }

    /**
     * Returns true iff this is a win due to a repeated position.
     */
    boolean repeatedPosition() {
        return _repeated;
    }

    /**
     * Record current position and set winner() next mover if the current
     * position is a repeat.
     */
    private void checkRepeated() {
        if (repeatedPosition()) {
            _winner = _turn.opponent();
        }
    }

    /**
     * Return the number of moves since the initial position that have not been
     * undone.
     */
    int moveCount() {
        return _moveCount;
    }

    /**
     * Return location of the king.
     */
    Square kingPosition() {
        for (Square sq : SQUARE_LIST) {
            if (get(sq).toName() != null && get(sq).toName().equals("King")) {
                return sq;
            }
        }
        return null;
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW <= 9.
     */
    final Piece get(int col, int row) {
        return _board[col][row];
    }

    /**
     * Return the contents of the square at COL ROW.
     */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        _board[s.col()][s.row()] = p;
    }

    /**
     * Set square S to P and record for undoing.
     */
    final void revPut(Piece p, Square s) {
        put(p, s);
    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /**
     * Return true iff FROM - TO is an unblocked rook move on the current
     * board.  For this to be true, FROM-TO must be a rook move and the
     * squares along it, other than FROM, must be empty.
     */
    boolean isUnblockedMove(Square from, Square to) {
        if (from.row() == to.row()) {
            int min = Math.min(from.col(), to.col());
            int max = Math.max(from.col(), to.col());
            for (int j = min + 1; j < max; j++) {
                if (get(sq(j, from.row())).toName() != null) {
                    return false;
                }
            }
        } else if (from.col() == to.col()) {
            int min = Math.min(from.row(), to.row());
            int max = Math.max(from.row(), to.row());
            for (int i = min + 1; i < max; i++) {
                if (get(sq(from.col(), i)).toName() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /**
     * Return true iff FROM-TO is a valid move.
     */
    boolean isLegal(Square from, Square to) {
        if (!isLegal(from)) {
            return false;
        }

        if (get(from).toName() == null) {
            return false;
        }

        if (get(to).toName() != null) {
            return false;
        }

        if (!isUnblockedMove(from, to)) {
            return false;
        }
        if (to.row() == 4 && to.col() == 4 && !get(from).equals(KING)) {
            return false;
        }

        if (!from.isRookMove(to)) {
            return false;
        }

        return true;

    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /**
     * Move FROM-TO, assuming this is a legal move.
     */
    void makeMove(Square from, Square to) {
        if (winner() != null) {
            return;
        }
        _moveCount += 1;
        put(get(from), sq(to.col(), to.row()));
        put(EMPTY, sq(from.col(), from.row()));
        if (get(to) == KING && to.isEdge()) {
            _winner = WHITE;
        }
        for (int i = 0; i < 4; i++) {
            Square s = to.rookMove(i, 2);
            if (s != null && (get(s).toName() != null || s.equals(THRONE))) {
                capture(to, s);
            }
        }
        if (_allBoards.contains(this.encodedBoard())) {
            _repeated = true;
            _winner = _turn.opponent();
        } else {
            _allBoards.add(this.encodedBoard());
        }
        checkRepeated();
        _bStack.push(new Board(this));
        _turn = get(to).opponent();
    }

    /**
     * Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /**
     * Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     * SQ0 and the necessary conditions are satisfied.
     */
    private void capture(Square sq0, Square sq2) {
        boolean captured = false;
        Square middle = sq0.between(sq2);
        Piece p0 = get(sq0);
        Piece p1 = get(middle);
        Piece p2 = get(sq2);

        if (p1.toName() != null) {
            if ((p0.side() == p2.side() || (sq2 == THRONE
                    && p2 == EMPTY)) && p0.side() != p1.side()) {
                captured = true;
            } else {
                return;
            }
            if (p1.toName().equals("King") && p0.side()
                    == BLACK && !middle.isEdge()) {
                Square sqd1 = sq0.diag1(sq2);
                Square sqd2 = sq0.diag2(sq2);
                if (sq2 == THRONE) {
                    if (get(sqd1).side() != BLACK
                            || get(sqd2).side() != BLACK) {
                        captured = false;
                    }
                } else if (sqd1 == THRONE) {
                    if (get(sqd2).side() != BLACK) {
                        captured = false;
                    }
                } else if (sqd2 == THRONE) {
                    if (get(sqd1).side() != BLACK) {
                        captured = false;
                    }
                } else if (middle == THRONE) {
                    if (get(sqd1).side() != BLACK
                            || get(sqd2).side() != BLACK
                            || p2.side() != BLACK) {
                        captured = false;
                    }
                }
            }
        }

        if (captured) {
            if (p1.toName().equals("King")) {
                _winner = BLACK;
            }
            this.put(EMPTY, middle);
        }
    }

    /**
     * Undo one move.  Has no effect on the initial board.
     */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            _bStack.pop();
            if (_bStack.peek() == null) {
                init();
            } else {
                copy(_bStack.peek());
            }
        }
    }

    /**
     * Remove record of current position in the set of positions encountered,
     * unless it is a repeated position or we are at the first move.
     */
    private void undoPosition() {
        _repeated = false;
    }

    /**
     * Clear the undo stack and board-position counts. Does not modify the
     * current position or win status.
     */
    void clearUndo() {
        _bStack.clear();
        _moveCount = 0;
    }

    /**
     * Return a new mutable list of all legal moves on the current board for
     * SIDE (ignoring whose turn it is at the moment).
     */
    List<Move> legalMoves(Piece side) {
        Piece turn = _turn;
        _turn = side;
        List<Move> legalMoves = new ArrayList<>();

        if (_turn == WHITE && kingPosition() != null) {
            Square sq = kingPosition();
            for (int col = 0; col < 9; col++) {
                if (isLegal(sq, sq(col, sq.row()))) {
                    legalMoves.add(mv(sq, sq(col, sq.row())));
                }
            }
            for (int row = 0; row < 9; row++) {
                if (isLegal(sq, sq(sq.col(), row))) {
                    legalMoves.add(mv(sq, sq(sq.col(), row)));
                }
            }
        }

        for (Square sq : SQUARE_LIST) {
            Piece p = get(sq);
            if (p.toName() != null && !p.toName().equals("King")
                    && p.side() == side.side()) {
                for (int col = 0; col < 9; col++) {
                    if (isLegal(sq, sq(col, sq.row()))) {
                        legalMoves.add(mv(sq, sq(col, sq.row())));
                    }
                }
                for (int row = 0; row < 9; row++) {
                    if (isLegal(sq, sq(sq.col(), row))) {
                        legalMoves.add(mv(sq, sq(sq.col(), row)));
                    }
                }
            }
        }
        _turn = turn;
        return legalMoves;

    }

    /**
     * Return true iff SIDE has a legal move.
     */
    boolean hasMove(Piece side) {

        return legalMoves(side) != null;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /**
     * Return a text representation of this Board.  If COORDINATES, then row
     * and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /**
     * Return the locations of all pieces on SIDE.
     */
    private HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;

        HashSet<Square> toReturn = new HashSet<>();
        for (Square sq : SQUARE_LIST) {
            Piece p = get(sq);
            if (p.toName() != null && p.side() == side.side()) {
                toReturn.add(sq);
            }
        }
        return toReturn;
    }

    /**
     * Return the contents of _board in the order of SQUARE_LIST as a sequence
     * of characters: the toString values of the current turn and Pieces.
     */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /**
     * return number of pieces of one side for staticScore.
     *
     * @param side side
     * @return number of pieces
     */
    int numPieces(Piece side) {
        return this.pieceLocations(side).size();
    }


    /**
     * Piece whose turn it is (WHITE or BLACK).
     */
    private Piece _turn;


    /**
     * Cached value of winner on this board, or null if it has not been
     * computed.
     */
    private Piece _winner;

    /**
     * Number of (still undone) moves since initial position.
     */
    private int _moveCount;

    /**
     * True when current board is a repeated position (ending the game).
     */
    private boolean _repeated = false;


    /**
     * Move limit.
     */
    private int _moveLimit;

    /**
     * Board.
     */
    private Piece[][] _board;

    /**
     * All boards appeared for check repeated.
     */
    private HashSet<String> _allBoards;

    /**
     * Board stack to keep track for undo.
     */
    private Stack<Board> _bStack;

}
