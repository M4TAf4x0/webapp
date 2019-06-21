package com.archetype.monolithic.webapp.service.mapper;

import com.archetype.monolithic.webapp.domain.User;
import com.archetype.monolithic.webapp.service.dto.UserDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {

    /**
     * Create an entity from its id.
     *
     * @param id the entity id
     * @return the entity
     */
    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

}