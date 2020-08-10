package com.wellsforgo.dependency;

import com.wellsforgo.dependency.api.Parser;
import com.wellsforgo.dependency.exporters.DependencyCSVExporter;
import com.wellsforgo.dependency.exporters.DependencyExcelExporter;
import com.wellsforgo.source.UnableToCloneException;
import com.wellsforgo.source.VersionControl;
import com.wellsforgo.source.git.GitCloner;
import com.wellsforgo.source.git.Repositories;
import com.wellsforgo.source.git.SourceFileSearcher;
import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class.getName());
    static List<String> allowedKeys = new ArrayList<>();

    static {
        allowedKeys.add("--username");
        allowedKeys.add("--password");
        allowedKeys.add("--organization");
        allowedKeys.add("--branch");
    }

    /**
     * @param args
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void main(String[] args) throws IOException, XmlPullParserException, UnableToCloneException {
        logger.debug(" Execution started..");

        Map<String, String> params = convertToKeyValuePair(args);
        String password = params.get("password");
        String username = params.get("username");
        String organization = params.get("organization");
        String branch = params.get("branch");

        params.forEach((k, v) -> logger.debug("Key : {},  Value : {}", new Object[]{k, v}));

        List<Dependency> dependencies = new ArrayList<>();

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AnnotationConfig.class);
        Parser parser = context.getBean(Parser.class);

        List<Path> paths = new ArrayList<>();

        // get the repositories clone url.
        Map<String, String> map = Repositories.getRepositories(organization, password);
        if (map != null && !map.isEmpty()) {
            logger.debug("cloning the repositories.....");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                File localPath = new File(entry.getKey(), "");
                logger.debug("Local temp directory : {} ", localPath.getAbsolutePath());

                // delete repository before running this
                removeDirectory(localPath);
                clone(null, localPath, entry.getValue(), username, password);
                logger.debug(" Git repository {} cloned successfully...", entry.getKey());

                // searching the pom.xml in the git repository recursively
                logger.debug("searching the repositories for pom.xml...");
                SourceFileSearcher sourceFileSearcher = new SourceFileSearcher();
                List<Path> temp = sourceFileSearcher
                        .search(new File(localPath.getAbsolutePath()), "pom.xml")
                        .stream().collect(Collectors.toList());

                if (temp != null && !temp.isEmpty()) {
                    paths.addAll(temp);
                }
            }
        }


        DependencyExcelExporter excelExporter = new DependencyExcelExporter();

        for (Path path : paths) {
            logger.debug("Path: {}", path.getFileName());
            //parse the pom file.
            logger.debug(" Parsing the {} file", path.toAbsolutePath().toString());
            Model model = parser.parse(path.toFile());

            List<Dependency> collect = model.getDependencies()
                    .stream()
                    .map(dependency -> new Dependency(dependency))
                    // filter the system dependencies
                    .filter(Dependency::isSystemDependency)
                    .collect(Collectors.toList());
            dependencies.addAll(collect);
        }

        // exporting the system dependencies to excel format.
        exportToExcel(excelExporter, dependencies);
    }

    /**
     * @param dir
     */
    public static void removeDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    /**
     * @param localPath
     * @param uri
     * @param username
     * @param password
     * @throws UnableToCloneException
     */
    private static void clone(String branch, File localPath, String uri, String username, String password) throws UnableToCloneException {
        if (branch != null && !branch.isEmpty()) {
            new GitCloner().clone(uri,
                    localPath.getAbsolutePath(), branch, username, password);
        } else {
            new GitCloner().clone(uri,
                    localPath.getAbsolutePath(), username, password);
        }
    }

    /**
     * Converts the arguments into HashMap
     *
     * @param args
     * @return
     */
    private static Map<String, String> convertToKeyValuePair(String[] args) {
        HashMap<String, String> params = new HashMap<>();
        for (String arg : args) {
            String[] kv = arg.split("=", 2);
            if (allowedKeys != null && !allowedKeys.contains(kv[0])) {
                throw new RuntimeException(String.format("the key %s is not in allowedKeys. Allowed keys are %s", kv[0], allowedKeys.toString()));
            }
            params.put(kv[0].substring(2), kv.length < 2 ? null : kv[1]);
        }
        return params;
    }

    /**
     * @param excelExporter
     * @param dependencies
     * @throws IOException
     */
    private static void exportToExcel(DependencyExcelExporter excelExporter, List<Dependency> dependencies) throws IOException {
        File out = new File("dependencies.xlsx");
        excelExporter.export(out, dependencies, null);
    }

    /**
     * @param csvExporter
     * @param dependencies
     * @throws IOException
     */
    private static void exportToCSV(DependencyCSVExporter csvExporter, List<Dependency> dependencies) throws IOException {
        Writer output = new FileWriter("/home/arun/Govind/output.csv");
        csvExporter.export(output, Arrays.asList("Name", "Group Id", "Artifact Id", "Version", "Scope", "Type", "System Path"), null);
        if (dependencies != null && !dependencies.isEmpty()) {

            for (Dependency dependency : dependencies) {
                List<Object> row = new ArrayList<>();
                row.add(dependency.getGroupId());
                row.add(dependency.getArtifactId());
                String version = dependency.getVersion();
                row.add(dependency.getVersion());
                row.add(dependency.getScope());
                row.add(dependency.getSystemPath());
                row.add(dependency.getType());
                csvExporter.export(output, row, null);
            }
        }
        output.flush();
        output.close();
    }
}
