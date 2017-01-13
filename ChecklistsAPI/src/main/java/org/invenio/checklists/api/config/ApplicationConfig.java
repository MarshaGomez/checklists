package org.invenio.checklists.api.config;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;

/**
 * Spring JAX-RS Checklists Application configuration.
 *
 * @author avillalobos
 */
public class ApplicationConfig extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public ApplicationConfig() {
        register(ResourceConfig.class);
        register(LoggingFeature.class);
        register(MultiPartFeature.class);
        registerFinder(new PackageNamesScanner(new String[] { "org.invenio.checklists" }, true));
    }

}
