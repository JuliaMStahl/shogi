import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Armin on 4/4/2016.
 */
public class ChessAI {
    private final int AIplayerNo = 2;
    private final ChessController parent;

    public ChessAI(ChessController parent) {
        this.parent = parent;
    }

    private ArrayList<PieceMove> getMoves() {
        ArrayList<PieceMove> moves = new ArrayList<PieceMove>();
        for (int chpi = 0; chpi < parent.getChessPieces().size(); chpi++) {
            ChessPiece chp = parent.getChessPieces().get(chpi);
            if (chp.getPlayer() == AIplayerNo) {
                for (Point move : parent.getPossibleMoves(chp)) {
                    int pf = 1;
                    int newx = (chp.getX() + move.x * pf);
                    int newy = (chp.getY() + move.y * pf);

                    if (newx >= 0 && newy >= 0 && newx <= 8 && newy <= 8) {
                        if (parent.isOccupied(newx, newy) == 0) {
                            PieceMove m = new PieceMove(chpi, new Point(newx, newy));
                            m.isGoingToCapture = false;
                            moves.add(m);
                            System.out.println("MOVE  [x=" + chp.getX() + ",y=" + chp.getY() + "] to {x=" + newx + ",y="
                                    + newy + "]");
                        } else if (parent.isOccupied(newx, newy) != AIplayerNo) {
                            PieceMove m = new PieceMove(chpi, new Point(newx, newy));
                            m.isGoingToCapture = true;
                            m.CapturingPiece = parent.getPieceAt(newx, newy);
                            moves.add(m);
                            System.out.println("CAPTURE  [x=" + chp.getX() + ",y=" + chp.getY() + "] to {x=" + newx
                                    + ",y=" + newy + "]");
                        }
                    }
                }
            }
        }
        return moves;
    }

    public PieceMove playMove() {
        Random rnd = new Random();
        ArrayList<PieceMove> moves = getMoves();
        int i = rnd.nextInt(moves.size());
        return moves.get(i);
    }

    public void printMoves() {
        System.out.println("Printing Moves ...");
        System.out.println(getMoves().size());
    }
}
