package pl.gradzik.GUI.UserInterface;

import pl.gradzik.GUI.BackgroundPanel;
import pl.gradzik.GUI.MainFrame;
import pl.gradzik.GUI.MainPanel;
import pl.gradzik.GUI.UserInterface.GUIString;
import pl.gradzik.MySQlConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserPanel extends BackgroundPanel implements ActionListener, Runnable
{

    private JButton depositButton;
    private JButton payoutButton;
    private JButton transferButton;
    private JButton exitButton;

    private JLabel helloLabel;
    private JLabel chooseLabel;

    public static Connection connection;

    public static ResultSet clientResultSet;
    public static ResultSet balanceResultSet;

    public static UserPanel currentUser;

    public static int id;
    private String name;

    public static Thread timeThread;
    public static int secCounter;


    public UserPanel(int id)
    {
        this.id = id;

        currentUser = this;

        connection = MySQlConnector.connect();

        try {
            DBconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        helloLabel = new JLabel(GUIString.hello + " " + name);
        helloLabel.setFont(new Font("Arial",Font.PLAIN,60));
        helloLabel.setBounds(0,150,1200,100);
        helloLabel.setHorizontalAlignment(JLabel.CENTER);
        helloLabel.setForeground(Color.white);
        this.add(helloLabel);

        chooseLabel = new JLabel(GUIString.pleaseChoose + " :");
        chooseLabel.setFont(new Font("Arial",Font.PLAIN,60));
        chooseLabel.setBounds(0,250,1200,100);
        chooseLabel.setHorizontalAlignment(JLabel.CENTER);
        chooseLabel.setForeground(Color.white);
        this.add(chooseLabel);

        depositButton = new JButton(GUIString.deposit);
        depositButton.setBounds(100,400,300,100);
        depositButton.setFont(new Font("Arial",Font.PLAIN,40));
        depositButton.setFocusable(false);
        depositButton.addActionListener(this);
        this.add(depositButton);

        payoutButton = new JButton(GUIString.payout);
        payoutButton.setBounds(450,400,300,100);
        payoutButton.setFont(new Font("Arial",Font.PLAIN,40));
        payoutButton.setFocusable(false);
        payoutButton.addActionListener(this);
        this.add(payoutButton);

        transferButton = new JButton(GUIString.transfer);
        transferButton.setBounds(800,400,300,100);
        transferButton.setFont(new Font("Arial",Font.PLAIN,40));
        transferButton.setFocusable(false);
        transferButton.addActionListener(this);
        this.add(transferButton);

        exitButton = new JButton(GUIString.exit);
        exitButton.setBounds(950,730,200,100);
        exitButton.setFont(new Font("Arial",Font.PLAIN,40));
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);
        this.add(exitButton);

        this.setBackground();

       timeThread = new Thread(this);
       timeThread.start();
    }

    private void DBconnect() throws SQLException
    {

         Statement statement1 = connection.createStatement();
         Statement statement2 = connection.createStatement();
         Statement statement3 = connection.createStatement();


        clientResultSet = statement1.executeQuery
                (
                  "select * from  clients\n" +
                          "where id = "+ id + ";"
                );


        balanceResultSet = statement2.executeQuery
                (
                        "select * from  balance\n" +
                                "where client_id = "+ id + ";"
                );



        clientResultSet.next();
        balanceResultSet.next();

        name = clientResultSet.getString("name");

    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == depositButton)
        {
            MainFrame.frame.setCurrentPanel(new DepositPanel());
        }

        if(e.getSource() == payoutButton)
        {
            MainFrame.frame.setCurrentPanel(new PayoutPanel());
        }

        if(e.getSource() == transferButton)
        {
            MainFrame.frame.setCurrentPanel(new TransferPanel());
        }

        if(e.getSource() == exitButton)
        {
            try {
               closeUserPanel();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        UserPanel.secCounter = 0;
    }

    public void closeUserPanel() throws SQLException
    {
        clientResultSet.close();
        balanceResultSet.close();
        connection.close();
        id = 0;
        currentUser = null;

        MainFrame.frame.setCurrentPanel(new MainPanel());
    }

    @Override
    public void run() // wątek odpowiedzialny za zamkniecie profilu po 30 sekundach nieaktywności
    {
        secCounter = 0;

        while(true)
        {
            try {
                Thread.sleep(1000);
                secCounter += 1;
                System.out.println(secCounter);

                if(secCounter == 30)
                {
                    UserPanel.currentUser.closeUserPanel();
                    break;
                }


            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }


    }
}
