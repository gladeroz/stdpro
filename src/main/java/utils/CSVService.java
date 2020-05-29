package utils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import model.ConfigOdrCsv;

public class CSVService {

    private static final char DEFAULT_SEPARATOR = ';';
    
    public static MappingIterator<ConfigOdrCsv> getOdrdata() throws IOException {
    	return getOdrdata("configuration/Assurant.csv");
    }
    
    public static MappingIterator<ConfigOdrCsv> getOdrdata(String path) throws IOException {
		CsvFactory csvFactory = new CsvFactory();
		csvFactory.enable(CsvParser.Feature.TRIM_SPACES);
		csvFactory.enable(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS);
		CsvMapper mapper = new CsvMapper(csvFactory);
		mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

		CsvSchema schema = mapper.schemaFor(ConfigOdrCsv.class).withHeader().withColumnSeparator(';').withoutEscapeChar();
		return mapper.readerFor(ConfigOdrCsv.class).with(schema).readValues(CSVService.class.getResource(path));
    }

    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    //https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {
        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;
    }

    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {
        boolean first = true;

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }
}
