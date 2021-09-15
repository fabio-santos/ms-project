package com.vkncode.project.domain.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    public UsernamePasswordAuthenticationToken toAuthToken () {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}