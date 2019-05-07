package com.imooc.ad.mysql.dto;

import java.util.List;
import java.util.Map;

import com.github.shyiko.mysql.binlog.event.EventType;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wangjie
 * @version 1.0.0
 * @create 19-2-12 下午1:34
 * @description
 */

@Data
public class BinlogRowData {

    private TableTemplate table;
    private EventType eventType;

    private List<Map<String, String>> after;
    private List<Map<String, String>> before;
}
