package indoors.aalto.json.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MeasurementsSerializer extends JsonSerializer<Measurements> {

    @Override
    public void serialize(Measurements measurements, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        for (Measurement msm : measurements.getMeasurements()) {
            //jgen.writeStringField("Measurement ", msm.getName());
            jgen.writeObject(msm);
        }
        jgen.writeEndObject();
    }
}
