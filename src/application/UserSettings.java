package application;

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
    public void setSfx(float sfx) {
        this.sfx = sfx;
    }
    public void setMusic(float music) {
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
}
