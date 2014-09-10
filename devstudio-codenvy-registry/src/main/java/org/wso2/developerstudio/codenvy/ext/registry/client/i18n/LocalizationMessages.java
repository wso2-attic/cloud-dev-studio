package org.wso2.developerstudio.codenvy.ext.registry.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Created by kavith on 7/23/14.
 */
public interface LocalizationMessages extends Messages {

    @Key("project.wizard.page.createresource.presenter.notification.start")
    String fileCreationStartedMsg(String projectName);

    @Key("project.wizard.page.createresource.presenter.notification.success")
    String fileCreationFinishedMsg(String projectName);
}
