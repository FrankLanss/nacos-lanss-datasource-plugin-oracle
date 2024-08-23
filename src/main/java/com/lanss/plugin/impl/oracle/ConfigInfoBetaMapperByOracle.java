/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoBetaMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.lanss.plugin.constants.CustomDataSourceConstant;

import java.util.ArrayList;
import java.util.List;

public class ConfigInfoBetaMapperByOracle extends OracleAbstractMapper implements ConfigInfoBetaMapper {

    @Override
    public MapperResult updateConfigInfo4BetaCas(MapperContext context) {
        return ConfigInfoBetaMapper.super.updateConfigInfo4BetaCas(context);
    }

    @Override
    public MapperResult findAllConfigInfoBetaForDumpAllFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();

        String sql = " SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5,gmt_modified,beta_ips,encrypted_data_key "
                + " FROM ( SELECT rownum ROW_ID,id FROM config_info_beta  WHERE  ROW_ID<=  " + (startRow + pageSize)
                + " ORDER BY id )" + " g, config_info_beta t WHERE g.id = t.id AND g.ROW_ID >" + startRow;

        List<Object> paramList = new ArrayList<>();
        paramList.add(startRow);
        paramList.add(pageSize);

        return new MapperResult(sql, paramList);
    }

    @Override
    public String getTableName() {
        return TableConstant.CONFIG_INFO_BETA;
    }

    @Override
    public String getDataSource() {
        return CustomDataSourceConstant.ORACLE;
    }

}