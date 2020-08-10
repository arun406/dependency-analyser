package com.wellsforgo.source.git;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Repositories {

    private static final Logger logger = LoggerFactory.getLogger(Repositories.class.getName());

    /**
     * @param organization
     * @param oAuth2Token
     * @return
     * @throws IOException
     */
    public static Map<String, String> getRepositories(String organization, String oAuth2Token) throws IOException {

        RepositoryService service = new RepositoryService();
        service.getClient().setOAuth2Token(oAuth2Token);
        List<Repository> mercatorSolutionsFZE = service.getOrgRepositories(organization);
        Map<String, String> map = mercatorSolutionsFZE.stream()
                .collect(Collectors.toMap(Repository::getName, Repository::getCloneUrl));
        logger.debug("repository list: {}", map.toString());
        return map;
    }
}
