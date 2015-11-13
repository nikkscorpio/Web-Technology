package com.example.nikitharathnakar.ebaysearch;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Resultlist extends ListActivity {
    private TextView text;
    private List<String> listValues;


    public static ArrayList<ResultBean> resultBeanArr;
    public static ArrayList<String> itemIDS;

    public Resultlist() {
        resultBeanArr = new ArrayList<ResultBean>();
        itemIDS = new ArrayList<String>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String keyword = Search.keywordText;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultlist);
        RelativeLayout view = (RelativeLayout)findViewById(R.id.resultlist);
        view.setBackgroundColor(Color.WHITE);
        Intent i = getIntent();
        int j = 0 ;
        String jsonResult = i.getStringExtra("jsonResult");
        // Log.v("ONCREATERESULTACTIVITY",jsonResult);
        Boolean success = retrieveJSON(jsonResult);

        TextView resulttext = ((TextView)findViewById(R.id.resulttext));
        resulttext.setText("Results for '"+keyword+"'");
        String[] simpleArr = itemIDS.toArray(new String[0]);
        setListAdapter(new MobileArrayAdapter(this, simpleArr));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Boolean retrieveJSON(String jsonArray) {

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Boolean success = false;
        try {
            String acknowledge = jsonObj.get("ack").toString();

            if (acknowledge.equals("Success")) {
                Double resultCount = Double.parseDouble(jsonObj.get("resultCount").toString());
                for (int i = 0; i < 5 && i < resultCount; i++) {
                    ResultBean resultbean = new ResultBean();
                    String itemID = "item" + i;
                    itemIDS.add(itemID);
                    JSONObject itemArr = jsonObj.getJSONObject(itemID);
                    resultbean.setItemID(itemID);
                    JSONObject basicInfoArr = itemArr.getJSONObject("basicinfo");
                    resultbean.setTitle((null != basicInfoArr.get("title") && !basicInfoArr.get("title").toString().isEmpty()) ? basicInfoArr.get("title").toString() : "N/A");
                    resultbean.setViewItemURL((null != basicInfoArr.get("viewItemURL") && !basicInfoArr.get("viewItemURL").toString().isEmpty()) ? basicInfoArr.get("viewItemURL").toString() : "N/A");
                    resultbean.setGalleryURL((null != basicInfoArr.get("galleryURL") && !basicInfoArr.get("galleryURL").toString().isEmpty()) ? basicInfoArr.get("galleryURL").toString() : "N/A");
                    resultbean.setPictureURLSuperSize((null != basicInfoArr.get("pictureURLSuperSize") && !basicInfoArr.get("pictureURLSuperSize").toString().isEmpty()) ? basicInfoArr.get("pictureURLSuperSize").toString() : "N/A");
                    resultbean.setConvertedCurrentPrice((null != basicInfoArr.get("convertedCurrentPrice") && !basicInfoArr.get("convertedCurrentPrice").toString().isEmpty()) ? basicInfoArr.get("convertedCurrentPrice").toString() : "N/A");
                    resultbean.setShippingServiceCost((null != basicInfoArr.get("shippingServiceCost") && !basicInfoArr.get("shippingServiceCost").toString().isEmpty()) ? basicInfoArr.get("shippingServiceCost").toString() : "N/A");
                    resultbean.setConditionDisplayName((null != basicInfoArr.get("conditionDisplayName") && !basicInfoArr.get("conditionDisplayName").toString().isEmpty()) ? basicInfoArr.get("conditionDisplayName").toString() : "N/A");
                    resultbean.setListingType((null != basicInfoArr.get("listingType") && !basicInfoArr.get("listingType").toString().isEmpty()) ? basicInfoArr.get("listingType").toString() : "N/A");
                    resultbean.setLocation((null != basicInfoArr.get("location") && !basicInfoArr.get("location").toString().isEmpty()) ? basicInfoArr.get("location").toString() : "N/A");
                    resultbean.setCategoryName((null != basicInfoArr.get("categoryName") && !basicInfoArr.get("categoryName").toString().isEmpty()) ? basicInfoArr.get("categoryName").toString() : "N/A");
                    resultbean.setTopRatedListing((null != basicInfoArr.get("topRatedListing") && !basicInfoArr.get("topRatedListing").toString().isEmpty()) ? basicInfoArr.get("topRatedListing").toString() : "N/A");

                    JSONObject sellerInfoArr = itemArr.getJSONObject("sellerInfo");
                    resultbean.setSellerUserName((null != sellerInfoArr.get("sellerUserName") && !sellerInfoArr.get("sellerUserName").toString().isEmpty()) ? sellerInfoArr.get("sellerUserName").toString() : "N/A");
                    resultbean.setFeedbackScore((null != sellerInfoArr.get("feedbackScore") && !sellerInfoArr.get("feedbackScore").toString().isEmpty()) ? sellerInfoArr.get("feedbackScore").toString() : "N/A");
                    resultbean.setPositiveFeedbackPercent((null != sellerInfoArr.get("positiveFeedbackPercent") && !sellerInfoArr.get("positiveFeedbackPercent").toString().isEmpty()) ? sellerInfoArr.get("positiveFeedbackPercent").toString() : "N/A");
                    resultbean.setFeedbackRatingStar((null != sellerInfoArr.get("feedbackRatingStar") && !sellerInfoArr.get("feedbackRatingStar").toString().isEmpty()) ? sellerInfoArr.get("feedbackRatingStar").toString() : "N/A");
                    resultbean.setTopRatedSeller((null != sellerInfoArr.get("topRatedSeller") && !sellerInfoArr.get("topRatedSeller").toString().isEmpty()) ? sellerInfoArr.get("topRatedSeller").toString() : "N/A");
                    resultbean.setSellerStoreName((null != sellerInfoArr.get("sellerStoreName") && !sellerInfoArr.get("sellerStoreName").toString().isEmpty()) ? sellerInfoArr.get("sellerStoreName").toString() : "N/A");
                    resultbean.setSellerStoreURL((null != sellerInfoArr.get("sellerStoreURL") && !sellerInfoArr.get("sellerStoreURL").toString().isEmpty()) ? sellerInfoArr.get("sellerStoreURL").toString() : "N/A");


                    JSONObject shippingInfoArr = itemArr.getJSONObject("shippingInfo");
                    resultbean.setShippingType((null != shippingInfoArr.get("shippingType") && !shippingInfoArr.get("shippingType").toString().isEmpty()) ? shippingInfoArr.get("shippingType").toString() : "N/A");
                    resultbean.setShipToLocations((null != shippingInfoArr.get("shipToLocations") && !shippingInfoArr.get("shipToLocations").toString().isEmpty()) ? shippingInfoArr.get("shipToLocations").toString() : "N/A");
                    resultbean.setExpeditedShipping((null != shippingInfoArr.get("expeditedShipping") && !shippingInfoArr.get("expeditedShipping").toString().isEmpty()) ? shippingInfoArr.get("expeditedShipping").toString() : "N/A");
                    resultbean.setOneDayShippingAvailable((null != shippingInfoArr.get("oneDayShippingAvailable") && !shippingInfoArr.get("oneDayShippingAvailable").toString().isEmpty()) ? shippingInfoArr.get("oneDayShippingAvailable").toString() : "N/A");
                    resultbean.setReturnsAccepted((null != shippingInfoArr.get("returnsAccepted") && !shippingInfoArr.get("returnsAccepted").toString().isEmpty()) ? shippingInfoArr.get("returnsAccepted").toString() : "N/A");
                    resultbean.setHandlingTime((null != shippingInfoArr.get("handlingTime") && !shippingInfoArr.get("handlingTime").toString().isEmpty()) ? shippingInfoArr.get("handlingTime").toString() : "N/A");

                    resultBeanArr.add(resultbean);

                }

                success=true;
            }
            else{
                success = false;
            }
        }
        catch (Exception e){
            e.printStackTrace();


        }

        return success;
    }

}
