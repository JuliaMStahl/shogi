import java.awt.*;
import java.util.ArrayList;

public class ChessController {
    private ChessAI cpuAI;
    private boolean isPlayingWithAI = false;
    private ArrayList<ChessPiece> chessPieces = new ArrayList<>();
    private int turn = 1;
    private Player playerOne = new Player(1);
    private Player playerTwo = new Player(2);


    public ChessController() {
    }

    private void resetCapturedPieces() {
        playerOne.getCapturedPieces().clear();
        playerTwo.getCapturedPieces().clear();
    }

    public void setupNewGame(boolean isWithAI, ChessGUI gui ) {

        isPlayingWithAI = isWithAI;

        resetCapturedPieces();

        if (isWithAI) cpuAI = new ChessAI(this);

        chessPieces = new ArrayList<>();

        chessPieces.add(new ChessPiece(1, PieceType.LANCE, 0, 8));
        chessPieces.add(new ChessPiece(1, PieceType.KNIGHT, 1, 8));
        chessPieces.add(new ChessPiece(1, PieceType.SILVERGEN, 2, 8));
        chessPieces.add(new ChessPiece(1, PieceType.GOLDGEN, 3, 8));
        chessPieces.add(new ChessPiece(1, PieceType.KING, 4, 8));
        chessPieces.add(new ChessPiece(1, PieceType.GOLDGEN, 5, 8));
        chessPieces.add(new ChessPiece(1, PieceType.SILVERGEN, 6, 8));
        chessPieces.add(new ChessPiece(1, PieceType.KNIGHT, 7, 8));
        chessPieces.add(new ChessPiece(1, PieceType.LANCE, 8, 8));

        chessPieces.add(new ChessPiece(1, PieceType.BISHOP, 1, 7));
        chessPieces.add(new ChessPiece(1, PieceType.ROOK, 7, 7));

        for (int pawni = 0; pawni < 9; pawni++) {
            chessPieces.add(new ChessPiece(1, PieceType.PAWN, pawni, 6));
        }

        chessPieces.add(new ChessPiece(2, PieceType.LANCE, 0, 0));
        chessPieces.add(new ChessPiece(2, PieceType.KNIGHT, 1, 0));
        chessPieces.add(new ChessPiece(2, PieceType.SILVERGEN, 2, 0));
        chessPieces.add(new ChessPiece(2, PieceType.GOLDGEN, 3, 0));
        chessPieces.add(new ChessPiece(2, PieceType.KING, 4, 0));
        chessPieces.add(new ChessPiece(2, PieceType.GOLDGEN, 5, 0));
        chessPieces.add(new ChessPiece(2, PieceType.SILVERGEN, 6, 0));
        chessPieces.add(new ChessPiece(2, PieceType.KNIGHT, 7, 0));
        chessPieces.add(new ChessPiece(2, PieceType.LANCE, 8, 0));

        chessPieces.add(new ChessPiece(2, PieceType.ROOK, 1, 1));
        chessPieces.add(new ChessPiece(2, PieceType.BISHOP, 7, 1));

        for (int pawni = 0; pawni < 9; pawni++) {
            chessPieces.add(new ChessPiece(2, PieceType.PAWN, pawni, 2));
        }

        turn = 1;
    }

    public int isOccupied(int x, int y) {
        for (ChessPiece chp : getChessPieces()) {
            if (chp.getX() == x && chp.getY() == y) {
                if (chp.getPlayer() == 1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        return 0; //not occupied
    }

    public ChessPiece getPieceAt(int x, int y) {
        for (ChessPiece chp : getChessPieces()) {
            if (chp.getX() == x && chp.getY() == y) {
                return chp;
            }
        }
        return null;
    }

    public ArrayList<Point> getPossibleMoves(ChessPiece chp) {
        ArrayList<Point> possibleMoves = new ArrayList<>();
        int pf = (chp.getPlayer() == 2) ? 1 : -1;
        switch (chp.getType()) {
            case KING:
                possibleMoves.add(new Point(-1, 1));
                possibleMoves.add(new Point(0, 1));
                possibleMoves.add(new Point(1, 1));
                possibleMoves.add(new Point(-1, 0));
                possibleMoves.add(new Point(1, 0));
                possibleMoves.add(new Point(-1, -1));
                possibleMoves.add(new Point(0, -1));
                possibleMoves.add(new Point(1, -1));
                break;
            case ROOK:
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(r, 0));
                    if (isOccupied(chp.getX() + pf * r, chp.getY()) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(-r, 0));
                    if (isOccupied(chp.getX() - pf * r, chp.getY()) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(0, r));
                    if (isOccupied(chp.getX(), chp.getY() + pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(0, -r));
                    if (isOccupied(chp.getX(), chp.getY() - pf * r) != 0) break;
                }
                break;
            case BISHOP:
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(r, r));
                    if (isOccupied(chp.getX() + pf * r, chp.getY() + pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(-r, -r));
                    if (isOccupied(chp.getX() - pf * r, chp.getY() - pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(-r, r));
                    if (isOccupied(chp.getX() - pf * r, chp.getY() + pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(r, -r));
                    if (isOccupied(chp.getX() + pf * r, chp.getY() - pf * r) != 0) break;
                }
                break;
            case GOLDGEN:
            case SILVERGEN_P:
            case KNIGHT_P:
            case LANCE_P:
            case PAWN_P:
                possibleMoves.add(new Point(-1, 1));
                possibleMoves.add(new Point(0, 1));
                possibleMoves.add(new Point(1, 1));
                possibleMoves.add(new Point(-1, 0));
                possibleMoves.add(new Point(1, 0));
                possibleMoves.add(new Point(0, -1));
                break;
            case SILVERGEN:
                possibleMoves.add(new Point(-1, 1));
                possibleMoves.add(new Point(0, 1));
                possibleMoves.add(new Point(1, 1));
                possibleMoves.add(new Point(-1, -1));
                possibleMoves.add(new Point(1, -1));
                break;
            case KNIGHT:
                possibleMoves.add(new Point(-1, 2));
                possibleMoves.add(new Point(1, 2));
                break;
            case LANCE:
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(0, r));
                    if (isOccupied(chp.getX(), chp.getY() + pf * r) != 0) break;
                }
                break;
            case PAWN:
                possibleMoves.add(new Point(0, 1));
                break;
            case ROOK_P:
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(r, 0));
                    if (isOccupied(chp.getX() + pf * r, chp.getY()) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(-r, 0));
                    if (isOccupied(chp.getX() - pf * r, chp.getY()) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(0, r));
                    if (isOccupied(chp.getX(), chp.getY() + pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(0, -r));
                    if (isOccupied(chp.getX(), chp.getY() - pf * r) != 0) break;
                }
                possibleMoves.add(new Point(1, 1));
                possibleMoves.add(new Point(1, -1));
                possibleMoves.add(new Point(-1, 1));
                possibleMoves.add(new Point(-1, -1));
                break;
            case BISHOP_P:
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(r, r));
                    if (isOccupied(chp.getX() + pf * r, chp.getY() + pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(-r, -r));
                    if (isOccupied(chp.getX() - pf * r, chp.getY() - pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(-r, r));
                    if (isOccupied(chp.getX() - pf * r, chp.getY() + pf * r) != 0) break;
                }
                for (int r = 1; r < 9; r++) {
                    possibleMoves.add(new Point(r, -r));
                    if (isOccupied(chp.getX() + pf * r, chp.getY() - pf * r) != 0) break;
                }
                possibleMoves.add(new Point(0, 1));
                possibleMoves.add(new Point(0, -1));
                possibleMoves.add(new Point(-1, 0));
                possibleMoves.add(new Point(1, 0));
                break;

        }
        return possibleMoves;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public boolean isPlayingWithAI() {
        return isPlayingWithAI;
    }

    public void setPlayingWithAI(boolean playingWithAI) {
        isPlayingWithAI = playingWithAI;
    }

    public ArrayList<ChessPiece> getChessPieces() {
        return chessPieces;
    }

    public void setChessPieces(ArrayList<ChessPiece> chessPieces) {
        this.chessPieces = chessPieces;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public ChessAI getCpuAI() {
        return cpuAI;
    }

    public void setCpuAI(ChessAI cpuAI) {
        this.cpuAI = cpuAI;
    }
}
