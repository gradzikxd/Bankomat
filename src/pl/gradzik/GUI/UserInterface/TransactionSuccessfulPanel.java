package pl.gradzik.GUI.UserInterface;

import pl.gradzik.GUI.BackgroundPanel;
import pl.gradzik.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class TransactionSuccessfulPanel extends BackgroundPanel implements ActionListener
{

    private JLabel label;

    private JButton exitButton;
    private JButton againButton;

    public TransactionSuccessfulPanel()
    {

        label = new JLabel(GUIString.transactionSuccessful);
        label.setFont(new Font("Arial",Font.PLAIN,60));
        label.setBounds(0,200,1200,100);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        this.add(label);

        againButton = new JButton(GUIString.nextTransaction);
        againButton.setBounds(175,400,400,100);
        againButton.setFont(new Font("Arial",Font.PLAIN,40));
        againButton.setFocusable(false);
        againButton.addActionListener(this);
        this.add(againButton);

        exitButton = new JButton(GUIString.exit);
        exitButton.setBounds(625,400,400,100);
        exitButton.setFont(new Font("Arial",Font.PLAIN,40));
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);
        this.add(exitButton);

        this.setBackground();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == againButton)
        {
            MainFrame.frame.setCurrentPanel(new UserPanel(UserPanel.id));

            UserPanel.secCounter = 0;
        }

        if(e.getSource() == exitButton)
        {
            try {
                UserPanel.currentUser.closeUserPanel();
                UserPanel.timeThread.stop();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }


        }




    }
}
