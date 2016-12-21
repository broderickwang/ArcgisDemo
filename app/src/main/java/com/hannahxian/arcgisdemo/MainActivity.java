package com.hannahxian.arcgisdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISPopupInfo;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.popup.ArcGISAttachmentsView;
import com.esri.android.map.popup.ArcGISAttributeView;
import com.esri.android.map.popup.Popup;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geodatabase.GeodatabaseFeatureServiceTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.map.popup.PopupInfo;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    static String MAP_URL = "http://219.146.254.74:6080/arcgis/rest/services/ArcGisOL/qdplane/MapServer";
    MapView map = null;
    GraphicsLayer gLayer = null;

    SimpleFillSymbol sfs;
    GeodatabaseFeatureServiceTable table;
    FeatureLayer featureLayer;
    ArcGISFeatureLayer arcGISFeatureLayer;
    T touchListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = (MapView)findViewById(R.id.mapview);

        initLayer();

        gLayer = new GraphicsLayer(GraphicsLayer.RenderingMode.DYNAMIC);
        sfs = new SimpleFillSymbol(Color.BLACK);
        sfs.setOutline(new SimpleLineSymbol(Color.RED, 2));
        sfs.setAlpha(100);

        map.addLayer(gLayer);

        touchListener = new T(this, map);

    }
    private void initLayer(){
//        map.setMapBackground(0xffffff, 0xffffff, 0, 0);

       /*ArcGISTiledMapServiceLayer tms = new ArcGISTiledMapServiceLayer(
                "http://219.146.254.74:6080/arcgis/rest/services/ArcGisOL/qdplane/MapServer");
        map.addLayer(tms);*/

        table = new GeodatabaseFeatureServiceTable(MapOptions.MapType.STREETS.toString(),0);
        table.setSpatialReference(SpatialReference.create(102100));

        table.initialize(
                new CallbackListener<GeodatabaseFeatureServiceTable.Status>() {

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(table.getInitializationError());
                    }

                    @Override
                    public void onCallback(GeodatabaseFeatureServiceTable.Status status) {
                        if (status == GeodatabaseFeatureServiceTable.Status.INITIALIZED) {
                            featureLayer = new FeatureLayer(table);
                            map.addLayer(featureLayer);
                            touchListener.setFeatureLayer(featureLayer);
                            map.setOnTouchListener(touchListener);
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.unpause();
    }

}