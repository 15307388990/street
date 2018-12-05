package com.juxun.business.street.widget;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.wheel.CityModel2;
import com.juxun.business.street.widget.wheel.DistrictModel;
import com.juxun.business.street.widget.wheel.ProvinceModel;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.PopupWindow;

public class WheelViewBaseDialog extends PopupWindow {

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - id
	 */
	protected Map<String, String> mIdDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */

	public WheelViewBaseDialog(Context context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	// protected String[] strProvince;
	protected String[] strCity;
	protected String[] strDistrict;
	protected Context mContext;

	protected void initDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = mContext.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = asset.open("area.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String infos = Tools.readTextFile(inputStream);
		try {
			JSONObject dataJson = new JSONObject(infos);
			JSONArray jsonArray = dataJson.getJSONArray("proData");
			JSONArray jsonArray2 = dataJson.getJSONArray("cityInPro");
			JSONArray jsonArray3 = dataJson.getJSONArray("urbanInCity");

			List<ProvinceModel> list_province = new ArrayList<ProvinceModel>();
			List<CityModel2> list_city = new ArrayList<CityModel2>();
			List<DistrictModel> list_district = new ArrayList<DistrictModel>();
			mProvinceDatas = new String[jsonArray.length()];
			// strDistrict = new String[list_district.size()];
			// strCity = new String[list_city.size()];

			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				String key = jsonObj.getString("id");
				String proName = jsonObj.getString("name");
				JSONObject jsonObj2 = (JSONObject) jsonArray2.getJSONObject(0);
				JSONArray cityArray = jsonObj2.getJSONArray(key);
				int citySize = cityArray.length();
				ProvinceModel proModel = new ProvinceModel();
				proModel.setName(proName);

				strCity = new String[citySize];

				for (int j = 0; j < citySize; j++) {
					JSONObject jsonObj3 = (JSONObject) cityArray.get(j);
					String key2 = jsonObj3.getString("id");
					String cityName = jsonObj3.getString("name");
					JSONObject jsonObj4 = (JSONObject) jsonArray3.getJSONObject(0);
					JSONArray districtArray = jsonObj4.getJSONArray(key2);
					int districtSize = districtArray.length();

					CityModel2 cityModel = new CityModel2();
					cityModel.setName(cityName);
					list_city.add(cityModel);

					strCity[j] = cityName;

					strDistrict = new String[districtSize];
					for (int k = 0; k < districtSize; k++) {
						JSONObject jsonObj5 = (JSONObject) districtArray.get(k);
						String districtId = jsonObj5.getString("id");
						String districtName = jsonObj5.getString("name");

						DistrictModel districtModel = new DistrictModel();
						districtModel.setName(districtName);
						districtModel.setId(districtId);
						list_district.add(districtModel);

						strDistrict[k] = districtName;
						mIdDatasMap.put(districtName, districtId);

					}
					cityModel.setDistrictList(list_district);
					mDistrictDatasMap.put(strCity[j], strDistrict);
					// mDistrictDatasMap.put(cityNames[j],
					// list_district.toArray(new String[]{}));
				}
				proModel.setCityList(list_city);
				list_province.add(proModel);
				mProvinceDatas[i] = proName;
				mCitisDatasMap.put(proName, strCity);
			}

			// strProvince = list_province.toArray(new String[]{});
			// strCity = list_city.toArray(new String[]{});
			// strDistrict = list_district.toArray(new String[]{});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = mContext.getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建�?个解析xml的工厂对�?
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			// SAXParser parser = spf.newSAXParser();
			// XmlParserHandler handler = new XmlParserHandler();
			// parser.parse(input, handler);
			input.close();
			// 获取解析出来的数�?
			// provinceList = handler.getDataList();
			// */ 初始化默认�?�中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel2> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历�?有省的数�?
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel2> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的�?有市的数�?
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						// �?/县对于的邮编，保存到mZipcodeDatasMap
						mIdDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// �?-�?/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// �?-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

}
