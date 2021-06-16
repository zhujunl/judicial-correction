package com.miaxis.judicialcorrection.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.camera.MXFrame;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.db.po.Place;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.bean.LiveAddressChangeBean;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.dialog.HighShotMeterDialog;
import com.miaxis.judicialcorrection.dialog.PreviewPictureDialog;
import com.miaxis.judicialcorrection.face.GetFacePageFragment;
import com.miaxis.judicialcorrection.live.LiveAddressChangeActivity;
import com.miaxis.judicialcorrection.live.LiveAddressChangeViewModel;
import com.miaxis.judicialcorrection.live.R;
import com.miaxis.judicialcorrection.live.databinding.FragmentLiveAddressBinding;
import com.miaxis.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LiveAddressFragment extends BaseBindingFragment<FragmentLiveAddressBinding> implements CameraPreviewCallback {

    private static final String TAG = "LiveAddressFragment";
    private LiveAddressChangeViewModel model;
    //申请书  暂时都只扫描1个文件
    private String base64LiveChangeApplication;
    //申请资料
    private String base64LiveChangeData;
    private File mFilePath;
    //扫描类型
    private int scanType = 0;
    private PreviewPictureDialog mDialog;

    @Inject
    AppHints appHints;

    @Override
    protected int initLayout() {
        return R.layout.fragment_live_address;
    }

    @Override
    protected void initView(@NonNull FragmentLiveAddressBinding binding, @Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.btnSubmit.setOnClickListener(v -> {
            try {
                submitInfo();
            } catch (Exception e) {
                e.getStackTrace();
                appHints.showError("数据出错，提交失败！");
            }
        });

        binding.btnApplicationScan.setOnClickListener(v -> {
            scanType = 0;
            showPreInfo();
        });
        binding.btnApplicationMaterialsScan.setOnClickListener(v -> {
            scanType = 1;
            openScanning();
        });
    }

    @Override
    protected void initData(@NonNull FragmentLiveAddressBinding binding, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(getActivity()).get(LiveAddressChangeViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(model);
        mFilePath = FileUtils.createFileParent(getContext());
        model.provinceList.observe(this, observer -> {
            setSpView(observer, binding.spProvince);
            int selectedItemPosition = binding.spProvince.getSelectedItemPosition();
            Place place = observer.get(selectedItemPosition);
            if (place.ZXS == 1) {
                List<Place> list = new ArrayList<>();
                list.add(place);
                model.cityList.setValue(list);
            } else {
                model.findAllCity(observer.get(selectedItemPosition).ID);
            }
        });
        model.cityList.observe(this, observer -> {
            if (!observer.isEmpty()) {
                setSpView(observer, binding.spCity);
                int selectedItemPosition = binding.spCity.getSelectedItemPosition();
                Place place = observer.get(selectedItemPosition);
                model.findAllDistrict(place.ID);
            }
        });
        model.smallTown.observe(this, observer -> {
            if (!observer.isEmpty()) {
                setSpView(observer, binding.spDistrict);
                int selectedItemPosition = binding.spDistrict.getSelectedItemPosition();
                Place place = observer.get(selectedItemPosition);
                model.getProvince(place.ID);
            }
        });
        model.street.observe(this, observer -> {
            setSpView(observer, binding.spStreet);
        });
        model.getProvince();
        binding.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (model.provinceList.getValue() != null && !model.provinceList.getValue().isEmpty()) {
                    model.findAllCity(model.provinceList.getValue().get(position).ID);
                    if (!"浙江省".equals(model.provinceList.getValue().get(position).VALUE)) {
                        binding.spinnerSheng.setEnabled(false);
                        binding.spinnerShi.setEnabled(false);
                        binding.spinnerXian.setEnabled(false);
                        binding.spinnerJiedao.setEnabled(false);
                    } else {
                        binding.spinnerSheng.setEnabled(true);
                        binding.spinnerShi.setEnabled(true);
                        binding.spinnerXian.setEnabled(true);
                        binding.spinnerJiedao.setEnabled(true);
                        binding.llUnit.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (model.cityList.getValue() != null && !model.cityList.getValue().isEmpty()) {
                    model.findAllDistrict(model.cityList.getValue().get(position).ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (model.smallTown.getValue() != null && !model.smallTown.getValue().isEmpty()) {
                    model.getProvince(model.smallTown.getValue().get(position).ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerShi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);
                Log.i(TAG, "市 选择: " + position + " - " + itemAtPosition);
                if (itemAtPosition instanceof JusticeBureau) {
                    model.setShi((JusticeBureau) itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "市 选择: null");
                model.setShi(null);
            }
        });

        binding.spinnerXian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);
                Log.i(TAG, "县选择: " + position + " - " + itemAtPosition);
                if (itemAtPosition instanceof JusticeBureau) {
                    model.setXian((JusticeBureau) itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                model.setXian(null);
                Log.i(TAG, "县选择: null");
            }
        });
        binding.spinnerJiedao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);
                Log.i(TAG, "街道 选择: " + position + " - " + itemAtPosition);
                if (itemAtPosition instanceof JusticeBureau) {
                    model.setJiedao((JusticeBureau) itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                model.setJiedao(null);
                Log.i(TAG, "街道 选择: null");
            }
        });

        model.shiListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                Log.i(TAG, "市 列表: " + listResource.data);
                setSpUnitView(listResource.data, binding.spinnerShi);

            }
        });
        model.xianListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                Log.i(TAG, "县 列表: " + listResource.data);
                if (listResource.data == null) {
                    return;
                }
                setSpUnitView(listResource.data, binding.spinnerXian);
            }
        });

        model.jiedaoListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                Log.i(TAG, "街道 列表: " + listResource.data);
                if (listResource.data == null) {
                    model.setJiedao(null);
                    return;
                }
                setSpUnitView(listResource.data, binding.spinnerJiedao);
            }
        });
        model.setSheng(null);
    }

    private void setSpView(List<Place> observer, Spinner spinner) {
        List<String> list = new ArrayList<>();
        for (Place p : observer) {
            list.add(p.VALUE);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
    }

    private void setSpUnitView(List<JusticeBureau> beanList, Spinner spinner) {
        SpAdapter adapter = new SpAdapter();
        adapter.submitList(beanList);
        spinner.setAdapter(adapter);
    }


    public static class SpAdapter extends BaseAdapter {

        private List<JusticeBureau> data;

        public void submitList(List<JusticeBureau> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
            view.setText(data.get(position).getTeamName());
            return view;
        }
    }

    private void submitInfo() {
        if (model.provinceList.getValue() == null || model.personInfoMutableLiveData.getValue() == null) {
            appHints.showError("数据提交失败！");
            return;
        }
        LiveAddressChangeBean value = model.liveBean.getValue();
        if (value != null) {
            value.pid = model.personInfoMutableLiveData.getValue().getId();
            value.sqsj = model.currentTime(true);
            value.qrdszs = model.provinceList.getValue().get(binding.spProvince.getSelectedItemPosition()).ID + "";
            value.qrdszsName = model.provinceList.getValue().get(binding.spProvince.getSelectedItemPosition()).VALUE;
            if (model.provinceList.getValue().get(binding.spProvince.getSelectedItemPosition()).ZXS == 1) {
                value.qrdszd = model.provinceList.getValue().get(binding.spProvince.getSelectedItemPosition()).ID + "";
                value.qrdszdName = model.provinceList.getValue().get(binding.spProvince.getSelectedItemPosition()).VALUE;
            } else {
                if (model.cityList.getValue() != null) {
                    value.qrdszd = model.cityList.getValue().get(binding.spCity.getSelectedItemPosition()).ID + "";
                    value.qrdszdName = model.cityList.getValue().get(binding.spCity.getSelectedItemPosition()).VALUE;
                }
            }
            if (model.smallTown.getValue() != null && !model.smallTown.getValue().isEmpty()) {
                value.qrdszx = model.smallTown.getValue().get(binding.spDistrict.getSelectedItemPosition()).ID + "";
                value.qrdszxName = model.smallTown.getValue().get(binding.spDistrict.getSelectedItemPosition()).VALUE;
            } else {
                value.qrdszx = "";
                value.qrdszxName = "";
            }
            if ("浙江省".equals(value.qrdszsName)) {
                if (model.street.getValue() != null && !model.street.getValue().isEmpty()) {
                    value.qrdxz = model.street.getValue().get(binding.spStreet.getSelectedItemPosition()).ID + "";
                    value.qrdxzName = model.street.getValue().get(binding.spStreet.getSelectedItemPosition()).VALUE;
                } else {
                    value.qrdxz = "";
                    value.qrdxzName = "";
                }
            } else {
                value.qrdxz = "";
                value.qrdxzName = "";
            }
            if (TextUtils.isEmpty(value.qrdmx)) {
                value.qrdmx = "";
            }
            Object itemAtPosition = binding.spinnerJiedao.getSelectedItem();
            if (itemAtPosition instanceof JusticeBureau) {
                value.njsjzdwId = ((JusticeBureau) itemAtPosition).getTeamId();
                value.njsjzdwName = ((JusticeBureau) itemAtPosition).getTeamName();
                if (TextUtils.isEmpty(value.njsjzdwId)) {
                    value.njsjzdwId = "";
                }
                if (TextUtils.isEmpty(value.njsjzdwName)) {
                    value.njsjzdwName = "";
                }
            }
            model.setLiveAddressChange(base64LiveChangeApplication, base64LiveChangeData).observe(this, observer -> {
                if (observer.isSuccess()) {
                    LiveAddressChangeBean v = model.liveBean.getValue();
                    v.sqsj = model.currentTime(false);
                    DialogResult dialogResult = new DialogResult(getActivity(), new DialogResult.ClickListener() {
                        @Override
                        public void onBackHome(AppCompatDialog appCompatDialog) {
                            appCompatDialog.dismiss();
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
                    }, new DialogResult.Builder(true, "提交成功", "", 0, true
                    ).hideAllHideSucceedInfo(true));
                    dialogResult.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (getActivity() != null && !getActivity().isFinishing()) {
                                dialogResult.hide();
                                ((LiveAddressChangeActivity) getActivity()).replaceFragment(new LiveListFragment());
                            }
                        }
                    }, 2000);
                }
                if (observer.isError()) {
                    appHints.showError(observer.errorMessage);
                }
            });
        }
    }

    //显示高拍仪预览内容
    private void showPreInfo() {
        new HighShotMeterDialog(getActivity(), new HighShotMeterDialog.ClickListener() {
            @Override
            public void onDetermine(List<String> base64List) {

            }

            @Override
            public void onTryAgain(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
            }

            @Override
            public void onTimeOut(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
            }
        }, new HighShotMeterDialog.Builder()).show();

    }

    private void openScanning() {
        ZZResponse<?> init = CameraHelper.getInstance().init();
        if (ZZResponse.isSuccess(init)) {
            ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_SM);
            if (ZZResponse.isSuccess(mxCamera)) {
//                mxCamera.getData().setOrientation(CameraConfig.Camera_SM.previewOrientation);
                mxCamera.getData().setPreviewCallback(this);
                mxCamera.getData().start(null);
                mxCamera.getData().setNextFrameEnable();
            } else {
                appHints.showError("扫描失败！");
            }
        } else {
            appHints.showError("扫描设备初始化失败请点击重试！");
        }
    }

    @Override
    public void onPreview(MXFrame frame) {
        if (MXFrame.isNullCamera(frame)) {
            return;
        }
        String fileName = "sm" + scanType + ".jpg";
        File file = new File(mFilePath, fileName);
        boolean frameImage = frame.camera.saveFrameImage(file.getAbsolutePath());
        if (frameImage) {
            String base64Path = FileUtils.imageToBase64(file.getAbsolutePath());
            mHandler.post(() -> {
                if (scanType == 0) {
                    base64LiveChangeApplication = base64Path;
                    binding.tvApplicationInfoShow.setText(fileName);
                } else {
                    base64LiveChangeData = base64Path;
                    binding.tvApplicationInfoShow2.setText(fileName);
                }
            });
        } else {
            mHandler.post(() -> appHints.showError("扫描保存文件失败，请点击重试！"));
        }
        try {
            CameraHelper.getInstance().stop();
            frame.camera.stop();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    private final static Handler mHandler = new Handler();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        base64LiveChangeApplication = null;
        base64LiveChangeData = null;
        mHandler.removeCallbacksAndMessages(null);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
