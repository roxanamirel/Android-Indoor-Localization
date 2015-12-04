package indoors.aalto.json.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MeasurementSerializer extends JsonSerializer<Measurement> {
    @Override
    public void serialize(Measurement measurement, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        for (AccessPoint ap : measurement.getAccessPoints()) {
            jgen.writeStringField("ap", ap.getName());
            jgen.writeStringField("capabilities", ap.getCapabilities());
            jgen.writeStringField("level",ap.getLevel()+"");
            jgen.writeStringField("frequency",ap.getFrequency()+"");
            jgen.writeStringField("BSSID", ap.getBssid());
            jgen.writeStringField("SSID", ap.getSsid());

        }
        jgen.writeEndObject();
    }
}
