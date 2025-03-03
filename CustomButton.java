package Wordle2;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    private Color backgroundColor;

    public CustomButton(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        this.repaint();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}

