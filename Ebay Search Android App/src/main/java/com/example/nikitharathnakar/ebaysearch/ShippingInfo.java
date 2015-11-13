package com.example.nikitharathnakar.ebaysearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by nikitharathnakar on 4/18/15.
 */
public class ShippingInfo extends Activity{
    public void onCreate(Bundle savedInstanceState) {
        String shippingType, handlingTime, shiptoLocation,expeditedShipping,oneDayShipping,returns="";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_layout);
        Intent i = getIntent();
        String title = i.getStringExtra("itemtitle");
        ArrayList<ResultBean> resultBeanList = Resultlist.resultBeanArr;
        Log.e("Value>>", title);
        TextView shippingTypeView = ((TextView) findViewById(R.id.shippingType));
        TextView handlingTimeView = ((TextView) findViewById(R.id.handlingTime));
        TextView shiptoLocationView = ((TextView) findViewById(R.id.shipToLocations));
        TextView expeditedShippingView = ((TextView) findViewById(R.id.expeditedShipping));
        TextView oneDayShippingView = ((TextView) findViewById(R.id.oneDayShippingAvailable));
        TextView returnsView = ((TextView) findViewById(R.id.returnsAccepted));

        Iterator<ResultBean> resultIter = resultBeanList.iterator();
        while (resultIter.hasNext()) {
            ResultBean rsBean = resultIter.next();
            //Log.e("Iterator title",rsBean.title);
            if (rsBean.getTitle().toString().equals(title)) {

                shippingType = rsBean.getShippingType();
                handlingTime = rsBean.getHandlingTime();
                shiptoLocation = rsBean.getShipToLocations();
                expeditedShipping = rsBean.getExpeditedShipping();
                oneDayShipping = rsBean.getOneDayShippingAvailable();
                returns = rsBean.getReturnsAccepted();


                shippingTypeView.setText(shippingType);
                handlingTimeView.setText(handlingTime+" day(s)");
                shiptoLocationView.setText(shiptoLocation);
                if(expeditedShipping != null && expeditedShipping.equals("true"))
                    expeditedShippingView.setBackgroundDrawable( getResources().getDrawable(R.drawable.ok  ) );
                else
                    expeditedShippingView.setBackgroundDrawable( getResources().getDrawable(R.drawable.wrong  ) );
                if(oneDayShipping != null && oneDayShipping.equals("true"))
                    oneDayShippingView.setBackgroundDrawable( getResources().getDrawable(R.drawable.ok  ) );
                else
                    oneDayShippingView.setBackgroundDrawable( getResources().getDrawable(R.drawable.wrong  ) );
                if(returns != null && returns.equals("true"))
                    returnsView.setBackgroundDrawable( getResources().getDrawable(R.drawable.ok  ) );
                else
                    returnsView.setBackgroundDrawable( getResources().getDrawable(R.drawable.wrong  ) );

                break;
            }
        }
    }
}
