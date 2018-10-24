package com.coolweather.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.android.db.City;
import com.coolweather.android.db.Country;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    private static final String TAG = "Utility";
    /*
    解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){//判断返回的数据是否为空
            try {
                JSONArray allProvince = new JSONArray(response);
                for(int i = 0; i < allProvince.length(); i++){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
    解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for(int i = 0; i < allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
解析和处理服务器返回的县级数据
 */
    public static boolean handleCountryResponse(String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountries = new JSONArray(response);
                for(int i = 0; i < allCountries.length(); i++){
                    JSONObject cityObject = allCountries.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(cityObject.getString("name"));
                    country.setWeatherId(cityObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    将返回的JSON格式的数据解析成Weather实体类
     */
    public static Weather HandleWeatherResponse(String response){   //其中参数response为返回的json数据
        try {
            JSONObject jsonObject = new JSONObject(response);   //将json字符串转化为json对象
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");//获取json对象中数据头为HeWeather(或者名称为HeWeather)的数组
            String weatherContent = jsonArray.getJSONObject(0).toString();
            //获取HeWeather数组中位置为0( 即"HeWeather": [{...}]中的{...}之间的内容 )的json对象的字符串形式
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "HandleWeatherResponse: " + response + "!");
        return null;
    }
}
