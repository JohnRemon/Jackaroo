package application;

public class UserSettings {
    private String name;
    private float sfx;
    private float music;
    private String theme;

    public UserSettings(String name, float sfx, float music, String theme) {
        this.name = name;
        this.sfx = sfx;
        this.music = music;
        this.theme = theme;
    }
    public String getName() {
        return name;
    }
    public float getSfx() {
        return sfx;
    }
    public float getMusic() {
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

}
