package edu.task.foodorder.backend.service;


import edu.task.foodorder.backend.domain.Request;
import edu.task.foodorder.backend.domain.User;
import edu.task.foodorder.backend.repos.RequestRepo;
import edu.task.foodorder.backend.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    private RequestRepo requestRepo;
    private UserRepo userRepo;

    public RequestService(RequestRepo requestRepo, UserRepo userRepo) {
        this.requestRepo = requestRepo;
        this.userRepo = userRepo;
    }

    public List<Request> findAll(){
        return requestRepo.findAll();
    }

    public List<Request> findAll(User user){
        return requestRepo.search(user);
    }

    public Optional<Request> findById(Long id){
        return requestRepo.findById(id);
    }

    public void delete(Request request){
        requestRepo.delete(request);
    }

    public void save(Request request){
        requestRepo.save(request);
    }

    public RequestRepo getRequestRepo() {
        return requestRepo;
    }
}

