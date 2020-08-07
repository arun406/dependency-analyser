package com.wellsforgo.dependency;

/**
 *
 */
public class Dependency {

    // Maven dependency model.
    org.apache.maven.model.Dependency dependency;

    /**
     * @param dependency
     */
    public Dependency(org.apache.maven.model.Dependency dependency) {
        this.dependency = dependency;
    }

    /**
     * return true if its a system dependency.
     *
     * @return
     */
    public boolean isSystemDependency() {
        return "system".equalsIgnoreCase(this.dependency.getScope()) ? true : false;
    }

    /**
     * @return
     */
    public String getGroupId() {
        return this.dependency.getGroupId();
    }

    /**
     * @return
     */
    public String getArtifactId() {
        return this.dependency.getArtifactId();
    }

    /**
     * @return
     */
    public String getVersion() {
        return this.dependency.getVersion();
    }

    /**
     * @return
     */
    public String getScope() {
        return this.dependency.getScope();
    }

    /**
     * @return
     */
    public String getSystemPath() {
        return this.dependency.getSystemPath();
    }

    /**
     * @return
     */
    public String getType() {
        return this.dependency.getType();
    }
}
