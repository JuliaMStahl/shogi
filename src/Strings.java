import java.util.HashMap;
import java.util.Map;

public class Strings {
    private Map<StringsEnum, String> portuguese;
    private Map<StringsEnum, String> english;

    public Map<StringsEnum, String> getPortuguese() {
        return portuguese;
    }

    public Map<StringsEnum, String> getEnglish() {
        return english;
    }

    public void StartStrings() {
        populatePortuguese();
        populateEnglish();
    }

    private void populatePortuguese() {
        portuguese = new HashMap<StringsEnum, String>();
        portuguese.put(StringsEnum.PROMOTE, "Promover!");
        portuguese.put(StringsEnum.NEW_GAME, "Novo Jogo");
        portuguese.put(StringsEnum.PLAYER, "Jogador");
        portuguese.put(StringsEnum.WINS, "Venceu!");
        portuguese.put(StringsEnum.TWO_PLAYERS, " (2 Jogadores)");
        portuguese.put(StringsEnum.AGAINST_AI, " (Contra a IA)");
        portuguese.put(StringsEnum.GAME_TITLE, "Shogi v0.9");
        portuguese.put(StringsEnum.PLAYER_ONE_LABEL, "Peças do Jogador 1: ");
        portuguese.put(StringsEnum.PLAYER_TWO_LABEL, "Peças do Jogador 2: ");
        portuguese.put(StringsEnum.PLAYER_ONE_CAPTURED_LABEL, "Peças Capturadas pelo Jogador 1: ");
        portuguese.put(StringsEnum.PLAYER_TWO_CAPTURED_LABEL, "Peças Capturadas pelo Jogador 2: ");
        portuguese.put(StringsEnum.PLAYER_AI_LABEL, "Jogador IA: ");
        portuguese.put(StringsEnum.PLAYER_AI_CAPTURED_LABEL, "Peças Capturadas pela IA: ");
        portuguese.put(StringsEnum.PLAYER_ONE_TIMER_LABEL, "Jogador 1: ");
        portuguese.put(StringsEnum.PLAYER_TWO_TIMER_LABEL, "Jogador 2: ");
        portuguese.put(StringsEnum.TURN_LABEL, "Turno: Jogador 1. Total de jogadas: ");
        portuguese.put(StringsEnum.TURN_LABEL_2, "Turno: Jogador 2. Total de jogadas: ");
        portuguese.put(StringsEnum.GAME_MODE, " | Modo de jogo: ");
        portuguese.put(StringsEnum.GAME_MODE_PVE, "Jogador VS IA");
        portuguese.put(StringsEnum.GAME_MODE_PVP, "Jogador VS Jogador");
        portuguese.put(StringsEnum.GAME_OVER_TITLE, "Game Over");
        portuguese.put(StringsEnum.INSTRUCTIONS, "Instruções");
        portuguese.put(StringsEnum.INSTRUCTIONS_TITLE, "Instruções do Jogo Shogi");
        portuguese.put(StringsEnum.INSTRUCTIONS_MESSAGE, "Instruções do Jogo Shogi:\n\n"
                + "1. O Shogi é um jogo de tabuleiro japonês similar ao xadrez.\n"
                + "2. O objetivo do jogo é capturar o rei do oponente.\n"
                + "3. Cada jogador controla um exército de 20 peças, com diferentes movimentos.\n"
                + "4. Uma característica única do Shogi é a 'promoção', onde certas peças ganham novos movimentos ao avançar no tabuleiro.\n"
                + "5. Outra característica única é a 'gota', onde peças capturadas podem ser reaproveitadas no tabuleiro como parte do seu exército.\n"
                + "6. O jogo termina quando um dos reis é capturado ou um jogador se rende.\n"
                + "7. Utilize as opções do menu para iniciar um novo jogo para 2 jogadores ou para jogar contra a IA.");
        portuguese.put(StringsEnum.CHANGE_LANGUAGE, "Alterar Idioma");
    }

    private void populateEnglish() {
        english = new HashMap<StringsEnum, String>();
        english.put(StringsEnum.PROMOTE, "Promote!");
        english.put(StringsEnum.NEW_GAME, "New Game");
        english.put(StringsEnum.PLAYER, "Player");
        english.put(StringsEnum.WINS, "Wins!");
        english.put(StringsEnum.TWO_PLAYERS, " (2 Players)");
        english.put(StringsEnum.AGAINST_AI, " (Against AI)");
        english.put(StringsEnum.GAME_TITLE, "Shogi v0.9");
        english.put(StringsEnum.PLAYER_ONE_LABEL, "Player 1 Pieces: ");
        english.put(StringsEnum.PLAYER_TWO_LABEL, "Player 2 Pieces: ");
        english.put(StringsEnum.PLAYER_ONE_CAPTURED_LABEL, "Player 1 Captured Pieces: ");
        english.put(StringsEnum.PLAYER_TWO_CAPTURED_LABEL, "Player 2 Captured Pieces: ");
        english.put(StringsEnum.PLAYER_AI_LABEL, "AI Player: ");
        english.put(StringsEnum.PLAYER_AI_CAPTURED_LABEL, "AI Player Captured Pieces: ");
        english.put(StringsEnum.PLAYER_ONE_TIMER_LABEL, "Player 1: ");
        english.put(StringsEnum.PLAYER_TWO_TIMER_LABEL, "Player 2: ");
        english.put(StringsEnum.TURN_LABEL, "Turn: Player 1. Total moves: ");
        english.put(StringsEnum.TURN_LABEL_2, "Turn: Player 2. Total moves: ");
        english.put(StringsEnum.GAME_MODE, " | Game mode: ");
        english.put(StringsEnum.GAME_MODE_PVE, "Player VS AI");
        english.put(StringsEnum.GAME_MODE_PVP, "Player VS Player");
        english.put(StringsEnum.GAME_OVER_TITLE, "Game Over");
        english.put(StringsEnum.INSTRUCTIONS, "Instructions");
        english.put(StringsEnum.INSTRUCTIONS_TITLE, "Shogi Game Instructions");
        english.put(StringsEnum.INSTRUCTIONS_MESSAGE, "Shogi Game Instructions:\n\n"
                + "1. Shogi is a Japanese board game similar to chess.\n"
                + "2. The objective of the game is to capture the opponent's king.\n"
                + "3. Each player controls an army of 20 pieces, with different movements.\n"
                + "4. A unique feature of Shogi is 'promotion', where certain pieces gain new movements when advancing on the board.\n"
                + "5. Another unique feature is 'drop', where captured pieces can be reused on the board as part of your army.\n"
                + "6. The game ends when one of the kings is captured or a player surrenders.\n"
                + "7. Use the menu options to start a new game for 2 players or to play against the AI.");
        english.put(StringsEnum.CHANGE_LANGUAGE, "Change Language");
    }
}