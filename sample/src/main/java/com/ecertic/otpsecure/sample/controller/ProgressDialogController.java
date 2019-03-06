package com.ecertic.otpsecure.sample.controller;

import android.support.v4.app.FragmentManager;

import com.ecertic.otpsecure.sample.R;
import com.ecertic.otpsecure.sample.dialog.ProgressDialogFragment;

public class ProgressDialogController {

    private final FragmentManager mFragmentManager;
    private ProgressDialogFragment mProgressFragment;

    public ProgressDialogController(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mProgressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);
    }

    public void setMessageResource(int resId) {
        if (mProgressFragment.isVisible()) {
            mProgressFragment.dismiss();
            mProgressFragment = null;
        }
        mProgressFragment = ProgressDialogFragment.newInstance(resId);
    }

    public void startProgress() {
        mProgressFragment.show(mFragmentManager, "progress");
    }

    public void finishProgress() {
        mProgressFragment.dismiss();
    }

}
