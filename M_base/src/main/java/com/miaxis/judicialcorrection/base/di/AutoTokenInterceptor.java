package com.miaxis.judicialcorrection.base.di;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.Token;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JAuthInfo;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.wondersgroup.om.AuthInfo;
import com.wondersgroup.om.JZAuth;
import com.wondersgroup.om.ResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * AutoTokenInterceptor
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@Singleton
public class AutoTokenInterceptor implements Interceptor {
    private final JZAuth jzAuth;
    private final Object authLock = new Object();
    private final Object tokenLock = new Object();
    private Token token = null;
    private final JAuthInfo jAuthInfo = new JAuthInfo();

    @Inject
    public AutoTokenInterceptor(@ApplicationContext Context context, AppDatabase appDatabase) {
        appDatabase.tokenAuthInfoDAO().loadAuthInfo().observeForever((JAuthInfo info) -> {
            jAuthInfo.activationCode = info == null ? null : info.activationCode;
            Timber.i("New JAuthInfo by A: %s", jAuthInfo);
        });
        appDatabase.justiceBureauDao().loadAll().observeForever((List<JusticeBureau> justiceBureaus) -> {
            Timber.i("Auth Active justice Change :[%d], %s", justiceBureaus.size(), justiceBureaus);
            if (jAuthInfo == null) {
                return;
            }
            for (int i = 0; i < justiceBureaus.size(); i++) {
                JusticeBureau jb = justiceBureaus.get(i);
                switch (jb.getTeamLevel()) {
                    case "TEAM_LEVEL_1":
                        Timber.i("New JAuthInfo level 1: %s", jb.getTeamName());
                        jAuthInfo.dishiId = jb.getTeamId();
                        jAuthInfo.dishiName = jb.getTeamName();
                        break;
                    case "TEAM_LEVEL_2":
                        Timber.i("New JAuthInfo level 2: %s", jb.getTeamName());
                        jAuthInfo.quxianId = jb.getTeamId();
                        jAuthInfo.quxianName = jb.getTeamName();
                        break;
                    case "TEAM_LEVEL_3":
                        Timber.i("New JAuthInfo level 3: %s", jb.getTeamName());
                        jAuthInfo.jiedaoId = jb.getTeamId();
                        jAuthInfo.jiedaoName = jb.getTeamName();
                        break;
                }
            }
            Timber.i("New JAuthInfo B: %s", jAuthInfo);
        });
        jzAuth = JZAuth.getInstance();
        jzAuth.setGlobalURL(BuildConfig.TOKEN_URL);
        jzAuth.initialize(context,"zkja");
        if (BuildConfig.DEBUG) {
            jzAuth.setDebug(true);
        }
        Timber.i("JAuthInfo  初始化");
    }

    String getToken() throws IOException {
        if (token == null || token.isExpires()) {
            synchronized (tokenLock) {
                if (token == null || token.isExpires()) {
                    if (!jzAuth.checkAuth()) {
                        registerJzAuth();
                        Timber.i("getToken ，register success !");
                    }
                    refreshToken();
                    Timber.i("getToken ，NEW  : %s", token);
                }
            }
        }
        return token.getBearerToken();
    }


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder newBuilder = original.newBuilder();
        String token = getToken();
        Timber.v("token : %s", token);
        newBuilder.addHeader("Authorization", token);
        if (Objects.equals("POST", original.method())) {
            RequestBody bodyUnSign = original.body();
            Timber.v("OKHttp Request body= [%s]", new Gson().toJson(bodyUnSign));
            assert bodyUnSign != null;
            newBuilder.post(bodyUnSign);
        } else if (Objects.equals("PUT", original.method())) {
            RequestBody bodyUnSign = original.body();
            assert bodyUnSign != null;
            newBuilder.put(bodyUnSign);
        } else {
            newBuilder.get();
        }
        Request request = newBuilder.build();
        Timber.v("OKHttp Request URL= [%s]", original.url());
        //Timber.v("OKHttp Request Header=[%s]\nURL= [%s]", request.headers(), request.url());
        return chain.proceed(request);
    }


    @SuppressLint("HardwareIds")
    void registerJzAuth() throws IOException {
        if (TextUtils.isEmpty(jAuthInfo.activationCode)) {
            throw new IOException("请输入激活码");
        }
        AuthInfo.getInstance().setActivationCode(jAuthInfo.activationCode);
        AuthInfo.getInstance().setContact(jAuthInfo.contact);
        AuthInfo.getInstance().setContactInformation(jAuthInfo.contactInformation);

        AuthInfo.getInstance().setDeviceType(jAuthInfo.deviceType);
        AuthInfo.getInstance().setVendor(jAuthInfo.vendor);//
        AuthInfo.getInstance().setDishiId(jAuthInfo.dishiId);
        AuthInfo.getInstance().setDishiName(jAuthInfo.dishiName);
        AuthInfo.getInstance().setQuxianId(jAuthInfo.quxianId);
        AuthInfo.getInstance().setQuxianName(jAuthInfo.quxianName);
        AuthInfo.getInstance().setSerialNumber(Build.SERIAL);
        String time = HexStringUtils.convertCurrentGMT();
        AuthInfo.getInstance().setTime(time);

        AuthInfo.getInstance().setCurrentVersion("1.0.0");
        AuthInfo.getInstance().setClientName(BuildConfig.CLIENT_NAME);//clientName
        AuthInfo.getInstance().setLoc(jAuthInfo.local);

        final Exception[] errorR = new Exception[1];
        final String[] resultR = new String[1];
        JZAuth.getInstance().registerDevice(new ResultListener() {

            @Override
            public void onError(Exception error) {
                synchronized (authLock) {
                    errorR[0] = error;
                    authLock.notify();
                }
            }

            @Override
            public void onResult(String result) {
                synchronized (authLock) {
                    resultR[0] = result;
                    authLock.notify();
                }
            }
        });

        synchronized (authLock) {
            try {
                authLock.wait(9000);
            } catch (InterruptedException e) {
                throw new IOException("token server register timeout ! ");
            }
        }
        Timber.i("3`");
        if (errorR[0] != null) {
            throw new IOException(errorR[0].getMessage());
        } else if (resultR[0] != null) {
            //{"desc":"非法参数vendor","result":"999"}
            try {
                JSONObject jsonObject = new JSONObject(resultR[0]);
                Object result = jsonObject.get("result");
                if (!Objects.equals("0", result) && !Objects.equals("1", result)) {
                    throw new IOException("token server register error : " + resultR[0]);
                }
            } catch (JSONException e) {
                throw new IOException("token server register error ! ");
            }
        } else {
            throw new IOException("token server register error ! ");
        }
    }


    void refreshToken() throws IOException {
        final Exception[] errorR = new Exception[1];
        final String[] resultR = new String[1];
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        JZAuth.getInstance().getToken(new ResultListener() {
            @Override
            public void onError(Exception error) {
                synchronized (authLock) {
                    errorR[0] = error;
                    atomicBoolean.set(true);
                    authLock.notify();
                }
            }

            @Override
            public void onResult(String result) {
                synchronized (authLock) {
                    resultR[0] = result;
                    atomicBoolean.set(true);
                    authLock.notify();
                }
            }
        });
        synchronized (authLock) {
            if (!atomicBoolean.get()) {
                try {
                    authLock.wait(9000);
                } catch (InterruptedException e) {
                    throw new IOException("token server register timeout ");
                }
            }
        }
        if (errorR[0] != null) {
            throw new IOException(errorR[0].getMessage());
        } else if (resultR[0] != null) {
            token = new Gson().fromJson(resultR[0], Token.class);
            Timber.i("Got new token : %s", token);
        } else {
            throw new IOException("token server register timeout ");
        }
    }
}
