package com.bbmsl.payapidemo.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.databinding.FragmentShowresultBinding;

import org.jetbrains.annotations.NotNull;

import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_SUCCESS;
import static com.bbmsl.payapidemo.constants.BaseConstants.INTENTEXTRA_KEYSTATUS;

public class FragmentShowResult extends Fragment {
    private static final String TAG = "FragmentShowResult";

    FragmentShowresultBinding binding;

    /**
     *   This page is a mock merchant app result page.
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentShowresultBinding.inflate(inflater, container, false);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // add  btn listener
        binding.layoutResult.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backToDemo();
                    }
                });

        String status = getArguments().getString(INTENTEXTRA_KEYSTATUS);

        if (status.equals(CALLBACK_SUCCESS)){
            binding.layoutResult.setBackgroundColor(getResources().getColor(R.color.colorSuccessCallback, null));
            binding.txtResult.setText(getString(R.string.result_success));
        } else{
            binding.layoutResult.setBackgroundColor(getResources().getColor(R.color.colorFailedCallback, null));
            binding.txtResult.setText(getString(R.string.result_failed));
        }

    }


    /**
     * Handle back button behaviour
     */
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        
        getActivity().getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Log.i(TAG, "onBackPressed");
                        backToDemo();
                    }
                });
    }

    private void backToDemo(){
        Log.i(TAG, "BackToDemo");
        NavHostFragment.findNavController(FragmentShowResult.this)
                .popBackStack();

    }
}
