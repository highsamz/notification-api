package br.com.avaliacao_globo.application.mapper;

import br.com.avaliacao_globo.application.dto.response.SubscriptionResponse;
import br.com.avaliacao_globo.domain.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "status.name", target = "status")
    SubscriptionResponse toResponse(Subscription subscription);

}
