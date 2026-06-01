package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.UserRequest;
import br.com.fiap.agroorbit.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_USER")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "nm_name", nullable = false, length = 100)
    private String name;

    @Column(name = "ds_email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "ds_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_role", nullable = false, length = 30)
    private UserRole role;

    @Column(name = "dt_created", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }

    public void updateFrom(UserRequest request) {
        this.name = request.name();
        this.email = request.email();
        this.role = request.role();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == UserRole.ROLE_ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_TECHNICIAN"), new SimpleGrantedAuthority("ROLE_PRODUCER"));
        }
        if (role == UserRole.ROLE_TECHNICIAN) {
            return List.of(new SimpleGrantedAuthority("ROLE_TECHNICIAN"), new SimpleGrantedAuthority("ROLE_PRODUCER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_PRODUCER"));
    }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
