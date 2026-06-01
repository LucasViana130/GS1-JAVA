package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.FarmRequest;
import br.com.fiap.agroorbit.dtos.response.FarmResponse;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.Farm;
import br.com.fiap.agroorbit.models.User;
import br.com.fiap.agroorbit.repositories.FarmRepository;
import br.com.fiap.agroorbit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmService {

    private final FarmRepository repository;
    private final UserRepository userRepository;

    @Cacheable(value = "farms")
    public Page<FarmResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(FarmResponse::fromEntity);
    }

    @Cacheable(value = "farmsByUser", key = "#userId")
    public List<FarmResponse> findByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(FarmResponse::fromEntity).toList();
    }

    public Farm findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fazenda não encontrada"));
    }

    @Cacheable(value = "farm", key = "#id")
    public FarmResponse findById(Long id) {
        return FarmResponse.fromEntity(findEntityById(id));
    }

    @CacheEvict(value = {"farms", "farmsByUser", "dashboard"}, allEntries = true)
    public FarmResponse create(FarmRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return FarmResponse.fromEntity(repository.save(new Farm(request, user)));
    }

    @CacheEvict(value = {"farms", "farmsByUser", "farm", "dashboard"}, allEntries = true)
    public FarmResponse update(Long id, FarmRequest request) {
        Farm farm = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fazenda não encontrada"));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        farm.updateFrom(request, user);
        return FarmResponse.fromEntity(repository.save(farm));
    }

    @CacheEvict(value = {"farms", "farmsByUser", "farm", "dashboard"}, allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Fazenda não encontrada");
        }
        repository.deleteById(id);
    }
}
