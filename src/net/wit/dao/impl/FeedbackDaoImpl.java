package net.wit.dao.impl;

import net.wit.dao.FeedbackDao;
import net.wit.entity.Feedback;
import org.springframework.stereotype.Repository;

/**
 *
 * Created by WangChao on 2016-7-4.
 */
@Repository("feedbackDaoImpl")
public class FeedbackDaoImpl extends BaseDaoImpl<Feedback,Long> implements FeedbackDao {

}
