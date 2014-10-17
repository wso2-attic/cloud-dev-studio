/*
 *
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.wso2.developerstudio.codenvy.core.client.ui.dashboard.page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.core.client.CoreExtensionResources;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.DashboardItem;

import java.util.List;
import java.util.Map;

@Singleton
public class DashboardPageViewImpl implements DashboardPageView {

    public static final int ITEM_SPACING = 5; //TODO all these sizes needs to be verified
    public static final int DASHBOARD_SPACING = 10; //inter element spacing in the dashboard
    public static final int ITEM_WIDTH = 220;
    public static final int ITEM_HEIGHT = 70;
    public static final int ITEMBUTTON_HEIGHT = 80;
    public static final int ITEMBUTTON_WIDTH = 220;
    public static final int BORDER_WIDTH = 2;
    public static final int MAIN_BACKGROUND_PANEL_NORTH = 1200;
    public static final int CAT_HEADER_SPACING = 20;
	public static final String CATEGORY_STYLE = "bar";
	public static final String ITEM_STYLE = "btn";

	private static final Logger logger = LoggerFactory.getLogger(DashboardPageViewImpl.class);

	interface DashboardPageBinder extends UiBinder<Widget, DashboardPageViewImpl> {
    }

    private static DashboardPageBinder uiBinder = GWT.create(DashboardPageBinder.class);

    private ActionDelegate delegate;
    private int NO_OF_ITEMS_PER_ROW = 5; //set for optimized user interaction, to 5

    @UiField
    Image bannerImage;

    @UiField
    DockLayoutPanel actionList;
    private final Widget rootElement;

    public DashboardPageViewImpl() {
        rootElement = uiBinder.createAndBindUi(this);
    }

    @Override
    public void setDelegate(ActionDelegate actionDelegate) {
        this.delegate = actionDelegate;
    }

    @Override
    public Widget asWidget() {
        return rootElement;
    }

	/**
	 *  generate the dashboard for display
	 * @param itemList
	 */

    @Override
    public void generateDashboard(Map<String, List<DashboardItem>> itemList) {
	    //for css resource injection
	    CoreExtensionResources.INSTANCE.styleCSS().ensureInjected();
	    ScrollPanel mainBackgroundPanel= new ScrollPanel();
	    HorizontalPanel backGroundDashboardPanel = new HorizontalPanel(); // we should ad the common items in a vertical panel into this

        backGroundDashboardPanel.setSpacing(DASHBOARD_SPACING);

        if (!itemList.isEmpty()) {//designing the dashboard
	        VerticalPanel dashBoardSectionPanel = new VerticalPanel();
            dashBoardSectionPanel.setStyleName(CATEGORY_STYLE);
            for (Map.Entry<String, List<DashboardItem>> entry : itemList.entrySet()) {
                dashBoardSectionPanel.add(createCategoryHeaderPanel(entry));
                List<DashboardItem> dashBoardList = entry.getValue();
                if (!dashBoardList.isEmpty()) {
                    dashBoardSectionPanel.add(createCategory(dashBoardList));
                } else {
                    //log : no values for that category
                }
                dashBoardSectionPanel.setBorderWidth(BORDER_WIDTH);
                backGroundDashboardPanel.add(dashBoardSectionPanel);
            }
        } else {
            logger.info("no project categories in the category list, hence no categories appear in the dashboard display panel");
        }
        mainBackgroundPanel.add(backGroundDashboardPanel);
        actionList.addNorth(mainBackgroundPanel, MAIN_BACKGROUND_PANEL_NORTH);
    }

	/**
	 * Generate the Dashboard Items for a single DEV_STUDIO project creation option available
	 * @param dashboardItem
	 * @return
	 */

    public Button createDashBoardItem(DashboardItem dashboardItem) {
        HorizontalPanel itemPanel = new HorizontalPanel();
        itemPanel.setSpacing(ITEM_SPACING);

        Image itemImage = new Image();
	    String itemName = dashboardItem.getName().toString();
	    if (dashboardItem.getImageResource() != null){
		    itemImage.setResource(dashboardItem.getImageResource());
	    }
	    else{
		    logger.info("the item" + itemName + "does not have an image associated, hence only the name is displayed");
	    }
        itemImage.getElement().setId(dashboardItem.getName());

        Label itemLabel = new Label();
        itemLabel.setText(itemName);

        itemPanel.add(itemImage);
        itemPanel.add(itemLabel);
        itemPanel.setPixelSize(ITEM_WIDTH, ITEM_HEIGHT);

        Button itembutton = new Button();
        itembutton.setPixelSize(ITEMBUTTON_WIDTH, ITEMBUTTON_HEIGHT);
        itembutton.getElement().appendChild(itemPanel.getElement());
        itembutton.setStyleName(ITEM_STYLE);

        return itembutton;
    }

	/**
	 * Create the dashboard category headings
	 * @param entry
	 * @return
	 */

    public HorizontalPanel createCategoryHeaderPanel(Map.Entry<String, List<DashboardItem>> entry) {

        HorizontalPanel categoryHeaderPanel = new HorizontalPanel();
        categoryHeaderPanel.setSpacing(CAT_HEADER_SPACING);
        Label categoryHeader = new Label();

        Image catImage = new Image();
	    String catName = entry.getKey();
	    categoryHeader.setText(catName);
        if (entry.getValue().get(0).getImageResource() != null) {
            catImage.setResource(entry.getValue().get(0).getCategory().getImageResource());
            categoryHeaderPanel.add(catImage);
        }
	    else{
	        logger.info("the category" + catName + "does not have an image associated, hence only the name is displayed");
        }
        categoryHeaderPanel.add(categoryHeader);
	    categoryHeaderPanel.setStyleName(CATEGORY_STYLE);

        return categoryHeaderPanel;
    }

	/**
	 * generate the dashboard category grid with Dashboard Items
	 * @param dashBoardList
	 * @return
	 */

    public Grid createCategory(List<DashboardItem> dashBoardList) {
        int dashBoardItemColCount = 0;
        int dashBoardItemRowCount = 0;
        int dashBoarItems = dashBoardList.size() + 1;
        int noOfRows = dashBoarItems / NO_OF_ITEMS_PER_ROW;
        if (noOfRows == 0) {
            ++noOfRows;
        }
        else if (dashBoarItems % NO_OF_ITEMS_PER_ROW != 0) { //if not divisible by 5 add one more row
            ++noOfRows;
        }
        Grid categoryPanel = new Grid(noOfRows, NO_OF_ITEMS_PER_ROW);
        for (DashboardItem dashboardItem : dashBoardList) {
            Button item_button = createDashBoardItem(dashboardItem);
            categoryPanel.setWidget(dashBoardItemRowCount, dashBoardItemColCount, item_button);
            dashBoardItemColCount++;
            if (dashBoardItemColCount == NO_OF_ITEMS_PER_ROW) {
                dashBoardItemColCount = 0;
                dashBoardItemRowCount++;
            }
        }
        return categoryPanel;
    }

}

