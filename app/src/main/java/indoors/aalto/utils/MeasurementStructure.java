package indoors.aalto.utils;

import java.util.ArrayList;
import java.util.List;

import indoors.aalto.json.models.Measurements;

public class MeasurementStructure {
    private String room;
    private List<Measurements> measurementsForRoom;
    private List<Double> distancesForRoom;

    public MeasurementStructure(String room) {
        this.room = room;
        measurementsForRoom = new ArrayList<>();
        distancesForRoom = new ArrayList<>();
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<Double> getDistancesForRoom() {
        return distancesForRoom;
    }

    public void setDistancesForRoom(List<Double> distancesForRoom) {
        this.distancesForRoom = distancesForRoom;
    }

    public List<Measurements> getMeasurementsForRoom() {
        return measurementsForRoom;
    }

    public void setMeasurementsForRoom(List<Measurements> measurementsForRoom) {
        this.measurementsForRoom = measurementsForRoom;
    }
    public void addDistance (Double d){
        this.distancesForRoom.add(d);
    }

    public double getMinDistance() {
        double min = Double.MAX_VALUE;
        for (Double distance : distancesForRoom) {
            if (distance < min) {
                min = distance;
            }
        }
        return min;
    }

    @Override
    public String toString() {
        return  "Room = " + room  + "\nDistance = " + getMinDistance();
    }
}
