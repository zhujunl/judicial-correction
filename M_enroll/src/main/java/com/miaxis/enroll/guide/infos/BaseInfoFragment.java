package com.miaxis.enroll.guide.infos;

import androidx.databinding.ViewDataBinding;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundAdapter;

/**
 * BaseInfoFragment
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public abstract class BaseInfoFragment<T extends ViewDataBinding> extends BaseBindingFragment<T> {

    /**
     * 返回数据是否有异常，在离开页面前调用
     */
    public boolean checkData(){
        return true;
    }
}
