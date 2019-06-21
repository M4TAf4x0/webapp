package com.archetype.monolithic.webapp.service.mapper;

import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityMapper<D, E> {

    D toDto(E entity);

    List<D> toDto(List<E> entityList);

    List<E> toEntity(List<D> dtoList);

    E toEntity(D dto);
}