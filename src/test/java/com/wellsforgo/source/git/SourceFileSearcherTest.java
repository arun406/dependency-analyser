package com.wellsforgo.source.git;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class SourceFileSearcherTest {

    private SourceFileSearcher sourceFileSearcher = new SourceFileSearcher();

    /**
     * @throws IOException
     */
    @Test
    public void searchPomFile() throws IOException {

        long count = sourceFileSearcher
                .search(new File("/tmp/JGitTestRepository1536333026619106396"), "pom.xml")
                .stream().count();

        assertThat("count", count, greaterThan(0l));
    }
}
