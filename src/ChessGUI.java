import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList;

public class ChessGUI{

    // Variáveis para contar as peças capturadas para ambos os jogadores
    private int playerOnePieceCount = 13;
    private int playerTwoPieceCount = 13;
    private int playerOneCapturedCount = 0;
    private int playerTwoCapturedCount = 0;
    private final JLabel playerOnePieceCountLabel = new JLabel();
    private final JLabel playerTwoPieceCountLabel = new JLabel();
    private final JLabel playerOneCapturedCountLabel = new JLabel();
    private final JLabel playerTwoCapturedCountLabel = new JLabel();

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private final JButton[][] chessBoardSquares = new JButton[9][9];

    public ArrayList<ChessPiece> chessPieces = new ArrayList<>();
    public ChessPiece selectedPiece;

    private final JLabel turnPlay = new JLabel();
    public PromotionActions selectedPieceActions;
    public ArrayList<Point> readyToMoveSquares = new ArrayList<>();
    public ArrayList<Point> readyToCaptureSquares = new ArrayList<>();

    public ArrayList<ChessPiece> p1CapturedPieces = new ArrayList<>();
    private final JButton[] p1CapturedSquares = new JButton[7];
    public ArrayList<ChessPiece> p2CapturedPieces = new ArrayList<>();
    private final JButton[] p2CapturedSquares = new JButton[7];

    private boolean isPlayingWithAI = false;

    public ChessAI cpuAI;

    private BufferedImage kingImage1;
    private BufferedImage rookImage1;
    private BufferedImage bishopImage1;
    private BufferedImage goldImage1;
    private BufferedImage silverImage1;
    private BufferedImage knightImage1;
    private BufferedImage lanceImage1;
    private BufferedImage pawnImage1;
    private BufferedImage rookPImage1;
    private BufferedImage bishopPImage1;
    private BufferedImage silverPImage1;
    private BufferedImage knightPImage1;
    private BufferedImage lancePImage1;
    private BufferedImage pawnPImage1;

    private BufferedImage kingImage2;
    private BufferedImage rookImage2;
    private BufferedImage bishopImage2;
    private BufferedImage goldImage2;
    private BufferedImage silverImage2;
    private BufferedImage knightImage2;
    private BufferedImage lanceImage2;
    private BufferedImage pawnImage2;
    private BufferedImage rookPImage2;
    private BufferedImage bishopPImage2;
    private BufferedImage silverPImage2;
    private BufferedImage knightPImage2;
    private BufferedImage lancePImage2;
    private BufferedImage pawnPImage2;

    private JPanel chessBoard;

    private int turn = 1;
    private int player1MoveCount = 0;
    private int player2MoveCount = 0;
    private String modoDeJogo = "Não definido";

    private final Color mainbg = new Color(230,230,230);
    private final Color ochre = new Color(204,119,34);

    public static final int PLAYER_ONE = 1;

    public static final int PLAYER_TWO = 2;
    private boolean daltonicMode = false;

    //Chess Square Buttons ActionListener
    private class ChessButtonActionListener implements ActionListener {
        private final int x;
        private final int y;

        public ChessButtonActionListener(int x,int y) {
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            handleButtonPress(x,y);
        }
    }


    private void updatePieceCounts() {
        playerOnePieceCountLabel.setText("Peças do Jogador 1: " + playerOnePieceCount);
        if (isPlayingWithAI) {
            playerTwoPieceCountLabel.setText("Peças do IA Player: " + playerTwoPieceCount);
            playerTwoCapturedCountLabel.setText("Peças Capturadas pelo IA Player: " + playerTwoCapturedCount);
        } else {
            playerTwoPieceCountLabel.setText("Peças do Jogador 2: " + playerTwoPieceCount);
            playerTwoCapturedCountLabel.setText("Peças Capturadas pelo Jogador 2: " + playerTwoCapturedCount);
        }
        playerOneCapturedCountLabel.setText("Peças Capturadas pelo Jogador 1: " + playerOneCapturedCount);
    }

    // Método para reiniciar o jogo
    public void resetGame() {
        playerOnePieceCount = 13;
        playerTwoPieceCount = 13;
        playerOneCapturedCount = 0;
        playerTwoCapturedCount = 0;
        updatePieceCounts();
    }

    // Método para capturar peças
    public void capturePiece(ChessPiece piece) {
        if (piece.getPlayer() != PLAYER_ONE) {
            playerOneCapturedCount++;
            playerTwoPieceCount--;
        } else {
            playerTwoCapturedCount++;
            playerOnePieceCount--;
        }
        updatePieceCounts();
    }

    //Promotion Button ActionListener
    private class PromoteButtonActionListener implements ActionListener {
        private final ChessPiece chp;

        public PromoteButtonActionListener(ChessPiece chp) {
            this.chp = chp;
        }

        public void actionPerformed(ActionEvent e) {
            switch(chp.getType()){
                case SILVERGEN:
                    chp.setType(PieceType.SILVERGEN_P);
                    break;
                case KNIGHT:
                    chp.setType(PieceType.KNIGHT_P);
                    break;
                case LANCE:
                    chp.setType(PieceType.LANCE_P);
                    break;
                case PAWN:
                    chp.setType(PieceType.PAWN_P);
                    break;
                case ROOK:
                    chp.setType(PieceType.ROOK_P);
                    break;
                case BISHOP:
                    chp.setType(PieceType.BISHOP_P);
            }
            redrawBoard();
        }
    }
    public boolean handlePieceMove (int x, int y) {
        boolean isHandled = false;
        for (Point rtm : readyToMoveSquares) {
            if (rtm.x == x && rtm.y == y) {
                //Move to Square
                selectedPiece.setX(rtm.x);
                selectedPiece.setY(rtm.y);

                if (selectedPieceActions.mustAdd) {
                    chessPieces.add(selectedPiece);
                    if (selectedPieceActions.mustAddplayer == 1) {
                        p1CapturedSquares[selectedPieceActions.mustAddbutIndex].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[selectedPieceActions.mustAddbutIndex].getText()) - 1));
                    } else {
                        p2CapturedSquares[selectedPieceActions.mustAddbutIndex].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[selectedPieceActions.mustAddbutIndex].getText()) - 1));
                    }
                    selectedPieceActions.mustAdd = false;
                }
                //redrawBoard();
                isHandled = true;
                if (turn == 1) {
                    player1MoveCount++;
                    turn = 2;
                } else if (turn == 2) {
                    player2MoveCount++;
                    turn = 1;
                }
                break;
            }
        }
        return isHandled;
    }

    public boolean handlePieceCapture (int x, int y) {
        boolean isHandled = false;
        for(Point rtm : readyToCaptureSquares){
            if(rtm.x == x && rtm.y == y) {
                if(selectedPiece.getPlayer() == PLAYER_ONE){
                    for(ChessPiece chp : chessPieces) {
                        if (chp.getX() == rtm.x && chp.getY() == rtm.y) {
                            //p1CapturedPieces.add(chp);
                            if(chp.getType() == PieceType.KING){
                                JOptionPane.showMessageDialog(null,"Player 1 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                                setupNewGame(isPlayingWithAI);
                                turn=3;
                            }
                            addP1CapturedPiece(chp);
                            chessPieces.remove(chp);
                            capturePiece(chp);
                            break;
                        }
                    }
                }else{
                    for(ChessPiece chp : chessPieces) {
                        if (chp.getX() == rtm.x && chp.getY() == rtm.y) {
                            //p2CapturedPieces.add(chp);
                            if(chp.getType() == PieceType.KING){
                                JOptionPane.showMessageDialog(null,"Player 2 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                                setupNewGame(isPlayingWithAI);
                                turn=3;
                            }
                            addP2CapturedPiece(chp);
                            chessPieces.remove(chp);
                            capturePiece(chp);
                            break;
                        }
                    }
                }
                //Move to Square
                selectedPiece.setX(rtm.x);
                selectedPiece.setY(rtm.y);
                //redrawBoard();
                isHandled=true;
                if (turn == 1) {
                    player1MoveCount++;
                    turn = 2;
                } else if(turn==2) {
                    player2MoveCount++;
                    turn = 1;
                }
                break;
            }
        }

        return isHandled;
    }

    private void checkPromotion (ChessPiece chp, int x, int y) {
        if (isPromotable(chp)) {
            JPopupMenu popup = new JPopupMenu();
            JMenuItem promoteButton = new JMenuItem("Promote !");
            promoteButton.addActionListener(new PromoteButtonActionListener(chp));
            popup.add(promoteButton);
            chessBoardSquares[x][y].setComponentPopupMenu(popup);
        } else {
            chessBoardSquares[x][y].setComponentPopupMenu(null);
        }
    }

    private void pieceMove (ChessPiece chp) {
        for (Point move : getPossibleMoves(chp)) {
            int pf = (chp.getPlayer() == PLAYER_TWO) ? 1 : -1;
            int newx = (chp.getX() + move.x * pf);
            int newy = (chp.getY() + move.y * pf);

            if (newx >= 0 && newy >= 0 && newx <= 8 && newy <= 8) {
                if (isOccupied(newx, newy) == 0) {
                    chessBoardSquares[newx][newy].setBackground(Color.cyan);
                    readyToMoveSquares.add(new Point(newx, newy));
                } else if (isOccupied(newx, newy) != chp.getPlayer()) {
                    chessBoardSquares[newx][newy].setBackground(Color.magenta);
                    readyToCaptureSquares.add(new Point(newx, newy));
                }
            }
        }
    }
    public void handlePieceSelect(int x, int y) {
        for(ChessPiece chp : chessPieces){
            if(chp.getX() == x && chp.getY() == y) {
                //Select the Piece
                if (turn == chp.getPlayer()) {
                    selectedPiece = chp;
                    chessBoardSquares[x][y].setBackground(Color.green);
                    checkPromotion(chp, x, y);
                    pieceMove(chp);
                }
            }
        }
    }

    private PieceMove iAPlays () {

        PieceMove pm = cpuAI.playMove();
        System.out.println("AI Moving " + pm.chessPieceIndex + " to " + pm.finalPos.toString());

        return pm;

    }

    private void showWinner (PieceMove pm, ChessPiece pieceGoingToCapture ) {

        if(chessPieces.get(pm.chessPieceIndex).getPlayer() == PLAYER_ONE){
            if(pieceGoingToCapture.getType() == PieceType.KING){
                JOptionPane.showMessageDialog(null,"Player 1 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                setupNewGame(isPlayingWithAI);
                turn=3;
            }
            addP1CapturedPiece(pieceGoingToCapture);
            chessPieces.remove(pieceGoingToCapture);
            capturePiece(pieceGoingToCapture);
        }else{
            if(pieceGoingToCapture.getType() == PieceType.KING){
                JOptionPane.showMessageDialog(null,"Player 2 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                setupNewGame(isPlayingWithAI);
                turn=3;
            }
            addP2CapturedPiece(pieceGoingToCapture);
            chessPieces.remove(pieceGoingToCapture);
            capturePiece(pieceGoingToCapture);
        }

    }

    private boolean playGame() {

        boolean isHandled = false;

        if(turn==2 && isPlayingWithAI){
            PieceMove pm = iAPlays();

            if(!pm.isGoingToCapture){
                chessPieces.get(pm.chessPieceIndex).setX(pm.finalPos.x);
                chessPieces.get(pm.chessPieceIndex).setY(pm.finalPos.y);
            }else{
                ChessPiece pieceGoingToCapture = getPieceAt(pm.finalPos.x,pm.finalPos.y);

                showWinner(pm, pieceGoingToCapture);

                //Move to Square
                chessPieces.get(pm.chessPieceIndex).setX(pm.finalPos.x);
                chessPieces.get(pm.chessPieceIndex).setY(pm.finalPos.y);
            }
            if (turn == 1) {
                player1MoveCount++;
                turn = 2;
            } else if(turn==2) {
                player2MoveCount++;
                turn = 1;
            }
            isHandled = true;
        }
        return isHandled;
    }
    public void handleButtonPress(int x,int y){

        boolean isHandled;

        handlePieceMove(x, y);

        handlePieceCapture(x, y);

       isHandled = playGame();


        selectedPiece = null;
        readyToMoveSquares = new ArrayList<>();
        readyToCaptureSquares = new ArrayList<>();

        redrawBoard();

        if(isHandled){
            return;
        }
        handlePieceSelect(x, y);
    }

    private class CapturedPieceButtonActionListener implements ActionListener {
        private final PieceType pieceKind;
        private final int player;
        private final int butIndex;

        public CapturedPieceButtonActionListener(int player,PieceType pieceKind,int butIndex) {
            this.player = player;
            this.pieceKind = pieceKind;
            this.butIndex = butIndex;
        }

        public void actionPerformed(ActionEvent e) {
            boolean b;
            if (turn == player) {
                if (player == PLAYER_ONE) {
                    b = Integer.parseInt(p1CapturedSquares[butIndex].getText()) > 0;
                } else {
                    b = Integer.parseInt(p2CapturedSquares[butIndex].getText()) > 0;
                }
                if (b) {
                    ChessPiece droppingPiece = new ChessPiece(player, pieceKind, 0, 0);
                    selectedPieceActions.mustAdd = true;
                    selectedPieceActions.mustAddbutIndex = butIndex;
                    selectedPieceActions.mustAddplayer = player;
                    selectedPiece = droppingPiece;
                    readyToMoveSquares = new ArrayList<>();
                    readyToCaptureSquares = new ArrayList<>();

                    redrawBoard();
                    if (pieceKind != PieceType.PAWN) {
                        for (int ii = 0; ii < 9; ii++) {
                            for (int jj = 0; jj < 9; jj++) {
                                if (isOccupied(ii, jj) == 0) {
                                    //Select the Piece
                                    chessBoardSquares[ii][jj].setBackground(Color.cyan);
                                    readyToMoveSquares.add(new Point(ii, jj));
                                }
                            }
                        }
                    }else{
                        for(int ii=0;ii<9;ii++){
                            boolean isHavePawn = false;
                            for(int jj=0;jj<9;jj++){
                                //check for pawn
                                for(ChessPiece chp : chessPieces){
                                    if(chp.getX() == ii && chp.getY() == jj){
                                        if(chp.getPlayer() == player && chp.getType() == PieceType.PAWN){
                                            isHavePawn = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(!isHavePawn){
                                for (int jj = 0; jj < 9; jj++) {
                                    if (isOccupied(ii, jj) == 0) {
                                        //Select the Piece
                                        chessBoardSquares[ii][jj].setBackground(Color.cyan);
                                        readyToMoveSquares.add(new Point(ii, jj));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void depromote(ChessPiece chp){
        switch(chp.getType()){
            case SILVERGEN_P:
                chp.setType(PieceType.SILVERGEN);
                break;
            case KNIGHT_P:
                chp.setType(PieceType.KNIGHT);
                break;
            case LANCE_P:
                chp.setType(PieceType.LANCE);
                break;
            case PAWN_P:
                chp.setType(PieceType.PAWN);
                break;
            case ROOK_P:
                chp.setType(PieceType.ROOK);
                break;
            case BISHOP_P:
                chp.setType(PieceType.BISHOP);
                break;
        }
    }

    private void addP1CapturedPiece(ChessPiece chp){

        //Depromote
        depromote(chp);

        p1CapturedPieces.add(chp);

        switch(chp.getType()){
            case KNIGHT:
                p1CapturedSquares[0].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[0].getText())+1));
                break;
            case BISHOP:
                p1CapturedSquares[1].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[1].getText())+1));
                break;
            case ROOK:
                p1CapturedSquares[2].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[2].getText())+1));
                break;
            case GOLDGEN:
                p1CapturedSquares[3].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[3].getText())+1));
                break;
            case SILVERGEN:
                p1CapturedSquares[4].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[4].getText())+1));
                break;
            case PAWN:
                p1CapturedSquares[5].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[5].getText())+1));
                break;
            case LANCE:
                p1CapturedSquares[6].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[6].getText())+1));
                break;
        }

        redrawBoard();
    }

    private void addP2CapturedPiece(ChessPiece chp){

        //Depromote
        depromote(chp);

        p2CapturedPieces.add(chp);

        switch(chp.getType()){
            case KNIGHT:
                p2CapturedSquares[6].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[6].getText())+1));
                break;
            case BISHOP:
                p2CapturedSquares[4].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[4].getText())+1));
                break;
            case ROOK:
                p2CapturedSquares[5].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[5].getText())+1));
                break;
            case GOLDGEN:
                p2CapturedSquares[2].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[2].getText())+1));
                break;
            case SILVERGEN:
                p2CapturedSquares[3].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[3].getText())+1));
                break;
            case PAWN:
                p2CapturedSquares[0].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[0].getText())+1));
                break;
            case LANCE:
                p2CapturedSquares[1].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[1].getText())+1));
                break;
        }

        redrawBoard();
    }

    //TODO Add other pieces moves
    //Get Each Piece Possible Moves
    public ArrayList<Point> getPossibleMoves(ChessPiece chp){
        ArrayList<Point> possibleMoves = new ArrayList<>();
        int pf = (chp.getPlayer() == PLAYER_TWO) ? 1 : -1;
        switch(chp.getType()){
            case KING:
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(-1,0));
                possibleMoves.add(new Point(1,0));
                possibleMoves.add(new Point(-1,-1));
                possibleMoves.add(new Point(0,-1));
                possibleMoves.add(new Point(1,-1));
                break;
            case ROOK:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,0));
                    if(isOccupied(chp.getX() + pf*r,chp.getY())!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,0));
                    if(isOccupied(chp.getX() - pf*r,chp.getY())!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,r));
                    if(isOccupied(chp.getX(),chp.getY() + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,-r));
                    if(isOccupied(chp.getX(),chp.getY() - pf*r)!=0) break;
                }
                break;
            case BISHOP:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,r));
                    if(isOccupied(chp.getX() + pf*r,chp.getY() + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,-r));
                    if(isOccupied(chp.getX() - pf*r,chp.getY() - pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,r));
                    if(isOccupied(chp.getX() - pf*r,chp.getY() + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,-r));
                    if(isOccupied(chp.getX() + pf*r,chp.getY() - pf*r)!=0) break;
                }
                break;
            case GOLDGEN:
            case SILVERGEN_P:
            case KNIGHT_P:
            case LANCE_P:
            case PAWN_P:
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(-1,0));
                possibleMoves.add(new Point(1,0));
                possibleMoves.add(new Point(0,-1));
                break;
            case SILVERGEN:
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(-1,-1));
                possibleMoves.add(new Point(1,-1));
                break;
            case KNIGHT:
                possibleMoves.add(new Point(-1,2));
                possibleMoves.add(new Point(1,2));
                break;
            case LANCE:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,r));
                    if(isOccupied(chp.getX(),chp.getY() + pf*r)!=0) break;
                }
                break;
            case PAWN:
                possibleMoves.add(new Point(0,1));
                break;
            case ROOK_P:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,0));
                    if(isOccupied(chp.getX() + pf*r,chp.getY())!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,0));
                    if(isOccupied(chp.getX() - pf*r,chp.getY())!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,r));
                    if(isOccupied(chp.getX(),chp.getY() + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,-r));
                    if(isOccupied(chp.getX(),chp.getY() - pf*r)!=0) break;
                }
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(1,-1));
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(-1,-1));
                break;
            case BISHOP_P:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,r));
                    if(isOccupied(chp.getX() + pf*r,chp.getY() + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,-r));
                    if(isOccupied(chp.getX() - pf*r,chp.getY() - pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,r));
                    if(isOccupied(chp.getX() - pf*r,chp.getY() + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,-r));
                    if(isOccupied(chp.getX() + pf*r,chp.getY() - pf*r)!=0) break;
                }
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(0,-1));
                possibleMoves.add(new Point(-1,0));
                possibleMoves.add(new Point(1,0));
                break;

        }
        return possibleMoves;
    }

    // 0 1 2
    public int isOccupied(int x, int y) {
        for(ChessPiece chp : chessPieces){
            if(chp.getX() == x && chp.getY() == y){
                if(chp.getPlayer() == PLAYER_ONE){
                    return 1;
                }else{
                    return 2;
                }
            }
        }
        return 0; //not occupied
    }


    public ChessPiece getPieceAt(int x, int y) {
        for(ChessPiece chp : chessPieces){
            if(chp.getX() == x && chp.getY() == y){
                return chp;
            }
        }
        return null;
    }

    private boolean isPromotable(ChessPiece chp) {
        if (chp.getType() == PieceType.LANCE || chp.getType() == PieceType.PAWN || chp.getType() == PieceType.SILVERGEN
                || chp.getType() == PieceType.KNIGHT || chp.getType() == PieceType.ROOK || chp.getType() == PieceType.BISHOP){
            if (chp.getPlayer() == PLAYER_ONE) {
                if (chp.getY() <= 2) {
                    return true;
                }
            } else {
                if (chp.getY() >= 6) {
                    return true;
                }
            }
        }
        return false;
    }

    public ChessGUI() {
        initializeGui();
    }

    public final void initializeGui() {
        loadImages();
        setupToolBar();
        setupMainPanel();
        setupChessBoard();

        JButton modoDaltonicoButton = new JButton("Modo Daltonico");
        modoDaltonicoButton.addActionListener(e -> toggleDaltonicMode());

        // Criar painéis para cada jogador
        JPanel playerOnePanel = new JPanel(new GridLayout(2, 1));
        playerOnePanel.setBorder(BorderFactory.createTitledBorder("Jogador 1"));
        playerOnePanel.add(playerOnePieceCountLabel);
        playerOnePanel.add(playerOneCapturedCountLabel);

        JPanel playerTwoPanel = new JPanel(new GridLayout(2, 1));
        playerTwoPanel.setBorder(BorderFactory.createTitledBorder("Jogador 2 / IA"));
        playerTwoPanel.add(playerTwoPieceCountLabel);
        playerTwoPanel.add(playerTwoCapturedCountLabel);

        // Criar um painel principal com GridLayout
        JPanel mainInfoPanel = new JPanel(new GridLayout(1, 2));
        mainInfoPanel.add(playerOnePanel);
        mainInfoPanel.add(playerTwoPanel);

        gui.add(mainInfoPanel, BorderLayout.PAGE_END);

        resetGame(); // Reiniciar contadores na inicialização do jogo


        updatePieceCounts(); // Atualizar na inicialização

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(modoDaltonicoButton);
        gui.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void toggleDaltonicMode() {
        daltonicMode = !daltonicMode;
        Color newColor = daltonicMode ? Color.GRAY : new Color(204, 119, 34);
        gui.setBackground(newColor);
        for (Component component : gui.getComponents()) {
            updateComponentColors(component, newColor);
        }
    }

    private void updateComponentColors(Component component, Color newColor) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.setBackground(newColor);
            for (Component child : panel.getComponents()) {
                updateComponentColors(child, newColor);
            }
        } else if (component instanceof JButton) {
            JButton button = (JButton) component;
            if (button.getBackground().equals(new Color(204, 119, 34)) || button.getBackground().equals(Color.GRAY)) {
                button.setBackground(newColor);
            }
        } else {
            component.setBackground(newColor);
        }
    }

    private void resetCapturedPieces() {
        p1CapturedPieces.clear();
        p2CapturedPieces.clear();

        resetCapturedSquares();
    }

    private void resetCapturedSquares() {
        for (int i = 0; i < p1CapturedSquares.length; i++) {
            p1CapturedSquares[i].setText("0");
        }
        for (int i = 0; i < p2CapturedSquares.length; i++) {
            p2CapturedSquares[i].setText("0");
        }
    }

    private void setupToolBar() {
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);

        gui.add(tools, BorderLayout.NORTH);

        turnPlay.setHorizontalAlignment(JLabel.CENTER);
        turnPlay.setForeground(Color.RED);
        turnPlay.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(turnPlay, BorderLayout.CENTER);
        tools.add(messagePanel);

        gui.setBackground(ochre);
        tools.add(createGameAction("Novo Jogo (2 Jogadores)", false));
        tools.add(createGameAction("Novo Jogo (Contra IA)", true));

        JButton instructionsButton = new JButton("Instruções");
        instructionsButton.addActionListener(e -> {
            String instructions = "Instruções do Jogo Shogi:\n\n"
                    + "1. O Shogi é um jogo de tabuleiro japonês similar ao xadrez.\n"
                    + "2. O objetivo do jogo é capturar o rei do oponente.\n"
                    + "3. Cada jogador controla um exército de 20 peças, com diferentes movimentos.\n"
                    + "4. Uma característica única do Shogi é a 'promoção', onde certas peças ganham novos movimentos ao avançar no tabuleiro.\n"
                    + "5. Outra característica única é a 'gota', onde peças capturadas podem ser reaproveitadas no tabuleiro como parte do seu exército.\n"
                    + "6. O jogo termina quando um dos reis é capturado ou um jogador se rende.\n"
                    + "7. Utilize as opções do menu para iniciar um novo jogo para 2 jogadores ou para jogar contra a IA.";
            JOptionPane.showMessageDialog(gui, instructions, "Instruções do Jogo Shogi", JOptionPane.INFORMATION_MESSAGE);
        });

        tools.add(instructionsButton);
        gui.add(tools, BorderLayout.NORTH);
    }

    private Action createGameAction(String name, boolean isAI) {
        return new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame(isAI);
            }
        };
    }

    private void setupMainPanel() {
        JPanel cp = new JPanel(new BorderLayout());
        cp.setBackground(ochre);
        cp.setBorder(new EmptyBorder(9, 9, 9, 9));

        cp.add(setupCapturedPiecesPanel(1), BorderLayout.SOUTH);
        cp.add(setupCapturedPiecesPanel(2), BorderLayout.NORTH);

        gui.add(cp, BorderLayout.WEST);
    }

    private JPanel setupCapturedPiecesPanel(int playerNumber) {
        JPanel panel = new JPanel(new GridLayout(4, 4));
        panel.setBackground(ochre);
        JButton[] capturedSquares = playerNumber == PLAYER_ONE ? p1CapturedSquares : p2CapturedSquares;
        ImageIcon[] images = playerNumber == PLAYER_ONE ?
                new ImageIcon[] {
                        new ImageIcon(knightImage1),
                        new ImageIcon(bishopImage1),
                        new ImageIcon(rookImage1),
                        new ImageIcon(goldImage1),
                        new ImageIcon(silverImage1),
                        new ImageIcon(pawnImage1),
                        new ImageIcon(lanceImage1)
                } :
                new ImageIcon[] {
                        new ImageIcon(pawnImage2),
                        new ImageIcon(lanceImage2),
                        new ImageIcon(goldImage2),
                        new ImageIcon(silverImage2),
                        new ImageIcon(bishopImage2),
                        new ImageIcon(rookImage2),
                        new ImageIcon(knightImage2)
                };

        PieceType[] types = {
                PieceType.PAWN, PieceType.LANCE, PieceType.KNIGHT,
                PieceType.SILVERGEN, PieceType.GOLDGEN, PieceType.BISHOP, PieceType.ROOK
        };

        for (int i = 0; i < capturedSquares.length; i++) {
            capturedSquares[i] = createCapturedPieceButton("0", images[i].getImage(), playerNumber, types[i], i);
            panel.add(capturedSquares[i]);
        }

        return panel;
    }

    private JButton createCapturedPieceButton(String text, Image image, int playerNumber, PieceType type, int index) {
        JButton button = new JButton(text, new ImageIcon(image));
        button.addActionListener(new CapturedPieceButtonActionListener(playerNumber, type, index));
        styleButton(button);
        return button;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 28));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    private void setupChessBoard() {
        chessBoard = new JPanel(new GridLayout(9, 9));
        chessBoard.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), new LineBorder(Color.BLACK)));
        chessBoard.setBackground(ochre);

        // Preenchendo o tabuleiro verticalmente
        for (int jj = 0; jj < chessBoardSquares[0].length; jj++) { // Itera pelas colunas
            for (int ii = 0; ii < chessBoardSquares.length; ii++) { // Itera pelas linhas
                chessBoardSquares[ii][jj] = createSquareButton(ii, jj);
                chessBoard.add(chessBoardSquares[ii][jj]);
            }
        }
        gui.add(chessBoard);
    }

    private JButton createSquareButton(int ii, int jj) {
        JButton button = new JButton();
        button.setMargin(new Insets(2, 2, 2, 2));
        button.setIcon(new ImageIcon(new BufferedImage(70, 80, BufferedImage.TYPE_INT_ARGB)));
        button.addActionListener(new ChessButtonActionListener(ii, jj)); // Passando coordenadas como argumentos
        button.setBackground(new Color(230, 230, 230));
        return button;
    }

    public final JComponent getGui() {
        return gui;
    }

    //Load Piece Images from Resource
    private void loadImages() {
        try{
            kingImage1 = ImageIO.read(this.getClass().getResource("0.png"));
        rookImage1 = ImageIO.read(this.getClass().getResource("1.png"));
        bishopImage1 = ImageIO.read(this.getClass().getResource("2.png"));
        goldImage1 = ImageIO.read(this.getClass().getResource("3.png"));
        silverImage1 = ImageIO.read(this.getClass().getResource("4.png"));
        knightImage1 = ImageIO.read(this.getClass().getResource("5.png"));
        lanceImage1 = ImageIO.read(this.getClass().getResource("6.png"));
        pawnImage1 = ImageIO.read(this.getClass().getResource("7.png"));
        rookPImage1 = ImageIO.read(this.getClass().getResource("8.png"));
        bishopPImage1 = ImageIO.read(this.getClass().getResource("9.png"));
        silverPImage1 = ImageIO.read(this.getClass().getResource("10.png"));
        knightPImage1 = ImageIO.read(this.getClass().getResource("11.png"));
        lancePImage1 = ImageIO.read(this.getClass().getResource("12.png"));
        pawnPImage1 = ImageIO.read(this.getClass().getResource("13.png"));

        kingImage2 = rotate180(ImageIO.read(this.getClass().getResource("0_2.png")));
        rookImage2 = rotate180(ImageIO.read(this.getClass().getResource("1_2.png")));
        bishopImage2 = rotate180(ImageIO.read(this.getClass().getResource("2_2.png")));
        goldImage2 = rotate180(ImageIO.read(this.getClass().getResource("3_2.png")));
        silverImage2 = rotate180(ImageIO.read(this.getClass().getResource("4_2.png")));
        knightImage2 = rotate180(ImageIO.read(this.getClass().getResource("5_2.png")));
        lanceImage2 = rotate180(ImageIO.read(this.getClass().getResource("6_2.png")));
        pawnImage2 = rotate180(ImageIO.read(this.getClass().getResource("7_2.png")));
        rookPImage2 = rotate180(ImageIO.read(this.getClass().getResource("8_2.png")));
        bishopPImage2 = rotate180(ImageIO.read(this.getClass().getResource("9_2.png")));
        silverPImage2 = rotate180(ImageIO.read(this.getClass().getResource("10_2.png")));
        knightPImage2 = rotate180(ImageIO.read(this.getClass().getResource("11_2.png")));
        lancePImage2 = rotate180(ImageIO.read(this.getClass().getResource("12_2.png")));
        pawnPImage2 = rotate180(ImageIO.read(this.getClass().getResource("13_2.png")));
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    //Initializes chess board piece places
    private void setupNewGame(boolean isWithAI) {

        player1MoveCount = 0;
        player2MoveCount = 0;
        isPlayingWithAI = isWithAI;

        resetCapturedPieces();

        if(isWithAI) cpuAI = new ChessAI(this);

        chessPieces = new ArrayList<>();

        chessPieces.add(new ChessPiece(1, PieceType.LANCE,0,8));
        chessPieces.add(new ChessPiece(1, PieceType.KNIGHT,1,8));
        chessPieces.add(new ChessPiece(1, PieceType.SILVERGEN,2,8));
        chessPieces.add(new ChessPiece(1, PieceType.GOLDGEN,3,8));
        chessPieces.add(new ChessPiece(1, PieceType.KING,4,8));
        chessPieces.add(new ChessPiece(1, PieceType.GOLDGEN,5,8));
        chessPieces.add(new ChessPiece(1, PieceType.SILVERGEN,6,8));
        chessPieces.add(new ChessPiece(1, PieceType.KNIGHT,7,8));
        chessPieces.add(new ChessPiece(1, PieceType.LANCE,8,8));

        chessPieces.add(new ChessPiece(1, PieceType.BISHOP,1,7));
        chessPieces.add(new ChessPiece(1, PieceType.ROOK,7,7));
        
        for(int pawni=0;pawni<9;pawni++){
            chessPieces.add(new ChessPiece(1, PieceType.PAWN,pawni,6));
        }

        chessPieces.add(new ChessPiece(2, PieceType.LANCE,0,0));
        chessPieces.add(new ChessPiece(2, PieceType.KNIGHT,1,0));
        chessPieces.add(new ChessPiece(2, PieceType.SILVERGEN,2,0));
        chessPieces.add(new ChessPiece(2, PieceType.GOLDGEN,3,0));
        chessPieces.add(new ChessPiece(2, PieceType.KING,4,0));
        chessPieces.add(new ChessPiece(2, PieceType.GOLDGEN,5,0));
        chessPieces.add(new ChessPiece(2, PieceType.SILVERGEN,6,0));
        chessPieces.add(new ChessPiece(2, PieceType.KNIGHT,7,0));
        chessPieces.add(new ChessPiece(2, PieceType.LANCE,8,0));

        chessPieces.add(new ChessPiece(2, PieceType.ROOK,1,1));
        chessPieces.add(new ChessPiece(2, PieceType.BISHOP,7,1));

        for(int pawni=0;pawni<9;pawni++){
            chessPieces.add(new ChessPiece(2, PieceType.PAWN,pawni,2));
        }

        turn = 1;

        redrawBoard();
    }

    public BufferedImage rotate180(BufferedImage bi) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.PI, bi.getWidth() / 2, bi.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
        return op.filter(bi, null);
    }

    public void redrawBoard(){
        //Clear Previous State
        for(int ii=0;ii<9;ii++){
            for(int jj=0;jj<9;jj++){
                chessBoardSquares[ii][jj].setIcon(new ImageIcon(new BufferedImage(70, 80, BufferedImage.TYPE_INT_ARGB)));
                chessBoardSquares[ii][jj].setBackground(mainbg);
                //chessBoardSquares[ii][jj].setComponentPopupMenu(null);
            }
        }

        updatePieceCounts();

        if (isPlayingWithAI){
            modoDeJogo = "Player VS IA";
        }else{
            modoDeJogo = "Player VS Player";
        }

        if (turn == 1) {
            turnPlay.setText("Turno: Jogador 1. Total de jogadas: " + player1MoveCount + " | Modo de jogo: " + modoDeJogo);
        } else if (turn == 2) {
            turnPlay.setText("Turno: Jogador 2. Total de jogadas: " + player2MoveCount + " | Modo de jogo: " + modoDeJogo);
        } else if (turn == 3) {
            turnPlay.setText("Fim de jogo");
            resetGame();
        }

        for(ChessPiece chp : chessPieces){
            if(chp.getPlayer() == PLAYER_ONE){
                switch(chp.getType()){
                    case KING:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(kingImage1));
                        break;
                    case ROOK:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(rookImage1));
                        break;
                    case BISHOP:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(bishopImage1));
                        break;
                    case GOLDGEN:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(goldImage1));
                        break;
                    case SILVERGEN:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(silverImage1));
                        break;
                    case KNIGHT:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(knightImage1));
                        break;
                    case LANCE:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(lanceImage1));
                        break;
                    case PAWN:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(pawnImage1));
                        break;
                    case ROOK_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(rookPImage1));
                        break;
                    case BISHOP_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(bishopPImage1));
                        break;
                    case SILVERGEN_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(silverPImage1));
                        break;
                    case KNIGHT_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(knightPImage1));
                        break;
                    case LANCE_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(lancePImage1));
                        break;
                    case PAWN_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(pawnPImage1));
                        break;
                }
            }else{
                switch(chp.getType()){
                    case KING:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(kingImage2));
                        break;
                    case ROOK:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(rookImage2));
                        break;
                    case BISHOP:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(bishopImage2));
                        break;
                    case GOLDGEN:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(goldImage2));
                        break;
                    case SILVERGEN:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(silverImage2));
                        break;
                    case KNIGHT:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(knightImage2));
                        break;
                    case LANCE:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(lanceImage2));
                        break;
                    case PAWN:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(pawnImage2));
                        break;
                    case ROOK_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(rookPImage2));
                        break;
                    case BISHOP_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(bishopPImage2));
                        break;
                    case SILVERGEN_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(silverPImage2));
                        break;
                    case KNIGHT_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(knightPImage2));
                        break;
                    case LANCE_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(lancePImage2));
                        break;
                    case PAWN_P:
                        chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(pawnPImage2));
                        break;
                }
            }
        }
    }


    public static void main(String[] args) {
        Runnable r = () -> {
            ChessGUI cg = new ChessGUI();

            JFrame f = new JFrame("Shogi v0.9");
            f.add(cg.getGui());
            // Ensures JVM closes after frame(s) closed and
            // all non-daemon threads are finished
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // See http://stackoverflow.com/a/7143398/418556 for demo.
            f.setLocationByPlatform(true);

            // ensures the frame is the minimum size it needs to be
            // in order display the components within it
            f.pack();
            // ensures the minimum size is enforced.
            f.setMinimumSize(f.getSize());
            f.setVisible(true);
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }
}