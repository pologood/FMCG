package net.wit.domain;

import net.wit.entity.Member;
import net.wit.entity.Order;

public interface DistributionStrategy {

	/** 利润分配 */
	public void distribution(Order order, Member operator);
}
