package indoors.aalto.json.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonDeserialize(using = MeasurementsDeserializer.class)
@JsonSerialize(using = MeasurementsSerializer.class)
public class Measurements implements Iterable<Measurement> {

    private final List<Measurement> measurements;

    public Measurements(final List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public Measurements() {
        this.measurements = new ArrayList<>();
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    @Override
    public Iterator<Measurement> iterator() {
        return measurements.iterator();
    }

    public boolean containsMeasurement(Measurement measurementToLookFor) {
        for (Measurement measurement : measurements) {
            if (measurement.getName().equals(measurementToLookFor.getName())) {
                return true;
            }
        }
        return false;
    }

    public Measurement getMeasurementByName(String name) {
        for (Measurement measurement : measurements) {
            if (measurement.getName().equals(name)) {
                return measurement;
            }
        }
        return null;
    }
}
