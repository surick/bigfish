package com.github.surick.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Data
@TableName("rss")
public class Rss {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String url;
}
