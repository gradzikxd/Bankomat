package pl.gradzik.GUI.AdminInterface;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel
{

    private JLabel label;
    private JTextField field;

    public InputPanel(int x, int y, String text)
    {
        this.setBounds(x,y,300,30);
        this.setLayout(null);
        this.setOpaque(false);


        label = new JLabel(text);
        label.setBounds(0,5,120,25);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setFont(new Font("Arial",Font.PLAIN,25));
        label.setForeground(Color.white);
        this.add(label);

        field = new JTextField("");
        field.setBounds(130,0,200,30);
        field.setFont(new Font("Arial",Font.PLAIN,20));
        this.add(field);

    }

    public String getFieldText()
    {
        if(field.getText().equals(""))
        {
            return "0";
        }

        try
        {
            int x = Integer.valueOf(field.getText());
            return field.getText();
        }
        catch (NumberFormatException e)
        {
            return "0";
        }
    }

    public void clearFlied()
    {
        field.setText("");
    }

    public void setFieldText(String text)
    {
        field.setText(text);
    }
}
