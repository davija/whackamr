package whackamr.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import whackamr.AlreadyExistsException;
import whackamr.NoSuchEntityException;
import whackamr.data.dto.TeamDto;
import whackamr.data.entity.Team;
import whackamr.data.repository.TeamRepository;
import whackamr.data.repository.UserRepository;
import whackamr.mapping.FromDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@RestController
@AllArgsConstructor
@RequestMapping("/api/teams")
public class TeamController
{

    private ModelMapper mapper;
    private TeamRepository teamRepository;
    private UserRepository userRepository;

    @GetMapping
    public List<TeamDto> getAllTeams()
    {
        var teams = new ArrayList<TeamDto>();
        var entities = teamRepository.findAll();

        entities.forEach(team -> teams.add(mapper.map(team, TeamDto.class)));

        return teams;
    }

    @GetMapping("/{teamId}")
    public TeamDto getTeam(@PathVariable int teamId)
    {
        var team = teamRepository.findById(teamId);

        if (team.isEmpty())
        {
            throw new NoSuchEntityException(String.format("Team with id %d could not be found", teamId));
        }

        return mapper.map(team, TeamDto.class);
    }

    @PostMapping
    public TeamDto createTeam(@RequestBody
    @FromDto(TeamDto.class) Team team)
    {
        var entity = teamRepository.save(team);

        return mapper.map(entity, TeamDto.class);
    }

    @PostMapping("/{teamId}/members/{userId}")
    public TeamDto addMember(@PathVariable int teamId, @PathVariable int userId)
    {
        var teamContainer = teamRepository.findById(teamId);
        var userContainer = userRepository.findById(userId);

        if (teamContainer.isEmpty() || userContainer.isEmpty())
        {
            throw new IllegalStateException("Could not find specified team or user");
        }

        var team = teamContainer.get();
        var user = userContainer.get();

        if (team.getMembers().stream().anyMatch(teamMember -> teamMember.getUserId() == userId))
        {
            throw new AlreadyExistsException(String.format("User with id %d is already a member of team %d", userId, teamId));
        }

        team.getMembers().add(user);
        user.getTeams().add(team);

        team = teamRepository.save(team);

        return mapper.map(team, TeamDto.class);
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public TeamDto removeMember(@PathVariable int teamId, @PathVariable int userId)
    {
        var teamContainer = teamRepository.findById(teamId);
        var userContainer = userRepository.findById(userId);

        if (teamContainer.isEmpty() || userContainer.isEmpty())
        {
            throw new IllegalStateException("Could not find specified team or user");
        }

        var team = teamContainer.get();

        team.getMembers().remove(userContainer.get());

        team = teamRepository.save(team);

        return mapper.map(team, TeamDto.class);
    }

    @DeleteMapping("/{teamId}")
    public void deleteTeam(@PathVariable int teamId)
    {
        var teamContainer = teamRepository.findById(teamId);

        if (teamContainer.isPresent())
        {
            var team = teamContainer.get();

            // Remove all team members before removing.
            team.getMembers().clear();

            // Remove team
            teamRepository.delete(team);
        }
    }
}
