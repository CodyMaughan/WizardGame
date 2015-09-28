import java.awt.*;

/**
 * Created by Cody on 9/18/2015.
 */
public class StatusMenu implements Menu {

    private MainCharacter character;
    private int windowWidth;
    private int windowHeight;
    private int menuX;
    private int menuY;
    private String title;
    private Font titleFont;
    private int titleWidth;
    private int titleHeight;
    private boolean active;

    public StatusMenu(MainCharacter character, Framework framework) {
        this.character = character;
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        menuX = windowWidth/3;
        menuY = 0;
        title = character.characterName + "'s Status";
        titleFont = new Font("Arial", Font.BOLD, 30);
        Graphics2D g2d = (Graphics2D)framework.getGraphics();
        titleWidth = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
        titleHeight = (int)(titleFont.getStringBounds(title,g2d.getFontRenderContext()).getHeight());
        active = false;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(menuX, menuY, 2 * windowWidth / 3, windowHeight);
        g2d.setColor(Color.BLACK);
        g2d.setFont(titleFont);
        g2d.drawString(title, menuX + (windowWidth / 3 - titleWidth / 2), menuY + 50);
        Font headerFont = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(headerFont);
        g2d.drawString("Health", windowWidth / 3 + 40, 100);
        g2d.drawString("Mana", windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 2);
        g2d.drawString("Level:", 2 * windowWidth / 3 + 20, 100);
        g2d.drawString("Stats", windowWidth / 3 + 40, 100 + 3 * (20 + 15));
        g2d.drawString("Skills", 2 * windowWidth / 3 + 20, 100 + 3 * (20 + 15));
        Font itemFont = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(itemFont);
        g2d.drawString("Total Exp:", 2*windowWidth / 3 + 20, 100 + (20 + 15));
        g2d.drawString("Exp to nxt Lvl:", 2 * windowWidth / 3 + 20, 100 + 2 * (20 + 15));
        int textWidth = (int)(itemFont.getStringBounds(String.valueOf(MainCharacter.level), g2d.getFontRenderContext()).getWidth());
        g2d.drawString(String.valueOf(MainCharacter.level), windowWidth - 40 - textWidth, 100);
        textWidth = (int)(itemFont.getStringBounds(String.valueOf(MainCharacter.experience), g2d.getFontRenderContext()).getWidth());
        g2d.drawString(String.valueOf(MainCharacter.experience), windowWidth - 40 - textWidth, 100 + (20 + 15));
        textWidth = (int)(itemFont.getStringBounds(String.valueOf(MainCharacter.experience), g2d.getFontRenderContext()).getWidth());
        g2d.drawString(String.valueOf(MainCharacter.experience), windowWidth - 40 - textWidth, 100 + 2 * (20 + 15));
        String text = String.valueOf(MainCharacter.health) + "/" + String.valueOf(MainCharacter.maxHealth);
        textWidth = (int)(itemFont.getStringBounds(text, g2d.getFontRenderContext()).getWidth());
        g2d.drawString(text, 2*windowWidth / 3 - 20 - textWidth, 100);
        text = String.valueOf(MainCharacter.mana) + "/" + String.valueOf(MainCharacter.maxMana);
        textWidth = (int)(itemFont.getStringBounds(text, g2d.getFontRenderContext()).getWidth());
        g2d.drawString(text, 2*windowWidth / 3 - 20 - textWidth, 100 + 3 * (20 + 15) / 2);
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 9 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        g2d.setColor(Color.GREEN);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 4 - 17,
                (int) (((float) MainCharacter.health / (float) MainCharacter.maxHealth) * (windowWidth / 3 - 60)), 17, 3, 3);
        g2d.setColor(Color.BLUE);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 9 * (20 + 15) / 4 - 17,
                (int) (((float) MainCharacter.mana / (float) MainCharacter.maxMana) * (windowWidth / 3 - 60)), 17, 3, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        g2d.drawRoundRect(windowWidth / 3 + 40, 100 + 9 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        int i = 4;
        for (String name : MainCharacter.stats.keySet()) {
            g2d.drawString(name, windowWidth / 3 + 40, 100 + (15 + 20) * (i));
            textWidth = (int)(itemFont.getStringBounds(String.valueOf(MainCharacter.stats.get(name)), g2d.getFontRenderContext()).getWidth());
            g2d.drawString(String.valueOf(MainCharacter.stats.get(name)), 2*windowWidth/3 - 20 - textWidth, 100 + (15 + 20)*(i));
            i++;
        }
        i = 4;
        for (String name : MainCharacter.skills.keySet()) {
            g2d.setColor(SkillCache.getColor(name));
            g2d.fillRoundRect(2 * windowWidth / 3, 100 + 13 + (15 + 20) * (i - 1), windowWidth / 3, 15 + 20 - 2 * 3, 5, 5);
            g2d.setColor(Color.BLACK);
            g2d.drawString(name, 2*windowWidth / 3 + 20, 100 + (15 + 20) * (i));
            textWidth = (int)(itemFont.getStringBounds(String.valueOf(MainCharacter.skills.get(name)), g2d.getFontRenderContext()).getWidth());
            g2d.drawString(String.valueOf(MainCharacter.skills.get(name)), windowWidth - 40 - textWidth, 100 + (15 + 20)*(i));
            i++;
        }

    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    @Override
    public void setActive(boolean bool) {
        active = bool;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
