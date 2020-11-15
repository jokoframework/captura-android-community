package py.com.sodep.mobileforms.location;

import static android.content.Context.LOCATION_SERVICE;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MFLocationManager implements LocationListener {

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	private LocationManager mgr;

	private Location bestLocation;

	private Handler handler;

	private int intendedAccuracy = 50;

	private int maxUpdates = 10;

	private int updates = 0;

	public static final int MF_LOCATION_ACQUIRED = 1;
	
	public static final int MF_LOCATION_ACQUIRED_NOT_ACCURATE = 2;

	public static final int MF_LOCATION_GPS_NOT_ENABLED = 3;

	public static final int MF_LOCATION_GPS_DISABLED = 4;
	
	public static final int MF_LOCATION_FINISHED = 5;

	public MFLocationManager(Context context){
		mgr = (LocationManager) context.getSystemService(LOCATION_SERVICE);
	}
	
	public MFLocationManager(Context context, Handler handler) {
		mgr = (LocationManager) context.getSystemService(LOCATION_SERVICE);
		this.handler = handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public void calculate(int intendedAccuracy) {
		this.intendedAccuracy = intendedAccuracy;
		calculate();
	}

	public void calculate() {
//		Criteria criteria = new Criteria();
//		String best = mgr.getBestProvider(criteria, true);
//		Location lastKnownLocation = mgr.getLastKnownLocation(best);
//		if (lastKnownLocation != null) {
//			Message msg = getMessage(MF_LOCATION_ACQUIRED, lastKnownLocation);
//			handler.sendMessage(msg);
//		}
		mgr.removeUpdates(this);
		mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 20, this);
		updates = 0;
		if (mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 20, this);
		} else {
			if (handler != null) {
				handler.sendEmptyMessage(MF_LOCATION_GPS_NOT_ENABLED);
			}
		}
	}
	
	private boolean finished;
	
	public void finish() {
		finished = true;
		mgr.removeUpdates(this);
		updates = 0;
		if (handler != null) {
			handler.sendEmptyMessage(MF_LOCATION_FINISHED);
		}
	}
	
	public boolean isFinished(){
		return finished;
	}

	@Override
	public void onLocationChanged(Location location) {
		updates++;
		if (this.isBetterLocation(location, bestLocation)) {
			this.bestLocation = location;
			if (this.bestLocation.getAccuracy() < intendedAccuracy) {
				if (handler != null) {
					Message msg = getMessage(MF_LOCATION_ACQUIRED, location);
					handler.sendMessage(msg);
					finish();
				}
			} else {
				if (handler != null) {
					Message msg = getMessage(MF_LOCATION_ACQUIRED_NOT_ACCURATE, location);
					handler.sendMessage(msg);
				}
			}
		}
		
		if (updates > maxUpdates) {
			finish();
		}
	}

	private Message getMessage(int what, Location location) {
		Message msg = new Message();
		Bundle data = getBundle(location);
		msg.setData(data);
		msg.what = what;
		return msg;
	}

	private Bundle getBundle(Location location) {
		Bundle data = new Bundle();
		data.putDouble("latitude", location.getLatitude());
		data.putDouble("longitude", location.getLongitude());
		data.putDouble("altitude", location.getAltitude());
		data.putDouble("accuracy", location.getAccuracy());
		return data;
	}

	@Override
	public void onProviderDisabled(String provider) {
		if (provider.equals(LocationManager.GPS_PROVIDER)) {
			if (handler != null) {
				handler.sendEmptyMessage(MF_LOCATION_GPS_DISABLED);
			}
		}
	}

	// This was empty
	@Override
	public void onProviderEnabled(String provider) {

	}

	// This was empty
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	// Code extracted from
	// http://developer.android.com/training/basics/location/currentlocation.html
	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	public boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public Location getBestLocation() {
		if(bestLocation == null){
			Criteria criteria = new Criteria();
			String best = mgr.getBestProvider(criteria, true);
			return mgr.getLastKnownLocation(best);
		}
		return bestLocation;
	}
}
