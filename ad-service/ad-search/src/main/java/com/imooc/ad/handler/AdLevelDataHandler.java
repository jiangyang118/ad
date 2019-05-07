package com.imooc.ad.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.table.AdCreativeTable;
import com.imooc.ad.dump.table.AdCreativeUnitTable;
import com.imooc.ad.dump.table.AdPlanTable;
import com.imooc.ad.dump.table.AdUnitDistrictTable;
import com.imooc.ad.dump.table.AdUnitItTable;
import com.imooc.ad.dump.table.AdUnitKeywordTable;
import com.imooc.ad.dump.table.AdUnitTable;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.index.adplan.AdPlanObject;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.creativeunit.CreativeUnitObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wj
 * @version 1.0.0
 * @create 2019/2/2 17:26
 * @description
 */

@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(planTable.getId(), planTable.getUserId(), planTable.getPlanStatus(),
                planTable.getStartDate(), planTable.getEndDate());

        handleBinlogEvent(DataTable.of(AdPlanIndex.class), planObject.getPlanId(), planObject, type);
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(creativeTable.getAdId(), creativeTable.getName(),
                creativeTable.getType(), creativeTable.getMaterialType(), creativeTable.getWidth(),
                creativeTable.getHeight(), creativeTable.getAuditStatus(), creativeTable.getAdUrl());

        handleBinlogEvent(DataTable.of(CreativeIndex.class), creativeObject.getAdId(), creativeObject, type);
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (adPlanObject == null) {
            log.error("handleLevel3 found AdPlanObject error: {}", unitTable.getPlanId());
            return;
        }
        AdUnitObject adUnitObject = new AdUnitObject(unitTable.getUnitId(), unitTable.getUnitStatus(),
                unitTable.getPositionType(), unitTable.getPlanId(), adPlanObject);

        handleBinlogEvent(DataTable.of(AdUnitIndex.class), unitTable.getUnitId(), adUnitObject, type);
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if (unitObject == null || creativeObject == null) {
            log.error("AdCreativeUnitTable index error: {}", JSON.toJSONString(creativeUnitTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId());

        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class), CommonUtils
                .stringConcat(creativeUnitObject.getAdId().toString(), creativeUnitObject.getUnitId().toString()),
                creativeUnitObject, type);
    }

    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("district index not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error: {}", unitDistrictTable.getUnitId());
            return;
        }
        String key = CommonUtils.stringConcat(unitDistrictTable.getProvince(), unitDistrictTable.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitDistrictIndex.class), key, value, type);
    }

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("it index not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}", unitItTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));

        handleBinlogEvent(DataTable.of(UnitItIndex.class), unitItTable.getItTag(), value, type);
    }

    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("keyword index not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}", unitKeywordTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitKeywordTable.getUnitId()));

        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class), unitKeywordTable.getKeyword(), value, type);
    }

    private static <K, V> void handleBinlogEvent(IndexAware<K, V> index, K key, V value, OpType type) {

        switch (type) {
        case ADD:
            index.add(key, value);
            break;
        case UPDATE:
            index.update(key, value);
            break;
        case DELETE:
            index.delete(key, value);
            break;
        default:
            break;
        }
    }
}
