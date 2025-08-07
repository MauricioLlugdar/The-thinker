package com.maullu.the_thinker.Controllers;

import com.maullu.the_thinker.IdeasRepository;
import com.maullu.the_thinker.Model.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/ideas")
public class IdeasController {

    private final IdeasRepository ideasRepository;

    private IdeasController(IdeasRepository ideasRepository){
        this.ideasRepository = ideasRepository;
    }

    @GetMapping
    public ResponseEntity<List<Idea>> findAll(Pageable pageable){
        Page<Idea> page = ideasRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort()
                )
        );
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("{id}")
    public ResponseEntity<Idea> findById(@PathVariable Long id){
        Optional<Idea> idea = ideasRepository.findById(id);
        if(idea.isPresent()){
            return ResponseEntity.ok(idea.get());
        }
        return ResponseEntity.notFound().build();
    }
}
