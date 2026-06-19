package com.mercer.mettl.assessment.service.service;

import com.mercer.mettl.assessment.service.dto.TeamMemberResponse;
import com.mercer.mettl.assessment.service.entity.Team;
import com.mercer.mettl.assessment.service.entity.TeamMember;
import com.mercer.mettl.assessment.service.entity.TeamStatus;
import com.mercer.mettl.assessment.service.entity.User;
import com.mercer.mettl.assessment.service.repository.TeamMemberRepository;
import com.mercer.mettl.assessment.service.repository.TeamRepository;
import com.mercer.mettl.assessment.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    public Team createTeam(Team team) {
      return  teamRepository.save(team);
    }

    public TeamMember addMemberToTeam(Long teamId, Integer userId) {
        boolean exists = memberRepository
                .findByTeamId(teamId)
                .stream()
                .anyMatch(m -> m.getUserId().equals(userId));

        if (exists) {
            throw new RuntimeException("User already in team");
        }
        TeamMember member = new TeamMember();

        member.setTeamId(teamId);
        member.setUserId(userId);
        member.setStatus(TeamStatus.ADDED);

        return memberRepository.save(member);
    }

    public List<TeamMember> getTeamMembers(Long teamId) {
        return memberRepository.findByTeamId(teamId);
    }

    public List<Team> getTeamsByOrganization(Integer orgId) {
        return teamRepository.findByOrgId(orgId);
    }

    public List<TeamMemberResponse> getMembers(Long teamId){

        List<TeamMember> members = memberRepository.findByTeamId(teamId);

        return members.stream().map(member -> {
            User user = userRepository.findById(member.getUserId()).orElseThrow();

            return new TeamMemberResponse(user.getUserId(), user.getFullName(), user.getEmail(), member.getStatus());
        }).toList();
    }

    public List<Team> getTeamsByUser(Integer userId){
        List<TeamMember> members =
                memberRepository.findByUserId(userId);

        List<Long> ids =
                members.stream()
                        .map(TeamMember::getTeamId)
                        .toList();

        return teamRepository.findAllById(ids);


    }
}
