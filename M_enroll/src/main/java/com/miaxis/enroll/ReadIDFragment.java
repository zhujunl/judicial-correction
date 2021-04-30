package com.miaxis.enroll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * GoHomeFragment
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@AndroidEntryPoint
public class ReadIDFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = DataBindingUtil.inflate(inflater, R.layout.fragment_read_id, container, false).getRoot();
        root.findViewById(R.id.btn_back_to_home).setOnClickListener(v -> {
            getActivity().finish();
        });
        return root;
    }
}
