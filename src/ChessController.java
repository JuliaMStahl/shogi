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

    public void resetPlayers() {
        playerOne = new Player(1);
        playerTwo = new Player(2);
    }

    public void capturePiece(ChessPiece piece) {
        if (piece.getPlayer() != 1) {
            playerOne.setCapturedCount(playerOne.getCapturedCount() + 1);
            playerTwo.setPieceCount(playerTwo.getPieceCount() - 1);
        } else {
            playerTwo.setCapturedCount(playerTwo.getCapturedCount() + 1);
            playerOne.setPieceCount(playerOne.getPieceCount() - 1);
        }
    }

    public void updateTurnAndMoveCount() {
        if (turn == 1) {
            playerOne.setMoveCount(playerOne.getMoveCount() + 1);
            turn = 2;
        } else if (turn == 2) {
            playerTwo.setMoveCount(playerTwo.getMoveCount() + 1);
            turn = 1;
        }
    }

    private void resetCapturedPieces() {
        playerOne.getCapturedPieces().clear();
        playerTwo.getCapturedPieces().clear();
    }

    public void setupNewGame() {
        resetCapturedPieces();
        initializeAI();
        chessPieces = new ArrayList<>();

        setupPlayerPieces(1, 8, 7, 6);
        setupPlayerPieces(2, 0, 1, 2);

        turn = 1;
    }

    private void initializeAI() {
        if (isPlayingWithAI) {
            cpuAI = new ChessAI(this);
        }
    }

    private void setupPlayerPieces(int player, int mainRow, int rookBishopRow, int pawnRow) {
        chessPieces.add(new ChessPiece(player, PieceType.LANCE, 0, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.KNIGHT, 1, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.SILVERGEN, 2, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.GOLDGEN, 3, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.KING, 4, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.GOLDGEN, 5, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.SILVERGEN, 6, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.KNIGHT, 7, mainRow));
        chessPieces.add(new ChessPiece(player, PieceType.LANCE, 8, mainRow));

        if (player == 1) {
            chessPieces.add(new ChessPiece(player, PieceType.ROOK, 1, rookBishopRow));
            chessPieces.add(new ChessPiece(player, PieceType.BISHOP, 7, rookBishopRow));
        } else {
            chessPieces.add(new ChessPiece(player, PieceType.BISHOP, 1, rookBishopRow));
            chessPieces.add(new ChessPiece(player, PieceType.ROOK, 7, rookBishopRow));
        }

        for (int i = 0; i < 9; i++) {
            chessPieces.add(new ChessPiece(player, PieceType.PAWN, i, pawnRow));
        }
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

    // TODO Add other pieces moves
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

    public Player getPlayerTwo() {
        return playerTwo;
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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public ChessAI getCpuAI() {
        return cpuAI;
    }

}
