package com.miaxis.enroll.guide;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.miaxis.enroll.R;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * GuideNavigationController
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
public class GuideNavigationController {

    private final FragmentManager fragmentManager;
    private final int containId;

    public GuideNavigationController(FragmentManager fragmentManager, int containId) {
        this.fragmentManager = fragmentManager;
        this.containId = containId;
    }

    public void back(){
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        }
    }

    public void nvBoot() {
        fragmentManager.beginTransaction()
                //.replace(containId, new BootFragment())
                .commit();
    }

    public void nvLanguage() {
        fragmentManager.beginTransaction()
                //.replace(containId, new LanguageFragment())
                .commit();
    }

    public void nvTo(Fragment fragment, boolean withAnim) {
        Timber.i("fragment %s,withAnim %s",fragment,withAnim);
        if (withAnim) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_right_in, R.anim.fragment_left_out,R.anim.fragment_left_in,R.anim.fragment_right_out)
                    .replace(containId, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(containId, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}