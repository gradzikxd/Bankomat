package pl.gradzik.GUI.AdminInterface;

import pl.gradzik.ATMInfo;
import pl.gradzik.GUI.BackgroundPanel;
import pl.gradzik.GUI.MainFrame;
import pl.gradzik.GUI.UserInterface.GUIString;
import pl.gradzik.MySQlConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddCashPanel extends BackgroundPanel implements ActionListener
{

    JLabel titleLabel;

    InputPanel pln100;
    InputPanel pln50;
    InputPanel pln20;
    InputPanel pln10;

    JButton backButton;
    JButton addButton;


    public AddCashPanel()
    {

        titleLabel = new JLabel(GUIString.addMoneyTitle);
        titleLabel.setFont(new Font("Arial",Font.PLAIN,50));
        titleLabel.setBounds(0,150,1200,100);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.white);
        this.add(titleLabel);

        pln100 = new InputPanel(50,300,"100 PLN :");
        this.add(pln100);

        pln50 = new InputPanel(50,400,"50 PLN :");
        this.add(pln50);

        pln20 = new InputPanel(50,500,"20 PLN :");
        this.add(pln20);

        pln10 = new InputPanel(50,600,"10 PLN :");
        this.add(pln10);

        backButton = new JButton(GUIString.back);
        backButton.setBounds(950,730,200,100);
        backButton.setFont(new Font("Arial",Font.PLAIN,40));
        backButton.setFocusable(false);
        backButton.addActionListener(this);
        this.add(backButton);

        addButton = new JButton(GUIString.add);
        addButton.setBounds(700,730,200,100);
        addButton.setFont(new Font("Arial",Font.PLAIN,40));
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        this.add(addButton);



        setBackground();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == addButton)
        {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Connection connection = MySQlConnector.connect();
                        Statement statement = connection.createStatement();

                        statement.executeUpdate(
                                "update atms\n" +
                                        "set 100PLN = 100PLN + "+ pln100.getFieldText()+" \n" +
                                        ",50PLN = 50PLN + "+ pln50.getFieldText() +" \n" +
                                        ",20PLN = 20PLN + "+ pln20.getFieldText() +"  \n" +
                                        ",10PLN = 10PLN + "+ pln10.getFieldText() +"  \n" +
                                        "where ATM_id = " + ATMInfo.atmID + ";"
                        );

                        pln100.clearFlied();
                        pln50.clearFlied();
                        pln20.clearFlied();
                        pln10.clearFlied();

                        titleLabel.setText("Pomyślnie dodano pieniądze");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        titleLabel.setText(GUIString.addMoneyTitle);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            });
            t.start();

        }

        if(e.getSource() == backButton)
        {
            MainFrame.frame.setCurrentPanel(new AdminMainPanel());
        }

    }
}
