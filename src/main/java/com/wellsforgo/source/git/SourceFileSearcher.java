package com.wellsforgo.source.git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Searches a file in source repository.
 */
public class SourceFileSearcher {

    /**
     * Searches a file in given path recursively
     *
     * @param localPath
     * @param filename
     * @return list of file paths
     * @throws IOException
     */
    public List<Path> search(File localPath, String filename) throws IOException {
        return Files.walk(localPath.toPath())
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().equals(filename))
                .collect(Collectors.toList());
    }
}
