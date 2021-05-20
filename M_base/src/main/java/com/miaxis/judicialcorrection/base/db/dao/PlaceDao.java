package com.miaxis.judicialcorrection.base.db.dao;

import com.miaxis.judicialcorrection.base.db.po.Place;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

/**
 * PlaceDao
 * <p>
 * 全国行政区
 *
 * @author tangkai
 * Created on 4/27/21.
 */

@Dao
public interface PlaceDao {

    /**
     * 获取全国省/直辖市列表
     */
    @Query("SELECT * FROM PLACE WHERE LEVEL=1")
    LiveData<List<Place>> findAllProvince();

    @Query("SELECT * FROM PLACE WHERE LEVEL=1")
    List<Place> findAllProvinceSync();

    /**
     * 获取指定省的城市级列表
     *
     * @param provinceId 省ID
     */
    @Query("SELECT * FROM PLACE WHERE LEVEL=2 AND PARENT_ID=:provinceId")
    LiveData<List<Place>> findAllCity(long provinceId);

    @Query("SELECT * FROM PLACE WHERE LEVEL=2 AND PARENT_ID=:provinceId")
    List<Place> findAllCitySync(long provinceId);

    /**
     * 获取指定市的区/县级列表
     *
     * @param cityId 城市ID
     */
    @Query("SELECT * FROM PLACE WHERE LEVEL=3 AND PARENT_ID=:cityId")
    LiveData<List<Place>> findAllDistrict(long cityId);

    @Query("SELECT * FROM PLACE WHERE LEVEL=3 AND PARENT_ID=:cityId")
    List<Place> findAllDistrictSync(long cityId);

    /**
     * 获取指定区/县的乡/镇/办事处/机构列表
     *
     * @param districtId 区/县ID
     */
    @Query("SELECT * FROM PLACE WHERE LEVEL=4 AND PARENT_ID=:districtId")
    LiveData<List<Place>> findAllAgencies(long districtId);

    @Query("SELECT * FROM PLACE WHERE LEVEL=4 AND PARENT_ID=:districtId")
    List<Place> findAllAgenciesSync(long districtId);


}
