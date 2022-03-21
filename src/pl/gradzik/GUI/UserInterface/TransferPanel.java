package pl.gradzik.GUI.UserInterface;

import pl.gradzik.ATMInfo;
import pl.gradzik.GUI.BackgroundPanel;
import pl.gradzik.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class TransferPanel extends BackgroundPanel implements ActionListener
{
    JLabel label;
    JLabel label2;
    JLabel label3;
    JLabel balanceLabel;

    JTextField textFieldBankAccount;
    JTextField textFieldValue;

    JButton button;
    JButton backButton;

    float currentBalance;


    public TransferPanel()
    {
        label = new JLabel(GUIString.transferLabel);
        label.setFont(new Font("Arial",Font.PLAIN,60));
        label.setBounds(0,150,1200,100);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        this.add(label);

        label2 = new JLabel(GUIString.transferDestinationLabel);
        label2.setFont(new Font("Arial",Font.PLAIN,60));
        label2.setBounds(50,250,1200,100);
        label2.setForeground(Color.white);
        this.add(label2);

        label3 = new JLabel(GUIString.transferDestinationValue + " : " );
        label3.setFont(new Font("Arial",Font.PLAIN,60));
        label3.setBounds(50,450,600,100);
        label3.setForeground(Color.white);
        this.add(label3);

        try {
            balanceLabel = new JLabel(GUIString.balance + " : " + UserPanel.balanceResultSet.getFloat("balance") + " PLN");
            currentBalance = Float.parseFloat(String.valueOf(UserPanel.balanceResultSet.getFloat("balance")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        balanceLabel.setFont(new Font("Arial",Font.PLAIN,60));
        balanceLabel.setBounds(50,750,1200,100);
        balanceLabel.setForeground(Color.white);
        this.add(balanceLabel);

        textFieldBankAccount = new JTextField();
        textFieldBankAccount.setBounds(100,350,1000,80);
        textFieldBankAccount.setFont(new Font("Arial",Font.PLAIN,60));
        this.add(textFieldBankAccount);

        textFieldValue = new JTextField();
        textFieldValue.setBounds(630,460,300,80);
        textFieldValue.setFont(new Font("Arial",Font.PLAIN,60));
        this.add(textFieldValue);

        button = new JButton(GUIString.transfer2);
        button.setBounds(500,580,200,100);
        button.setFont(new Font("Arial",Font.PLAIN,40));
        button.setFocusable(false);
        button.addActionListener(this);
        this.add(button);

        backButton = new JButton(GUIString.back);
        backButton.setBounds(950,730,200,100);
        backButton.setFont(new Font("Arial",Font.PLAIN,40));
        backButton.setFocusable(false);
        backButton.addActionListener(this);
        this.add(backButton);

        this.setBackground();

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == button)
        {
            String accountNumber = textFieldBankAccount.getText();

            float value;

            try {
                value = Float.parseFloat(textFieldValue.getText());
            }
            catch (NumberFormatException ex)
            {
                label.setText(GUIString.invalidValue1);
                return;
            }

            if(value > currentBalance) return;

            try {
                Statement statement1 = UserPanel.connection.createStatement();

                ResultSet destinationResultSet = statement1.executeQuery
                        (
                                "select * from balance where account_number = '"+ accountNumber +" ';"
                        );

                destinationResultSet.next();

                Statement statement2 = UserPanel.connection.createStatement();

                statement2.executeUpdate("update balance\n" +
                        "set balance = balance - " + value + "\n" +
                        "where client_id = " + UserPanel.id + ";");

                statement2.executeUpdate("update balance\n" +
                        "set balance = balance + " + value + "\n" +
                        "where client_id = " + destinationResultSet.getString("client_id") + ";");


                java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime()); //aktualna data

                java.sql.Time time = new java.sql.Time(Calendar.getInstance().getTime().getTime()); //aktualny czas

                statement2.executeUpdate // dodawanie tranzakcji do histori
                        (
                                "INSERT INTO `bank-system`.transactions_history \n" +
                                        "(date,time,transaction_value,client_id, transaction_atm_id,transaction_type,transaction_destination_id) \n" +
                                        "values ('" + date + "','" + time + "'," + value + "," + UserPanel.id + "," + ATMInfo.atmID + ",\"przelew\", "+ destinationResultSet.getInt("client_id") +");"
                        );

                MainFrame.frame.setCurrentPanel(new TransactionSuccessfulPanel());

            } catch (SQLException ex)
            {
                label.setText(GUIString.invalidAccountNumber);
                this.repaint();
            }


        }

        if(e.getSource() == backButton)
        {
            MainFrame.frame.setCurrentPanel(UserPanel.currentUser);
        }

        UserPanel.secCounter = 0;

    }
}
