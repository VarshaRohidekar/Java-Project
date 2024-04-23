package com.example.OOADVarsha.service;

import com.example.OOADVarsha.model.TeamMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TeamFormationFacade {

    @Autowired
    private TeamFormationService teamFormationService;

    public void registerUser(String srn, String name, String password, String role) {
        teamFormationService.createUser(srn, name, password, role);
    }

    public void requestTeamFormation(String student1Srn, String student2Srn, String student3Srn, String student4Srn, String teacherSrn, String domain, String problemStatement) {
        teamFormationService.createTeamRequest(student1Srn, student2Srn, student3Srn, student4Srn, teacherSrn, domain, problemStatement);
    }

    public List<TeamMapping> fetchTeacherTeamRequests(String teacherSrn) {
        return teamFormationService.getTeacherTeamRequests(teacherSrn);
    }

    public List<Map<String, Object>> fetchStudentTeamRequests(String studentSrn) {
        return teamFormationService.getStudentTeamRequests(studentSrn);
    }

    public void respondToTeamRequest(Long requestId, boolean accepted) {
        teamFormationService.updateTeamRequest(requestId, accepted);
    }

    public List<Map<String, String>> listAllTeachers() {
        return teamFormationService.getAllTeachers();
    }
}