package com.imooc.ad.mysql.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imooc.ad.mysql.constant.OpType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wangjie
 * @version 1.0.0
 * @create 19-2-12 下午12:54
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {

    private String tableName;
    private String level;

    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /**
     * 字段索引 -> 字段名
     */
    private Map<Integer, String> posMap = new HashMap<>();
}
