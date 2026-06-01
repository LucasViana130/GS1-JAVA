package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse extends RepresentationModel<AuthResponse> {

    private String token;
    private UserResponse user;

    public static AuthResponse fromEntity(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .user(UserResponse.fromEntity(user))
                .build();
    }
}
