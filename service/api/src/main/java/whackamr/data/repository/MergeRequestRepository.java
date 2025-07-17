package whackamr.data.repository;

import org.springframework.data.repository.CrudRepository;

import whackamr.data.entity.MergeRequest;

public interface MergeRequestRepository extends CrudRepository<MergeRequest, Integer>{

}
