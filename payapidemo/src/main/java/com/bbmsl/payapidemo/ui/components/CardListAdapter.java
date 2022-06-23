package com.bbmsl.payapidemo.ui.components;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.network.model.dataobject.TokenizationToken;
import com.bbmsl.payapidemo.util.UiUtil;

import java.util.ArrayList;


public class CardListAdapter extends ArrayAdapter<TokenizationToken> {
        private static final String TAG = "LineItemAdapter";

        private final Context mContext;
        private final int layoutId;

        public CardListAdapter(Context context, int resource, ArrayList<TokenizationToken> itemList){
            super(context, resource, itemList);
            mContext = context;
            layoutId = resource;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(layoutId, null);
            }
            Log.i(TAG, "getView ="+position);
            TextView txtCardNum =  view.findViewById(R.id.txt_cardNum);
            TextView txtExpDate =  view.findViewById(R.id.txt_expDate);
            ImageView imgCardLogo =  view.findViewById(R.id.img_cardlogo);

            txtCardNum.setText(getItem(position).getMaskedPan());
            txtExpDate.setText(getItem(position).getFormattedExpiryDate());
            imgCardLogo.setImageResource(UiUtil.getCardImageId(getItem(position).getCardBrand()));

            return view;
        }


}
