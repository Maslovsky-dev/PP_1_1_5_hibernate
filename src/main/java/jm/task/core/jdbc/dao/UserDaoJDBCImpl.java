package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()){
            statement.addBatch("CREATE TABLE IF NOT EXISTS user ("
                            + "id bigint NOT NULL AUTO_INCREMENT  PRIMARY KEY,"
                            + "name varchar(45) DEFAULT NULL,"
                            + "lastName varchar(90) DEFAULT NULL,"
                            + "age tinyint DEFAULT NULL"
                            + ")");
            statement.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException("Problem with creating user table");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()){
            statement.addBatch("DROP TABLE IF EXISTS user;");
            statement.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Statement statement = connection.createStatement()){
            statement.addBatch("INSERT INTO user(name, lastName, age) VALUES(" +
                    String.format("'%s','%s','%s'", name, lastName, age) + ")");
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
            statement.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = connection.createStatement()){
            statement.addBatch( String.format("DELETE FROM user WHERE id = %d;", id));
            statement.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List <User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while(resultSet.next()){
                userList.add(new  User(resultSet.getString(2)
                        ,resultSet.getString(3),resultSet.getByte(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()){
            statement.addBatch("DELETE FROM user");
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
