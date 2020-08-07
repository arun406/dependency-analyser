package com.wellsforgo.dependency.exporters;

import com.wellsforgo.dependency.Constants;
import com.wellsforgo.dependency.Options;
import com.wellsforgo.dependency.api.DependencyExporter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class DependencyCSVExporter implements DependencyExporter {

    private static DependencyCSVExporter INSTANCE = null;

    private DependencyCSVExporter() {
    }

    /**
     * @return
     */
    public static DependencyExporter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DependencyCSVExporter();
        }
        return INSTANCE;
    }

    /**
     * @param value
     * @return
     */
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    private static String apply(Object o) {
        return o != null ? o.toString() : "";
    }

    /**
     * @param writer
     * @param values
     * @param options
     * @throws IOException
     */
    @Override
    public void export(Writer writer, List<Object> values, Options options) throws IOException {
        boolean first = true;
        char separators = Constants.DEFAULT_SEPARATOR;

        char customQuote = ' ';
        //default customQuote is empty
        if (options != null) {
            separators = (char) options.get(Constants.SEPARATOR);
            customQuote = (char) options.get(Constants.CUSTOM_QUOTE);
        }
        System.out.println("values : " + values);
        List<String> stringList = values.stream().map(DependencyCSVExporter::apply).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (String value : stringList) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == Constants.EMPTY_CHAR) {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }
            first = false;
        }
        sb.append("\n");
        writer.append(sb.toString());
    }
}
