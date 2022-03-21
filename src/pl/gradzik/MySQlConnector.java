package pl.gradzik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQlConnector
{
    private static String hostName = "localhost"; // or localhost
    private static String port = "3306";
    private static String DataBaseName = "bank-system";

    private static String url = "jdbc:mysql://"+hostName+":"+port+"/"+DataBaseName+"";
    private static String login = "Bank";
    private static String password = "P@ssw0rd";


    public static Connection connect()
    {
        try {
            return DriverManager.getConnection(url,login,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
