package com.miaxis.judicialcorrection.base.db.dao;

import com.miaxis.judicialcorrection.base.db.po.JAuthInfo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * MainFuncDAO
 *
 * @author zhangyw
 * Created on 4/27/21.
 */
@Dao
public interface JAuthInfoDao {

    @Query("SELECT * FROM auth_info")
    LiveData<JAuthInfo> loadAuthInfo();

    @Query("SELECT * FROM auth_info")
    JAuthInfo loadAuthInfoSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(JAuthInfo info);

}
