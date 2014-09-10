package org.wso2.developerstudio.codenvy.ext.registry.server.inject;

import com.codenvy.inject.DynaModule;
import com.google.inject.AbstractModule;
import org.wso2.developerstudio.codenvy.ext.registry.server.project.*;

/**
 * Created by kavith on 7/15/14.
 */
@DynaModule
public class WSO2RegistryModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(RegistryProjectTypeDescriptionExtension.class);
        bind(RegistryProjectTypeExtension.class);
    }
}
