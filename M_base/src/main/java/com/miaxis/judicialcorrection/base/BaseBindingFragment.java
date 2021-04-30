package com.miaxis.judicialcorrection.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * @author Tank
 * @date 2021/4/25 3:59 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public abstract class BaseBindingFragment<V extends ViewDataBinding> extends Fragment {

    protected V binding;

//    protected HashMap<String, Object> data = new HashMap<>();
//
//    public <T extends BaseBindingFragment<?>> T bind(String key, Object value) {
//        if (key != null) {
//            data.put(key, value);
//        }
//        return (T) this;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
//        try {
//            Class<? extends BaseBindingFragment> aClass = getClass();
//            if (!data.isEmpty()) {
//                Set<Map.Entry<String, Object>> entries = data.entrySet();
//                for (Map.Entry<String, Object> map : entries) {
//                    String key = map.getKey();
//                    Object value = map.getValue();
//                    Field field = aClass.getDeclaredField(key);
//                    if (field != null) {
//                        boolean accessible = field.isAccessible();
//                        if (!accessible) {
//                            field.setAccessible(true);
//                        }
//                        field.set(this, value);
//                        if (!accessible) {
//                            field.setAccessible(false);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initLayout(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        initView(binding, savedInstanceState);
        initData(binding, savedInstanceState);
    }

    protected abstract int initLayout();

    protected abstract void initView(@NonNull V binding, @Nullable Bundle savedInstanceState);

    protected abstract void initData(@NonNull V binding, @Nullable Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (data != null) {
//            data.clear();
//        }
    }

    protected void finish() {
        getActivity().finish();
    }

}
