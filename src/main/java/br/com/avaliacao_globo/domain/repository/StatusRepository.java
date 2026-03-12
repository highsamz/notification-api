package br.com.avaliacao_globo.domain.repository;

import br.com.avaliacao_globo.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Optional<Status> findByName(String name);
}
