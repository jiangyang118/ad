package com.imooc.ad.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.ParseTemplate;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.mysql.dto.Template;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wangjie
 * @version 1.0.0
 * @create 19-2-12 下午1:14
 * @description
 */

@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate parseTemplate;

    private final JdbcTemplate jdbcTemplate;

    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    public TableTemplate getTable(String tableName) {
        return parseTemplate.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStream = cl.getResourceAsStream(path)) {
            assert inputStream != null;
            Template template = JSON.parseObject(inputStream, Charset.defaultCharset(), Template.class);
            this.parseTemplate = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException("fail to parse json file.");
        }
    }

    private void loadMeta() {
        for (Map.Entry<String, TableTemplate> entry : parseTemplate.getTableTemplateMap().entrySet()) {
            TableTemplate table = entry.getValue();
            List<String> updateFields = table.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> insertFields = table.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(OpType.DELETE);

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    parseTemplate.getDatabase(), table.getTableName()
            }, (rs, i) -> {
                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");
                if ((updateFields != null && updateFields.contains(colName))
                        || (insertFields != null && insertFields.contains(colName))
                        || (deleteFields != null && deleteFields.contains(colName))) {
                    table.getPosMap().put(pos - 1, colName);
                }
                return null;
            });
        }
    }
}
