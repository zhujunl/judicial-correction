package com.miaxis.judicialcorrection.base.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miaxis.judicialcorrection.base.db.po.JAuthInfo;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;

import java.util.List;

/**
 * MainFuncDAO
 *
 * @author zhangyw
 * Created on 4/27/21.
 */
@Dao
public interface JusticeBureauDao {


    @Query("SELECT * FROM JusticeBureau")
    LiveData<JusticeBureau> load();


    @Query("SELECT * FROM JusticeBureau")
    LiveData<List<JusticeBureau>> loadAll();

    @Query("SELECT * FROM JusticeBureau")
    JusticeBureau loadSync();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(JusticeBureau info);

    @Delete
    void delete(JusticeBureau info);

    @Query("SELECT * FROM JusticeBureau where teamLevel =:level")
    LiveData<JusticeBureau> loadWithLevel(String level);


    @Query("SELECT * FROM JusticeBureau where teamLevel =:level")
    JusticeBureau loadSyncWithLevel(String level);


    @Query("delete FROM JusticeBureau where teamLevel =:level")
    void deleteWithTeamLevel(String level);


}
