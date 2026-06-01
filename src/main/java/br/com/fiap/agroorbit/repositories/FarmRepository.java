package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {
    long countByUserId(Long userId);
    java.util.List<Farm> findByUserId(Long userId);
}
