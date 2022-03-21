package pl.gradzik.GUI;

import pl.gradzik.ATMInfo;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    private BackgroundPanel currentPanel;

    public static MainFrame frame;


    public MainFrame()
    {

        frame = this;

        new ATMInfo();

        UIManager.put("Button.background", Color.lightGray);

        currentPanel = new MainPanel();

        this.setTitle("Bankomat");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200,900);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.add(currentPanel);
        this.setLocationRelativeTo(null);



    }

    public void setCurrentPanel(BackgroundPanel panel)
    {
        this.remove(currentPanel);
        currentPanel = panel;
        this.add(currentPanel);
        this.refresh();
    }

    public void refresh()
    {
        this.invalidate();
        this.validate();
        this.repaint();
    }


}
