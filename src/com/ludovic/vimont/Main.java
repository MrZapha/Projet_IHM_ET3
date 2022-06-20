package com.ludovic.vimont;

public class Main {
	public static void main(String[] args) {
		final String locatioName = "富士山";
		final Location location = new Location(locatioName, 3.306422, 138.7186086);
		System.out.println(location);
		final String locationGeohash = GeoHashHelper.getGeohash(location);
		System.out.println(locatioName + ": " + locationGeohash);

		final String encodedGeoHash = "jc";
		final Location decodedLocation = GeoHashHelper.getLocation(encodedGeoHash);
		System.out.println(decodedLocation);
	}
}