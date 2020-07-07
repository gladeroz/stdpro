package utils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import model.ConfigOdrRefCsv;

public class CSVService {

    private static final char DEFAULT_SEPARATOR = ';';
    
    public static MappingIterator<ConfigOdrRefCsv> getOdrdata() throws IOException {
    	return getOdrdata("configuration/Assurant.csv", true, ConfigOdrRefCsv.class);
    }
    
    public static <T> MappingIterator<T> getOdrdata(String path, boolean ressource, Class<T> classType) throws IOException {
		CsvFactory csvFactory = new CsvFactory();
		csvFactory.enable(CsvParser.Feature.TRIM_SPACES);
		csvFactory.enable(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS);
		CsvMapper mapper = new CsvMapper(csvFactory);
		mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

		CsvSchema schema = mapper.schemaFor(classType).withHeader().withColumnSeparator(DEFAULT_SEPARATOR).withoutEscapeChar();
		if(ressource) {
			return mapper.readerFor(classType).with(schema).readValues(CSVService.class.getResource(path));
		} else {
			return mapper.readerFor(classType).with(schema).readValues(new File(path));
		}
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
        
        // change LF (\n) to CRLF (\r\n)
        sb.append("\r\n");
        w.append(sb.toString());
    }
    
    public static String formatDateForCompare(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd", Locale.ENGLISH);
		return dateFormat.format(date);
    }

}
