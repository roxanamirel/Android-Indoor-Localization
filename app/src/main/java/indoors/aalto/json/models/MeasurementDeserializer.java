package indoors.aalto.json.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeasurementDeserializer extends JsonDeserializer<Measurement> {

    private static final TypeReference<Map<String, AccessPoint>> TYPE_REFERENCE
            = new TypeReference<Map<String, AccessPoint>>() {
    };

    @Override
    public Measurement deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
        final Map<String, AccessPoint> map = jp.readValueAs(TYPE_REFERENCE);
        List<AccessPoint> accessPoints = new ArrayList<>();
        for (Map.Entry<String, AccessPoint> entry : map.entrySet()) {
            AccessPoint accessPoint = entry.getValue();
            accessPoint.setName(entry.getKey());
            accessPoints.add(accessPoint);
        }
        Measurement measurement = new Measurement();
        measurement.setAccessPoints(accessPoints);
        return measurement;
    }
}
