package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.UserRequest;
import br.com.fiap.agroorbit.dtos.response.UserResponse;
import br.com.fiap.agroorbit.exceptions.BusinessException;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.User;
import br.com.fiap.agroorbit.repositories.FarmRepository;
import br.com.fiap.agroorbit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final FarmRepository farmRepository;

    @Cacheable(value = "users")
    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(UserResponse::fromEntity).toList();
    }

    public User findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    @Cacheable(value = "user", key = "#id")
    public UserResponse findById(Long id) {
        return UserResponse.fromEntity(findEntityById(id));
    }

    @CacheEvict(value = {"users", "user", "dashboard"}, allEntries = true)
    public UserResponse update(Long id, UserRequest request) {
        User user = findEntityById(id);

        repository.findByEmail(request.email()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new BusinessException("E-mail já cadastrado");
            }
        });

        user.updateFrom(request);
        return UserResponse.fromEntity(repository.save(user));
    }

    @CacheEvict(value = {"users", "user", "dashboard"}, allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }

        if (farmRepository.countByUserId(id) > 0) {
            throw new BusinessException("Não é possível excluir um usuário vinculado a fazendas");
        }

        repository.deleteById(id);
    }
}
