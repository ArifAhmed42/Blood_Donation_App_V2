package com.example.arif.blood_donation_app_v2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import 	java.util.ArrayList;

public class DataParser {
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson){

        HashMap<String, String>googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitute = "";
        String reference = "";

        try {
            if (!googlePlaceJson.isNull("name")) {

                placeName = googlePlaceJson.getString("name");
            }

            if (!googlePlaceJson.isNull("vicinity")) {

                vicinity = googlePlaceJson.getString("vicinity");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitute = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name",placeName);
            googlePlacesMap.put("vicinity",vicinity);
            googlePlacesMap.put("lat",latitude);
            googlePlacesMap.put("lng",longitute);
            googlePlacesMap.put("reference",reference);

        }

        catch (JSONException e) {
                e.printStackTrace();
            }

        return googlePlacesMap;

    }

    private List<HashMap<String , String>> getPlaces(JSONArray jsonArray){
        int count = jsonArray.length();
        List<HashMap<String , String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;

        for (int i=0 ; i<count; i++){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return placesList;
    }

    public List<HashMap<String, String>>parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject ;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);

    }

}
