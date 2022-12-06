package com.github.surick.bigfish.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("webdetails")
public class WebDetails {

    @TableId
    private Integer id;

    private String name;

    private String url;


}
