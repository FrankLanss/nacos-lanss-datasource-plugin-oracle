/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.lanss.plugin.constants.CustomDataSourceConstant;

public class HistoryConfigInfoMapperByOracle extends OracleAbstractMapper implements HistoryConfigInfoMapper {

    @Override
    public MapperResult removeConfigHistory(MapperContext context) {
        String sql = "DELETE FROM his_config_info WHERE gmt_modified < ? AND ROWNUM <= ?";
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.START_TIME),
                context.getWhereParameter(FieldConstant.LIMIT_SIZE)));
    }

    @Override
    public MapperResult findConfigHistoryCountByTime(MapperContext context) {
        return HistoryConfigInfoMapper.super.findConfigHistoryCountByTime(context);
    }

    @Override
    public MapperResult findDeletedConfig(MapperContext context) {
        return HistoryConfigInfoMapper.super.findDeletedConfig(context);
    }

    @Override
    public MapperResult findConfigHistoryFetchRows(MapperContext context) {
        return HistoryConfigInfoMapper.super.findConfigHistoryFetchRows(context);
    }

    @Override
    public MapperResult pageFindConfigHistoryFetchRows(MapperContext context) {
        String sql = "SELECT * FROM ("
                + "SELECT nid,data_id,group_id,tenant_id,app_name,src_ip,src_user,op_type,gmt_create,"
                + "gmt_modified,ROWNUM as rnum FROM his_config_info "
                + "WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY nid DESC) WHERE rnum >= "
                + (context.getStartRow() + 1) + " AND rnum <= " + (context.getPageSize() + context.getStartRow()) + " ";
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.DATA_ID),
                context.getWhereParameter(FieldConstant.GROUP_ID), context.getWhereParameter(FieldConstant.TENANT_ID)));
    }

    @Override
    public MapperResult detailPreviousConfigHistory(MapperContext context) {
        return HistoryConfigInfoMapper.super.detailPreviousConfigHistory(context);
    }

    @Override
    public String getTableName() {
        return TableConstant.HIS_CONFIG_INFO;
    }

    @Override
    public String getDataSource() {
        return CustomDataSourceConstant.ORACLE;
    }

}