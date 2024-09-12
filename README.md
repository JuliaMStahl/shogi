# Shogi: Traditional Japanese Chess 🀄

Welcome to **Shogi**, a Java/Swing implementation of the traditional Japanese chess game. This project is part of a software maintenance initiative.

![Shogi Game](shogi.gif)

## 📜 About
This project is designed to provide a digital version of Shogi, with a focus on maintaining and improving software quality. The game is implemented using Java, leveraging the Swing library for the graphical user interface.

## 🚀 Features
- **Classic Shogi gameplay**: Experience traditional Japanese chess.
- **Interactive GUI**: User-friendly interface for a smooth gaming experience.

## 🛠 Installation
To run the game locally:
1. Clone the repository:
    ```bash
    git clone https://github.com/JuliaMStahl/shogi.git
    ```
2. Navigate to the project directory and compile the code:
    ```bash
    cd shogi
    javac -d out/production/Shogi src/*.java
    ```
3. Run the game:
    ```bash
    java -cp out/production/Shogi ChessGUI
    ```

## 🎮 Usage
- Use the graphical interface to play Shogi against the computer or a friend.

## 🧰 Technologies Used
- **Java**: Core programming language.
- **Swing**: For creating the graphical user interface.

## 🌟 Contributing
Feel free to fork the project and submit pull requests. Contributions are welcome!

## Mudanças 2024/1 

### 1. ChessController
Criação da classe controller que tem como objetivo gerenciar variáveis e a parte lógica do jogo

### 2. Aba de instruções do jogo
Criação do botão da navbar "Instruções" com intruções do jogo

### 3. Cronômetros dos jogadores
Adição de cronômetros para os jogadores

### 4. Correção das cores dos possíveis movimentos em macOS
No macOS, as cores dos possíveis movimentos de cada peça não estvam aparecendo, mas no Windows sim. 
Esse e outros bugs no macOS foram corrigidos.

### 5. Tradução do jogo
Criação da classe Translations que permite alterar o idioma do jogo em Inglês e Português

### 6. Adicção de README.md no projeto
Adição um Readme com informações sobre o projeto: prints, getting started, como rodar.
