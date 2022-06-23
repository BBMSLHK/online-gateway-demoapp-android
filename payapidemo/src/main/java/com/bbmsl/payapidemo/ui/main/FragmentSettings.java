package com.bbmsl.payapidemo.ui.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.databinding.FragmentSettingsBinding;


public class FragmentSettings extends Fragment {
    private static final String TAG = "FragmentSettings";

    FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentSettingsBinding.inflate(inflater, container, false);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ValueCallback<Boolean> clearCookieCallback = new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean aBoolean) {
                String result_msg = getString(R.string.setting_login_cleared);

                new AlertDialog.Builder(getContext())
                       .setMessage(result_msg)
                        .setNeutralButton(getString(R.string.dialog_ok), null)
                        .show();

            }
        };
        binding.btnClearcookie.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "clearcookies onclick");
                        CookieManager.getInstance().removeAllCookies(clearCookieCallback);
                    }});

    }

}
