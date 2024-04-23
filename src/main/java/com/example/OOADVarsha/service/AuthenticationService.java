package com.example.OOADVarsha.service;

import com.example.OOADVarsha.util.DatabaseUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    public Map<String, Object> authenticateUser(String srn, String password, String role) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT srn, name FROM " + role + "s WHERE srn = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, srn);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Map<String, Object> user = new HashMap<>();
                user.put("srn", srn);
                user.put("name", name);
                user.put("role", role);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}