package com.miaxis.judicialcorrection.base.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miaxis.judicialcorrection.base.db.po.MainFunc;

import java.util.List;

/**
 * MainFuncDAO
 *
 * @author zhangyw
 * Created on 4/27/21.
 */
@Dao
public interface MainFuncDao {

    @Query("SELECT * FROM main_func")
    LiveData<List<MainFunc>> loadFuncAll();

    @Query("SELECT * FROM main_func where active = 1 ")
    LiveData<List<MainFunc>> loadFuncActive();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertFunc(MainFunc... users);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertFuncList(List<MainFunc> funcs);

}
