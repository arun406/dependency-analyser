package com.wellsforgo.dependency;

import com.wellsforgo.dependency.api.DependencyExporter;
import com.wellsforgo.dependency.api.Parser;
import com.wellsforgo.dependency.exporters.DependencyCSVExporter;
import com.wellsforgo.dependency.parsers.MavenPomParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotationConfig {

    @Bean
    public Parser parser() {
        return new MavenPomParser();
    }

    @Bean
    public DependencyExporter exporter() {
        return DependencyCSVExporter.getInstance();
    }
}
