package com.example.vlad01.excersize2;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.vlad01.excersize2.places.PlacesContent;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//network stuff should be run in background
public   class GetJson extends AsyncTask<String,Void,Object>
{
    String role = "";

    FragmentActivity activity = null;

    public GetJson(FragmentActivity mcontext)
    {
        super();
        activity = mcontext;
    }

    @Override
    protected Object doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        // Connect to the URL using java's native library
        try {
            String sURL = params[0];
            //role = params[1];
            URL url = new URL(sURL);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            //we4 don't want to be hanging here all the time
            InputStream is = urlConnection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(is);

            return getDataFromStream(in);
        }
        catch (Exception ex)
        {


            return ex;
        }
        finally
        {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        try
        {
            onPostExecuteImpl(o);
        }
        catch(MalformedURLException ex)
        {
            //TODO some meaningful exception handling here
            Toast.makeText(activity, "Sorry! The URL is malformed", Toast.LENGTH_SHORT).show();
            return ;
        }
        catch (java.io.IOException ex)
        {
            //TODO some meaningful exception handling here
            Toast.makeText(activity, "Sorry! IOException ", Toast.LENGTH_SHORT).show();
            return;
        }
        catch (Exception ex)
        {
            //TODO some meaningful exception handling here
            Toast.makeText(activity, "Sorry! Unknown Exception ", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    protected  Object getDataFromStream(BufferedInputStream in)
    {
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader(in)); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
        return rootobj;
    }

    protected  void onPostExecuteImpl(Object o) throws Exception
    {
        JsonObject result;
        if (o instanceof JsonObject)
            result = (JsonObject) o;
        else if (o instanceof Exception)
            throw (Exception) o;
        else {
            result = null;
        }
        handleJsonResult(result, role, activity);
    }

    protected   void handleJsonResult(JsonObject json, String role, FragmentActivity activity )
    {

        if (json == null) {

            return; //poshowAlertssible we should log an error
        }
        JsonArray results = json.get("results").getAsJsonArray();
        PlacesContent.clearItems();
        if (results.size() < 1)
        {
            ItemListActivity.showAlert("No restaurants found", activity);
        }
        for (JsonElement result : results)
        {
            JsonObject myjson = result.getAsJsonObject();
            String name = processJsonValue(myjson, "name");//myjson.get("name").getAsString();
            String streetAddress = processJsonValue(myjson, "vicinity");//myjson.get("vicinity").getAsString();
            String detailId = processJsonValue(myjson, "place_id");//myjson.get("place_id").getAsString();
            String icon = processJsonValue(myjson, "icon");//myjson.get("icon").getAsString();
            String priceLevel =processJsonValue(myjson, "price_level");//myjson.get("price_level").getAsString();
            String rating =processJsonValue(myjson, "rating");//myjson.get("price_level").getAsString();
            JsonElement jel = myjson.get("opening_hours");
            String openingHours = "";
            String photo = "";
            if (jel != null) {
                JsonObject el = jel.getAsJsonObject();
                if (el != null)
                    openingHours = processJsonValue(el, "open_now");//myjson.get("price_level").getAsString();
            }
            JsonElement fel = myjson.get("photos");
            if (fel != null)
            {
                JsonArray el = fel.getAsJsonArray();
                if (el != null)
                {
                    if (el.size() > 0)
                    {
                        JsonElement pic = el.get(0);
                        if (pic != null)
                        {
                            JsonObject opic = pic.getAsJsonObject();
                            if (opic != null)
                            {
                                photo = opic.get("photo_reference").getAsString();
                            }
                        }
                    }
                }
            }
            PlacesContent.PlaceItem placeItem = new PlacesContent.PlaceItem(detailId,name,streetAddress,icon, priceLevel, rating, openingHours, photo);
            PlacesContent.addItem(placeItem);
        }
        View recyclerView = activity.findViewById(R.id.item_list);

        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, activity);
    }

    private  String processJsonValue(JsonObject vjson, String vakey)
    {
        JsonElement mjson = vjson.get(vakey);
        String result = "";
        if (mjson != null)
            result = mjson.getAsString();
        return result;
    }

    private    void setupRecyclerView(@NonNull RecyclerView recyclerView, FragmentActivity activity) {
        SimpleItemRecyclerViewAdapter adpt = new SimpleItemRecyclerViewAdapter(PlacesContent.ITEMS, activity);
        recyclerView.setAdapter(adpt);
    }



}

