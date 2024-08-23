/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantInfoMapper;
import com.lanss.plugin.constants.CustomDataSourceConstant;

public class TenantInfoMapperByOracle extends OracleAbstractMapper implements TenantInfoMapper {

    @Override
    public String getTableName() {
        return TableConstant.TENANT_INFO;
    }

    @Override
    public String getDataSource() {
        return CustomDataSourceConstant.ORACLE;
    }

}