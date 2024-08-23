/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.lanss.plugin.enums.oracle.TrustedOracleFunctionEnum;

import java.util.List;

public abstract class OracleAbstractMapper extends AbstractMapper {

    @Override
    public String select(List<String> columns, List<String> where) {
        StringBuilder sql = new StringBuilder("SELECT ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
            if (i == columns.size() - 1) {
                sql.append(" ");
            } else {
                sql.append(",");
            }
        }
        sql.append("FROM ");
        sql.append(getTableName());
        sql.append(" ");

        if (where.isEmpty()) {
            return sql.toString();
        }

        sql.append("WHERE ");
        for (int i = 0; i < where.size(); i++) {
            String column = where.get(i);

            // 租户列特殊处理 避免前端传空字符串是Oracle查询不到数据
            if ("tenant_id".equalsIgnoreCase(column)) {
                sql.append("(");
                sql.append(column).append(" = ").append("?");
                sql.append(" OR ");
                sql.append(column).append(" IS NULL ");
                sql.append(")");
            } else {
                sql.append(column).append(" = ").append("?");
            }

            if (i != where.size() - 1) {
                sql.append(" AND ");
            }
        }
        return sql.toString();
    }

    @Override
    public String count(List<String> where) {
        StringBuilder sql = new StringBuilder();
        String method = "SELECT ";
        sql.append(method);
        sql.append("COUNT(*) FROM ");
        sql.append(getTableName());
        sql.append(" ");

        if (null == where || where.isEmpty()) {
            return sql.toString();
        }

        sql.append("WHERE ");
        for (int i = 0; i < where.size(); i++) {
            String column = where.get(i);
            if ("tenant_id".equalsIgnoreCase(column)) {
                sql.append("(");
                sql.append(column).append(" = ").append("?");
                sql.append(" OR ");
                sql.append(column).append(" IS NULL ");
                sql.append(")");
            } else {
                sql.append(column).append(" = ").append("?");
            }
            if (i != where.size() - 1) {
                sql.append(" AND ");
            }
        }
        return sql.toString();
    }

    public String buildPaginationSql(String originalSql, int startRow, int pageSize) {
        return "SELECT * FROM ( SELECT TMP2.* FROM (SELECT TMP.*, ROWNUM ROW_ID FROM ( " + originalSql
                + " ) TMP) TMP2 WHERE ROWNUM <=" + (startRow + pageSize) + ") WHERE ROW_ID > " + startRow;
    }

    @Override
    public abstract String getTableName();

    @Override
    public abstract String getDataSource();

    @Override
    public String getFunction(String functionName) {
        return TrustedOracleFunctionEnum.getFunctionByName(functionName);
    }
}