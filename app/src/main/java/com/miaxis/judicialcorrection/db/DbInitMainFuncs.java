package com.miaxis.judicialcorrection.db;

import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import dagger.Lazy;
import timber.log.Timber;

/**
 * DataIn
 *
 * @author zhangyw
 * Created on 4/27/21.
 */

public class DbInitMainFuncs extends RoomDatabase.Callback {

    @Inject
    Lazy<AppDatabase> appDatabaseLazy;

    @Inject
    Lazy<AppExecutors> appExecutors;

    @Inject
    public DbInitMainFuncs() {
    }

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        appExecutors.get().diskIO().execute(() -> {
            List<MainFunc> items = new ArrayList<>();
            items.add(new MainFunc("首次报到登记", R.mipmap.main_enroll, "/enroll/EnrollActivity", true));
            items.add(new MainFunc("日常报告", R.mipmap.main_report, "/report/ReportActivity", true));
            items.add(new MainFunc("集中教育", R.mipmap.main_edu_all, "/null/null", true));
            items.add(new MainFunc("个别教育", R.mipmap.main_edu_one, "/null/null", true));
            items.add(new MainFunc("公益活动", R.mipmap.main_love, "/null/null", true));
            items.add(new MainFunc("请销假", R.mipmap.main_atten, "/null/null", true));
            items.add(new MainFunc("居住地变更", R.mipmap.main_addr, "/null/null", true));
            List<Long> longs = appDatabaseLazy.get().mainFuncDAO().insertFuncList(items);
            Timber.i("ids : %s ", longs);
        });
    }
}
