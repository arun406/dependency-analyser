package com.wellsforgo.dependency.api;

import com.wellsforgo.dependency.Options;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

@Component
public interface DependencyExporter {
    /**
     * @param writer
     * @param values
     * @param option
     * @throws IOException
     */
    public void export(Writer writer, List<Object> values, Options option) throws IOException;
}
