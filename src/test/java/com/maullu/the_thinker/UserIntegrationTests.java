package com.maullu.the_thinker;

import com.maullu.the_thinker.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIntegrationTests extends BaseIntegrationTest{
    @Autowired
    UserRepository userRepository;
}
