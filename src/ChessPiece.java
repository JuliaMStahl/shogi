/**
 * Created by Armin on 3/28/2016.
 */
public class ChessPiece {

    private PieceType type;
    private int player;
    private int x;
    private int y;

    public ChessPiece(int player, PieceType type, int x, int y){
        this.player = player;
        this.type = type;
        this.x = x;
        this.y = y;
        PromotionActions.mustAdd = false;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}