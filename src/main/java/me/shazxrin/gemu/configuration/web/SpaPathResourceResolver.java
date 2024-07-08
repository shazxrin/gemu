package me.shazxrin.gemu.configuration.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

public class SpaPathResourceResolver extends PathResourceResolver {
    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        // Return static resources
        Resource requestedResource = location.createRelative(resourcePath);
        if (requestedResource.exists() && requestedResource.isReadable()) {
            return requestedResource;
        }

        // Return index page for SPA to handle
        return new ClassPathResource("/public/index.html");
    }
}
