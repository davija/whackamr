package whackamr.data.entity;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "team")
public class Team
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    @Column(
            name = "team_id")
    private int teamId;

    @Column(
            name = "team_name",
            nullable = false)
    private String teamName;

    @ManyToMany
    @JoinTable(
            name = "team_user",
            joinColumns = @JoinColumn(
                    name = "team_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id"))
    private Collection<User> members;
}
