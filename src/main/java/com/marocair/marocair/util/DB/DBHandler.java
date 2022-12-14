package com.marocair.marocair.util.DB;

import com.marocair.marocair.base.ModelTypesToMapInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends DBConnection {

    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public DBHandler() {
        establishConnection();
    }


    public <T>void setParam(int index, T data) {
        try{
            switch (((Object) data).getClass().getSimpleName()) {
                case "Boolean" :
                    this.preparedStatement.setBoolean(index, (Boolean) data);
                    break;
                case "Integer" :
                    this.preparedStatement.setInt(index, (int) data);
                    break;
                case "Long" :
                    this.preparedStatement.setLong(index, (long) data);
                    break;
                case "String" :
                    this.preparedStatement.setString(index, (String) data);
                    break;
                case "Float" :
                    this.preparedStatement.setDouble(index, (Float) data);
                    break;
                case "Double" :
                    this.preparedStatement.setDouble(index, (Double) data);
                    break;
                case "LocalDate" :
                    this.preparedStatement.setObject(index, data);
                    break;
                case "Timestamp" :
                    this.preparedStatement.setTimestamp(index, (Timestamp) data);
                    break;
            }

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error with  statement parameter placeholder");
            this.preparedStatement = null;
        }
    }

    public boolean prepare(String query) {
        try{
            this.preparedStatement = conn.prepareStatement(query);
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Failed preparing statement");
            return false;
        }
    }

    public ResultSet execute () {
        try{
            if(this.preparedStatement != null){
                resultSet = this.preparedStatement.executeQuery();
                return resultSet;
            } else {
                System.out.println("Prepared query is null!");
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
    public int executeUpdate () {
        int count = 0;
        try{
            if(this.preparedStatement != null){
                count = this.preparedStatement.executeUpdate();
            } else {
                System.out.println("Prepared query is null!");
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return count;
    }

    public boolean isEmpty() {
        Boolean isEmpty = true;
        if (this.resultSet != null) {
            try {
                if (!this.resultSet.isBeforeFirst()) {
                    isEmpty = true;
                } else {
                    isEmpty = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isEmpty;
    }
    //check if provided result set is empty
    public boolean isEmpty(ResultSet result) {
        boolean isEmpty = true;
        if (result != null) {
            try {
                if (result.isBeforeFirst()) {
                    isEmpty = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isEmpty;
    }



    public void closeConnection () {
        if (this.conn != null) try {
            this.conn.close();
            System.out.println("Closing DB connection..");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error closing DB connection");
        }
    }

    public void closeQueryOperations () {
        if (this.resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
        if (this.preparedStatement != null) try { preparedStatement.close(); } catch (SQLException ignore) {}
    }

    public static<T> T getParams(ResultSet result, ModelTypesToMapInterface objectTypesMap, Class<T> impl) throws SQLException, NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        Constructor<? extends T> classConstructor = impl.getConstructor();

        List<Object> list = new ArrayList<>();
        for (String key : objectTypesMap.run().keySet()) {
            switch (key) {
                case "Boolean":
                    list.add(result.getBoolean(key));
                    break;
                case "Integer":
                    list.add(result.getInt(key));
                    break;
                case "Long":
                    list.add(result.getLong(key));
                    break;
                case "String":
                    list.add(result.getString(key));
                    break;
                case "Float":
                    list.add(result.getFloat(key));
                    break;
                case "Double":
                    list.add(result.getDouble(key));
                    break;
                case "LocalDate":
                    list.add(result.getObject(key));
                    break;
                case "Timestamp":
                    list.add(result.getTimestamp(key));
                    break;
            }
        }

        T model = classConstructor.newInstance(list);
        return model;
    }
}

