package com.maullu.the_thinker;

import com.maullu.the_thinker.Model.Idea;
import com.maullu.the_thinker.Model.Visibility;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface IdeasRepository extends CrudRepository<Idea, Long>, PagingAndSortingRepository<Idea, Long> {
    List<Idea> findByVisibility(Visibility visibility, Pageable pageable);
}
