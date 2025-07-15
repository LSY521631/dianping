package com.hmdp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScrollResult {
    /**
     * 数据列表
     */
    private List<?> list;
    /**
     * 时间戳,上次查询的最小时间
     */
    private Long minTime;
    /**
     * 数据的偏移量，用于下一次查询
     */
    private Integer offset;
}
