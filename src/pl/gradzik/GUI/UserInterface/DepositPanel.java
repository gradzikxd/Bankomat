package pl.gradzik.GUI.UserInterface;

import pl.gradzik.ATMInfo;
import pl.gradzik.GUI.BackgroundPanel;
import pl.gradzik.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class DepositPanel extends BackgroundPanel implements ActionListener
{
    JLabel label;
    JLabel label2;

    JButton button;
    JButton backButton;

    JTextField textField;

    public DepositPanel()
    {
        label = new JLabel(GUIString.depositText1);
        label.setFont(new Font("Arial",Font.PLAIN,60));
        label.setBounds(0,150,1200,100);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        this.add(label);

        label2 = new JLabel(GUIString.depositText2);
        label2.setFont(new Font("Arial",Font.PLAIN,60));
        label2.setBounds(0,250,1200,150);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setForeground(Color.white);
        this.add(label2);

        textField = new JTextField();
        textField.setBounds(350,400,250,100);
        textField.setFont(new Font("Arial",Font.PLAIN,80));
        this.add(textField);

        button = new JButton(GUIString.deposit2);
        button.setBounds(650,400,200,100);
        button.setFont(new Font("Arial",Font.PLAIN,40));
        button.addActionListener(this);
        this.add(button);

        backButton = new JButton(GUIString.back);
        backButton.setBounds(950,730,200,100);
        backButton.setFont(new Font("Arial",Font.PLAIN,40));
        backButton.addActionListener(this);
        this.add(backButton);

        this.setBackground();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == button)
        {
            int value;

            try
            {
                value = Integer.valueOf(textField.getText());
            }
            catch (NumberFormatException ex)
            {
                invalidValue();
                return;
            }

            if(value % 10 != 0)
            {
                invalidValue();
                return;
            }

            try
            {
                Statement statement = UserPanel.connection.createStatement();

                statement.executeUpdate // zmiana salda
                        (
                                "update balance\n" +
                                        "set balance = balance + " + value + "\n" +
                                        "where client_id = " + UserPanel.id +";"
                        );

                java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime()); //aktualna data

                java.sql.Time time = new java.sql.Time(Calendar.getInstance().getTime().getTime()); //aktualny czas

                statement.executeUpdate // dodawanie tranzakcji do histori
                        (
                                "INSERT INTO `bank-system`.transactions_history \n" +
                                        "(date,time,transaction_value,client_id, transaction_atm_id,transaction_type,transaction_destination_id) \n" +
                                        "values ('"+ date +"','"+ time +"'," + value + ","+ UserPanel.id +","+ ATMInfo.atmID +",\"wpłata\", null);"
                        );

                MainFrame.frame.setCurrentPanel(new TransactionSuccessfulPanel());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if(e.getSource() == backButton)
        {
            MainFrame.frame.setCurrentPanel(UserPanel.currentUser);
        }

        UserPanel.secCounter = 0;
    }

    private void invalidValue()
    {
        label.setText(GUIString.invalidValue1);
        label2.setText(GUIString.invalidValue2);
        this.repaint();
    }
}
