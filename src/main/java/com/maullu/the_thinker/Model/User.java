package com.maullu.the_thinker;

import org.springframework.data.annotation.Id;

public record User(@Id Long id, String name, String email, String Password) {
}
