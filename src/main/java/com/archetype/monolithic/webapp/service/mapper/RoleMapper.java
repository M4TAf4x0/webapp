package com.archetype.monolithic.webapp.service.mapper;


import com.archetype.monolithic.webapp.domain.Role;
import com.archetype.monolithic.webapp.service.dto.RoleDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    /**
     * Create an entity from its id.
     *
     * @param id the entity id
     * @return the entity
     */
    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }

}