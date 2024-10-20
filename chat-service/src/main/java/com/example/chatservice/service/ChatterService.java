package com.example.chatservice.service;

import com.example.chatservice.model.Chatter;
import com.example.chatservice.repository.ChatterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ChatterService {

    private final ChatterRepository repository;
    public Chatter getChatterById(Long id) {
        if(repository.existsById(id))
            return repository.getReferenceById(id);
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
