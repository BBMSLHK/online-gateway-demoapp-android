package com.bbmsl.payapidemo.ui.main;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.navigation.fragment.NavHostFragment;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.databinding.FragmentMainBinding;

import org.jetbrains.annotations.NotNull;

import static com.bbmsl.payapidemo.constants.HostAddress.MSL_DOCS;

public class FragmentMain extends BaseFragment {
    private static final String TAG = "FragmentMain";
    private FragmentMainBinding binding;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnSettings Clicked");
                NavHostFragment.findNavController(FragmentMain.this)
                        .navigate(R.id.action_mainFragment_to_fragmentSettings);
            }
        });

        binding.btnHostedcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnHostedcheckout Clicked");
                NavHostFragment.findNavController(FragmentMain.this)
                        .navigate(R.id.action_mainFragment_to_hostedcheckout);
            }
        });
        binding.btnDirectsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnDirectsales Clicked");
                NavHostFragment.findNavController(FragmentMain.this)
                        .navigate(R.id.action_mainFragment_to_directsales);
            }
        });
        binding.btnTokenization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnTokenization Clicked");
                NavHostFragment.findNavController(FragmentMain.this)
                        .navigate(R.id.action_mainFragment_to_tokenization);
            }
        });

        binding.btnDocumentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                            .setShowTitle(false)
                            .setUrlBarHidingEnabled(true)
                            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
                            .setInstantAppsEnabled(false)
                            .build();
                    customTabsIntent.launchUrl(getContext(), Uri.parse(MSL_DOCS));
                } catch (ActivityNotFoundException e){
                    showDialog("", String.format(getString(R.string.error_nobrowser), MSL_DOCS));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}