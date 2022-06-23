package com.bbmsl.payapidemo.ui.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.constants.enums.CardType;
import com.bbmsl.payapidemo.util.UiUtil;

public class TokenizedCardRadioButton extends AppCompatRadioButton {
    private static final String TAG = "TokenizedCardRadioButton";

    private View view;
    private TextView txtCardNum, txtExpDate;
    private ImageView imgCardLogo;

    public TokenizedCardRadioButton(Context context) {
        super(context);
        init(context);
    }

    public TokenizedCardRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TokenizedCardRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_tokenized_cards, null);
        txtCardNum = view.findViewById(R.id.txt_cardNum);
        txtExpDate  = view.findViewById(R.id.txt_expDate);
        imgCardLogo = view.findViewById(R.id.img_cardlogo);
        redrawLayout();
    }

    private void redrawLayout() {
        view.setDrawingCacheEnabled(true);
        view.measure(MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), bitmap), null, null, null);
        view.setDrawingCacheEnabled(false);
    }

   public void setData(String cardNum, String expDate, CardType cardType){
       txtCardNum .setText(cardNum);
       txtExpDate  .setText(expDate);
       imgCardLogo.setImageResource(UiUtil.getCardImageId(cardType));
       redrawLayout();
   }

}
