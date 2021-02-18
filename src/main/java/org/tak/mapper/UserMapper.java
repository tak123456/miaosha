package org.tak.mapper;

import org.apache.ibatis.annotations.Param;
import org.tak.entity.User;

public interface UserMapper {

    User findUserById(@Param("id") Integer id);
}
