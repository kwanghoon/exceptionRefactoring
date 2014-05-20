package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhn.android.maps.ExceptionNMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MapActivity extends ExceptionNMapActivity {

	private static final String LOG_TAG = "MapViewer";
	private static final boolean DEBUG = false;
	
	public static final String API_KEY = "9ad9aff856ee03f566883c21e498d45a";
	
	NMapView mMapView = null;
	LinearLayout MapContainer;
	
	//private MapContainerView mMapContainerView;
	NMapController mMapController = null;
	
	// ��������
	private NMapViewerResourceProvider mMapViewerResourceProvider = null;
	private NMapOverlayManager mOverlayManager;
	
	// ��ġ 
	private NMapMyLocationOverlay mMyLocationOverlay;
	private NMapLocationManager mMapLocationManager;
	private NMapCompassManager mMapCompassManager;
	
	@Override
	public void OnCreate(Bundle savedInstanceState) throws Throwable {
		super.OnCreate(savedInstanceState);
		MapContainer = (LinearLayout) findViewById(R.id.map);

		// create map view
		mMapView = new NMapView(this);

		// set a registered API key for Open MapViewer Library
		mMapView.setApiKey(API_KEY);

		// set the activity content to the map view
		setContentView(mMapView);
		
		// initialize map view
		mMapView.setClickable(true);

		// register listener for map state changes
		mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
		mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

		// use built in zoom controls
		mMapView.setBuiltInZoomControls(true, null);

		// use map controller to zoom in/out, pan and set map center, zoom level etc.
		mMapController = mMapView.getMapController();

		// create resource provider
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// create overlay manager
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

		
		//////////////////////////////////////////////////////////
		// POI ��ü ���� ����
		int markerId = NMapPOIflagType.PIN;

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(4, mMapViewerResourceProvider);
		poiData.beginPOIdata(4);
		poiData.addPOIitem(127.9026180, 37.2770646, "Grazie", markerId, 0);
		poiData.addPOIitem(127.9011700, 37.2810600, "�ѿ��", markerId, 0);
		poiData.addPOIitem(127.9112564, 37.2801086, "ī�亣��", markerId, 0);
		poiData.addPOIitem(127.9063533, 37.2730469, "Mazi", markerId, 0);
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		// show all POI data
		//poiDataOverlay.showAllPOIdata(0);

		// set event listener to the overlay
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
		// POI ��ü ���� ��
		//////////////////////////////////////////////////////////

		
		// register callout overlay listener to customize it.
		mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
		
		// location manager
		mMapLocationManager = new NMapLocationManager(this);
		mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

		// compass manager
		mMapCompassManager = new NMapCompassManager(this);

		// create my location overlay
		mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
	}
	
	/* ���� ��ġ ã�� �Լ� */
	private void startMyLocation() {

		if (mMyLocationOverlay != null) {
			if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
				mOverlayManager.addOverlay(mMyLocationOverlay);
			}

			if (mMapLocationManager.isMyLocationEnabled()) {

				if (!mMapView.isAutoRotateEnabled()) {
					mMyLocationOverlay.setCompassHeadingVisible(true);

					mMapCompassManager.enableCompass();

					mMapView.setAutoRotateEnabled(true, false);

					//mMapContainerView.requestLayout();
				} else {
					stopMyLocation();
				}

				mMapView.postInvalidate();
			} else {
				boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
				if (!isMyLocationEnabled) {
					Toast.makeText(MapActivity.this, "Please enable a My Location source in system settings",
						Toast.LENGTH_LONG).show();

					Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					StartActivity(goToSettings);

					return;
				}
			}
		}
	}

	/* ���� ��ġ ã�� �ߴ��ϴ� �Լ� */
	private void stopMyLocation() {
		if (mMyLocationOverlay != null) {
			mMapLocationManager.disableMyLocation();

			if (mMapView.isAutoRotateEnabled()) {
				mMyLocationOverlay.setCompassHeadingVisible(false);

				mMapCompassManager.disableCompass();

				mMapView.setAutoRotateEnabled(false, false);

				//mMapContainerView.requestLayout();
			}
		}
	}

	//////////////////////////////////////////////////////////
	// ������� �Ʒ��� ������ ����
	//////////////////////////////////////////////////////////
	
	/* MapView State Change Listener*/
	// ���� ���� ���� �� ȣ��Ǵ� �ݹ� �������̽��� �����Ѵ�
	private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

		@Override
		public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

			if (errorInfo == null) { // success
				startMyLocation(); // ó�� ���� ���� ��, ������ġ�� �̵�
				
				//mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11); // �����û
			} else { // fail
				Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());

				Toast.makeText(MapActivity.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
			}
		}

		@Override
		public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
			}
		}

		@Override
		public void onZoomLevelChange(NMapView mapView, int level) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
			}
		}

		@Override
		public void onMapCenterChangeFine(NMapView mapView) {

		}
	};
	
	/* POI data State Change Listener*/
	// POI �������� ���� ���� ���� �� ȣ��Ǵ� �ݹ� �������̽��� �����Ѵ�
	private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

		@Override
		// POI �������� ��ǳ���� ���õǸ� ȣ��ȴ�
		public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
			}

			// [[TEMP]] handle a click event of the callout
			//Toast.makeText(MapActivity.this, item.getTitle() + "(��)�� �̵��մϴ�", Toast.LENGTH_LONG).show();
        	
        	Intent i = new Intent();
        	ComponentName comp = new ComponentName("com.example.cafe", "com.example.cafe.CafeActivity");
        	i.setComponent(comp);
        	i.putExtra("cafename", item.getTitle());
        	StartActivity(i);
		}

		@Override
		public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				if (item != null) {
					Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
				} else {
					Log.i(LOG_TAG, "onFocusChanged: ");
				}
			}
		}
	};

	/* MyLocation Listener */
	// �ܸ����� ���� ��ġ ���� ���� �� ȣ��Ǵ� �ݹ� �������̽��� �����Ѵ�
	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

			if (mMapController != null) {
				mMapController.animateTo(myLocation);
			}

			return true;
		}

		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

			// stop location updating
			//			Runnable runnable = new Runnable() {
			//				public void run() {										
			//					stopMyLocation();
			//				}
			//			};
			//			runnable.run();	

			Toast.makeText(MapActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

			Toast.makeText(MapActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

			stopMyLocation();
		}
	};

	/* Touch Event Listener */
	// �������� ��ġ �̺�Ʈ �߻� �� ȣ��Ǵ� �ݹ� �������̽��� �����Ѵ�
	private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

		@Override
		public void onLongPress(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLongPressCanceled(NMapView mapView) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTouchDown(NMapView mapView, MotionEvent ev) {

		}

		@Override
		public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
		}

		@Override
		public void onTouchUp(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}
	};
	
	/* CalloutOverlay Listener */
	// ��ǳ�� �������� ��ü ���� �� ȣ��Ǵ� �ݹ� �������̽��� �����Ѵ�
	private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

		@Override
		public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
			Rect itemBounds) {

			// handle overlapped items
			if (itemOverlay instanceof NMapPOIdataOverlay) {
				NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

				// check if it is selected by touch event
				if (!poiDataOverlay.isFocusedBySelectItem()) {
					int countOfOverlappedItems = 1;

					NMapPOIdata poiData = poiDataOverlay.getPOIdata();
					for (int i = 0; i < poiData.count(); i++) {
						NMapPOIitem poiItem = poiData.getPOIitem(i);

						// skip selected item
						if (poiItem == overlayItem) {
							continue;
						}

						// check if overlapped or not
						if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
							countOfOverlappedItems++;
						}
					}

					if (countOfOverlappedItems > 1) {
						String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
						Toast.makeText(MapActivity.this, text, Toast.LENGTH_LONG).show();
						return null;
					}
				}
			}

			// use custom old callout overlay
			if (overlayItem instanceof NMapPOIitem) {
				NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

				if (poiItem.showRightButton()) {
					return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
						mMapViewerResourceProvider);
				}
			}

			// use custom callout overlay
			return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

			// set basic callout overlay
			//return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);			
		}
	};

}
