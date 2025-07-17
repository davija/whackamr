package whackamr.data.repository;

import org.springframework.data.repository.CrudRepository;

import whackamr.data.entity.UserPassword;

public interface UserPasswordRepository extends CrudRepository<UserPassword, Integer>
{
    UserPassword findByUserId(int userId);
    
    void deleteByUserId(int userId);
}
