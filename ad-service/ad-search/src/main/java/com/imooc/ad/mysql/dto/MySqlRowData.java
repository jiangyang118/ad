package com.imooc.ad.mysql.dto;

import java.util.ArrayList;
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
 * @create 19-2-12 下午2:12
 * @description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySqlRowData {

    private String tableName;
    private String level;
    private OpType opType;
    private List<Map<String, String>> fieldValueMap = new ArrayList<>();
}
