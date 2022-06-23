package com.bbmsl.payapidemo.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.databinding.FragmentCommonInputsBinding;
import com.bbmsl.payapidemo.network.model.dataobject.LineItems;
import com.bbmsl.payapidemo.util.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FragmentCommonInputs extends Fragment implements LineItemAdapter.LineItemsUpdateListener {
    private static final String TAG = "FragmentCommonInputs";
    private FragmentCommonInputsBinding binding;

    private LineItemAdapter lineItemAdapter;
    private MessageListener mListener;

    public interface MessageListener {
        void messageSent(String message, boolean isError);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentCommonInputsBinding.inflate(inflater, container, false);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddListitem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lineItemAdapter.addnewItem();
                    }});

        ArrayList<LineItems> lineItemsList = new ArrayList<>();
        lineItemsList.add(new LineItems("Sample item", new BigDecimal("0.01"), 1)); // default item

        lineItemAdapter = new LineItemAdapter(getContext(), R.layout.adapter_lineitems,lineItemsList);
        lineItemAdapter.setItemUpdateListener(FragmentCommonInputs.this);
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.itemList.setAdapter(lineItemAdapter);
        binding.txtAmount.setText(String.format(Locale.getDefault(),"%.2f", lineItemAdapter.getTotalAmount()));
    }

    public void setListener(MessageListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onLineItemsUpdated(BigDecimal data) {
        binding.txtAmount.setText(String.format(Locale.getDefault(),"%.2f", data));
    }

    public BigDecimal getTotalAmount (){
        try {
            return new BigDecimal(binding.txtAmount.getText().toString());
        } catch (Exception e){
            mListener.messageSent("Amount parse error", true);
            return new BigDecimal(0);
        }
    }
    public String getMerchantRef (){
        try {
            String merchantRef = binding.edittextMerchantRef.getText().toString();
            if (StringUtil.isEmpty(merchantRef)) {
                merchantRef = new Date().getTime()+"";
            }
            return merchantRef;
        } catch (Exception e){
            mListener.messageSent("getMerchantRef error", true);
            return new Date().getTime()+"";
        }
    }

    public ArrayList<LineItems> getItemList (){
        try {
            return lineItemAdapter.getmItemList();
        } catch (Exception e){
            mListener.messageSent("get Lineitem error", true);
            return new ArrayList<>();
        }
    }

    public void enable(boolean b){
        binding.btnAddListitem.setEnabled(b);
        binding.btnUpdate.setEnabled(b);
        binding.edittextMerchantRef.setEnabled(b);
        lineItemAdapter.setItemEnabled(b);
    }

}
