package br.com.avaliacao_globo.domain.repository;

import br.com.avaliacao_globo.domain.entity.EventHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventHistoryRepository extends JpaRepository<EventHistory, Long> {

    List<EventHistory> findBySubscriptionIdOrderByProcessedAtAsc(String subscriptionId);
}
