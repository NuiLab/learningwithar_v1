package fiu.learningwithar;

import android.graphics.Color;
import android.location.Location;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by MariaP on 11/20/2016.
 */

public class FIULocations {

    ArrayList<MyLocation> myLocations;

    public FIULocations(){
        myLocations = new ArrayList<>();

        Location loc = new Location("");

        loc.setLatitude(25.757048);
        loc.setLongitude(-80.373939);
        myLocations.add(new MyLocation("Green Library", loc));

        //25.756133, -80.374681
        loc.setLatitude(25.756133);
        loc.setLongitude(-80.374681);
        myLocations.add(new MyLocation("Deuxieme Maison", loc));

        //25.757926, -80.374718
        loc.setLatitude(25.757926);
        loc.setLongitude(-80.374718);
        myLocations.add(new MyLocation("Viertes Haus", loc));

        //25.758997, -80.373877
        loc.setLatitude(25.758997);
        loc.setLongitude(-80.373877);
        myLocations.add(new MyLocation("SCIS", loc));

        //25.760216, -80.374538
        loc.setLatitude(25.760216);
        loc.setLongitude(-80.374538);
        myLocations.add(new MyLocation("PG6", loc));
    }

    public ArrayList<MyLocation> getLocations(){
        return myLocations;
    }

    protected class MyLocation{

        private String name;
        private Location loc;
        private int cl;

        public MyLocation(String name, Location loc) {
            this.name = name;
            this.loc = loc;

            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            cl = Color.rgb(r,g,b);

            this.loc.setAltitude(rand.nextInt(200)+50);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Location getLoc() {
            return loc;
        }

        public void setLoc(Location loc) {
            this.loc = loc;
        }

        public int getCl() {
            return cl;
        }

        public void setCl(int cl) {
            this.cl = cl;
        }
    }
}

