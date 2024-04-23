package com.example.OOADVarsha.model;

import lombok.Data;

@Data
public class TeamMapping {
    private Long id;
    private String student1Srn;
    private String student2Srn;
    private String student3Srn;
    private String student4Srn;
    private String teacherSrn;
    private String domain;
    private String problemStatement;
    private boolean requestAccepted;
}