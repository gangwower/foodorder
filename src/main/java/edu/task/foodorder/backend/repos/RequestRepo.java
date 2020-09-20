package edu.task.foodorder.backend.repos;

import edu.task.foodorder.backend.domain.Request;
import edu.task.foodorder.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {

    @Query("select r from Request r where r.client_id like :user_id")
    List<Request> search(@Param("user_id") User user);

}
