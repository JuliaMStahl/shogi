/**
 * Created by Armin on 3/28/2016.
 */
public class ChessPiece {

    public PieceType type;
    public int player;

    public int x;
    public int y;


    public ChessPiece(int player, PieceType type, int x, int y){
        this.player = player;
        this.type = type;
        this.x = x;
        this.y = y;
        PromotionActions.mustAdd = false;
    }
}
