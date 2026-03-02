package org.interview.projectinterview.mapper;

import org.interview.projectinterview.dto.request.UserCreationRequest;
import org.interview.projectinterview.dto.response.UserResponse;
import org.interview.projectinterview.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
}
