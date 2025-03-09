package com.internship.portal.mapper;

import com.internship.portal.model.entity.User;
import com.internship.portal.model.resource.UserResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.roleName", target = "roleName", defaultValue = "UNKNOWN")
    UserResource userToUserResource(User user);
}
