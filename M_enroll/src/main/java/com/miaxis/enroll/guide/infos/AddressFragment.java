package com.miaxis.enroll.guide.infos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentAddressBinding;
import com.miaxis.enroll.vo.Addr;
import com.miaxis.judicialcorrection.base.db.po.Place;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * AddressFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class AddressFragment extends BaseInfoFragment<FragmentAddressBinding> {
    @Override
    protected int initLayout() {
        return R.layout.fragment_address;
    }

    AddressViewModel viewModel;


    @Override
    protected void initView(@NonNull FragmentAddressBinding binding, @Nullable Bundle savedInstanceState) {
        //居住地
        binding.spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect[0] = (Place) item;
                    viewModel.mProvince.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mSelect[0] = null;
                viewModel.mProvince.postValue(null);
            }
        });
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect[1] = (Place) item;
                    viewModel.mCity.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mCity.postValue(null);
                viewModel.mSelect[1] = null;
            }
        });
        binding.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect[2] = (Place) item;
                    viewModel.mDistrict.postValue((Place) item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mDistrict.postValue(null);
                viewModel.mSelect[2] = null;
            }
        });
        binding.spinnerAgencies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect[3] = (Place) item;
                    viewModel.mAgencies.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mAgencies.postValue(null);
                viewModel.mSelect[3] = null;
            }
        });

        binding.addrGroup.setOnCheckedChangeListener((group, checkedId) -> {
            boolean isSame = checkedId == R.id.addrT;
            Boolean vmB = viewModel.addrSame.getValue();
            if (vmB != isSame) {
                viewModel.addrSame.setValue(isSame);
            }
        });
    }

    EnrollSharedViewModel viewModelShard;

    @Override
    protected void initData(@NonNull FragmentAddressBinding binding, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVmAddr(viewModel);
        viewModelShard = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);

        viewModel.addrSame.observe(this, same -> {
            int nid = same ? R.id.addrT : R.id.addrF;
            if (binding.addrGroup.getCheckedRadioButtonId() != nid) {
                binding.addrGroup.check(nid);
            }
        });

        // 居住地
        viewModel.allProvince.observe(this, places -> {
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerProvince.setAdapter(adapter);
            binding.spinnerProvince.setSelection(getCheckedPosition(places, viewModel.mSelect[0]));
        });
        viewModel.allCity.observe(this, places -> {
            Timber.i("allCity %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerCity.setAdapter(adapter);
            binding.spinnerCity.setSelection(getCheckedPosition(places, viewModel.mSelect[1]));

        });
        viewModel.allDistrict.observe(this, places -> {
            Timber.i("allDistrict %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerDistrict.setAdapter(adapter);
            binding.spinnerDistrict.setSelection(getCheckedPosition(places, viewModel.mSelect[2]));
        });
        viewModel.allAgencies.observe(this, places -> {
            Timber.i("allAgencies %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerAgencies.setAdapter(adapter);
            binding.spinnerAgencies.setSelection(getCheckedPosition(places, viewModel.mSelect[3]));
        });

        setHJ();
        initHJ();
    }

    void updateAddrToSharedViewModel() {
        Addr addr = viewModel.addrLiveData.getValue();
        addr.gdjzdszsName = getSpName(binding.spinnerProvince);
        addr.gdjzdszdsName = getSpName(binding.spinnerCity);
        addr.gdjzdszxqName = getSpName(binding.spinnerDistrict);
        addr.gdjzdName = getSpName(binding.spinnerDistrict);

        if (viewModel.addrSame.getValue()) {
            addr.hjszsName = getSpName(binding.spinnerProvince);
            addr.hjszdsName = getSpName(binding.spinnerCity);
            addr.hjszxqName = getSpName(binding.spinnerDistrict);
            addr.hjszdName = getSpName(binding.spinnerDistrict);
            addr.hjszdmx = addr.gdjzdmx;
        } else {
            addr.hjszsName = getSpName(binding.spinnerProvince2);
            addr.hjszdsName = getSpName(binding.spinnerCity2);
            addr.hjszxqName = getSpName(binding.spinnerDistrict2);
            addr.hjszdName = getSpName(binding.spinnerDistrict2);
        }
        Timber.i("Address %s", addr);
        viewModelShard.setAddress(addr);
    }

    String getSpName(Spinner spinner) {
        Object selectedItem = spinner.getSelectedItem();
        if (selectedItem instanceof Place) {
            return ((Place) selectedItem).VALUE;
        }
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateAddrToSharedViewModel();
    }

    void initHJ() {
        // 户籍地
        viewModel.allProvince2.observe(this, places -> {
            Timber.i("allProvince2 %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerProvince2.setAdapter(adapter);
            binding.spinnerProvince2.setSelection(getCheckedPosition(places, viewModel.mSelect2[0]));
        });
        viewModel.allCity2.observe(this, places -> {
            Timber.i("allCity2 %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerCity2.setAdapter(adapter);
            binding.spinnerCity2.setSelection(getCheckedPosition(places, viewModel.mSelect2[1]));

        });
        viewModel.allDistrict2.observe(this, places -> {
            Timber.i("allDistrict2 %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerDistrict2.setAdapter(adapter);
            binding.spinnerDistrict2.setSelection(getCheckedPosition(places, viewModel.mSelect2[2]));
        });
        viewModel.allAgencies2.observe(this, places -> {
            Timber.i("allAgencies2%s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spinnerAgencies2.setAdapter(adapter);
            binding.spinnerAgencies2.setSelection(getCheckedPosition(places, viewModel.mSelect2[3]));
        });
    }

    void setHJ() {
        //户籍地
        binding.spinnerProvince2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect2[0] = (Place) item;
                    viewModel.mProvince2.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mSelect2[0] = null;
                viewModel.mProvince2.postValue(null);
            }
        });

        binding.spinnerCity2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect2[1] = (Place) item;
                    viewModel.mCity2.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mCity2.postValue(null);
                viewModel.mSelect2[1] = null;
            }
        });
        binding.spinnerDistrict2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect2[2] = (Place) item;
                    viewModel.mDistrict2.postValue((Place) item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mDistrict2.postValue(null);
                viewModel.mSelect2[2] = null;
            }
        });
        binding.spinnerAgencies2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    viewModel.mSelect2[3] = (Place) item;
                    viewModel.mAgencies2.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.mAgencies2.postValue(null);
                viewModel.mSelect2[3] = null;
            }
        });
    }


    private int getCheckedPosition(List<Place> justiceBureaus, Place total) {
        if (justiceBureaus == null || justiceBureaus.size() == 0 || total == null) {
            return 0;
        }
        for (int i = 0; i < justiceBureaus.size(); i++) {
            if (Objects.equals(justiceBureaus.get(i).ID, total.ID)) {
                return i;
            }
        }
        return 0;
    }


    public static class SpAdapter extends BaseAdapter {

        private List<Place> data;

        public void submitList(List<Place> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Place getItem(int position) {
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
            view.setText(getItem(position).VALUE);
            return view;
        }
    }

}
