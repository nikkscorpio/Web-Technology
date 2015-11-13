package com.example.nikitharathnakar.ebaysearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by nikitharathnakar on 4/18/15.
 */
public class SellerInfo extends Activity{
    public void onCreate(Bundle savedInstanceState) {
        String username, feedbackScore, positiveFeedback,feedbackRating,topRated,store="";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_layout);
        Intent i = getIntent();
        String title = i.getStringExtra("itemtitle");
        ArrayList<ResultBean> resultBeanList = Resultlist.resultBeanArr;
        Log.e("Value>>", title);
        TextView usernameview = ((TextView) findViewById(R.id.sellerUserName));
        TextView feedbackscoreView = ((TextView) findViewById(R.id.feedbackScore));
        TextView positiveFeedBackView = ((TextView) findViewById(R.id.positiveFeedbackPercent));
        TextView feedbackratingView = ((TextView) findViewById(R.id.feedbackRatingStar));
        TextView topRatedView = ((TextView) findViewById(R.id.topRatedSeller));
        TextView storeView = ((TextView) findViewById(R.id.sellerStoreName));


        Iterator<ResultBean> resultIter = resultBeanList.iterator();
        while (resultIter.hasNext()) {
            ResultBean rsBean = resultIter.next();
            //Log.e("Iterator title",rsBean.title);
            if (rsBean.getTitle().toString().equals(title)) {
                username = rsBean.getSellerUserName();
                feedbackScore = rsBean.getFeedbackScore();
                positiveFeedback = rsBean.getPositiveFeedbackPercent();
                feedbackRating = rsBean.getFeedbackRatingStar();
                topRated = rsBean.getTopRatedSeller();
                store = rsBean.getSellerStoreName();
                final String storeURL = rsBean.getSellerStoreURL();
                String topratedjpg  = (topRated != null && topRated.equals("true")?"R.drawable.ok":"R.drawable.ok");

                usernameview.setText(username);
                feedbackscoreView.setText(feedbackScore);
                positiveFeedBackView.setText(positiveFeedback);
                feedbackratingView.setText(feedbackRating);
                feedbackscoreView.setText(feedbackScore);
                positiveFeedBackView.setText(positiveFeedback);
                if(topRated != null && topRated.equals("true"))
                    topRatedView.setBackgroundDrawable( getResources().getDrawable(R.drawable.ok  ) );
                else
                    topRatedView.setBackgroundDrawable( getResources().getDrawable(R.drawable.wrong  ) );
                storeView.setText(store);

                storeView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(storeURL));
                        startActivity(intent);
                    }
                });
            break;
            }
        }
    }
}
