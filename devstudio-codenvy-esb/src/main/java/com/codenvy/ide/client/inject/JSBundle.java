package com.codenvy.ide.client.inject;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
* Extend the {@link ClientBundle} to provide Javascript
* resource linkage.
*
* @author Sohani
*/
public interface JSBundle extends ClientBundle {

@Source("esb-extension.js")
TextResource esbExtensionJS();

@Source("jquery.min.js")
TextResource jqueryLib();

@Source("jquery.ui.min.js")
TextResource jqueryUILib();

@Source("jquery.jsPlumb-1.6.2-min.js")
TextResource jsPlumbLib();

}
