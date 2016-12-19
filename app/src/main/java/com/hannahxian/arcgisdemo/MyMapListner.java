package com.hannahxian.arcgisdemo;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;


/**
 * Created by hannahxian on 2016/12/19.
 */

public class MyMapListner extends MapOnTouchListener {
    private  Context mContext;
    private ArcGISFeatureLayer mFeatureLayer;
    public MyMapListner(Context context, MapView mapView) {
        super(context, mapView);
        this.mContext = context;
    }

    public void setmFeatureLayer(ArcGISFeatureLayer mFeatureLayer) {
        this.mFeatureLayer = mFeatureLayer;
    }

    @Override
    public boolean onSingleTap(MotionEvent point) {
        return super.onSingleTap(point);
    }
}
