import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ChessGUI {

    // Variáveis para contar as peças capturadas para ambos os jogadores

    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;
    private final JLabel playerOnePieceCountLabel = new JLabel();
    private final JLabel playerTwoPieceCountLabel = new JLabel();
    private final JLabel playerOneCapturedCountLabel = new JLabel();
    private final JLabel playerTwoCapturedCountLabel = new JLabel();
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private final JButton[][] chessBoardSquares = new JButton[9][9];
    private final JLabel turnPlay = new JLabel();
    private final JButton[] p1CapturedSquares = new JButton[7];
    private final JButton[] p2CapturedSquares = new JButton[7];
    private final Color mainbg = new Color(230, 230, 230);
    private final Color ochre = new Color(204, 119, 34);
    private final ChessController controller = new ChessController();
    public ChessPiece selectedPiece;
    public PromotionActions selectedPieceActions;
    public ArrayList<Point> readyToMoveSquares = new ArrayList<>();
    public ArrayList<Point> readyToCaptureSquares = new ArrayList<>();
    private JPanel chessBoard;
    private String modoDeJogo = "Não definido";
    private boolean daltonicMode = false;

    private Timer timer1, timer2; // cronometro
    private JLabel timerLabel1, timerLabel2; // cronometro

    public ChessGUI() {
        initializeGui();
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

    private void initializeTimers() {

        timerLabel1 = new JLabel("Jogador 1: 15:00");
        timerLabel2 = new JLabel("Jogador 2: 15:00");

        timer1 = new Timer(1000, e -> {
            controller.getPlayerOne().getTotalTime().getAndDecrement();
            int minutes = controller.getPlayerOne().getTotalTime().get() / 60;
            int seconds = controller.getPlayerOne().getTotalTime().get() % 60;
            timerLabel1.setText(String.format("Jogador 1: %02d:%02d", minutes, seconds));

            if (controller.getPlayerOne().getTotalTime().get() <= 0) {
                ((Timer) e.getSource()).stop();
                timerLabel1.setText("Jogador 1: 00:00");
            }
        });

        timer2 = new Timer(1000, e -> {
            controller.getPlayerTwo().getTotalTime().getAndDecrement();
            int minutes = controller.getPlayerTwo().getTotalTime().get() / 60;
            int seconds = controller.getPlayerTwo().getTotalTime().get() % 60;
            timerLabel2.setText(String.format("Jogador 2: %02d:%02d", minutes, seconds));

            if (controller.getPlayerTwo().getTotalTime().get() <= 0) {
                ((Timer) e.getSource()).stop();
                timerLabel2.setText("Jogador 2: 00:00");
            }
        });
    }

    // Atualiza labels de peças dos jogadores
    private void updatePieceCounts() {
        playerOnePieceCountLabel.setText("Peças do Jogador 1: " + controller.getPlayerOne().getPieceCount());
        if (controller.isPlayingWithAI()) {
            playerTwoPieceCountLabel.setText("Peças do IA Player: " + controller.getPlayerTwo().getPieceCount());
            playerTwoCapturedCountLabel.setText("Peças Capturadas pelo IA Player: " + controller.getPlayerTwo().getCapturedCount());
        } else {
            playerTwoPieceCountLabel.setText("Peças do Jogador 2: " + controller.getPlayerTwo().getPieceCount());
            playerTwoCapturedCountLabel.setText("Peças Capturadas pelo Jogador 2: " + controller.getPlayerTwo().getCapturedCount());
        }
        playerOneCapturedCountLabel.setText("Peças Capturadas pelo Jogador 1: " + controller.getPlayerOne().getCapturedCount());
    }

    // Método para reiniciar o jogo
    public void resetGame() {
        controller.resetPlayers();
        updatePieceCounts();
    }

    public void resetTime() {
        if (timer1.isRunning()) {
            timer1.stop();
        }
        if (timer2.isRunning()) {
            timer2.stop();
        }
        controller.getPlayerOne().getTotalTime().set(900);
        controller.getPlayerTwo().getTotalTime().set(900);

        timerLabel1.setText("Jogador 1: 15:00");
        timerLabel2.setText("Jogador 2: 15:00");

        timer1.start();

    }

    public boolean handlePieceMove(int x, int y) {
        boolean isHandled = false;
        for (Point rtm : readyToMoveSquares) {
            if (rtm.x == x && rtm.y == y) {
                // Move to Square
                selectedPiece.setX(rtm.x);
                selectedPiece.setY(rtm.y);

                if (PromotionActions.mustAdd) {
                    controller.getChessPieces().add(selectedPiece);
                    if (selectedPieceActions.mustAddplayer == 1) {
                        p1CapturedSquares[selectedPieceActions.mustAddbutIndex].setText(Integer.toString(
                                Integer.parseInt(p1CapturedSquares[selectedPieceActions.mustAddbutIndex].getText())
                                        - 1));
                    } else {
                        p2CapturedSquares[selectedPieceActions.mustAddbutIndex].setText(Integer.toString(
                                Integer.parseInt(p2CapturedSquares[selectedPieceActions.mustAddbutIndex].getText())
                                        - 1));
                    }
                    PromotionActions.mustAdd = false;
                }

                isHandled = true;
                controller.updateTurnAndMoveCount();
                break;
            }
        }
        return isHandled;
    }

    public boolean handlePieceCapture(int x, int y) {
        boolean isHandled = false;
        for (Point rtm : readyToCaptureSquares) {
            if (rtm.x == x && rtm.y == y) {
                if (selectedPiece.getPlayer() == PLAYER_ONE) {
                    for (ChessPiece chp : controller.getChessPieces()) {
                        if (chp.getX() == rtm.x && chp.getY() == rtm.y) {
                            // p1CapturedPieces.add(chp);
                            if (chp.getType() == PieceType.KING) {
                                JOptionPane.showMessageDialog(null, "Player 1 Wins !", "Game Over",
                                        JOptionPane.INFORMATION_MESSAGE);
                                handleWin();
                            }
                            addP1CapturedPiece(chp);
                            controller.getChessPieces().remove(chp);
                            controller.capturePiece(chp);
                            updatePieceCounts();
                            break;
                        }
                    }
                } else {
                    for (ChessPiece chp : controller.getChessPieces()) {
                        if (chp.getX() == rtm.x && chp.getY() == rtm.y) {
                            // p2CapturedPieces.add(chp);
                            if (chp.getType() == PieceType.KING) {
                                JOptionPane.showMessageDialog(null, "Player 2 Wins !", "Game Over",
                                        JOptionPane.INFORMATION_MESSAGE);
                                handleWin();
                            }
                            addP2CapturedPiece(chp);
                            controller.getChessPieces().remove(chp);
                            controller.capturePiece(chp);
                            updatePieceCounts();
                            break;
                        }
                    }
                }
                // Move to Square
                selectedPiece.setX(rtm.x);
                selectedPiece.setY(rtm.y);
                // redrawBoard();
                isHandled = true;
                controller.updateTurnAndMoveCount();
                break;
            }
        }

        return isHandled;
    }

    private void handleWin() {
        resetCapturedSquares();
        controller.setupNewGame();
        resetTime();
        redrawBoard();
        controller.setTurn(3);
    }

    private void checkPromotion(ChessPiece chp, int x, int y) {
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

    private void pieceMove(ChessPiece chp) {
        for (Point move : controller.getPossibleMoves(chp)) {
            int pf = (chp.getPlayer() == PLAYER_TWO) ? 1 : -1;
            int newx = (chp.getX() + move.x * pf);
            int newy = (chp.getY() + move.y * pf);

            if (newx >= 0 && newy >= 0 && newx <= 8 && newy <= 8) {
                if (controller.isOccupied(newx, newy) == 0) {
                    chessBoardSquares[newx][newy].setBackground(Color.cyan);
                    readyToMoveSquares.add(new Point(newx, newy));
                } else if (controller.isOccupied(newx, newy) != chp.getPlayer()) {
                    chessBoardSquares[newx][newy].setBackground(Color.magenta);
                    readyToCaptureSquares.add(new Point(newx, newy));
                }
            }
        }
    }

    public void handlePieceSelect(int x, int y) {
        for (ChessPiece chp : controller.getChessPieces()) {
            if (chp.getX() == x && chp.getY() == y) {
                // Select the Piece
                if (controller.getTurn() == chp.getPlayer()) {
                    selectedPiece = chp;
                    chessBoardSquares[x][y].setBackground(Color.green);
                    checkPromotion(chp, x, y);
                    pieceMove(chp);
                }
            }
        }
    }

    private PieceMove iAPlays() {

        PieceMove pm = controller.getCpuAI().playMove();
        System.out.println("AI Moving " + pm.chessPieceIndex + " to " + pm.finalPos.toString());

        return pm;

    }

    private void showWinner(PieceMove pm, ChessPiece pieceGoingToCapture) {

        if (controller.getChessPieces().get(pm.chessPieceIndex).getPlayer() == PLAYER_ONE) {
            if (pieceGoingToCapture.getType() == PieceType.KING) {
                JOptionPane.showMessageDialog(null, "Player 1 Wins !", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                handleWin();
            }
            addP1CapturedPiece(pieceGoingToCapture);
            controller.getChessPieces().remove(pieceGoingToCapture);
            controller.capturePiece(pieceGoingToCapture);
            updatePieceCounts();
        } else {
            if (pieceGoingToCapture.getType() == PieceType.KING) {
                JOptionPane.showMessageDialog(null, "Player 2 Wins !", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                handleWin();
            }
            addP2CapturedPiece(pieceGoingToCapture);
            controller.getChessPieces().remove(pieceGoingToCapture);
            controller.capturePiece(pieceGoingToCapture);
            updatePieceCounts();
        }

    }

    private boolean playGame() {

        boolean isHandled = false;

        if (controller.getTurn() == 2 && controller.isPlayingWithAI()) {
            PieceMove pm = iAPlays();

            if (!pm.isGoingToCapture) {
                controller.getChessPieces().get(pm.chessPieceIndex).setX(pm.finalPos.x);
                controller.getChessPieces().get(pm.chessPieceIndex).setY(pm.finalPos.y);
            } else {
                ChessPiece pieceGoingToCapture = controller.getPieceAt(pm.finalPos.x, pm.finalPos.y);

                showWinner(pm, pieceGoingToCapture);

                // Move to Square
                controller.getChessPieces().get(pm.chessPieceIndex).setX(pm.finalPos.x);
                controller.getChessPieces().get(pm.chessPieceIndex).setY(pm.finalPos.y);
            }
            controller.updateTurnAndMoveCount();
            isHandled = true;
        }
        return isHandled;
    }

    public void handleButtonPress(int x, int y) {

        boolean isHandled;

        handlePieceMove(x, y);

        handlePieceCapture(x, y);

        isHandled = playGame();

        selectedPiece = null;
        readyToMoveSquares = new ArrayList<>();
        readyToCaptureSquares = new ArrayList<>();

        redrawBoard();

        if (isHandled) {
            // muda pra contar tempo do outro jogador
            if (controller.getTurn() == 1) {
                timer2.stop();
                timer1.start();
            } else if (controller.getTurn() == 2) {
                timer1.stop();
                timer2.start();
            }
            return;
        }
        handlePieceSelect(x, y);
    }

    private void depromote(ChessPiece chp) {
        switch (chp.getType()) {
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

    private void addP1CapturedPiece(ChessPiece chp) {

        // Depromote
        depromote(chp);

        controller.getPlayerOne().getCapturedPieces().add(chp);

        switch (chp.getType()) {
            case KNIGHT:
                p1CapturedSquares[0].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[0].getText()) + 1));
                break;
            case BISHOP:
                p1CapturedSquares[1].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[1].getText()) + 1));
                break;
            case ROOK:
                p1CapturedSquares[2].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[2].getText()) + 1));
                break;
            case GOLDGEN:
                p1CapturedSquares[3].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[3].getText()) + 1));
                break;
            case SILVERGEN:
                p1CapturedSquares[4].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[4].getText()) + 1));
                break;
            case PAWN:
                p1CapturedSquares[5].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[5].getText()) + 1));
                break;
            case LANCE:
                p1CapturedSquares[6].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[6].getText()) + 1));
                break;
        }

        redrawBoard();
    }

    private void addP2CapturedPiece(ChessPiece chp) {

        // Depromote
        depromote(chp);

        controller.getPlayerTwo().getCapturedPieces().add(chp);

        switch (chp.getType()) {
            case KNIGHT:
                p2CapturedSquares[6].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[6].getText()) + 1));
                break;
            case BISHOP:
                p2CapturedSquares[4].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[4].getText()) + 1));
                break;
            case ROOK:
                p2CapturedSquares[5].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[5].getText()) + 1));
                break;
            case GOLDGEN:
                p2CapturedSquares[2].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[2].getText()) + 1));
                break;
            case SILVERGEN:
                p2CapturedSquares[3].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[3].getText()) + 1));
                break;
            case PAWN:
                p2CapturedSquares[0].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[0].getText()) + 1));
                break;
            case LANCE:
                p2CapturedSquares[1].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[1].getText()) + 1));
                break;
        }

        redrawBoard();
    }

    private boolean isPromotable(ChessPiece chp) {
        if (chp.getType() == PieceType.LANCE || chp.getType() == PieceType.PAWN || chp.getType() == PieceType.SILVERGEN
                || chp.getType() == PieceType.KNIGHT || chp.getType() == PieceType.ROOK
                || chp.getType() == PieceType.BISHOP) {
            if (chp.getPlayer() == PLAYER_ONE) {
                return chp.getY() <= 2;
            } else {
                return chp.getY() >= 6;
            }
        }
        return false;
    }

    public final void initializeGui() {
        ChessPiece.loadImages();
        initializeTimers(); // inicializa cronometro
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

        JPanel timerPanel = new JPanel(new GridLayout(1, 2));
        timerPanel.add(timerLabel1);
        timerPanel.add(timerLabel2);
        messagePanel.add(timerPanel, BorderLayout.SOUTH);

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
            JOptionPane.showMessageDialog(gui, instructions, "Instruções do Jogo Shogi",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        tools.add(instructionsButton);
        gui.add(tools, BorderLayout.NORTH);
    }

    private Action createGameAction(String name, boolean isAI) {
        return new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetCapturedSquares();
                resetGame();
                controller.setPlayingWithAI(isAI);
                controller.setupNewGame();
                resetTime();
                redrawBoard();
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
        ImageIcon[] images = playerNumber == PLAYER_ONE ? new ImageIcon[]{
                new ImageIcon(ChessPiece.getPieceImageByType(1, PieceType.KNIGHT)),
                new ImageIcon(ChessPiece.getPieceImageByType(1, PieceType.BISHOP)),
                new ImageIcon(ChessPiece.getPieceImageByType(1, PieceType.ROOK)),
                new ImageIcon(ChessPiece.getPieceImageByType(1, PieceType.GOLDGEN)),
                new ImageIcon(ChessPiece.getPieceImageByType(1, PieceType.SILVERGEN)),
                new ImageIcon(ChessPiece.getPieceImageByType(1, PieceType.PAWN)),
                new ImageIcon(ChessPiece.getPieceImageByType(1, PieceType.LANCE)),
        }
                : new ImageIcon[]{
                new ImageIcon(ChessPiece.getPieceImageByType(2, PieceType.PAWN)),
                new ImageIcon(ChessPiece.getPieceImageByType(2, PieceType.LANCE)),
                new ImageIcon(ChessPiece.getPieceImageByType(2, PieceType.GOLDGEN)),
                new ImageIcon(ChessPiece.getPieceImageByType(2, PieceType.SILVERGEN)),
                new ImageIcon(ChessPiece.getPieceImageByType(2, PieceType.BISHOP)),
                new ImageIcon(ChessPiece.getPieceImageByType(2, PieceType.ROOK)),
                new ImageIcon(ChessPiece.getPieceImageByType(2, PieceType.KNIGHT)),
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

    public void redrawBoard() {
        // Clear Previous State
        for (int ii = 0; ii < 9; ii++) {
            for (int jj = 0; jj < 9; jj++) {
                chessBoardSquares[ii][jj]
                        .setIcon(new ImageIcon(new BufferedImage(70, 80, BufferedImage.TYPE_INT_ARGB)));
                chessBoardSquares[ii][jj].setBackground(mainbg);
                // chessBoardSquares[ii][jj].setComponentPopupMenu(null);
            }
        }

        updatePieceCounts();

        if (controller.isPlayingWithAI()) {
            modoDeJogo = "Player VS IA";
        } else {
            modoDeJogo = "Player VS Player";
        }

        if (controller.getTurn() == 1) {
            timer2.stop();
            timer1.start();
            turnPlay.setText("Turno: Jogador 1. Total de jogadas: " + controller.getPlayerOne().getMoveCount() + " | Modo de jogo: "
                    + modoDeJogo);
        } else if (controller.getTurn() == 2) {
            timer1.stop();
            timer2.start();
            turnPlay.setText("Turno: Jogador 2. Total de jogadas: " + controller.getPlayerTwo().getMoveCount() + " | Modo de jogo: "
                    + modoDeJogo);
        } else if (controller.getTurn() == 3) {
            turnPlay.setText("Fim de jogo");
            resetGame();
        }

        for (ChessPiece chp : controller.getChessPieces()) {
            chessBoardSquares[chp.getX()][chp.getY()].setIcon(new ImageIcon(chp.getPieceImage()));
        }
    }

    // Initializes chess board piece places

    // Chess Square Buttons ActionListener
    private class ChessButtonActionListener implements ActionListener {
        private final int x;
        private final int y;

        public ChessButtonActionListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            handleButtonPress(x, y);
        }
    }

    // Promotion Button ActionListener
    private class PromoteButtonActionListener implements ActionListener {
        private final ChessPiece chp;

        public PromoteButtonActionListener(ChessPiece chp) {
            this.chp = chp;
        }

        public void actionPerformed(ActionEvent e) {
            switch (chp.getType()) {
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

    private class CapturedPieceButtonActionListener implements ActionListener {
        private final PieceType pieceKind;
        private final int player;
        private final int butIndex;

        public CapturedPieceButtonActionListener(int player, PieceType pieceKind, int butIndex) {
            this.player = player;
            this.pieceKind = pieceKind;
            this.butIndex = butIndex;
        }

        public void actionPerformed(ActionEvent e) {
            boolean b;
            if (controller.getTurn() == player) {
                if (player == PLAYER_ONE) {
                    b = Integer.parseInt(p1CapturedSquares[butIndex].getText()) > 0;
                } else {
                    b = Integer.parseInt(p2CapturedSquares[butIndex].getText()) > 0;
                }
                if (b) {
                    ChessPiece droppingPiece = new ChessPiece(player, pieceKind, 0, 0);
                    PromotionActions.mustAdd = true;
                    selectedPieceActions.mustAddbutIndex = butIndex;
                    selectedPieceActions.mustAddplayer = player;
                    selectedPiece = droppingPiece;
                    readyToMoveSquares = new ArrayList<>();
                    readyToCaptureSquares = new ArrayList<>();

                    redrawBoard();
                    if (pieceKind != PieceType.PAWN) {
                        for (int ii = 0; ii < 9; ii++) {
                            for (int jj = 0; jj < 9; jj++) {
                                if (controller.isOccupied(ii, jj) == 0) {
                                    // Select the Piece
                                    chessBoardSquares[ii][jj].setBackground(Color.cyan);
                                    readyToMoveSquares.add(new Point(ii, jj));
                                }
                            }
                        }
                    } else {
                        for (int ii = 0; ii < 9; ii++) {
                            boolean isHavePawn = false;
                            for (int jj = 0; jj < 9; jj++) {
                                // check for pawn
                                for (ChessPiece chp : controller.getChessPieces()) {
                                    if (chp.getX() == ii && chp.getY() == jj) {
                                        if (chp.getPlayer() == player && chp.getType() == PieceType.PAWN) {
                                            isHavePawn = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (!isHavePawn) {
                                for (int jj = 0; jj < 9; jj++) {
                                    if (controller.isOccupied(ii, jj) == 0) {
                                        // Select the Piece
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
}
