package com.bbmsl.payapidemo.ui.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.ui.components.FragmentCommonInputs;

public class BaseFragment extends Fragment implements FragmentCommonInputs.MessageListener {
    private static final String TAG = "BaseFragment";

    protected FragmentCommonInputs commonInputsFragment;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showError (String message){
        Log.e(TAG, "showError"+ message);
        showDialog(getString(R.string.dialog_error),message);
    }

    protected void showSuccess(String message){
        Log.e(TAG, "showSuccess: "+message);
        showDialog(getString(R.string.dialog_success),message);
    }

    protected void showDialog(String title, String message){
        Log.e(TAG, "showDialog: "+message);
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(getString(R.string.dialog_ok), null)
                .show();
    }

    @Override
    public void messageSent(String message, boolean isError) {
        if (isError){
            showError(message);
        } else{
            showSuccess(message);
        }
    }

}
