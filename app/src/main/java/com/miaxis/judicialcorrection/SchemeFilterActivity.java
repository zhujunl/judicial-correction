package com.miaxis.judicialcorrection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.miaxis.judicialcorrection.dialog.DialogVerifyResult;

import androidx.appcompat.app.AppCompatDialog;

public class SchemeFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //直接通过ARouter处理外部Uri
        //        Uri uri = getIntent().getData();
        //        ARouter.getInstance().build(uri).navigation(this, new NavCallback() {
        //            @Override
        //            public void onArrival(Postcard postcard) {
        //                finish();
        //            }
        //        });


        //        ARouter.getInstance().build("/activity/readIDCard")
        //                .withString("Title", "这是测试的")
        //                .withBoolean("NoIdCardEnable", false)
        //                .withBoolean("AutoCheckEnable", false)
        //                .navigation(this, 0);

        //        ARouter.getInstance().build("/activity/verifyPage")
        //                .withString("Name", "小名")
        //                .withString("IdCardNumber", "11111111111111111X")
        //                .navigation(this, 0);

        new DialogVerifyResult(this, new DialogVerifyResult.ClickListener() {
            @Override
            public void onBackHome(AppCompatDialog appCompatDialog) {
                finish();
            }

            @Override
            public void onTryAgain(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
            }

            @Override
            public void onTimeOut(AppCompatDialog appCompatDialog) {
                finish();
            }
        }, new DialogVerifyResult.Builder()).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Toast.makeText(this, "data:" + data.getSerializableExtra("result"), Toast.LENGTH_SHORT).show();
        }
    }

}
