package utils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class FrenchBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        // Nettoyer la chaîne : remplacer tous les espaces et espaces insécables
        value = value.replaceAll("\\s", "").replace("\u00A0", "");
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
            return new BigDecimal(nf.parse(value).toString());
        } catch (ParseException e) {
            throw new IOException("Format numérique invalide : " + value, e);
        }
    }
}
