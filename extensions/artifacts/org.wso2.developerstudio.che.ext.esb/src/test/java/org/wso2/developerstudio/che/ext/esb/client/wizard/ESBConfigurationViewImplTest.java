/**
 * <!--
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -->
 */
package org.wso2.developerstudio.che.ext.esb.client.wizard;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class ESBConfigurationViewImplTest {

    private static final String SOME_TEXT = "some text";

    @Mock
    private ESBConfigurationView.ActionDelegate delegate;
    @InjectMocks
    private ESBConfigurationViewImpl view;

    @Before
    public void setUp() throws Exception {
        view.setDelegate(delegate);
    }

    @Test
    public void groupIdContentShouldBeReturned() throws Exception {
        when(view.groupId.getText()).thenReturn(SOME_TEXT);

        assertThat(view.getGroupId(), is(SOME_TEXT));

        verify(view.groupId).getText();
    }

    @Test
    public void groupIdShouldBeChanged() throws Exception {
        view.setGroupId(SOME_TEXT);

        verify(view.groupId).setText(SOME_TEXT);
    }

    @Test
    public void userActionShouldBeDelegatedWhenUserChangedGroupId() throws Exception {
        view.onGroupIdChanged(mock(KeyUpEvent.class));

        verify(delegate).onGroupIdChanged();
    }

}