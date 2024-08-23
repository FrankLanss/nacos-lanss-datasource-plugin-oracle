/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.lanss.plugin.constants.CustomDataSourceConstant;

public class TenantCapacityMapperByOracle extends OracleAbstractMapper implements TenantCapacityMapper {

    @Override
    public MapperResult incrementUsageWithDefaultQuotaLimit(MapperContext context) {
        return TenantCapacityMapper.super.incrementUsageWithDefaultQuotaLimit(context);
    }

    @Override
    public MapperResult incrementUsageWithQuotaLimit(MapperContext context) {
        return TenantCapacityMapper.super.incrementUsageWithQuotaLimit(context);
    }

    @Override
    public MapperResult incrementUsage(MapperContext context) {
        return TenantCapacityMapper.super.incrementUsage(context);
    }

    @Override
    public MapperResult decrementUsage(MapperContext context) {
        return TenantCapacityMapper.super.decrementUsage(context);
    }

    @Override
    public MapperResult correctUsage(MapperContext context) {
        return TenantCapacityMapper.super.correctUsage(context);
    }

    @Override
    public MapperResult getCapacityList4CorrectUsage(MapperContext context) {
        String sql = "SELECT id, tenant_id FROM tenant_capacity WHERE id>? AND ROWNUM <= ?";
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.ID),
                context.getWhereParameter(FieldConstant.LIMIT_SIZE)));
    }

    @Override
    public MapperResult insertTenantCapacity(MapperContext context) {
        return TenantCapacityMapper.super.insertTenantCapacity(context);
    }

    @Override
    public String getTableName() {
        return TableConstant.TENANT_CAPACITY;
    }

    @Override
    public String getDataSource() {
        return CustomDataSourceConstant.ORACLE;
    }

}