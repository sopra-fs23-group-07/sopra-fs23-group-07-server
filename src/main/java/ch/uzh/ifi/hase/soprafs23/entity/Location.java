package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "LOCATION")
public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(name = "location_seq", sequenceName = "location_sequence", initialValue = 1)
    private Long locationId;
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private double latitude;

    // Getters and setters for the longitude and latitude fields
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
