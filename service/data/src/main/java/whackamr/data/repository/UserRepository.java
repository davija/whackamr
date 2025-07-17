package whackamr.data.repository;

import org.springframework.data.repository.CrudRepository;

import whackamr.data.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>, RefreshableRepository<User>
{
    User findByUsername(String username);

    void refresh(User user);
}
