package com.github.surick.bigfish.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Data
@Builder
@TableName("resource")
public class Resource {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String link;

    private String desc;

    private String imgUrl;

    private String pushDate;

    private String rssId;
}
