/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigTagsRelationMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.lanss.plugin.constants.CustomDataSourceConstant;

import java.util.ArrayList;
import java.util.List;

public class ConfigTagsRelationMapperByOracle extends OracleAbstractMapper implements ConfigTagsRelationMapper {

    @Override
    public MapperResult findConfigInfo4PageCountRows(MapperContext context) {
        return ConfigTagsRelationMapper.super.findConfigInfo4PageCountRows(context);
    }

    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        final int startRow = context.getStartRow();
        final int pageSize = context.getPageSize();
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
        final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);

        List<Object> paramList = new ArrayList<>();

        StringBuilder where = new StringBuilder(" WHERE ");
        final String sqlF = "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content FROM config_info  a LEFT JOIN "
                + "config_tags_relation b ON a.id=b.id";

        where.append("( a.tenant_id=? OR tenant_id IS NULL)");
        paramList.add(tenant);

        if (StringUtils.isNotBlank(dataId)) {
            where.append(" AND a.data_id=? ");
            paramList.add(dataId);
        }
        if (StringUtils.isNotBlank(group)) {
            where.append(" AND a.group_id=? ");
            paramList.add(group);
        }
        if (StringUtils.isNotBlank(appName)) {
            where.append(" AND a.app_name=? ");
            paramList.add(appName);
        }

        if (!StringUtils.isBlank(content)) {
            where.append(" AND a.content LIKE ? ");
            paramList.add(content);
        }

        where.append(" AND b.tag_name IN (");
        for (int i = 0; i < tagArr.length; i++) {
            if (i != 0) {
                where.append(", ");
            }
            where.append('?');
            paramList.add(tagArr[i]);
        }
        where.append(") ");
        String sql = sqlF + where + " AND  ROWNUM > " + sqlF + " AND ROWNUM <= " + (startRow + pageSize);
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult findConfigInfoLike4PageCountRows(MapperContext context) {
        return ConfigTagsRelationMapper.super.findConfigInfoLike4PageCountRows(context);
    }

    @Override
    public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
        final int startRow = context.getStartRow();
        final int pageSize = context.getPageSize();
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
        final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);

        List<Object> paramList = new ArrayList<>();

        StringBuilder where = new StringBuilder(" WHERE ");
        final String sqlFetchRows = "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content "
                + "FROM config_info a LEFT JOIN config_tags_relation b ON a.id=b.id ";

        where.append(" a.(tenant_id LIKE ? OR tenant_id IS NULL) ");
        paramList.add(tenant);
        if (!StringUtils.isBlank(dataId)) {
            where.append(" AND a.data_id LIKE ? ");
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where.append(" AND a.group_id LIKE ? ");
            paramList.add(group);
        }
        if (!StringUtils.isBlank(appName)) {
            where.append(" AND a.app_name = ? ");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND a.content LIKE ? ");
            paramList.add(content);
        }

        where.append(" AND b.tag_name IN (");
        for (int i = 0; i < tagArr.length; i++) {
            if (i != 0) {
                where.append(", ");
            }
            where.append('?');
            paramList.add(tagArr[i]);
        }
        where.append(") ");
        String sql = sqlFetchRows + where + " AND ROWNUM > " + startRow + " AND ROWNUM <= " + (startRow + pageSize);
        return new MapperResult(sql, paramList);
    }

    @Override
    public String getTableName() {
        return TableConstant.CONFIG_TAGS_RELATION;
    }

    @Override
    public String getDataSource() {
        return CustomDataSourceConstant.ORACLE;
    }

}