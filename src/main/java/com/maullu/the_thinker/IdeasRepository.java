package com.maullu.the_thinker;

import com.maullu.the_thinker.Model.Idea;
import org.springframework.data.repository.ListCrudRepository;

public interface IdeasRepository extends ListCrudRepository<Idea, Long> {
}
