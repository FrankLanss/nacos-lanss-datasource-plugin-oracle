/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.lanss.plugin.constants.CustomDataSourceConstant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigInfoMapperByOracle extends OracleAbstractMapper implements ConfigInfoMapper {

    @Override
    public MapperResult findConfigInfoByAppFetchRows(MapperContext context) {
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content FROM config_info"
                + " WHERE (tenant_id LIKE ? OR tenant_id IS NULL) AND app_name= ?";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, CollectionUtils.list(tenantId, appName));
    }

    @Override
    public MapperResult getTenantIdList(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT tenant_id FROM config_info WHERE tenant_id IS NOT NULL GROUP BY tenant_id ";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, Collections.emptyList());
    }

    @Override
    public MapperResult getGroupIdList(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT group_id FROM config_info WHERE tenant_id IS NULL GROUP BY group_id ";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, Collections.emptyList());
    }

    @Override
    public MapperResult findAllConfigKey(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = " SELECT id,data_id,group_id,app_name FROM config_info WHERE (tenant_id LIKE ? OR tenant_id IS NULL)  ORDER BY id ";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.TENANT_ID)));
    }

    @Override
    public MapperResult findAllConfigInfoBaseFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT id,data_id,group_id,content,md5 FROM  config_info  ORDER BY id  ";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, Collections.emptyList());
    }

    @Override
    public MapperResult findAllConfigInfoFragment(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,md5,gmt_modified,type,encrypted_data_key "
                + "FROM config_info WHERE id > ? ORDER BY id ASC ";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.ID)));
    }

    @Override
    public MapperResult findChangeConfigFetchRows(MapperContext context) {
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
        final Timestamp startTime = (Timestamp) context.getWhereParameter(FieldConstant.START_TIME);
        final Timestamp endTime = (Timestamp) context.getWhereParameter(FieldConstant.END_TIME);
        final int pageSize = context.getPageSize();

        List<Object> paramList = new ArrayList<>();

        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,md5,gmt_modified FROM config_info WHERE ";
        String where = " 1=1 ";
        if (!StringUtils.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where += " AND group_id LIKE ? ";
            paramList.add(group);
        }

        if (!StringUtils.isBlank(tenantTmp)) {
            where += " AND (tenant_id = ? OR tenant_id IS NULL) ";
            paramList.add(tenantTmp);
        }

        if (!StringUtils.isBlank(appName)) {
            where += " AND app_name = ? ";
            paramList.add(appName);
        }
        if (startTime != null) {
            where += " AND gmt_modified >=? ";
            paramList.add(startTime);
        }
        if (endTime != null) {
            where += " AND gmt_modified <=? ";
            paramList.add(endTime);
        }

        String sql = sqlFetchRows + where + " AND id > " + context.getWhereParameter(FieldConstant.LAST_MAX_ID) + " ORDER BY id ASC";
        sql = buildPaginationSql(sql, 0, pageSize);
        return new MapperResult(sql, paramList);
    }

    /**
     * list group key md5 by page. The default sql: SELECT
     * t.id,data_id,group_id,tenant_id,app_name,md5,type,gmt_modified,encrypted_data_key FROM ( SELECT id FROM
     * config_info ORDER BY id LIMIT ?,?  ) g, config_info t WHERE g.id = t.id
     *
     * @param context The context of startRow, pageSize
     * @return The sql of listing group key md5 by page.
     */
    @Override
    public MapperResult listGroupKeyMd5ByPageFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,md5,type,gmt_modified,encrypted_data_key  config_info  ORDER BY id ";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, Collections.emptyList());
    }

    @Override
    public MapperResult findConfigInfoBaseLikeFetchRows(MapperContext context) {
        final int startRow = context.getStartRow();
        final int pageSize = context.getPageSize();
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,content FROM config_info WHERE ";
        String where = " 1=1 AND (tenant_id='' OR tenant_id IS NULL) ";

        List<Object> paramList = new ArrayList<>();

        if (!StringUtils.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where += " AND group_id LIKE ? ";
            paramList.add(group);
        }
        if (!StringUtils.isBlank(content)) {
            where += " AND content LIKE ? ";
            paramList.add(content);
        }
        String sql = sqlFetchRows + where;
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, paramList);
    }

    /**
     * find config info. The default sql: SELECT id,data_id,group_id,tenant_id,app_name,content,type,encrypted_data_key
     * FROM config_info ...
     *
     * @param context The mpa of dataId, groupId and appName.
     * @return The sql of finding config info.
     */
    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        final int startRow = context.getStartRow();
        final int pageSize = context.getPageSize();
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        List<Object> paramList = new ArrayList<>();

        final String sqlF = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,encrypted_data_key FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" ( tenant_id= ?  or tenant_id is NULL ） ");
        paramList.add(tenant);
        if (StringUtils.isNotBlank(dataId)) {
            where.append(" AND data_id=? ");
            paramList.add(dataId);
        }
        if (StringUtils.isNotBlank(group)) {
            where.append(" AND group_id=? ");
            paramList.add(group);
        }
        if (StringUtils.isNotBlank(appName)) {
            where.append(" AND app_name=? ");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND content LIKE ? ");
            paramList.add(content);
        }
        String sql = buildPaginationSql(sqlF + where, startRow, pageSize);
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult findConfigInfoBaseByGroupFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT id,data_id,group_id,content FROM config_info WHERE group_id=? AND ( tenant_id= ?  or tenant_id is NULL ）";
        sql = buildPaginationSql(sql, startRow, pageSize);
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.GROUP_ID),
                context.getWhereParameter(FieldConstant.TENANT_ID)));
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

        List<Object> paramList = new ArrayList<>();

        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,encrypted_data_key FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" (tenant_id LIKE ? OR tenant_id IS NULL) ");
        paramList.add(tenant);
        if (!StringUtils.isBlank(dataId)) {
            where.append(" AND data_id LIKE ? ");
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where.append(" AND group_id LIKE ? ");
            paramList.add(group);
        }
        if (!StringUtils.isBlank(appName)) {
            where.append(" AND app_name = ? ");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND content LIKE ? ");
            paramList.add(content);
        }
        String sql = buildPaginationSql(sqlFetchRows + where, startRow, pageSize);

        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult findAllConfigInfoFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();
        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,md5 "
                + " FROM  config_info WHERE (tenant_id LIKE ? OR tenant_id IS NULL) ORDER BY id ";
        sql = buildPaginationSql(sql, startRow, pageSize);

        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.TENANT_ID)));
    }

    @Override
    public String getTableName() {
        return TableConstant.CONFIG_INFO;
    }

    @Override
    public String getDataSource() {
        return CustomDataSourceConstant.ORACLE;
    }

}