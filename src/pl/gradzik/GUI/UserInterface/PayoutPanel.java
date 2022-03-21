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

public class PayoutPanel extends BackgroundPanel implements ActionListener
{
    JLabel label;
    JLabel label2;
    JLabel balanceLabel;

    JButton button;
    JButton backButton;

    JTextField textField;

    public PayoutPanel()
    {
        label = new JLabel("");
        label.setFont(new Font("Arial",Font.PLAIN,60));
        label.setBounds(0,150,1200,100);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        this.add(label);

        label2 = new JLabel(GUIString.payoutLabel);
        label2.setFont(new Font("Arial",Font.PLAIN,60));
        label2.setBounds(0,250,1200,100);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setForeground(Color.white);
        this.add(label2);

        try {
            balanceLabel = new JLabel(GUIString.balance + " : " + UserPanel.balanceResultSet.getFloat("balance") + " PLN");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        balanceLabel.setFont(new Font("Arial",Font.PLAIN,60));
        balanceLabel.setBounds(50,600,1200,100);
        balanceLabel.setForeground(Color.white);
        this.add(balanceLabel);


        textField = new JTextField();
        textField.setBounds(350,400,250,100);
        textField.setFont(new Font("Arial",Font.PLAIN,80));
        this.add(textField);

        button = new JButton(GUIString.payout);
        button.setBounds(650,400,200,100);
        button.setFont(new Font("Arial",Font.PLAIN,40));
        button.addActionListener(this);
        button.setFocusable(false);
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
        if (e.getSource() == button) {
            int value;

            try {
                value = Integer.valueOf(textField.getText());
            } catch (NumberFormatException ex) {
                invalidValue();
                return;
            }

            if (value % 10 != 0) {
                invalidValue();
                return;
            }

            if(isTransactionPossible(value))
            {
                try {
                    Statement statement = UserPanel.connection.createStatement();

                    statement.executeUpdate // zmiana salda
                            (
                                    "update balance\n" +
                                            "set balance = balance - " + value + "\n" +
                                            "where client_id = " + UserPanel.id + ";"
                            );

                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime()); //aktualna data

                    java.sql.Time time = new java.sql.Time(Calendar.getInstance().getTime().getTime()); //aktualny czas

                    statement.executeUpdate // dodawanie tranzakcji do histori
                            (
                                    "INSERT INTO `bank-system`.transactions_history \n" +
                                            "(date,time,transaction_value,client_id, transaction_atm_id,transaction_type,transaction_destination_id) \n" +
                                            "values ('" + date + "','" + time + "'," + value + "," + UserPanel.id + "," + ATMInfo.atmID + ",\"wypłata\", null);"
                            );

                    MainFrame.frame.setCurrentPanel(new TransactionSuccessfulPanel());

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            else
            {
                label.setText(GUIString.toLessMoney3);
                label2.setText(GUIString.toLessMoney2);
                this.repaint();
            }
        }

        if(e.getSource() == backButton)
        {
            MainFrame.frame.setCurrentPanel(UserPanel.currentUser);
        }

        UserPanel.secCounter = 0;

    }

    private boolean isTransactionPossible(int value)
    {

        try {
            if(UserPanel.balanceResultSet.getFloat("balance") < value)
            {

                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int needed100PLN = 0;
        int needed50PLN = 0;
        int needed20PLN = 0;
        int needed10PLN = 0;

        int current100PLN = ATMInfo.PLN100;
        int current50PLN = ATMInfo.PLN50;
        int current20PLN = ATMInfo.PLN20;
        int current10PLN = ATMInfo.PLN10;

        while (value >= 100 && current100PLN > 0)
        {
            needed100PLN += 1;
            current100PLN -= 1;
            value -= 100;
        }

        while (value >= 50 && current50PLN > 0)
        {
            needed50PLN += 1;
            current50PLN -= 1;
            value -= 50;
        }

        while (value >= 20 && current20PLN > 0)
        {
            needed20PLN += 1;
            current20PLN -= 1;
            value -= 20;
        }

        while (value >= 10 && current10PLN > 0)
        {
            needed10PLN += 1;
            current10PLN -= 1;
            value -= 10;
        }

        if(value > 0) return false;

        ATMInfo.PLN100 -= needed100PLN;
        ATMInfo.PLN50 -= needed50PLN;
        ATMInfo.PLN20 -= needed20PLN;
        ATMInfo.PLN10 -= needed10PLN;

        try {
            ATMInfo.updateDB();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Heloo");
        }

        return true;
    }

    private void invalidValue ()
    {
        label.setText(GUIString.invalidValue1);
        label2.setText(GUIString.invalidValue2);
        this.repaint();
    }

}
