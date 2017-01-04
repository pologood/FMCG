/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.util;

import java.math.BigDecimal;

import net.wit.entity.Location;


/**
 * Utils - Map
 * 
 * @author rsico Team
 * @version 3.0
 */
public final class MapUtils {

    /** 地球半径 */  
    private static final double EARTH_RADIUS = 6371000;  
    /** 范围距离 */  
    private double distance;  
    /** 左上角 */  
    private Location left_top = null;  
    /** 右上角 */  
    private Location right_top = null;  
    /** 左下角 */  
    private Location left_bottom = null;  
    /** 右下角 */  
    private Location right_bottom = null;  
	
	/**
	 * 不可实例化
	 */
    public MapUtils(double distance) {
        this.distance = distance;  
	}

	public void rectangle4Point(double lat, double lng) {  
        double dlng = 2 * Math.asin(Math.sin(distance / (2 * EARTH_RADIUS))  
                / Math.cos(lat));  
        dlng = Math.toDegrees(dlng);  
  
        double dlat = distance / EARTH_RADIUS;  
        dlat = Math.toDegrees(dlat); // # 弧度转换成角度  
        left_top = new Location(new BigDecimal(lat + dlat),new BigDecimal( lng - dlng));  
        right_top = new Location(new BigDecimal(lat + dlat), new BigDecimal(lng + dlng));  
        left_bottom = new Location(new BigDecimal(lat - dlat), new BigDecimal(lng - dlng));  
        right_bottom = new Location(new BigDecimal(lat - dlat), new BigDecimal(lng + dlng));  
    }  
    
    public static double hav(double theta) {  
        double s = Math.sin(theta / 2);  
        return s * s;  
    }  
	
	/**
	 * 经纬度计算两点距离
	 * 
	 * @param double lat1, double lat2, double lon1,    double lon2
	 *            对象
	 * @return double公里数
	 */
	public static double getDistatce(double lat0, double lat1, double lng0,double lng1) { 
        lat0 = Math.toRadians(lat0);  
        lat1 = Math.toRadians(lat1);  
        lng0 = Math.toRadians(lng0);  
        lng1 = Math.toRadians(lng1);  
        
        double dlng = Math.abs(lng0 - lng1);  
        double dlat = Math.abs(lat0 - lat1);  
        double h = hav(dlat) + Math.cos(lat0) * Math.cos(lat1) * hav(dlng);  
        double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));  
  
        return distance;  
    }

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Location getLeft_top() {
		return left_top;
	}

	public void setLeft_top(Location left_top) {
		this.left_top = left_top;
	}

	public Location getRight_top() {
		return right_top;
	}

	public void setRight_top(Location right_top) {
		this.right_top = right_top;
	}

	public Location getLeft_bottom() {
		return left_bottom;
	}

	public void setLeft_bottom(Location left_bottom) {
		this.left_bottom = left_bottom;
	}

	public Location getRight_bottom() {
		return right_bottom;
	}

	public void setRight_bottom(Location right_bottom) {
		this.right_bottom = right_bottom;
	}

	public static double getEarthRadius() {
		return EARTH_RADIUS;
	}
	
	
}