package pl.gradzik;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ATMInfo
{

    public static int atmID;

    public static int PLN100;
    public static int PLN50;
    public static int PLN20;
    public static int PLN10;


    public ATMInfo()
    {
        atmID = 1;

        try {
            getInfoFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void getInfoFromDB() throws SQLException {
        Connection connection = MySQlConnector.connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery
                (
                        "select * from  atms;"
                );

        resultSet.next();

        PLN100 = resultSet.getInt("100PLN");
        PLN50 = resultSet.getInt("50PLN");
        PLN20 = resultSet.getInt("20PLN");
        PLN10 = resultSet.getInt("10PLN");
    }


    public static void updateDB() throws SQLException {
        Connection connection = MySQlConnector.connect();
        Statement statement = connection.createStatement();
        statement.executeUpdate
                ("update atms\n" +
                        "set 100PLN = " + PLN100  +
                        ",50PLN = " + PLN50 +
                        ",20PLN = " + PLN20 +
                        ",10PLN = " + PLN10 +
                        " where ATM_id = "+ atmID +";");
    }

}
