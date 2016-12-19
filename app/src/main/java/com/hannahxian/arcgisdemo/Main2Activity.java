package com.hannahxian.arcgisdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.GeodatabaseFeatureServiceTable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {
    static String MAP_URL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";
    @Bind(R.id.map)
    MapView map;

    GeodatabaseFeatureServiceTable table;
    FeatureLayer featureLayer;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        initLayer();

        map.addLayer(featureLayer);
    }
    private void initLayer(){
        table = new GeodatabaseFeatureServiceTable(MAP_URL,0);
        featureLayer = new FeatureLayer(table);
    }
    class T extends MapOnTouchListener{
        public T(Context context, MapView view) {
            super(context, view);
        }

        @Override
        public boolean onSingleTap(MotionEvent point) {

            return super.onSingleTap(point);
        }
    }
}
