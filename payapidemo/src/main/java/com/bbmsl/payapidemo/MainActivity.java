package com.bbmsl.payapidemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.os.Bundle;

import com.bbmsl.payapidemo.constants.MerchantDetail;
import com.bbmsl.payapidemo.databinding.ActivityMainBinding;
import com.bbmsl.payapidemo.util.PrefUtil;

import org.xmlpull.v1.XmlPullParser;

import java.util.Locale;

import static com.bbmsl.payapidemo.constants.BaseConstants.KEY_USER_ID;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    /** set up the required merchant info.
     * MerchantID and private key must be loaded for calling Pay API
     */
    private void setupMerchant(){

        try {
            XmlPullParser parser = getResources().getXml(R.xml.merchant);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("merchantId")) {
                        MerchantDetail.setMerchantID(parser.getAttributeValue(0));
                    }if (parser.getName().equals("merchantPrivateKey")) {
                        MerchantDetail.setMerchantPrivateKey(parser.getAttributeValue(0));
                    }if (parser.getName().equals("wechatPay_APPID")) {
                        MerchantDetail.setWechatPay_APP_ID(parser.getAttributeValue(0));
                    }
                }
                parser.next();
            }
        } catch (Exception e){
            showXmlParseErrorDialog();
        }

        if (MerchantDetail.notComplete()){
            showXmlParseErrorDialog();
        }

    }

    private void showXmlParseErrorDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_error))
                .setMessage(getString(R.string.error_merchantxml_parse))
                .setNeutralButton(getString(R.string.dialog_ok), null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        // setup a random ID for mock user if empty
        String randomUserId = String.format (Locale.getDefault(),"%d",(int) (Math.random()*10000)) ;
        PrefUtil.setPrefIfEmpty(this, KEY_USER_ID, randomUserId);

        setupMerchant();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}