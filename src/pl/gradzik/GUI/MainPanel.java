package pl.gradzik.GUI;

import pl.gradzik.GUI.AdminInterface.AdminMainPanel;
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

public class MainPanel extends BackgroundPanel implements ActionListener
{
    private static final ImageIcon ukFlag = new ImageIcon(System.getProperty("user.dir") +"\\data\\UKflag.png");
    private static final ImageIcon gerFlag = new ImageIcon(System.getProperty("user.dir") +"\\data\\GERflag.png");
    private static final ImageIcon plFlag = new ImageIcon(System.getProperty("user.dir") +"\\data\\PLflag.png");

    JButton englishButton;
    JButton germanButton;
    JButton polishButton;

    JButton startButton;


    public MainPanel()
    {

        GUIString.setLanguagePolish();

        englishButton = new JButton();
        englishButton.setBounds(40,740,160,96);
        englishButton.addActionListener(this);
        englishButton.setIcon(ukFlag);

        germanButton = new JButton();
        germanButton.setBounds(220,740,160,96);
        germanButton.addActionListener(this);
        germanButton.setIcon(gerFlag);

        polishButton = new JButton();
        polishButton.setBounds(400,740,160,96);
        polishButton.addActionListener(this);
        polishButton.setIcon(plFlag);

        startButton = new JButton(GUIString.start);
        startButton.setBounds(420,300,350,100);
        startButton.setFocusable(false);
        startButton.setFont(new Font("Arial",Font.BOLD,20));
        startButton.setBackground(Color.lightGray);
        startButton.addActionListener(this);

        this.add(englishButton);
        this.add(germanButton);
        this.add(polishButton);
        this.add(startButton);
        this.setBackground(); // na koncu konstruktora
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() == englishButton)
        {
            GUIString.setLanguageEnglish();
            startButton.setText(GUIString.start);
            this.repaint();
        }

        if (e.getSource() == germanButton)
        {
            GUIString.setLanguageGermany();
            startButton.setText(GUIString.start);
            this.repaint();
        }

        if (e.getSource() == polishButton)
        {
            GUIString.setLanguagePolish();
            startButton.setText(GUIString.start);
            this.repaint();
        }

        if (e.getSource() == startButton)
        {
            UIManager.put("OptionPane.minimumSize",new Dimension(300,150));

            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(250,30));
            panel.setLayout(null);

            JLabel cardLabel = new JLabel(GUIString.cardNumber + " : ");
            cardLabel.setBounds(20,0,100,20);
            JLabel cvvLabel = new JLabel("CVV : ");
            cvvLabel.setBounds(20,50,100,20);

            TextField cardNumber =  new TextField();
            cardNumber.setBounds(20,20,230,20);
            TextField cvv =  new TextField();
            cvv.setBounds(20,70,230,20);

            panel.add(cardNumber);
            panel.add(cvv);
            panel.add(cardLabel);
            panel.add(cvvLabel);

            int input = JOptionPane.showConfirmDialog(MainFrame.frame, panel,
                    GUIString.card, JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE);

            if(input == 0)
            {
                System.out.println(cardNumber.getText());
                System.out.println(cvv.getText());

                if(cardNumber.getText().equals("1111 3333 2222 4444") && cvv.getText().equals("51423"))
                {
                    MainFrame.frame.setCurrentPanel(new AdminMainPanel());
                    return;
                }

                Connection connection = MySQlConnector.connect();

                try
                {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery
                            (
                                    "select * from `bank-system`.cards\n" +
                                            "where card_number = '" + cardNumber.getText() + " ' and cvv = "+ cvv.getText() +""
                    );

                    resultSet.next();

                    System.out.println(resultSet.getInt("client_id"));

                    MainFrame.frame.setCurrentPanel(new PINInputPanel(resultSet.getInt("client_id"),resultSet.getInt("card_pin")));


                } catch (SQLException ex) {
                    //ex.printStackTrace();
                    System.out.println("brak wyników");

                }
            }
        }



    }
}
