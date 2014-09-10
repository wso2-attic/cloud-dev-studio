package org.wso2.developerstudio.codenvy.ext.registry.client.wizard.maven;

import com.codenvy.ide.api.mvp.View;
import com.google.inject.ImplementedBy;

/**
 * Created by kavith on 7/16/14.
 */
@ImplementedBy(MavenSettingsPageViewImpl.class)
public interface MavenSettingsPageView extends View<MavenSettingsPageView.ActionDelegate> {

    public interface ActionDelegate{
        void onTextChange();
    }

    void setArtifactId(String artifact);

    void setGroupId(String group);

    void setVersion(String value);

    String getPackaging();

    String getGroupId();

    String getArtifactId();

    String getVersion();
}
