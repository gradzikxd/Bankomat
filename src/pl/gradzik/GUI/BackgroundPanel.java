package pl.gradzik.GUI;

import javax.swing.*;
import java.awt.*;

public abstract class BackgroundPanel extends JPanel
{
    private static final ImageIcon icon = new ImageIcon(System.getProperty("user.dir") +"\\data\\mainSite.png");

    private JLabel label;

    public BackgroundPanel()
    {
        this.setLayout(null);
        this.setBounds(0,0,1200,900);
    }


    public void setBackground() // instrukcja musi zostać wykonana pod koniec konstruktora klasy dziedziczącej, w innym przypadku obiekty narysują sie pod tłem
    {
        label = new JLabel(icon);
        label.setBounds(0,0,1200,900);
        this.add(label);
    }



}
