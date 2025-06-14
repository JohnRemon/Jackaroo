**Jackaroo** is a strategic, turn-based multiplayer board game implemented in Java using JavaFX. It is inspired by the traditional Middle Eastern game that combines elements of Ludo, cards, and tactical gameplay. This version features a full card system, marble movement logic, and a visually rich GUI

## 📦 Features

- ✅ Fully playable 1v3 game (1 human vs 3 CPU opponents)
- 🎴 Custom 52-card deck including wild cards like Burner and Saver
- 🎯 Goal: move 4 marbles from home zone to safe zone
- ⚔️ Includes traps, burning, swapping, saving mechanics
- 🧠 CPU players play automatically
- 🎨 Multi themed UI with animations and sound effects
- 🔊 Background music and SFX (card selection, marble movement, etc.)
- 🎮 Custom Keybinds
- 🖥️ Built using JavaFX (MVC architecture)

## 🎲 Game Rules Summary

- Each player gets 4 cards per round.
- Cards are used to move marbles along the board.
- Certain cards perform special actions:
  - 🔥 **Burner**: Select and eliminate an opponent’s marble that is on the board (sends it home)
  - 🛡️ **Saver**: 	Rescue one of your marbles from the track and place it in a random safe cell
  - 🔁 **Jack**: Swap one of your marbles with any other on the board OR move forward 11 steps
  - 👑 **King**: Field a marble from home OR move a marble 13 steps forward, killing all in its path
  - 👸 **Queen**: Skip a random opponent’s turn by discarding one of their cards OR move 12 steps forward
  - 🔟 **Ten**: Discard a random card from the next player and skip their turn OR move 10 steps forward
  - 7️⃣ **Seven**: Move one marble 7 steps OR split 7 steps across two of your own marbles
  - 🅰️ **Ace**: Field a marble from home OR move one of your marbles 1 step forward
  - 5️⃣ **Five**: Move **any** marble 5 steps forward
- Goal: move all 4 marbles into your own safe zone.
- Traps and strategic placement are key to winning.

## 🧑‍💻 Getting Started
### ✅ Requirements

- Java 21+
- JavaFX SDK 21+ installed and linked in your IDE
- IntelliJ IDEA (recommended) or Eclipse

### 📥 Clone the Repo

```bash
git clone https://github.com/JohnRemon/Jackaroo.git
```

### 🚀 Run the Project
- Using IntelliJ (Recommended)
- Open the project.
- Go to Project Structure → Libraries.
- Add JavaFX SDK as a library.
- To Set VM Options
  - Right Click on Application/Main.java
  - More Run/Debug
  - Modify Run Configuration
  - Modify Options
  - Add VM Options
  ```bash
  -module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
  ```
- Run Main.java
