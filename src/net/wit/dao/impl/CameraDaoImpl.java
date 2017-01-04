/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import org.springframework.stereotype.Repository;

import net.wit.dao.CameraDao;
import net.wit.entity.Camera;

/**
 * Dao - 
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("cameraDaoImpl")
public class CameraDaoImpl extends BaseDaoImpl<Camera, Long> implements CameraDao {

}