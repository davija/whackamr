package whackamr.data.entity;

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merge_request")
public class MergeRequest
{
    @Id
    @GeneratedValue
    @Column(name = "merge_request_id")
    private int mergeRequestId;

    @Column(name = "link")
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "issue_number")
    private String issueNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "merge_request_tag", joinColumns = @JoinColumn(name = "merge_request_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tag> tags;

    @ManyToMany
    @JoinTable(name = "related_merge_request", joinColumns = @JoinColumn(name = "merge_request_id"), inverseJoinColumns = @JoinColumn(name = "related_merge_request_id"))
    private Collection<MergeRequest> relatedRequests;
}
