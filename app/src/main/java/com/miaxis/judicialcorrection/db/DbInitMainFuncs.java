package com.miaxis.judicialcorrection.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.miaxis.enroll.EnrollActivity;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.benefit.PublicWelfareActivity;
import com.miaxis.judicialcorrection.live.LiveAddressChangeActivity;
import com.miaxis.judicialcorrection.report.ReportActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import dagger.Lazy;
import dagger.hilt.android.qualifiers.ApplicationContext;
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

    Context context;

    @Inject
    public DbInitMainFuncs(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        appExecutors.get().diskIO().execute(() -> {
            List<MainFunc> items = new ArrayList<>();
            items.add(new MainFunc("首次报到登记", R.mipmap.main_enroll, EnrollActivity.class.getName(), true));
            items.add(new MainFunc("日常报告", R.mipmap.main_report, ReportActivity.class.getName(), true));
            items.add(new MainFunc("集中教育", R.mipmap.main_edu_all, "/null/null", true));
            items.add(new MainFunc("个别教育", R.mipmap.main_edu_one, "/null/null", true));
            items.add(new MainFunc("公益活动", R.mipmap.main_love, PublicWelfareActivity.class.getName(), true));
            items.add(new MainFunc("请销假", R.mipmap.main_atten, "/null/null", true));
            items.add(new MainFunc("居住地变更", R.mipmap.main_addr, LiveAddressChangeActivity.class.getName(), true));
            List<Long> longs = appDatabaseLazy.get().mainFuncDAO().insertFuncList(items);
            Timber.i("ids : %s ", longs);
            execSQL(db);
        });
    }

    private void execSQL(@NonNull SupportSQLiteDatabase db) {
        try {
            AssetManager assets = context.getAssets();
            InputStream inputStream = assets.open("place.sql");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                Timber.i("sql : %s ", s);
                if (!TextUtils.isEmpty(s)) {
                    db.execSQL(s);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("sql : %s ", e.getMessage());
        }
        Timber.i("sql :finish");
    }

}
