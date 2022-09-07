package com.github.surick.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.surick.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
