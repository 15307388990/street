package com.juxun.business.street.util;

import com.juxun.business.street.widget.wheel.CityModel;

import java.util.Comparator;


/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<CityModel> {

	public int compare(CityModel o1, CityModel o2) {
		if (o1.getSort().equals("@") || o2.getSort().equals("#")) {
			return -1;
		} else if (o1.getSort().equals("#") || o2.getSort().equals("@")) {
			return 1;
		} else {
			return o1.getSort().compareTo(o2.getSort());
		}
	}

}
