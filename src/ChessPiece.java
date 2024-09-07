import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Armin on 3/28/2016.
 */
public class ChessPiece {

    private static Map<PieceType, BufferedImage> pieceImages;
    private static Map<PieceType, BufferedImage> pieceImages2;
    private PieceType type;
    private int player;
    private int x;
    private int y;

    public ChessPiece(int player, PieceType type, int x, int y) {
        this.player = player;
        this.type = type;
        this.x = x;
        this.y = y;
        PromotionActions.mustAdd = false;
    }

    private static BufferedImage loadImage(String fileName) throws IOException {
        return ImageIO.read(Objects.requireNonNull(ChessPiece.class.getResource(fileName)));
    }

    private static BufferedImage rotate180(BufferedImage bi) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.PI, (double) bi.getWidth() / 2, (double) bi.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(bi, null);
    }

    public static void loadImages() {
        try {
            pieceImages = new HashMap<PieceType, BufferedImage>();
            pieceImages.put(PieceType.KING, loadImage("0.png"));
            pieceImages.put(PieceType.ROOK, loadImage("1.png"));
            pieceImages.put(PieceType.BISHOP, loadImage("2.png"));
            pieceImages.put(PieceType.GOLDGEN, loadImage("3.png"));
            pieceImages.put(PieceType.SILVERGEN, loadImage("4.png"));
            pieceImages.put(PieceType.KNIGHT, loadImage("5.png"));
            pieceImages.put(PieceType.LANCE, loadImage("6.png"));
            pieceImages.put(PieceType.PAWN, loadImage("7.png"));
            pieceImages.put(PieceType.ROOK_P, loadImage("8.png"));
            pieceImages.put(PieceType.BISHOP_P, loadImage("9.png"));
            pieceImages.put(PieceType.SILVERGEN_P, loadImage("10.png"));
            pieceImages.put(PieceType.KNIGHT_P, loadImage("11.png"));
            pieceImages.put(PieceType.LANCE_P, loadImage("12.png"));
            pieceImages.put(PieceType.PAWN_P, loadImage("13.png"));

            pieceImages2 = new HashMap<PieceType, BufferedImage>();
            pieceImages2.put(PieceType.KING, rotate180(loadImage("0_2.png")));
            pieceImages2.put(PieceType.ROOK, rotate180(loadImage("1_2.png")));
            pieceImages2.put(PieceType.BISHOP, rotate180(loadImage("2_2.png")));
            pieceImages2.put(PieceType.GOLDGEN, rotate180(loadImage("3_2.png")));
            pieceImages2.put(PieceType.SILVERGEN, rotate180(loadImage("4_2.png")));
            pieceImages2.put(PieceType.KNIGHT, rotate180(loadImage("5_2.png")));
            pieceImages2.put(PieceType.LANCE, rotate180(loadImage("6_2.png")));
            pieceImages2.put(PieceType.PAWN, rotate180(loadImage("7_2.png")));
            pieceImages2.put(PieceType.ROOK_P, rotate180(loadImage("8_2.png")));
            pieceImages2.put(PieceType.BISHOP_P, rotate180(loadImage("9_2.png")));
            pieceImages2.put(PieceType.SILVERGEN_P, rotate180(loadImage("10_2.png")));
            pieceImages2.put(PieceType.KNIGHT_P, rotate180(loadImage("11_2.png")));
            pieceImages2.put(PieceType.LANCE_P, rotate180(loadImage("12_2.png")));
            pieceImages2.put(PieceType.PAWN_P, rotate180(loadImage("13_2.png")));

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static BufferedImage getPieceImageByType(Integer player, PieceType type) {
        if (pieceImages == null) {
            loadImages();
        }

        if (player == 1) {
            return pieceImages.get(type);
        } else {
            return pieceImages2.get(type);
        }
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

    public Boolean hasSamePosition(Point point) {
        return this.x == point.x && this.y == point.y;
    }

    public BufferedImage getPieceImage() {
        if (pieceImages == null) {
            loadImages();
        }

        if (player == 1) {
            return pieceImages.get(this.getType());
        } else {
            return pieceImages2.get(this.getType());
        }
    }
}