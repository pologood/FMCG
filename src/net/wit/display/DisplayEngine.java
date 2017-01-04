/**
 * @Title：DisplayEngine.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:52:04 
 * @version：V1.0   
 */

package net.wit.display;

import java.util.List;

/**
 * @ClassName：DisplayEngine
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:52:04
 */
public interface DisplayEngine<D, T> {

	public List<T> convertList(List<D> list);
	
	public T convertEntity(D entity);

}
