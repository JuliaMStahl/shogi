import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {
    private final int id;
    private int pieceCount;
    private int capturedCount;
    private int moveCount;
    private final AtomicInteger totalTime;
    private final ArrayList<ChessPiece> capturedPieces;

    public Player(int id) {
        this.id = id;
        this.pieceCount = 13; // Inicialização padrão
        this.capturedCount = 0;
        this.moveCount = 0;
        this.totalTime = new AtomicInteger(900);
        this.capturedPieces = new ArrayList<>();
    }

    public int getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    public int getCapturedCount() {
        return capturedCount;
    }

    public void setCapturedCount(int capturedCount) {
        this.capturedCount = capturedCount;
    }

    public ArrayList<ChessPiece> getCapturedPieces() {
        return capturedPieces;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public AtomicInteger getTotalTime() {
        return totalTime;
    }

}
