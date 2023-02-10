package DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SocialMediaDAO {

    /*
     * 
     * Class Methods
     * 
     */

    public Account registerUser(String username, String password) {

        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

            ResultSet pkeyResultSet = statement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int account_id = pkeyResultSet.getInt("account_id");
                return new Account(account_id, username, password);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Account attemptLogin(String username, String password) {

        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Message postMessage(Message message) {

        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());
            statement.executeUpdate();
            
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                Message newMessage = new Message(rs.getInt("message_id"), message.getPosted_by(), 
                                                message.getMessage_text(), message.getTime_posted_epoch());
                return newMessage;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public List<Message> getAllMessages() {

        Connection connection = ConnectionUtil.getConnection();

        try {

            List<Message> messages = new ArrayList<>();
            String sql = "SELECT * FROM message;";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                                                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Message getMessageByID(int id) {

        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                                                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public boolean deleteMessageByID(int id) {

        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            int i = statement.executeUpdate();
            if (i == 1) return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

    public Message updateMessageText(Message message) {

        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, message.getMessage_text());
            statement.setInt(2, message.getMessage_id());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            while(rs.next()) {
                Message updatedMessage = new Message(rs.getInt("message_id"), message.getMessage_id(), 
                                                message.getMessage_text(), message.getTime_posted_epoch());
                return updatedMessage;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public List<Message> getAllMessagesFromAccountID(int id) {

        Connection connection = ConnectionUtil.getConnection();

        try {

            List<Message> messages = new ArrayList<>();
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                                                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

}
