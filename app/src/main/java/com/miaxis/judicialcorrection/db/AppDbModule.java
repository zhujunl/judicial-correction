package com.miaxis.judicialcorrection.db;

import android.content.Context;

import com.miaxis.judicialcorrection.base.db.AppDatabase;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * DbModule
 *
 * @author zhangyw
 * Created on 4/27/21.
 */
@InstallIn(SingletonComponent.class)
@Module()
public class AppDbModule {
    @Singleton
    @Provides
    static AppDatabase provideDB(@ApplicationContext Context application,DbInitMainFuncs dbInitMainFuncs) {
        return Room.databaseBuilder(application, AppDatabase.class, "room-sql")
                .addCallback(dbInitMainFuncs)
                .build();
    }

}
