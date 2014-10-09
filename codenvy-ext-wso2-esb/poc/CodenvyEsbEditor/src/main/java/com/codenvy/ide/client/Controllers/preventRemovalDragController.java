package com.codenvy.ide.client.Controllers;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class preventRemovalDragController extends PickupDragController {

    public preventRemovalDragController(AbsolutePanel boundaryPanel,
                                        boolean allowDroppingOnBoundaryPanel) {
        super(boundaryPanel, allowDroppingOnBoundaryPanel);
    }
}