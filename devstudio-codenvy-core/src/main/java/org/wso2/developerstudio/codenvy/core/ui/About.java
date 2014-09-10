package org.wso2.developerstudio.codenvy.core.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class About extends DialogBox {

  @UiField Button okButton;

  interface AboutBinder extends UiBinder<Widget, About> {}

  public About() {
    center();
    super.setWidget(GWT.<AboutBinder> create(AboutBinder.class).createAndBindUi(this));
  }

  @UiHandler("okButton")
  void onDismiss(ClickEvent e) {
    hide();
  }

  @Override
  public void center() {
    super.center();
  }
}
