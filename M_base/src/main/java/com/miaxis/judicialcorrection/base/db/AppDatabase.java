package com.miaxis.judicialcorrection.base.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.miaxis.judicialcorrection.base.db.dao.JAuthInfoDao;
import com.miaxis.judicialcorrection.base.db.dao.MainFuncDao;
import com.miaxis.judicialcorrection.base.db.po.JAuthInfo;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;

/**
 * MyDataBase
 *
 * @author zhangyw
 * Created on 4/27/21.
 */
@Database(entities = {MainFunc.class,JAuthInfo.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MainFuncDao mainFuncDAO();
    public abstract JAuthInfoDao tokenAuthInfoDAO();
}
