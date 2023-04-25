package com.geico.claim.roadsideassistance.model;


public class Geolocation {

    private double latitude;
    private double longitude;

    public Geolocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * This method will calculate the distance between customer location and assistant location.
     *
     * @param to is representing the customer location or the second location for which the distance needs to be calculated.
     * @return The distance between two points.
     * @see <a href="https://stackoverflow.com/questions/8494283/gps-distance-calculation">One of the references used for research is: "GPS Distance Calculation - haversine "</a>
     */
    public double distanceTo(Geolocation to) {
        double earthRadius = 6371; // in kilometers
        double latDistance = Math.toRadians(to.latitude - this.latitude);
        double lonDistance = Math.toRadians(to.longitude - this.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(to.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        return distance;
    }
}
