package com.example.aesher9o1.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aesher9o1.test.Thread.FetchLocations;
import com.example.aesher9o1.test.Thread.LocationInterface;
import com.example.aesher9o1.test.Thread.LocationModel;
import com.example.aesher9o1.test.Thread.LocationRecycler;
import com.facebook.login.LoginManager;
import com.foursquare.placepicker.PlacePickerSdk;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback  {


    Switch toggle_view;
    ImageView logout_button;
    EditText searchBox;
    Button searchButton;
    RecyclerView recyclerView;
    LinearLayout mapFragment;
    private GoogleMap mMap;

    boolean loginStatus, isFetched;

    private static final String CONSUMER_KEY = "YOURKEY";
    private static final String CONSUMER_SECRET = "YOURKEY";

    LocationRecycler locationRecycler;

    private MarkerOptions options = new MarkerOptions();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginStatus = getIntent().getBooleanExtra("loginStatus",false);

        PlacePickerSdk.with(new PlacePickerSdk.Builder(this)
                .consumer(CONSUMER_KEY, CONSUMER_SECRET).build());

        toggle_view= findViewById(R.id.toggle_switch);
        logout_button = findViewById(R.id.logout);
        searchBox= findViewById(R.id.search_box);
        searchButton = findViewById(R.id.search_button);
        mapFragment = findViewById(R.id.map_fragment);

        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert map != null;
        map.getMapAsync(this);


        isFetched = false;
        toggle_view.setClickable(false);

    if(loginStatus)
        Toast.makeText(this,"Use the toggle button to toggle between map and list view", Toast.LENGTH_LONG).show();


        toggle_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isFetched){
                    if (isChecked){
                        recyclerView.setVisibility(View.GONE);
                        mapFragment.setVisibility(View.VISIBLE);
                    }
                    else{
                        recyclerView.setVisibility(View.VISIBLE);
                        mapFragment.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please search for a place to use toggle", Toast.LENGTH_SHORT).show();
                }

            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               new FetchLocations(new LocationInterface() {
                   @Override
                   public void processFinished(List<LocationModel> s) {

                       toggle_view.setClickable(true);
                       isFetched = true;
                       recyclerView = findViewById(R.id.recyclerView);
                       recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                       recyclerView.setNestedScrollingEnabled(false);
                       locationRecycler = new LocationRecycler(s);
                       recyclerView.setAdapter(locationRecycler);

                    mMap.clear();
                    //add markers
                    for(int i=0;i<s.size();i++){
                        options.position(new LatLng(s.get(i).getLat(), s.get(i).getLng()));
                        options.title(s.get(i).getAddress());
                        mMap.addMarker(options);
                    }
                        //zoom to location
                       CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(s.get(0).getLat(), s.get(0).getLng()));
                       CameraUpdate zoom=CameraUpdateFactory.zoomTo(11);
                       mMap.moveCamera(center);
                       mMap.animateCamera(zoom);

                   }

                }).execute(createRequestUrl(searchBox.getText().toString()));

            }
        });

    }



    public String createRequestUrl(String param){
        return "https://api.foursquare.com/v2/venues/search?near="+param+"&query=petrolpump&client_id="+ CONSUMER_KEY+"&client_secret="+CONSUMER_SECRET+"&v=20191212";
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
