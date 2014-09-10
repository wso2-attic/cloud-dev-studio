package com.codenvy.ide;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.codenvy.ide.client.esb.ESBGraphicalEditorPresenter;
import com.codenvy.ide.client.inject.ESBJavaScriptInjector;
import com.codenvy.ide.client.inject.JSBundle;
import static com.codenvy.ide.api.ui.workspace.PartStackType.EDITING;

/** The skeleton of empty Codenvy extension. */
@Singleton
@Extension(title = "ESB Editor extension", version = "1.0.0")
public class ESBEditorExtension {

    @Inject
    public ESBEditorExtension(WorkspaceAgent workspaceAgent,
                                  ESBGraphicalEditorPresenter graphicalEditor,
                                  JSBundle bundle) {
        
        workspaceAgent.openPart(graphicalEditor, EDITING);
        ESBJavaScriptInjector.inject(bundle.jqueryLib().getText());
        ESBJavaScriptInjector.inject(bundle.jqueryUILib().getText());
        ESBJavaScriptInjector.inject(bundle.jsPlumbLib().getText());
        ESBJavaScriptInjector.inject(bundle.esbExtensionJS().getText());
        
        
        
        
    }
}
