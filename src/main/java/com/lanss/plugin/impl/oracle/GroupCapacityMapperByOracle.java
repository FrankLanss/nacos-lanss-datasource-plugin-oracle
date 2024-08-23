/*
 * Copyright lanss 2024
 */
package com.lanss.plugin.impl.oracle;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.lanss.plugin.constants.CustomDataSourceConstant;

public class GroupCapacityMapperByOracle extends OracleAbstractMapper implements GroupCapacityMapper {

    @Override
    public MapperResult insertIntoSelect(MapperContext context) {
        return GroupCapacityMapper.super.insertIntoSelect(context);
    }

    @Override
    public MapperResult insertIntoSelectByWhere(MapperContext context) {
        return GroupCapacityMapper.super.insertIntoSelectByWhere(context);
    }

    @Override
    public MapperResult incrementUsageByWhereQuotaEqualZero(MapperContext context) {
        return GroupCapacityMapper.super.incrementUsageByWhereQuotaEqualZero(context);
    }

    @Override
    public MapperResult incrementUsageByWhereQuotaNotEqualZero(MapperContext context) {
        return GroupCapacityMapper.super.incrementUsageByWhereQuotaNotEqualZero(context);
    }

    @Override
    public MapperResult incrementUsageByWhere(MapperContext context) {
        return GroupCapacityMapper.super.incrementUsageByWhere(context);
    }

    @Override
    public MapperResult decrementUsageByWhere(MapperContext context) {
        return GroupCapacityMapper.super.decrementUsageByWhere(context);
    }

    @Override
    public MapperResult updateUsage(MapperContext context) {
        return GroupCapacityMapper.super.updateUsage(context);
    }

    @Override
    public MapperResult updateUsageByWhere(MapperContext context) {
        return GroupCapacityMapper.super.updateUsageByWhere(context);
    }

    @Override
    public MapperResult selectGroupInfoBySize(MapperContext context) {
        int pageSize = context.getPageSize();
        String sql = "SELECT * FROM (SELECT id, group_id, ROWNUM as rnum FROM group_capacity WHERE id > ?) WHERE rnum <= ?";
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.ID),
                pageSize));
    }

    @Override
    public String getTableName() {
        return TableConstant.GROUP_CAPACITY;
    }

    @Override
    public String getDataSource() {
        return CustomDataSourceConstant.ORACLE;
    }

}