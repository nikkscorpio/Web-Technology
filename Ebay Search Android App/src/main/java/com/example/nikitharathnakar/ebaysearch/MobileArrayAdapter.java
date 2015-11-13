package com.example.nikitharathnakar.ebaysearch;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;


    public MobileArrayAdapter(Context context, String[] values) {
        super(context, R.layout.list_mobile, values);
        int i =0;

        ArrayList<ResultBean> resultBeanList = Resultlist.resultBeanArr;

        Iterator<ResultBean> resultIter = resultBeanList.iterator();
        String[] valueArr = new String[resultBeanList.size()+1];
        while(resultIter.hasNext()){
           ResultBean rsBean = resultIter.next();
            //Log.e("resultBeanList",rsBean.getTitle().toString());
            valueArr[i++] = rsBean.getTitle().toString();
            Log.e("Value Array",rsBean.getTitle().toString());
        }

        this.context = context;
        this.values = Arrays.copyOf(valueArr, valueArr.length);



    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String imgURL,price,convertedCurrentPrice,freeshipping="";

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_mobile, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        TextView priceView = (TextView) rowView.findViewById(R.id.price);
        ArrayList<ResultBean> resultBeanList = Resultlist.resultBeanArr;
        //Log.e("Value>>",values[position]);

        Iterator<ResultBean> resultIter = resultBeanList.iterator();
        while(resultIter.hasNext()){
            ResultBean rsBean = resultIter.next();
            //Log.e("Iterator title",rsBean.title);
           if(rsBean.getTitle().toString().equals(values[position])){
               imgURL = (null != rsBean.getGalleryURL()|| rsBean.getGalleryURL()!="N/A")?rsBean.getGalleryURL().toString():"";
               if(imgURL.isEmpty()){
                   imgURL = (null != rsBean.getPictureURLSuperSize()|| rsBean.getPictureURLSuperSize()!="N/A")?rsBean.getPictureURLSuperSize().toString():"";
               }
               final String itemTitle = values[position];
               final String imgLink = (null != rsBean.getViewItemURL()|| rsBean.getViewItemURL()!="N/A")?rsBean.getViewItemURL().toString():"#";
               freeshipping = (rsBean.getShippingServiceCost()== null || rsBean.getShippingServiceCost().toString().isEmpty()|| rsBean.getShippingServiceCost().equals("N/A")|| rsBean.getShippingServiceCost().equals("0.0"))?"(FREE Shipping)":"(+$"+rsBean.getShippingServiceCost()+ " for shipping)";
               price = "Price:$"+rsBean.getConvertedCurrentPrice()+freeshipping;

               textView.setText(values[position]);
               textView.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       Intent intent = new Intent();
                       intent.setClass(context.getApplicationContext(), DetailPage.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.putExtra("itemtitle", itemTitle);

                       context.startActivity(intent);
                   }
               });
               imageView.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View v) {
                       Intent intent = new Intent();
                       intent.setAction(Intent.ACTION_VIEW);
                       intent.addCategory(Intent.CATEGORY_BROWSABLE);
                       intent.setData(Uri.parse(imgLink));
                       context.startActivity(intent);
                   }
               });
                //Log.e("encoded uRL",imgURL);

               ImageDownloader task = new ImageDownloader(imageView);
               task.execute(imgURL);

               priceView.setText(price);

                break;
           }
        }


        return rowView;
    }


    public class ImageDownloader extends AsyncTask<String, String, Bitmap> {

        private ImageView imageview;


        public ImageDownloader(ImageView imageView) {
            super();

            this.imageview = imageView;
        }

        protected Bitmap doInBackground(String[] args) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
                imageview.setImageBitmap(image);

            }
        }
    }
}
