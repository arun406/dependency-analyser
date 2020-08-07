package com.wellsforgo.dependency.api;

import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 *
 */
@Component
public interface Parser {
    public Model parse(File file) throws IOException, XmlPullParserException;
}
