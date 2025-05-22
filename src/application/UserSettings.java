package application;

import javafx.scene.input.KeyCode;

import java.io.*;

public class UserSettings {
    private String name = "Player";
    private double sfx = 100.0f;
    private double music = 100.0f;
    private String theme = "Default";
    private final String path = "Settings.txt";

    public UserSettings() {
    }

    public UserSettings(String name, double sfx, double music, String theme) {
        this.name = name;
        this.sfx = sfx;
        this.music = music;
        this.theme = theme;
    }
    public String getName() {
        return name;
    }
    public double getSfx() {
        return sfx;
    }
    public double getMusic() {
        return music;
    }
    public String getTheme() {
        return theme;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSfx(double sfx) {
        this.sfx = sfx;
    }

    public void setMusic(double music) {
        this.music = music;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void SaveSettings(UserSettings settings) throws IOException {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(settings.getName() +
            "\n"+ settings.getSfx() +
            "\n" + settings.getMusic() +
            "\n" + settings.getTheme());
        } catch (IOException e) {
           System.out.println(path);
           System.out.println(name + " " + sfx + " " + music + " " + theme);
        }
    }
    public UserSettings LoadSettings() throws IOException {
        try (BufferedReader fr = new BufferedReader(new FileReader(path))) {
            String name = fr.readLine();
            double sfx = Float.parseFloat(fr.readLine());
            double music = Float.parseFloat(fr.readLine());
            String theme = fr.readLine();
            return new UserSettings(name, sfx, music, theme);
        } catch (Exception e) {
            return new UserSettings(name, sfx, music, theme);
        }
    }
    public static class KeyBinds {
        public KeyCode card1;
        public KeyCode card2;
        public KeyCode card3;
        public KeyCode card4;

        public KeyCode marble1;
        public KeyCode marble2;
        public KeyCode marble3;
        public KeyCode marble4;

        public KeyCode skipTurn;
        public KeyCode playTurn;
        private final String bindPath = "keybinds.txt";

        public KeyBinds(KeyCode card1, KeyCode card2, KeyCode card3, KeyCode card4, KeyCode marble1, KeyCode marble2, KeyCode marble3, KeyCode marble4, KeyCode skipTurn, KeyCode playTurn) {
            this.card1 = card1;
            this.card2 = card2;
            this.card3 = card3;
            this.card4 = card4;
            this.marble1 = marble1;
            this.marble2 = marble2;
            this.marble3 = marble3;
            this.marble4 = marble4;
            this.skipTurn = skipTurn;
            this.playTurn = playTurn;
        }

        public KeyBinds() {
            this.card1 = KeyCode.DIGIT1;
            this.card2 = KeyCode.DIGIT2;
            this.card3 = KeyCode.DIGIT3;
            this.card4 = KeyCode.DIGIT4;
            this.marble1 = KeyCode.Q;
            this.marble2 = KeyCode.W;
            this.marble3 = KeyCode.E;
            this.marble4 = KeyCode.R;
            this.skipTurn = KeyCode.BACK_SPACE;
            this.playTurn = KeyCode.ENTER;
        }

        public void saveBinds(KeyBinds binds) {
            try (FileWriter fw = new FileWriter(bindPath)) {
                fw.write(
                card1.toString() +
                "\n" + card2.toString() +
                "\n" + card3.toString() +
                "\n" + card4.toString() +
                "\n" + marble1.toString() +
                "\n" + marble2.toString() +
                "\n" + marble3.toString() +
                "\n" + marble4.toString() +
                "\n" + skipTurn.toString() +
                "\n" + playTurn.toString());
            } catch (IOException e) {
                System.out.println("Could not save keybinds.txt");
            }
        }
        public KeyBinds loadBinds() {
            try (BufferedReader br = new BufferedReader(new FileReader(bindPath))) {
                return new KeyBinds(
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine()),
                        KeyCode.valueOf(br.readLine())
                );
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Could not load keybinds.txt, using defaults.");
                return new KeyBinds(); // default constructor
            }
        }





        public void setCard1(KeyCode card1) {
            this.card1 = card1;
        }

        public void setCard2(KeyCode card2) {
            this.card2 = card2;
        }

        public void setCard3(KeyCode card3) {
            this.card3 = card3;
        }

        public void setCard4(KeyCode card4) {
            this.card4 = card4;
        }

        public void setMarble1(KeyCode marble1) {
            this.marble1 = marble1;
        }

        public void setMarble2(KeyCode marble2) {
            this.marble2 = marble2;
        }

        public void setMarble3(KeyCode marble3) {
            this.marble3 = marble3;
        }

        public void setMarble4(KeyCode marble4) {
            this.marble4 = marble4;
        }

        public void setSkipTurn(KeyCode skipTurn) {
            this.skipTurn = skipTurn;
        }

        public void setPlayTurn(KeyCode playTurn) {
            this.playTurn = playTurn;
        }
    }
}
