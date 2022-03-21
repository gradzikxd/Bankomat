package pl.gradzik.GUI;

import pl.gradzik.GUI.UserInterface.GUIString;
import pl.gradzik.GUI.UserInterface.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PINInputPanel extends BackgroundPanel implements ActionListener
{
    JLabel label;
    JLabel label2;

    JTextField textField;

    JButton okButton;

    private int clientID;
    private int cardPIN;

    private int counter;

    public PINInputPanel(int clientID,int cardPIN )
    {
        this.cardPIN = cardPIN;
        this.clientID = clientID;
        this.counter = 3;

        label = new JLabel(GUIString.insertPin + " :");
        label.setFont(new Font("Arial",Font.PLAIN,60));
        label.setBounds(0,210,1200,60);
        label.setHorizontalAlignment(JTextField.CENTER);
        label.setForeground(Color.white);
        this.add(label);

        label2 = new JLabel();
        label2.setFont(new Font("Arial",Font.PLAIN,60));
        label2.setBounds(0,440,1200,60);
        label2.setHorizontalAlignment(JTextField.CENTER);
        label2.setForeground(Color.white);
        this.add(label2);

        textField = new JTextField();
        textField.setBounds(400,300,250,100);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("Arial",Font.PLAIN,80));
        this.add(textField);

        okButton = new JButton("OK");
        okButton.setBounds(700,300,100,100);
        okButton.setFocusable(false);
        okButton.setFont(new Font("Arial",Font.PLAIN,40));
        okButton.addActionListener(this);
        this.add(okButton);


        this.setBackground();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == okButton)
        {

            int pin;

            pin = Integer.valueOf(textField.getText());

                if(cardPIN == pin)
                {
                    MainFrame.frame.setCurrentPanel(new UserPanel(clientID));

                }
                else
                {
                    counter--;
                    label2.setText(GUIString.attemptsLeft + " : " + counter);
                }


            if(counter == 0) MainFrame.frame.setCurrentPanel(new MainPanel());

        }

    }
}
