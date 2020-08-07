package com.wellsforgo.source.git;

import com.wellsforgo.source.UnableToCloneException;
import com.wellsforgo.source.VersionControl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Arrays;

/**
 * This class is responsible to clone a git repository
 */
public class GitCloner implements VersionControl {

    /**
     * This method will clone the git repository to a local path
     *
     * @param uri
     * @param destination
     * @throws GitAPIException
     */
    @Override
    public void clone(String uri, String destination, String username, String password) throws UnableToCloneException {

        try {
            Git.cloneRepository()
                    .setURI(uri)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .setDirectory(new File(destination))
                    .call();
        } catch (GitAPIException e) {
            throw new UnableToCloneException(e.getMessage());
        }
    }

    /**
     * This method will clone  a specific branch of a git repository to a local path
     *
     * @param source
     * @param destination
     * @param branchName
     * @param username
     * @param password
     * @throws GitAPIException
     */
    @Override
    public void clone(String source, String destination, String branchName, String username, String password) throws UnableToCloneException {

        try {
            Git.cloneRepository()
                    .setURI(source)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .setDirectory(new File(destination))
                    .setBranchesToClone(Arrays.asList("refs/heads/" + branchName))
                    .call();
        } catch (GitAPIException e) {
            throw new UnableToCloneException(e.getMessage());
        }
    }
}
