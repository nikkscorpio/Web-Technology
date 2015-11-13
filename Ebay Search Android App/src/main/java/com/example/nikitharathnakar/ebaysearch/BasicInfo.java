package com.example.nikitharathnakar.ebaysearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by nikitharathnakar on 4/18/15.
 */
public class BasicInfo extends Activity{
    public void onCreate(Bundle savedInstanceState) {

        String categoryName, condition, buyingFormat="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basicinfo_layout);

        Intent i = getIntent();
        String title = i.getStringExtra("itemtitle");
        ArrayList<ResultBean> resultBeanList = Resultlist.resultBeanArr;
        Log.e("Value>>", title);
        TextView categoryView = ((TextView) findViewById(R.id.categoryName));
        TextView conditionView = ((TextView) findViewById(R.id.conditionDisplayName));
        TextView buyingView = ((TextView) findViewById(R.id.listingType));

        Iterator<ResultBean> resultIter = resultBeanList.iterator();
        while (resultIter.hasNext()) {
            ResultBean rsBean = resultIter.next();
            //Log.e("Iterator title",rsBean.title);
            if (rsBean.getTitle().toString().equals(title)) {
                categoryName = rsBean.getCategoryName();
                condition = rsBean.getConditionDisplayName();
                buyingFormat = rsBean.getListingType();

                categoryView.setText(categoryName);
                conditionView.setText(condition);
                buyingView.setText(buyingFormat);

                break;
            }
        }
    }
}
