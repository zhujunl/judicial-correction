package com.miaxis.judicialcorrection.base.di;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.Token;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JAuthInfo;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.utils.FileUtils;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.tencent.mmkv.MMKV;
import com.wondersgroup.om.AuthInfo;
import com.wondersgroup.om.JZAuth;
import com.wondersgroup.om.JZAuthException;
import com.wondersgroup.om.ResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
    private String baseUrlToken = "";

    @Inject
    public AutoTokenInterceptor(@ApplicationContext Context context, AppDatabase appDatabase) {
        appDatabase.tokenAuthInfoDAO().loadAuthInfo().observeForever((JAuthInfo info) -> {
            jAuthInfo.activationCode = info == null ? null : info.activationCode;
            jAuthInfo.local = info == null ? null : info.local;
            jAuthInfo.contact = info == null ? null : info.contact;
            jAuthInfo.contactInformation = info == null ? null : info.contactInformation;
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
                        MMKV.defaultMMKV().encode("JAuthInfolevel1",jb.getTeamName());
                        break;
                    case "TEAM_LEVEL_2":
                        Timber.i("New JAuthInfo level 2: %s", jb.getTeamName());
                        jAuthInfo.quxianId = jb.getTeamId();
                        jAuthInfo.quxianName = jb.getTeamName();
                        MMKV.defaultMMKV().encode("JAuthInfolevel2",jb.getTeamName());
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
        baseUrlToken = MMKV.defaultMMKV().getString("baseToken", BuildConfig.TOKEN_URL);
        jzAuth.setGlobalURL(baseUrlToken);
        jzAuth.initialize(context, "zkja");
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
//                    else {
//                        //如果注册过了并且 tokenUrl 不等于默认的 再次执行注册
//                        if (!baseUrlToken.equals(BuildConfig.TOKEN_URL)) {
//                            registerJzAuth();
//                            Timber.i("getToken ，register success !");
//                        }
//                    }
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

    //    a) 产品名称代号:用大写汉语拼音字母 ZZD 表示；
//    b) 产品类型代号:台式终端用“T”表示，立式终端用“L”表示，移动式终端用“Y”
//    表示；
//    c) 企业名称代号:用 3 位大写汉语拼音字母或阿拉伯数字组合表示；
//    d) 产品型号代号:用 2 位大写汉语拼音字母或阿拉伯数字组合表示；
//    e) 产品序列号:用 12 位阿拉伯数字表示，其中应包含产品的生产年份(YYYY， 见 GB/T 7408)，星期(Www，见 GB/T 7408)和顺序号(5 位阿拉伯数字)。
    //示例  ZZD-T-ABC-01-2020W0100001。
    //序列号因用系统的不符合规则 所有自己设置
    private String getSerialNumber() {
        if (BuildConfig.VERSION_STATE == 1) {
            return Build.SERIAL;
        } else {
            StringBuilder buffer = new StringBuilder();
            String client = "";
            if (BuildConfig.EQUIPMENT_TYPE == 1) {
                client = "L";
            } else if (BuildConfig.EQUIPMENT_TYPE == 3) {
                client = "T";
            } else {
                client = "Y";
            }
            //根据文件夹读取序列号
            String path = FileUtils.createSerialNumberFile();
            File file = new File(path);
            File[] files = file.listFiles();
            String name = "";
            if (files != null && files.length != 0) {
                name = files[0].getName();
            }
            if (TextUtils.isEmpty(name)) {
                return "";
            }
            buffer.append("ZZD-").append(client).append("-")
                    .append("ZZ1-").append("MR-").append(name);
            return buffer.toString();
        }
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
//        AuthInfo.getInstance().setSerialNumber(getSerialNumber());
//        AuthInfo.getInstance().setSerialNumber("ZZD-L-ZZ1-MR-2021w0350001");
        AuthInfo.getInstance().setSerialNumber(MMKV.defaultMMKV().getString("serialNumber", ""));
        String time = HexStringUtils.convertCurrentGMT();
        AuthInfo.getInstance().setTime(time);

//        AuthInfo.getInstance().setCurrentVersion("1.0.0");
        AuthInfo.getInstance().setClientName(BuildConfig.CLIENT_NAME);//clientName
        AuthInfo.getInstance().setLoc(jAuthInfo.local);

        final Exception[] errorR = new Exception[1];
        final String[] resultR = new String[1];
        Timber.e("clientId:%s", AuthInfo.getInstance().getClientId());
        JZAuth.getInstance().registerDevice(new ResultListener() {

            @Override
            public void onError(JZAuthException e) {
                synchronized (authLock) {
                    errorR[0] = e;
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
                //                if (!Objects.equals("0", result) && !Objects.equals("1", result)) {
//                    throw new IOException("token server register error : " + resultR[0]);
//                }
                if (!Objects.equals("0", result)) {
                    if (Objects.equals("1",result)) {
                        throw new IOException("该终端已经存在，不能重复注册");
                    } else {
                        throw new IOException("token server register error : " + resultR[0]);
                    }
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
            public void onError(JZAuthException e) {
                synchronized (authLock) {
                    errorR[0] = e;
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
