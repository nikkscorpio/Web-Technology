package com.example.nikitharathnakar.ebaysearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;




public class DetailPage extends TabActivity {
    CallbackManager callbackManager;

    static DetailPage detailpage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        detailpage = this;


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailpage);
        RelativeLayout view = (RelativeLayout)findViewById(R.id.detailist);
        view.setBackgroundColor(Color.WHITE);
        Intent i = getIntent();
        int j = 0;
        String title = i.getStringExtra("itemtitle");
        ImageView mainImg = (ImageView) findViewById(R.id.mainimg);
        TextView itemTitle = ((TextView) findViewById(R.id.itemtitle));
        TextView itemPrice = ((TextView) findViewById(R.id.itemprice));
        TextView itemLocation = ((TextView) findViewById(R.id.itemlocation));
        ImageView topratedImg = (ImageView) findViewById(R.id.topratedimg);
        Button buyNowBtn = ((Button) findViewById(R.id.buynowbtn));
        //ImageView fbBtn = (ImageView) findViewById(R.id.fb);

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        ImageView shareButton = (ImageView)findViewById(R.id.fb_share_button);










        itemTitle.setText(title);
        ArrayList<ResultBean> resultBeanList = Resultlist.resultBeanArr;
        Log.e("Value>>", title);

        Iterator<ResultBean> resultIter = resultBeanList.iterator();
        while (resultIter.hasNext()) {
            ResultBean rsBean = resultIter.next();
            //Log.e("Iterator title",rsBean.title);
            if (rsBean.getTitle().toString().equals(title)) {
              String imgURL ="";


                imgURL = (null != rsBean.getPictureURLSuperSize() || rsBean.getPictureURLSuperSize() != "N/A") ? rsBean.getPictureURLSuperSize().toString() : "";

                if (imgURL.isEmpty()) {
                    imgURL = (null != rsBean.getGalleryURL() || rsBean.getGalleryURL() != "N/A") ? rsBean.getGalleryURL().toString() : "";
                }
                final String itemtTitlefb = rsBean.getTitle().toString();
                final String imgLink = (null != rsBean.getViewItemURL() || rsBean.getViewItemURL() != "N/A") ? rsBean.getViewItemURL().toString() : "#";
                final String freeshipping = (rsBean.getShippingServiceCost() == null || rsBean.getShippingServiceCost().toString().isEmpty() || rsBean.getShippingServiceCost().equals("N/A") || rsBean.getShippingServiceCost().equals("0.0")) ? "(FREE Shipping)" : "(+$" + rsBean.getShippingServiceCost() + " for shipping)";
                final String price = "Price:$" + rsBean.getConvertedCurrentPrice() + freeshipping;
                final String location = rsBean.getLocation();
                final String image = imgURL;
                if ((rsBean.getTopRatedListing().toString().equals("true")))
                    topratedImg.setVisibility(View.VISIBLE);
                else
                    topratedImg.setVisibility(View.INVISIBLE);

                itemTitle.setText(title);
                ImageDownloader task = new ImageDownloader(mainImg);
                task.execute(imgURL);
                itemPrice.setText(price);
                itemLocation.setText(location);
//
                buyNowBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(imgLink));
                        startActivity(intent);
                    }
                });
                shareButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        callbackManager = CallbackManager.Factory.create();
                        ShareDialog shareDialog = new ShareDialog(detailpage);
                        shareDialog.registerCallback(callbackManager,new FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {

                                if(null != result.getPostId())
                                    Toast.makeText(getApplicationContext(),"Posted Story , ID: "+result.getPostId(), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(),"Post Cancelled", Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onCancel() {
                                Toast.makeText(getApplicationContext(),"Post Cancelled", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException e) {

                            }
                        });
                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                    .setContentTitle(itemtTitlefb)
                                    .setContentDescription(
                                            price+" Location: \n"+location)
                                    .setContentUrl(Uri.parse(imgLink))
                                    .setImageUrl(Uri.parse(image))
                                    .build();

                            shareDialog.show(linkContent, ShareDialog.Mode.FEED);


                        }
                    }

                });




                //////

                TabHost tabHost = getTabHost();


                TabHost.TabSpec basicInfo = tabHost.newTabSpec("Basic Info");
                // setting Title and Icon for the Tab
                basicInfo.setIndicator("", getResources().getDrawable(R.drawable.basicinfo));
                Intent basicIntent = new Intent(this, BasicInfo.class);
                basicIntent.putExtra("itemtitle",title);
                basicInfo.setContent(basicIntent);


                TabHost.TabSpec sellerSpec = tabHost.newTabSpec("Seller");
                sellerSpec.setIndicator("", getResources().getDrawable(R.drawable.seller));
                Intent sellerIntent = new Intent(this, SellerInfo.class);
                sellerIntent.putExtra("itemtitle",title);
                sellerSpec.setContent(sellerIntent);


                TabHost.TabSpec shippingSpec = tabHost.newTabSpec("Shipping");
                shippingSpec.setIndicator("", getResources().getDrawable(R.drawable.shipping));
                Intent shippingIntent = new Intent(this, ShippingInfo.class);
                shippingIntent.putExtra("itemtitle",title);
                shippingSpec.setContent(shippingIntent);

                // Adding all TabSpec to TabHost
                tabHost.addTab(basicInfo);
                tabHost.addTab(sellerSpec);
                tabHost.addTab(shippingSpec);
                break;
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailpage, menu);
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
        @Override
        protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);


        }


    public class ImageDownloader extends AsyncTask<String, String, Bitmap> {

        private ImageView imageview;


        public ImageDownloader(ImageView imageView) {
            super();

            this.imageview = imageView;
        }

        protected Bitmap doInBackground(String[] args) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap image) {
            if (image != null) {
                imageview.setImageBitmap(image);

            }
        }
    }



    }

