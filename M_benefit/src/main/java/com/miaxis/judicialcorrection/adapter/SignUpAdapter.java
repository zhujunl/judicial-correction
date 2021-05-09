package com.miaxis.judicialcorrection.adapter;


import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.miaxis.judicialcorrection.ChildItemClickListener;
import com.miaxis.judicialcorrection.base.api.vo.SignUpContentBean;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.miaxis.judicialcorrection.benefit.R;
import com.miaxis.judicialcorrection.benefit.databinding.ItemSignUpAdapterBinding;

import org.jetbrains.annotations.NotNull;


public class SignUpAdapter extends BaseQuickAdapter<SignUpContentBean, BaseDataBindingHolder<ItemSignUpAdapterBinding>> implements LoadMoreModule {

    public SignUpAdapter() {
        super(R.layout.item_sign_up_adapter);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemSignUpAdapterBinding> bindingHolder, SignUpContentBean signUpBean) {
        ItemSignUpAdapterBinding binding = bindingHolder.getDataBinding();

        if (binding != null) {
            binding.tvThemeLabel.setText("活动主题：" + signUpBean.getSqfwnr());
            String time = HexStringUtils.convertGMTToLocal(signUpBean.getSqfwkssj()) + "至" + HexStringUtils.convertGMTToLocal(signUpBean.getSqfwjssj());
            binding.tvActivityTimeContent.setText(time);
            binding.tvActivityTimeLongContent.setText(signUpBean.getSqfwsc());
            binding.tvActivityLocation.setText(signUpBean.getSqfwdd());

            binding.tvState.setTextColor(Color.parseColor("#FF721F"));
            binding.tvState.setBackgroundResource(R.drawable.bg_button_un);
            binding.tvState.setText("报名参加");

            binding.tvState.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(signUpBean);
                }
            });
        }
    }

    private ChildItemClickListener listener;

    public void setChildItemClickListener(ChildItemClickListener listener) {
        this.listener = listener;
    }

}
