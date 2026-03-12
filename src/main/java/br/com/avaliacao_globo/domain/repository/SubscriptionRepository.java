package br.com.avaliacao_globo.domain.repository;

import br.com.avaliacao_globo.domain.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
}
