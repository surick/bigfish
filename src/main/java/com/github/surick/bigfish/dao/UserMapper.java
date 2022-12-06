package com.github.surick.bigfish.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.surick.bigfish.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
