package tablut;

import java.util.Iterator;

import static java.lang.Math.max;
import static tablut.Piece.BLACK;
import static tablut.Piece.WHITE;

/**
 * A Player that automatically generates moves.
 *
 * @author Sally Peng
 */
class AI extends Player {

    /**
     * A position-score magnitude indicating a win (for white if positive,
     * black if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /**
     * A position-score magnitude indicating a forced win in a subsequent
     * move.  This differs from WINNING_VALUE to avoid putting off wins.
     */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;
    /**
     * The move found by the last call to one of the ...FindMove methods
     * below.
     */
    private Move _lastFoundMove;

    /**
     * A new AI with no piece or controller (intended to produce
     * a template).
     */
    AI() {
        this(null, null);
    }

    /**
     * A new AI playing PIECE under control of CONTROLLER.
     */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    /**
     * Return a heuristically determined maximum search depth
     * based on characteristics of BOARD.
     */
    private static int maxDepth(Board board) {
        return 4;
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move mv = findMove();
        String m = mv.toString();
        _controller.reportMove(mv);
        return m;
    }

    @Override
    boolean isManual() {
        return false;
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        _lastFoundMove = null;
        int sense;
        if (board().turn() == WHITE) {
            sense = -1;
        } else {
            sense = 1;
        }

        findMove(board(), 4, true, sense, Integer.MIN_VALUE, INFTY);
        return _lastFoundMove;
    }

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _lastFoundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _lastMoveFound.
     */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        int bestScore = Integer.MIN_VALUE;
        if (sense == -1) {
            bestScore = Integer.MAX_VALUE;
        }
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        Iterator<Move> moves = board.legalMoves(board.turn()).iterator();
        Move best = null;
        Move next;
        while (moves.hasNext()) {
            next = moves.next();
            board.makeMove(next);
            int score = findMove(board, depth - 1, false, -sense, alpha, beta);
            board.undo();
            if (sense == -1 && score < bestScore) {
                best = next;
                bestScore = score;
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    break;
                }
            } else if (sense == 1 && score > bestScore) {
                best = next;
                bestScore = score;
                alpha = max(alpha, bestScore);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;
    }

    /**
     * Return a heuristic value for BOARD.
     */
    private int staticScore(Board board) {
        final int magic = 10000;
        if (board.winner() == BLACK) {
            return magic;
        } else if (board.winner() == WHITE) {
            return -magic;
        }
        int numBlack = board.numPieces(BLACK);
        int numWhite = board.numPieces(WHITE);
        int kDist = Math.min(board.kingPosition().col(),
                board.kingPosition().row());
        int bK = 0;
        for (int i = 0; i < 4; i++) {
            Square s = board.kingPosition().rookMove(i, 1);
            if (s != null && board.get(s).toName() != null
                    && board.get(s).side() == WHITE) {
                bK += 5;
            }
        }

        return numBlack + kDist * 2 - numWhite + bK;
    }
}
