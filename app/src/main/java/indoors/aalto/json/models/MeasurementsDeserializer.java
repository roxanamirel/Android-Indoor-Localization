package indoors.aalto.json.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeasurementsDeserializer extends JsonDeserializer<Measurements> {

    private static final TypeReference<Map<String, Measurement>> TYPE_REFERENCE
            = new TypeReference<Map<String, Measurement>>() {
    };

    @Override
    public Measurements deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        final Map<String, Measurement> map = jp.readValueAs(TYPE_REFERENCE);
        List<Measurement> measurements = new ArrayList<>();
        for(Map.Entry<String,Measurement> entry : map.entrySet()) {
            Measurement measurement = entry.getValue();
            measurement.setName(entry.getKey());
            measurements.add(measurement);
        }
        return new Measurements(measurements);
    }
}
