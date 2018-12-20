package com.lab.growbox.growbox.utils;

import com.lab.growbox.growbox.entity.Data;

import java.sql.*;

public class JDBCConnection {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/growbox";
    private static final String user = "user";
    private static final String password = "user";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public boolean save(Data data) {
        try {
            Connection con = getConnection();
            String SQL = "INSERT INTO data " +
                    "(`air_hum`," +
                    "`bright`," +
                    "`date`," +
                    "`time`," +
                    "`ground_hum`," +
                    "`temperature`," +
                    "`water_level`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            System.out.println(SQL);
            PreparedStatement statement = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

            statement.setFloat(1, data.getAirHum());
            statement.setFloat(2, data.getBright());
            statement.setDate(3, data.getDate());
            statement.setTime(4, data.getTime());
            statement.setInt(5, data.getGroundHum());
            statement.setFloat(6, data.getTemperature());
            statement.setInt(7, data.getWaterLevel());


            statement.executeUpdate();

            return true;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
