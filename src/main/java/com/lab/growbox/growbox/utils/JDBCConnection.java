package com.lab.growbox.growbox.utils;

import com.lab.growbox.growbox.entity.Data;

import java.sql.*;

public class JDBCConnection {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:postgresql://localhost/growbox";
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
            String SQL = "INSERT INTO public.data(\n" +
                    "\tair_hum, bright, date, ground_hum, temperature, time, water_level)\n" +
                    "\tVALUES (?, ?, ?, ?, ?, ?, ?);";
            System.out.println("INSERT" + data.toString());
            PreparedStatement statement = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setFloat(1, data.getAirHum());
            statement.setFloat(2, data.getBright());
            statement.setDate(3, data.getDate());
            statement.setInt(4, data.getGroundHum());
            statement.setFloat(5, data.getTemperature());
            statement.setLong(6, data.getTime());
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
