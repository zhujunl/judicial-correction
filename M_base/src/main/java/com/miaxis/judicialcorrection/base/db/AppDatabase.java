package com.miaxis.judicialcorrection.base.db;

import com.miaxis.judicialcorrection.base.db.dao.JAuthInfoDao;
import com.miaxis.judicialcorrection.base.db.dao.JusticeBureauDao;
import com.miaxis.judicialcorrection.base.db.dao.MainFuncDao;
import com.miaxis.judicialcorrection.base.db.dao.PlaceDao;
import com.miaxis.judicialcorrection.base.db.po.JAuthInfo;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.db.po.Place;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * MyDataBase
 *
 * @author zhangyw
 * Created on 4/27/21.
 */
@Database(entities = {MainFunc.class,JAuthInfo.class, JusticeBureau.class, Place.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MainFuncDao mainFuncDAO();
    public abstract JAuthInfoDao tokenAuthInfoDAO();
    public abstract JusticeBureauDao justiceBureauDao();
    public abstract PlaceDao placeDao();
}
