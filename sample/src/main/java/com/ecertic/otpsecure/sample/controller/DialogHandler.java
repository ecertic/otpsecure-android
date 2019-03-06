package com.ecertic.otpsecure.sample.controller;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.ecertic.otpsecure.sample.R;
import com.ecertic.otpsecure.sample.dialog.ErrorDialogFragment;

/**
 * A convenience class to handle displaying error dialogs.
 */
public class DialogHandler {

    private final FragmentManager mFragmentManager;

    public DialogHandler(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void showMsg(String message) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(
                R.string.validationMsg, message);
        fragment.show(mFragmentManager, "error");
    }

}
