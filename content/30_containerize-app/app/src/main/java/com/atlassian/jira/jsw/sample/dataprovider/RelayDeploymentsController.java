package com.atlassian.jira.jsw.sample.dataprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.atlassian.jira.jsw.sample.dataprovider.Utils.buildUrl;
import static com.atlassian.jira.jsw.sample.dataprovider.Utils.buildUrlWithQueryParams;
import static java.util.Objects.requireNonNull;

@RequestMapping("/relay/deployments")
@RestController
class RelayDeploymentsController {

    private static final Logger log = LoggerFactory.getLogger(RelayDeploymentsController.class);
    private static final String API_VERSION = "0.1";

    private final HttpCommonOperations httpCommonOperations;

    RelayDeploymentsController(HttpCommonOperations httpCommonOperations) {
        this.httpCommonOperations = requireNonNull(httpCommonOperations);
    }

    @PostMapping("/bulk")
    void bulk(@RequestParam String hostBaseUrl,
              HttpServletRequest httpRequest,
              HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath("bulk");
        final String url = buildUrl(hostBaseUrl, path);

        log.info("calling get url = {}", url);
        httpCommonOperations.post(httpRequest, httpResponse, url);
    }

    @DeleteMapping("/bulkByProperties")
    void bulkDeleteByProps(@RequestParam String hostBaseUrl,
                           HttpServletRequest httpRequest,
                           HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath("bulkByProperties");
        final String url = buildUrlWithQueryParams(hostBaseUrl, path, httpRequest);

        log.info("calling delete url = {}", url);
        httpCommonOperations.delete(httpResponse, url);
    }

    @GetMapping("/pipelines/{pipelineId}/environments/{environmentId}/deployments/{deploymentSequenceNumber}")
    void getDeploymentByKey(@RequestParam String hostBaseUrl,
                            @PathVariable String pipelineId,
                            @PathVariable String environmentId,
                            @PathVariable String deploymentSequenceNumber,
                            HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath(
                String.format("pipelines/%s/environments/%s/deployments/%s", pipelineId, environmentId, deploymentSequenceNumber)
        );

        final String url = buildUrl(hostBaseUrl, path);

        log.info("calling get url = {}", url);
        httpCommonOperations.get(httpResponse, url);
    }

    @DeleteMapping("/pipelines/{pipelineId}/environments/{environmentId}/deployments/{deploymentSequenceNumber}")
    void deleteDeploymentByKey(@RequestParam String hostBaseUrl,
                               @PathVariable String pipelineId,
                               @PathVariable String environmentId,
                               @PathVariable String deploymentSequenceNumber,
                               HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath(
                String.format("pipelines/%s/environments/%s/deployments/%s", pipelineId, environmentId, deploymentSequenceNumber)
        );

        final String url = buildUrl(hostBaseUrl, path);

        log.info("calling delete url = {}", url);
        httpCommonOperations.delete(httpResponse, url);
    }

    private static String buildPath(String operartionPath) {
        return String.format("/rest/deployments/%s/%s", API_VERSION, operartionPath);
    }
}
