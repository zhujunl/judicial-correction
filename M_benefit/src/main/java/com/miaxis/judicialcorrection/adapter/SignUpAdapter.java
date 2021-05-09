package com.miaxis.judicialcorrection.adapter;


import android.graphics.Color;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.miaxis.judicialcorrection.ChildItemClickListener;
import com.miaxis.judicialcorrection.base.api.vo.SignUpBean;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.miaxis.judicialcorrection.benefit.R;
import com.miaxis.judicialcorrection.benefit.databinding.ItemSignUpAdapterBinding;

import org.jetbrains.annotations.NotNull;


public class SignUpAdapter extends BaseQuickAdapter<SignUpBean.ListBean, BaseDataBindingHolder<ItemSignUpAdapterBinding>> implements LoadMoreModule {

    public SignUpAdapter() {
        super(R.layout.item_sign_up_adapter);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemSignUpAdapterBinding> bindingHolder, SignUpBean.ListBean signUpBean) {
        ItemSignUpAdapterBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            if (signUpBean.getPublicActivityVo() != null) {
                binding.tvThemeLabel.setText("活动主题："+signUpBean.getPublicActivityVo().getSqfwnr());
                String time = HexStringUtils.convertGMTToLocal(signUpBean.getPublicActivityVo().getSqfwkssj()) + "至" + HexStringUtils.convertGMTToLocal(signUpBean.getPublicActivityVo().getSqfwjssj());
                binding.tvActivityTimeContent.setText(time);
                binding.tvActivityTimeLongContent.setText(signUpBean.getPublicActivityVo().getSqfwsc());
                binding.tvActivityLocation.setText(signUpBean.getPublicActivityVo().getJiedaoName());
//                binding.tvType.setText(signUpBean.getPublicActivityVo().getJlr());
            } else {
                binding.tvThemeLabel.setText("");
                binding.tvActivityTimeContent.setText("");
                binding.tvActivityTimeLongContent.setText("");
                binding.tvActivityLocation.setText("");
//                binding.tvType.setText("");
            }
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
