package com.wellsforgo.source;

/**
 *
 */
public interface VersionControl {

    public void clone(String source, String destination, String username, String password) throws UnableToCloneException;

    public void clone(String source, String destination, String branchName, String username, String password) throws UnableToCloneException;

}
