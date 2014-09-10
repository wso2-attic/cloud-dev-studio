package org.wso2.developerstudio.codenvy.ext.registry.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.TextResource;

/**
 * Created by kavith on 7/23/14.
 */
public interface WSO2RegistryExtensionResources extends ClientBundle{

    @Source("jquery.min.js")
    TextResource jqueryLib();

    @Source("jquery.ui.min.js")
    TextResource jqueryUILib();

    @Source("jquery.jsPlumb-1.6.2-min.js")
    TextResource jsPlumbLib();


}
