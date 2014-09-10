package org.wso2.developerstudio.codenvy.core.action;

/**
 * As usual, importing resources, related to Action API.
 * The 3rd import is required to call a default alert box.
 */
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import org.wso2.developerstudio.codenvy.core.ui.About;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.inject.Inject;
import com.google.gwt.user.client.ui.Button;


public class AboutAction extends Action
{
    /**
     * Define a constructor and pass over text to be displayed in the dialogue box
     */

    @Inject
    public AboutAction() {
        super("About");
    }

    /**
     * Define the action required when calling this method. In our case it'll open a dialogue box with 'Hello world'
     */

    @Override
    public void actionPerformed(ActionEvent arg0) {
        
        new About().show();
              
    }
   
}
