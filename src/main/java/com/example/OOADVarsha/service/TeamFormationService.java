package com.example.OOADVarsha.service;

import com.example.OOADVarsha.util.DatabaseUtils;
// import com.example.OOADVarsha.model.TeamMapping;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamFormationService {
    public void createUser(String srn, String name, String password, String role) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "INSERT INTO " + role + "s (srn, name, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, srn);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTeamRequest(String student1Srn, String student2Srn, String student3Srn, String student4Srn, String teacherSrn, String domain, String problemStatement) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "INSERT INTO team_mappings (student1_srn, student2_srn, student3_srn, student4_srn, teacher_srn, domain, problem_statement) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, student1Srn);
            statement.setString(2, student2Srn);
            statement.setString(3, student3Srn);
            statement.setString(4, student4Srn);
            statement.setString(5, teacherSrn);
            statement.setString(6, domain);
            statement.setString(7, problemStatement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<com.example.OOADVarsha.model.TeamMapping> getTeacherTeamRequests(String teacherSrn) {
        List<com.example.OOADVarsha.model.TeamMapping> teamRequests = new ArrayList<>();
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT id, student1_srn, student2_srn, student3_srn, student4_srn, domain, problem_statement, request_accepted FROM team_mappings WHERE teacher_srn = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, teacherSrn);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                com.example.OOADVarsha.model.TeamMapping teamMapping = new com.example.OOADVarsha.model.TeamMapping();
                teamMapping.setId(resultSet.getLong("id"));
                teamMapping.setStudent1Srn(resultSet.getString("student1_srn"));
                teamMapping.setStudent2Srn(resultSet.getString("student2_srn"));
                teamMapping.setStudent3Srn(resultSet.getString("student3_srn"));
                teamMapping.setStudent4Srn(resultSet.getString("student4_srn"));
                teamMapping.setDomain(resultSet.getString("domain"));
                teamMapping.setProblemStatement(resultSet.getString("problem_statement"));
                teamMapping.setRequestAccepted(resultSet.getBoolean("request_accepted"));
                teamRequests.add(teamMapping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamRequests;
    }

public List<Map<String, Object>> getStudentTeamRequests(String studentSrn) {
    List<Map<String, Object>> teamRequests = new ArrayList<>();
    try (Connection connection = DatabaseUtils.getConnection()) {
        String query = "SELECT tm.id, s1.name AS student1_name, s2.name AS student2_name, s3.name AS student3_name, s4.name AS student4_name, tm.domain, tm.problem_statement, tm.request_accepted, t.name AS teacher_name " +
                "FROM team_mappings tm " +
                "JOIN students s1 ON tm.student1_srn = s1.srn " +
                "JOIN students s2 ON tm.student2_srn = s2.srn " +
                "JOIN students s3 ON tm.student3_srn = s3.srn " +
                "JOIN students s4 ON tm.student4_srn = s4.srn " +
                "JOIN teachers t ON tm.teacher_srn = t.srn " +
                "WHERE tm.student1_srn = ? OR tm.student2_srn = ? OR tm.student3_srn = ? OR tm.student4_srn = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, studentSrn);
        statement.setString(2, studentSrn);
        statement.setString(3, studentSrn);
        statement.setString(4, studentSrn);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Map<String, Object> teamMapping = new HashMap<>();
            teamMapping.put("id", resultSet.getLong("id"));
            teamMapping.put("student1Name", resultSet.getString("student1_name"));
            teamMapping.put("student2Name", resultSet.getString("student2_name"));
            teamMapping.put("student3Name", resultSet.getString("student3_name"));
            teamMapping.put("student4Name", resultSet.getString("student4_name"));
            teamMapping.put("domain", resultSet.getString("domain"));
            teamMapping.put("problemStatement", resultSet.getString("problem_statement"));
            teamMapping.put("requestAccepted", resultSet.getBoolean("request_accepted"));
            teamMapping.put("teacherName", resultSet.getString("teacher_name"));
            teamRequests.add(teamMapping);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return teamRequests;
}

    public void updateTeamRequest(Long requestId, boolean accepted) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "UPDATE team_mappings SET request_accepted = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(1, accepted);
            statement.setLong(2, requestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> getAllTeachers() {
        List<Map<String, String>> teachers = new ArrayList<>();
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT srn, name FROM teachers";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, String> teacher = new HashMap<>();
                teacher.put("srn", resultSet.getString("srn"));
                teacher.put("name", resultSet.getString("name"));
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }
}