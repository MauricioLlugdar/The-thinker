package com.maullu.the_thinker.Model;

import org.springframework.data.annotation.Id;

public record Idea(@Id Long id, String title, String description, Visibility visibility) {
}
