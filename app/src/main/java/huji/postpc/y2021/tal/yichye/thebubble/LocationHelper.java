package huji.postpc.y2021.tal.yichye.thebubble;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationHelper {

	public static final String LOCATIONS_FILE_NAME = "locations.json";
	public static final String LAST_LOCATION_FILE_NAME = "last_location.json";

//	public void getLocation(){
//		requestPermissionToForegroundLocation = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//			if (isGranted) {
//				// everything is ok, continue
//			} else {
//				// explain to user
//			}
//		});
//
//
//		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//
//		getLocationButton.setOnClickListener(new View.OnClickListener() {
//			@RequiresApi(api = Build.VERSION_CODES.M)
//			@Override
//			public void onClick(View v) {
//				if (ContextCompat.checkSelfPermission(
//						MainActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
//						PackageManager.PERMISSION_GRANTED) {
//					// TODO: have permission
//
//					// Code for rec
//					fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
//						@Override
//						public void onComplete(@NonNull Task<android.location.Location> task) {
//							// Init location
//							android.location.Location location = task.getResult();
//							if (false){
////							if (location == null) {
////								try {
////									// Init geoCoder
////									Geocoder geocoder = new Geocoder(MainActivity2.this,
////											Locale.getDefault());
//////									// Init address list
////									List<Address> addresses = geocoder.getFromLocation(
////											location.getLatitude(), location.getLongitude(), 1
////									);
//////									latitudeView.setText("Latitude: " + addresses.get(0).getLatitude());
//////									longitudeView.setText("Longitude: " + addresses.get(0).getLongitude());
//////									countryView.setText("County: " + addresses.get(0).getCountryName());
//////									localityView.setText("Locality: " + addresses.get(0).getLocality());
////									latitudeView.setText("Latitude: " + location.getLatitude());
////									longitudeView.setText("Longitude: " + location.getLongitude());
////									// TODO: do something with received location
////								} catch (IOException e) {
////									e.printStackTrace();
////								}
//							} else {
//								// Request location updates
//								LocationRequest locationRequest = createLocationRequest();
//								LocationCallback locationCallback = new LocationCallback() {
//									@Override
//									public void onLocationResult(@NonNull LocationResult locationResult) {
//										super.onLocationResult(locationResult);
//										android.location.Location location1 = locationResult.getLastLocation();
//										latitudeView.setText("Latitude: " + location1.getLatitude());
//										longitudeView.setText("Longitude: " + location1.getLongitude());
//									}
//								};
//								if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//									// TODO: Consider calling
//									//    ActivityCompat#requestPermissions
//									// here to request the missing permissions, and then overriding
//									//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//									//                                          int[] grantResults)
//									// to handle the case where the user grants the permission. See the documentation
//									// for ActivityCompat#requestPermissions for more details.
//									return;
//								}
//								fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//							}
//						}
//					});
//				} else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//					// TODO: show UI to explain the user why this permission is needed
//				} else {
//					requestPermissionToForegroundLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION);
//				}
//			}
//		});
//	}

	public static StorageReference createLocationReference(String userName, String fileName) {
		return FirebaseStorage.getInstance().getReference().child(userName).child("locations_dir").child(fileName);
	}

	public static LocationRequest createLocationRequest() {
		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setInterval(10000);
		locationRequest.setFastestInterval(0);
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		return locationRequest;
	}

	public static void saveLocationToFS(Location location) {
		String userName = "tal01";
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		DocumentReference locationsArray = db.collection("locations").document(userName);
		locationsArray.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot documentSnapshot = task.getResult();
					if (!documentSnapshot.exists()){
						Map<String, Object> dataDocument = new HashMap<>();
						ArrayList<Location> locationArrayList = new ArrayList<>();
						locationArrayList.add(location);
						dataDocument.put("locationArray", locationArrayList);
						locationsArray.set(dataDocument);
					} else {
						locationsArray.update("locationArray", FieldValue.arrayUnion(location)).addOnSuccessListener(new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void unused) {
								Log.i(TAG, "MyLocation data added to FS");

							}
						}).addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								Log.i(TAG, "MyLocation upload data to FS error: " + e.getMessage());
							}
						});
					}

				} else {
					Log.i(TAG, "MyLocation FS error " + task.getException());
				}
			}
		});
	}

//	@SuppressLint("MissingPermission")
//	private void requestNewLocationData() {
//
//		// Initializing LocationRequest
//		// object with appropriate methods
//		LocationRequest mLocationRequest = new LocationRequest();
//		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//		mLocationRequest.setInterval(5);
//		mLocationRequest.setFastestInterval(0);
//		mLocationRequest.setNumUpdates(1);
//
//		// setting LocationRequest
//		// on FusedLocationClient
//		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//		mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//	}
}
