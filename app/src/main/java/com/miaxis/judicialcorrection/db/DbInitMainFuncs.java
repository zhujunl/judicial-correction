package com.miaxis.judicialcorrection.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.miaxis.enroll.EnrollActivity;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.centralized.CentralizedEducationActivity;
import com.miaxis.judicialcorrection.individual.IndividualEducationActivity;
import com.miaxis.judicialcorrection.leave.LeaveActivity;
import com.miaxis.judicialcorrection.benefit.PublicWelfareActivity;
import com.miaxis.judicialcorrection.live.LiveAddressChangeActivity;
import com.miaxis.judicialcorrection.report.ReportActivity;
import com.miaxis.judicialcorrection.ui.cloud.CloudActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
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
@Singleton
public class DbInitMainFuncs extends RoomDatabase.Callback {

    @Inject
    Lazy<AppDatabase> appDatabaseLazy;

    @Inject
    Lazy<AppExecutors> appExecutors;

    Context context;

    public MutableLiveData<Resource<Integer>> progressLiveData = new MutableLiveData<>();


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
            items.add(new MainFunc("集中教育", R.mipmap.main_edu_all, CentralizedEducationActivity.class.getName(), true));
            items.add(new MainFunc("个别教育", R.mipmap.main_edu_one, IndividualEducationActivity.class.getName(), true));
            items.add(new MainFunc("请销假", R.mipmap.main_atten, LeaveActivity.class.getName(), true));
            items.add(new MainFunc("公益活动", R.mipmap.main_love, PublicWelfareActivity.class.getName(), true));
            items.add(new MainFunc("执行地变更", R.mipmap.main_addr, LiveAddressChangeActivity.class.getName(), true));
            List<Long> longs = appDatabaseLazy.get().mainFuncDAO().insertFuncList(items);
            Timber.i("ids : %s ", longs);
            execSQL(db);
        });
    }
    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
        appExecutors.get().diskIO().execute(()->{
            MainFunc mainFunc=appDatabaseLazy.get().mainFuncDAO().loadFuncByTitle("云端查询");
            if(mainFunc==null) appDatabaseLazy.get().mainFuncDAO().insertFunc(new MainFunc("云端查询", R.mipmap.main_cloud, CloudActivity.class.getName(), true));

        });
    }

    private void execSQL(@NonNull SupportSQLiteDatabase db) {
        try {
            AssetManager assets = context.getAssets();
            InputStream inputStream = assets.open("place.sql");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String s = "";
            int size = 0;
            int allSize = inputStream.available();
            while ((s = bufferedReader.readLine()) != null) {
                Timber.i("sql : %s ", s);
                size += (s.length() + 1);
                if (!TextUtils.isEmpty(s)) {
                    db.execSQL(s);
                }
                int pro = size * 100 / allSize;
                progressLiveData.postValue(Resource.loading(pro));
            }
            bufferedReader.close();
            progressLiveData.postValue(Resource.success(100));
        } catch (Exception e) {
            e.printStackTrace();
            progressLiveData.postValue(Resource.error(-1, e.getMessage(),0));
            Timber.e("sql : %s ", e.getMessage());
        }
        Timber.i("sql :finish");
    }

}
