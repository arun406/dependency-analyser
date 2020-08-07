package com.wellsforgo.dependency.parsers;

import com.wellsforgo.dependency.api.Parser;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Component
public class MavenPomParser implements Parser {

    /**
     * parses the provided pom.xml file.
     *
     * @param file
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    @Override
    public Model parse(File file) throws IOException, XmlPullParserException {
        Reader reader = new FileReader(file);
        try {
            MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
            Model model = xpp3Reader.read(reader);
            return model;
        } finally {
            reader.close();
        }
    }
}
