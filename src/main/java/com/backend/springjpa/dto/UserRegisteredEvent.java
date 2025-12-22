package com.backend.springjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegisteredEvent {
    private String email;
    private String name;
    private String token;
}
