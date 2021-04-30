package com.miaxis.enroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaxis.enroll.databinding.FragmentGoHomeBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * GoHomeFragment
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@AndroidEntryPoint
public class GoHomeFragment extends BaseBindingFragment<FragmentGoHomeBinding> {



    @Override
    protected int initLayout() {
        return R.layout.fragment_go_home;
    }

    @Override
    protected void initView(@NonNull FragmentGoHomeBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@NonNull FragmentGoHomeBinding binding, @Nullable Bundle savedInstanceState) {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        handler.post(runS);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(runS);
    }

    Handler handler = new Handler();

    int CLOSE_TIME = 4;
    int count = 0;
    @SuppressLint("SetTextI18n")
    final Runnable runS = new Runnable() {
        @Override
        public void run() {
            count++;
            if (count >= CLOSE_TIME) {
                finish();
            } else {
                binding.setTime(CLOSE_TIME-count);
                handler.postDelayed(runS, 1000);
            }
        }
    };
}
