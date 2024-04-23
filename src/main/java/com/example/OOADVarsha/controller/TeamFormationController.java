package com.example.OOADVarsha.controller;

import com.example.OOADVarsha.model.TeamMapping;
import com.example.OOADVarsha.service.TeamFormationFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamFormationController {
    private final TeamFormationFacade teamFormationFacade;

    public TeamFormationController(TeamFormationFacade teamFormationFacade) {
        this.teamFormationFacade = teamFormationFacade;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> registerUser(@RequestBody Map<String, String> request) {
        String srn = request.get("srn");
        String name = request.get("name");
        String password = request.get("password");
        String role = request.get("role");
        teamFormationFacade.registerUser(srn, name, password, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests")
    public ResponseEntity<Void> requestTeamFormation(@RequestBody Map<String, String> request) {
        String student1Srn = request.get("student1Srn");
        String student2Srn = request.get("student2Srn");
        String student3Srn = request.get("student3Srn");
        String student4Srn = request.get("student4Srn");
        String teacherSrn = request.get("teacherSrn");
        String domain = request.get("domain");
        String problemStatement = request.get("problemStatement");
        teamFormationFacade.requestTeamFormation(student1Srn, student2Srn, student3Srn, student4Srn, teacherSrn, domain, problemStatement);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher/{teacherSrn}")
    public ResponseEntity<List<TeamMapping>> fetchTeacherTeamRequests(@PathVariable String teacherSrn) {
        List<TeamMapping> teamRequests = teamFormationFacade.fetchTeacherTeamRequests(teacherSrn);
        return ResponseEntity.ok(teamRequests);
    }

    @GetMapping("/student/{studentSrn}")
    public ResponseEntity<List<Map<String, Object>>> fetchStudentTeamRequests(@PathVariable String studentSrn) {
        List<Map<String, Object>> teamRequests = teamFormationFacade.fetchStudentTeamRequests(studentSrn);
        return ResponseEntity.ok(teamRequests);
    }

    @PutMapping("/requests/{requestId}")
    public ResponseEntity<Void> respondToTeamRequest(@PathVariable Long requestId, @RequestBody Map<String, Boolean> request) {
        boolean accepted = request.get("accepted");
        teamFormationFacade.respondToTeamRequest(requestId, accepted);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Map<String, String>>> listAllTeachers() {
        List<Map<String, String>> teachers = teamFormationFacade.listAllTeachers();
        return ResponseEntity.ok(teachers);
    }
}