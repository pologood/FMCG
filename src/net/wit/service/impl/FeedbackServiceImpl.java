package net.wit.service.impl;

import net.wit.dao.FeedbackDao;
import net.wit.dao.GoodsDao;
import net.wit.entity.Feedback;
import net.wit.service.BaseService;
import net.wit.service.FeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Created by WangChao on 2016-7-4.
 */
@Service("feedbackServiceImpl")
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback,Long> implements FeedbackService{
    @Resource(name = "feedbackDaoImpl")
    public void setBaseDao(FeedbackDao feedbackDao) {
        super.setBaseDao(feedbackDao);
    }
}
