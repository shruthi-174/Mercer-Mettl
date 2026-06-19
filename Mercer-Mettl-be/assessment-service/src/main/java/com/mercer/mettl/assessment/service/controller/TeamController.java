package com.mercer.mettl.assessment.service.controller;

import com.mercer.mettl.assessment.service.dto.TeamMemberResponse;
import com.mercer.mettl.assessment.service.entity.Team;
import com.mercer.mettl.assessment.service.entity.TeamMember;
import com.mercer.mettl.assessment.service.repository.TeamMemberRepository;
import com.mercer.mettl.assessment.service.repository.TeamRepository;
import com.mercer.mettl.assessment.service.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private TeamService teamService;

    @PostMapping
    public Team create(@RequestBody Team team) {
        return teamService.createTeam(team);
    }

    @PostMapping("/{teamId}/members")
    public TeamMember addMember(
            @PathVariable Long teamId,
            @RequestParam Integer userId
    ) {

        return teamService.addMemberToTeam(teamId, userId);
    }

    @GetMapping("/{teamId}/members")
    public List<TeamMember> members(
            @PathVariable Long teamId
    ) {
       return teamService.getTeamMembers(teamId);
    }

    @GetMapping("/org/{orgId}")
    public List<Team> getTeamsByOrg(
            @PathVariable Integer orgId
    ){
        return teamService.getTeamsByOrganization(orgId);
    }

    @GetMapping("/{teamId}/members/details")
    public List<TeamMemberResponse> getMembers(
            @PathVariable Long teamId
    ){
        return teamService.getMembers(teamId);
    }

    @GetMapping("/user/{userId}")
    public List<Team> getTeamsByUser(
            @PathVariable Integer userId
    ){
        return teamService.getTeamsByUser(userId);
    }
}