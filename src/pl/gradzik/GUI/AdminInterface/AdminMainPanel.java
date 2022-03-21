package pl.gradzik.GUI.AdminInterface;

import pl.gradzik.GUI.BackgroundPanel;
import pl.gradzik.GUI.MainFrame;
import pl.gradzik.GUI.MainPanel;
import pl.gradzik.GUI.UserInterface.GUIString;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminMainPanel extends BackgroundPanel implements ActionListener
{

    JLabel titleLabel;

    JButton addMoney;
    JButton exitButton;


    public AdminMainPanel()
    {

        titleLabel = new JLabel(GUIString.helloAdmin);
        titleLabel.setFont(new Font("Arial",Font.PLAIN,60));
        titleLabel.setBounds(0,150,1200,100);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.white);
        this.add(titleLabel);

        addMoney = new JButton(GUIString.addMoneyToATM);
        addMoney.setBounds(350,400,500,100);
        addMoney.setFont(new Font("Arial",Font.PLAIN,35));
        addMoney.setFocusable(false);
        addMoney.addActionListener(this);
        this.add(addMoney);

        exitButton = new JButton(GUIString.exit);
        exitButton.setBounds(950,730,200,100);
        exitButton.setFont(new Font("Arial",Font.PLAIN,40));
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);
        this.add(exitButton);


        this.setBackground();
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == addMoney)
        {
            MainFrame.frame.setCurrentPanel(new AddCashPanel());
        }
        if(e.getSource() == exitButton)
        {
            MainFrame.frame.setCurrentPanel(new MainPanel());
        }

    }
}
