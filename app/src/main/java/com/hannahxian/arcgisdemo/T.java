package com.hannahxian.arcgisdemo;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISPopupInfo;
import com.esri.android.map.popup.ArcGISAttachmentsView;
import com.esri.android.map.popup.ArcGISAttributeView;
import com.esri.android.map.popup.Popup;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.popup.PopupInfo;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Broderick on 2016/12/21.
 */

public class T extends MapOnTouchListener{

	FeatureLayer featureLayer;
	MapView map;
	Context context;
	GeodatabaseFeatureTable pointTable;

	public T(Context context, MapView view) {
		super(context, view);
		this.map = view;
		this.context = context;
	}

	public void setFeatureLayer(FeatureLayer featureLayer) {
		this.featureLayer = featureLayer;
	}

	public void setPointTable(GeodatabaseFeatureTable pointTable) {
		this.pointTable = pointTable;
	}

	/**
	 * 单击事件
	 * @param point
	 * @return
	 */
	@Override
	public boolean onSingleTap(MotionEvent point) {
		Point p = map.toMapPoint(point.getX(),point.getY());
		long[] ids = featureLayer.getFeatureIDs((float) p.getX(),(float)p.getY(),100);
		if(ids.length > 0){
			//设置ids 的feature选中
			featureLayer.selectFeatures(ids, true);
			Map<Integer, ArcGISPopupInfo> popMap = featureLayer.getPopupInfos();
			//通过ID 获取选中的所有feature
			List<Feature> selectFeatures  = featureLayer.getSelectedFeatures();
			for(int i=0;i<selectFeatures.size();i++){
				final Feature tempFea = selectFeatures.get(i);

				for(int j=0;j<popMap.size();j++){
					PopupInfo pi = popMap.get(j);
					Popup pp = new Popup(map,pi,tempFea);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

					ArcGISAttributeView atview = new ArcGISAttributeView(context,pp);
					atview.setMinimumWidth(1);
					atview.setGravity(Gravity.CENTER);
					atview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
					lp.gravity = Gravity.CENTER;
					atview.setLayoutParams(lp);

					final ArcGISAttachmentsView agview = new ArcGISAttachmentsView(context,pp);
					agview.setMinimumWidth(1);
					agview.setGravity(Gravity.CENTER);
					agview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
					agview.setLayoutParams(lp);

					LayoutInflater li = LayoutInflater.from(context);
					View v = li.inflate(R.layout.arcgis_popup_view,null);
					LinearLayout lv = (LinearLayout)v.findViewById(R.id.acview);
					lv.addView(atview,lp);
					lv.addView(agview,lp);

					new MaterialDialog.Builder(context)
							.title("属性信息")
							.titleGravity(GravityEnum.CENTER)
							.customView(v,true)
							.positiveText("确定")
							.show();
				}
			}
			//遍历所有的feature
               /* for(int i=0;i<selectFeatures.size();i++){
                    Feature tempFea = selectFeatures.get(i);
                    //获取所有feature中的键值对，即GEO数据中的列与值
                    Map<String, Object> attributes = tempFea.getAttributes();
                    Set<String> keySet = attributes.keySet();
                    if(attributes!=null && attributes.size()>0){
                        //获取相应的值，进行显示
                        String a = attributes.get("属性a")!=null?attributes.get("属性a").toString():"";
                        String b = attributes.get("属性b")!=null?attributes.get("属性b").toString():"";
                        String c = attributes.get("属性c")!=null?attributes.get("属性c").toString():"";
                    }
                }*/
		}

		return super.onSingleTap(point);
	}

	/**
	 * 添加数据 实例为点
	 * @param p
	 */
	private void addPoint(Point p) {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("name", p.toString());
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = simpleDateFormat.format(d);
		attributes.put("time", time);


		try {
			GeodatabaseFeature feature = new GeodatabaseFeature(attributes, p, pointTable);
			long fid = pointTable.addFeature(feature);

			String geoStr = pointTable.getFeature(fid).getGeometry().toString();

			Log.d("TAG", "addfeature: " + geoStr + " fid = " + fid);

		} catch (TableException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更新数据（包括附件）
	 * @param fid
	 * @param point
	 */
	private void updatefeature(long fid,Point point){
		try {
			pointTable.updateFeature(fid,point);
			//如果有attachment
			pointTable.updateAttachment(fid, 3, new File("Tds"), "test", "a", new CallbackListener<Void>() {
				@Override
				public void onCallback(Void aVoid) {

				}

				@Override
				public void onError(Throwable throwable) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除数据
	 * @param fid
	 */
	private void delfeature(long fid){
		try {
			pointTable.deleteFeature(fid);
			pointTable.deleteAttachment(fid, 3, new CallbackListener<Void>() {
				@Override
				public void onCallback(Void aVoid) {

				}

				@Override
				public void onError(Throwable throwable) {

				}
			});
		} catch (TableException e) {
			e.printStackTrace();
		}
	}
}
