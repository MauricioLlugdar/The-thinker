package com.maullu.the_thinker.Controllers;

import com.maullu.the_thinker.Repository.IdeasRepository;
import com.maullu.the_thinker.Model.Idea;
import com.maullu.the_thinker.Model.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
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

    @PostMapping
    public ResponseEntity<Void> createIdea(@RequestBody Idea newIdea, UriComponentsBuilder ucb){
        Idea savedIdea = ideasRepository.save(newIdea);
        URI locationOfTheNewIdea = ucb
                .path("/ideas/{id}")
                .buildAndExpand(savedIdea.getId())
                .toUri();
        return ResponseEntity.created(locationOfTheNewIdea).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchIdea(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        Optional<Idea> optionalIdea = ideasRepository.findById(id);
        if (optionalIdea.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Idea updatedIdea = optionalIdea.get();
        if (updates.containsKey("title")) {
            updatedIdea.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            updatedIdea.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("visibility")) {
            updatedIdea.setVisibility(Visibility.valueOf((String) updates.get("visibility")));
        }

        ideasRepository.save(updatedIdea);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        ideasRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
