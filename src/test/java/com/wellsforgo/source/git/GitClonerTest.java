package com.wellsforgo.source.git;

import com.wellsforgo.source.UnableToCloneException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
public class GitClonerTest {

    private GitCloner cloner = new GitCloner();

    String uri = "https://github.com/MercatorSolutionsFZE/Framework.git";
    String username = "204895";
    String password = "Welcome@mc1";

    @Test
    void testClone() throws IOException, UnableToCloneException {

        File localPath = File.createTempFile("GitRepository", "");
        // delete repository before running this
        Files.delete(localPath.toPath());
        System.out.println("folder: " + localPath.getAbsolutePath());
        cloner.clone(uri,
                localPath.getAbsolutePath(), username, password);
        assertTrue(localPath.exists());
    }
}
