package com.bobgarage.userservice.entities;

import lombok.Data;

@Data
public class UpdateUserRequest {
    public String name;
    public String email;
}
