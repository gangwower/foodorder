package edu.task.foodorder.backend.repos;


import edu.task.foodorder.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
