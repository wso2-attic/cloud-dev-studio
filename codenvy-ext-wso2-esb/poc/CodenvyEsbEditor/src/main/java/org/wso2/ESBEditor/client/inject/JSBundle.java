package com.codenvy.ide.client.inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 * Created by root on 9/10/14.
 */


public interface JSBundle extends ClientBundle {
    JSBundle INSTANCE = GWT.create(JSBundle.class);

        @Source("jquery.min.js")
        TextResource jquerysource();

        @Source("jquery.ui.min.js")
        TextResource jqueryuimin();

        @Source("jquery.jsPlumb-1.6.2-min.js")
        TextResource jsplumbsource();

        @Source("gwtjsplumbdemo.js")
        TextResource gwtresource();

        @Source("droppableManager.js")
        TextResource droppableManager();

        @Source("icons/Call.gif")
        ImageResource CallImage();

        @Source("icons/CallTemplate.gif")
        ImageResource CalleTempImage();

        @Source("icons/Log.gif")
        ImageResource LogImage();

        @Source("icons/Drop.gif")
        ImageResource DropImage();

        @Source("icons/Clone.gif")
        ImageResource CloneImage();

        @Source("icons/Respond.gif")
        ImageResource RespondImage();

        @Source("icons/Property.gif")
        ImageResource PropertyImage();

        @Source("icons/PayloadFactory.gif")
        ImageResource PayloadFactoryImage();

        @Source("icons/Throttle.gif")
        ImageResource ThrottleImage();

        @Source("icons/Send.gif")
        ImageResource SendImage();

        @Source("icons/Store.gif")
        ImageResource StoreImage();

    }

