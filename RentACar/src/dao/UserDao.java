package dao;

import core.Db;
import core.Helper;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private final Connection connection;

    public UserDao() {
        this.connection = Db.getInstance();
    }

    public ArrayList<User> findAll(){
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM public.user";
        User user = null;
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                userList.add(this.match(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    public User findByLogin(String username, String password){
        User user = null;
        String query = "SELECT * FROM public.user WHERE user_name = ? AND user_pass = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            ResultSet resultSet = pr.executeQuery();
            if (resultSet.next()){
                user = this.match(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return user;
    }
    public User match(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("user_name"));
        user.setPassword(resultSet.getString("user_pass"));
        user.setRole(resultSet.getString("user_role"));

        return user;
    }
}

