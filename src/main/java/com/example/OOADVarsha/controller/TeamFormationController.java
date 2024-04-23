package com.example.OOADVarsha.controller;
import com.example.OOADVarsha.model.TeamMapping;
import com.example.OOADVarsha.service.TeamFormationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamFormationController {
    private final TeamFormationService teamFormationService;

    public TeamFormationController(TeamFormationService teamFormationService) {
        this.teamFormationService = teamFormationService;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody Map<String, String> request) {
        String srn = request.get("srn");
        String name = request.get("name");
        String password = request.get("password");
        String role = request.get("role");
        teamFormationService.createUser(srn, name, password, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests")
    public ResponseEntity<Void> createTeamRequest(@RequestBody Map<String, String> request) {
        String student1Srn = request.get("student1Srn");
        String student2Srn = request.get("student2Srn");
        String student3Srn = request.get("student3Srn");
        String student4Srn = request.get("student4Srn");
        String teacherSrn = request.get("teacherSrn");
        String domain = request.get("domain");
        String problemStatement = request.get("problemStatement");
        teamFormationService.createTeamRequest(student1Srn, student2Srn, student3Srn, student4Srn, teacherSrn, domain, problemStatement);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher/{teacherSrn}")
    public ResponseEntity<List<TeamMapping>> getTeacherTeamRequests(@PathVariable String teacherSrn) {
        List<TeamMapping> teamRequests = teamFormationService.getTeacherTeamRequests(teacherSrn);
        return ResponseEntity.ok(teamRequests);
    }

    @GetMapping("/student/{studentSrn}")
    public ResponseEntity<List<Map<String, Object>>> getStudentTeamRequests(@PathVariable String studentSrn) {
        List<Map<String, Object>> teamRequests = teamFormationService.getStudentTeamRequests(studentSrn);
        System.out.println(teamRequests);
        return ResponseEntity.ok(teamRequests);
    }

    @PutMapping("/requests/{requestId}")
    public ResponseEntity<Void> updateTeamRequest(@PathVariable Long requestId, @RequestBody Map<String, Boolean> request) {
        boolean accepted = request.get("accepted");
        teamFormationService.updateTeamRequest(requestId, accepted);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Map<String, String>>> getAllTeachers() {
        List<Map<String, String>> teachers = teamFormationService.getAllTeachers();
        System.out.println(teachers);
        return ResponseEntity.ok(teachers);
    }
}