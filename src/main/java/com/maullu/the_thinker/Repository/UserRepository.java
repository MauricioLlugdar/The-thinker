package com.maullu.the_thinker.Repository;

import com.maullu.the_thinker.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
