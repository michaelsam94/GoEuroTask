package PlaceResponse;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class GeoPosition {

@SerializedName("latitude")
@Expose
private double latitude;
@SerializedName("longitude")
@Expose
private double longitude;

/**
*
* @return
* The latitude
*/
public double getLatitude() {
return latitude;
}

/**
*
* @param latitude
* The latitude
*/
public void setLatitude(double latitude) {
this.latitude = latitude;
}

/**
*
* @return
* The longitude
*/
public double getLongitude() {
return longitude;
}

/**
*
* @param longitude
* The longitude
*/
public void setLongitude(double longitude) {
this.longitude = longitude;
}

}
