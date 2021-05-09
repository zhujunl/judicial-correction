package com.miaxis.judicialcorrection.adapter;


import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.miaxis.judicialcorrection.base.api.vo.HistorySignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.SignUpContentBean;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.miaxis.judicialcorrection.benefit.R;
import com.miaxis.judicialcorrection.benefit.databinding.ItemSignUpAdapterBinding;

import org.jetbrains.annotations.NotNull;


public class HistorySignUpAdapter extends BaseQuickAdapter< HistorySignUpBean.ListBean, BaseDataBindingHolder<ItemSignUpAdapterBinding>> implements LoadMoreModule {

    public HistorySignUpAdapter() {
        super(R.layout.item_sign_up_adapter);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemSignUpAdapterBinding> bindingHolder, HistorySignUpBean.ListBean signUpBean) {
        ItemSignUpAdapterBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            if (signUpBean.getPublicActivityVo() != null) {
                binding.tvThemeLabel.setText("活动主题："+signUpBean.getPublicActivityVo().getSqfwnr());
                String time = HexStringUtils.convertGMTToLocal(signUpBean.getPublicActivityVo().getSqfwkssj()) + "至" + HexStringUtils.convertGMTToLocal(signUpBean.getPublicActivityVo().getSqfwjssj());
                binding.tvActivityTimeContent.setText(time);
                binding.tvActivityTimeLongContent.setText(signUpBean.getPublicActivityVo().getSqfwsc()+"小时");
                binding.tvActivityLocation.setText(signUpBean.getPublicActivityVo().getSqfwdd());
//                binding.tvType.setText(signUpBean.getPublicActivityVo().getJlr());
            } else {
                binding.tvThemeLabel.setText("");
                binding.tvActivityTimeContent.setText("");
                binding.tvActivityTimeLongContent.setText("");
                binding.tvActivityLocation.setText("");
//                binding.tvType.setText("");
            }

            binding.tvState.setTextColor(Color.parseColor("#979797"));
            binding.tvState.setBackgroundResource(R.drawable.bg_gray_button_box);
            binding.tvState.setText("已报名");
        }
    }
}
