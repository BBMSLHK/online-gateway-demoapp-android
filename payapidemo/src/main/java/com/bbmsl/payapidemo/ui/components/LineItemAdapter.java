package com.bbmsl.payapidemo.ui.components;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.network.model.dataobject.LineItems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

public class LineItemAdapter extends ArrayAdapter<LineItems> {
    private static final String TAG = "LineItemAdapter";

    private final Context mContext;
    private LineItemsUpdateListener mListener;
    private final int layoutId;

    private final ArrayList<LineItems> mItemList ;
    private boolean editmode = false;
    private boolean itemEnabled = true;

    public LineItemAdapter(Context context, int resource, ArrayList<LineItems> itemList){
        super(context, resource, itemList);
        mContext = context;
        layoutId = resource;
        mItemList = itemList;
    }

    public void setItemUpdateListener (LineItemsUpdateListener listener){
        this.mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(layoutId, null);
        }
        EditText editText_itemname =  view.findViewById(R.id.edittext_itemname);
        EditText editText_qty =  view.findViewById(R.id.edittext_qty);
        EditText editText_price =  view.findViewById(R.id.edittext_price);

        Button btn_update =  view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editmode) {
                    mItemList.get(position).setPriceName(editText_itemname.getText().toString());

                    try {
                        mItemList.get(position).setQuantity(Integer.parseInt(editText_qty.getText().toString()));
                    } catch (NumberFormatException e){
                        mItemList.get(position).setQuantity(0);
                    }

                    try {
                        mItemList.get(position).setPriceUnitAmount(new BigDecimal(editText_price.getText().toString()));
                    } catch (NumberFormatException e){
                        mItemList.get(position).setPriceUnitAmount(BigDecimal.ZERO);
                    }

                    notifyDataSetChanged();
                }

                editmode = !editmode;

                editText_itemname.setEnabled(editmode);
                editText_qty.setEnabled(editmode);
                editText_price.setEnabled(editmode);
                btn_update.setText( getContext().getString(editmode? R.string.cart_done : R.string.cart_edit));


                if (mListener != null) {
                    mListener.onLineItemsUpdated( getTotalAmount() );
                }
            }

        });

        if(!editmode){
            editText_itemname.setText(mItemList.get(position).getPriceData().getName());
            editText_qty.setText(String.format(Locale.getDefault(), "%d", mItemList.get(position).getQuantity()));
            editText_price.setText(String.format(Locale.getDefault(), "%.2f", mItemList.get(position).getPriceData().getUnitAmount()));
        }

        editText_itemname.setEnabled(editmode && itemEnabled);
        editText_qty.setEnabled(editmode && itemEnabled);
        editText_price.setEnabled(editmode && itemEnabled);

        btn_update.setEnabled(itemEnabled);
        btn_update.setText( getContext().getString(editmode? R.string.cart_done : R.string.cart_edit));

        return view;
    }

    public void addnewItem(){
        Log.i(TAG, "addnewItem");
        LineItems emptyItem = new LineItems();
        mItemList.add(emptyItem);
        notifyDataSetChanged();
    }

    public ArrayList<LineItems> getmItemList() {
        return mItemList;
    }

    public BigDecimal getTotalAmount() {

        BigDecimal sum = new BigDecimal(0);
        for (LineItems i : mItemList){
            sum = sum.add( i.getPriceData().getUnitAmount().multiply( new BigDecimal(i.getQuantity())));
        }
        Log.i(TAG, "getTotalAmount sum = "+sum);
        return sum;
    }

    public void setItemEnabled(boolean b){
        itemEnabled = b;
        notifyDataSetChanged();
    }

    public interface LineItemsUpdateListener {
        void onLineItemsUpdated(BigDecimal data);
    }

}
