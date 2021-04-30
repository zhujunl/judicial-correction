package com.miaxis.enroll.guide;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.miaxis.enroll.EnrollActivity;

/**
 * BaseGuideFragment
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
public class BaseGuideFragment extends Fragment  {


    protected void navigation(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity instanceof EnrollActivity) {
            ((EnrollActivity) activity).getNvController().nvTo(fragment, true);
        }
    }

    protected void back(){
        FragmentActivity activity = getActivity();
        if (activity instanceof EnrollActivity) {
            ((EnrollActivity) activity).getNvController().back();
        }
    }
    protected void finishActivity() {
        FragmentActivity activity = getActivity();
        activity.finish();
    }
}
