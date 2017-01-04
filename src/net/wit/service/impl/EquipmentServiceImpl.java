/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;


import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tenant;
import org.springframework.stereotype.Service;

import net.wit.dao.EquipmentDao;
import net.wit.entity.Equipment;
import net.wit.service.EquipmentService;

import java.util.List;

/**
 * Service - 设备管理
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("equipmentServiceImpl")
public class EquipmentServiceImpl extends BaseServiceImpl<Equipment, Long> implements EquipmentService {

    @Resource(name = "equipmentDaoImpl")
    private EquipmentDao equipmentDao;

    @Resource(name = "equipmentDaoImpl")
    public void setBaseDao(EquipmentDao equipmentDao) {
        super.setBaseDao(equipmentDao);
    }

    /**
     * 根据设备号查找
     *
     * @param uuid
     * @return 设备，若不存在则返回null
     */
    public Equipment findByUUID(String uuid) {
        return equipmentDao.findByUUID(uuid);
    }

    public Page<Equipment> findPage(String keyWord,Equipment.Status status, Pageable pageable) {
        return equipmentDao.findPage(keyWord,status, pageable);
    }

   public List<Equipment> findByTenant(Tenant tenant, List<Filter> filters){
       return equipmentDao.findByTenant(tenant,filters);
   }

   public Equipment findEquipment(Tenant tenant, List<Filter> filters){
       return equipmentDao.findEquipment(tenant,filters);
   }
}

