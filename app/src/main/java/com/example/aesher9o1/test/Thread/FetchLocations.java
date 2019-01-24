package com.example.aesher9o1.test.Thread;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;


public  class FetchLocations extends AsyncTask<String, Void, String> {

    private LocationInterface locationInterface;

    private List<LocationModel> locationModels= new ArrayList<>();

    public FetchLocations(LocationInterface locationInterface){
        this.locationInterface = locationInterface;
    }

    @Override
    protected String doInBackground(String... strings) {
        String body="";
        try{
             body = Jsoup.connect(strings[0]).ignoreContentType(true).execute().body();

             JSONObject jsonObject = new JSONObject(body);
             jsonObject = new JSONObject(jsonObject.get("response").toString());

             JSONArray jsonArray = jsonObject.getJSONArray("venues");


             for(int i =0; i<jsonArray.length();i++){

                 String location;
                 String address;

                 JSONObject jsonObject2 = new JSONObject(String.valueOf(jsonArray.get(i)));

                 if(jsonObject2.has("name"))
                    location = jsonObject2.get("name").toString();
                 else
                     location = "unknown";

                 jsonObject2 = new JSONObject(jsonObject2.get("location").toString());

                 if(jsonObject2.has("address"))
                     address = jsonObject2.getString("address");
                 else
                     address = "not available";


                 LocationModel locationModel = new LocationModel(location, address, jsonObject2.getDouble("lat"), jsonObject2.getDouble("lng"));
                 locationModels.add(locationModel);


             }

        }
        catch (Exception e){
            Log.w("Aashishere",e.toString());
        }
        return body;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        locationInterface.processFinished(locationModels);
    }

}
