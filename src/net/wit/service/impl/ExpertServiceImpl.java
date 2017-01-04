/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ExpertDao;
import net.wit.entity.Expert;
import net.wit.entity.ExpertCategory;
import net.wit.service.ExpertService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 专家
 * @author rsico Team
 * @version 3.0
 */
@Service("expertServiceImpl")
public class ExpertServiceImpl extends BaseServiceImpl<Expert, Long> implements ExpertService {

	@Resource(name = "expertDaoImpl")
	private ExpertDao expertDao;

	@Resource(name = "expertDaoImpl")
	public void setBaseDao(ExpertDao expertDao) {
		super.setBaseDao(expertDao);
	}

	@Transactional(readOnly = true)
	public Page<Expert> findPage(ExpertCategory expertCategory, Pageable pageable) {
		return expertDao.findPage(expertCategory, pageable);
	}

	@Transactional(readOnly = true)
	public List<Expert> findList(ExpertCategory expertCategory) {
		return expertDao.findList(expertCategory);
	}

}