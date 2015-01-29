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

import com.codenvy.api.project.shared.dto.GeneratorDescription;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizard;
import com.codenvy.ide.api.wizard.WizardContext;
import com.codenvy.ide.dto.DtoFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ESBConfigurationPresenterTest {

    @Mock
    private ESBConfigurationView view;
    @Mock
    private DtoFactory                dtoFactory;
    @Mock
    private WizardContext             wizardContext;
    @InjectMocks
    private ESBConfigurationPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter.setContext(wizardContext);
    }

    @Test
    public void noticeShouldBeEmpty() throws Exception {
        assertThat(presenter.getNotice(), nullValue());
    }

    @Test
    public void pageShouldBeIncomplitedWhenGroupIdIsEmpty() throws Exception {
        when(view.getGroupId()).thenReturn("");
        when(view.getArtifactId()).thenReturn("1");
        when(view.getVersion()).thenReturn("1");

        assertThat(presenter.isCompleted(), is(false));
    }

    @Test
    public void pageShouldBeIncomplitedWhenArtifactIdIsEmpty() throws Exception {
        when(view.getGroupId()).thenReturn("1");
        when(view.getArtifactId()).thenReturn("");
        when(view.getVersion()).thenReturn("1");

        assertThat(presenter.isCompleted(), is(false));
    }

    @Test
    public void pageShouldBeIncomplitedWhenVersionIsEmpty() throws Exception {
        when(view.getGroupId()).thenReturn("1");
        when(view.getArtifactId()).thenReturn("1");
        when(view.getVersion()).thenReturn("");

        assertThat(presenter.isCompleted(), is(false));
    }

    @Test
    public void pageShouldBeComplited() throws Exception {
        when(view.getGroupId()).thenReturn("1");
        when(view.getArtifactId()).thenReturn("1");
        when(view.getVersion()).thenReturn("1");

        assertThat(presenter.isCompleted(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void optionsShouldBeNotStoredWhenProjectDescriptionIsNull() throws Exception {
        presenter.storeOptions();
    }

    @Test()
    public void optionsShouldBeStored() throws Exception {
        ProjectDescriptor descriptor = mock(ProjectDescriptor.class);
        HashMap<String, List<String>> attributes = new HashMap<>();
        when(descriptor.getAttributes()).thenReturn(attributes);

        when(wizardContext.getData(ProjectWizard.PROJECT)).thenReturn(descriptor);

        GeneratorDescription generatorDescription = mock(GeneratorDescription.class);
        when(generatorDescription.withName(anyString())).thenReturn(generatorDescription);

        when(dtoFactory.createDto(GeneratorDescription.class)).thenReturn(generatorDescription);

        assertThat(attributes.size(), is(0));

        presenter.storeOptions();

        assertThat(attributes.size(), is(3));

        verify(wizardContext).putData(ProjectWizard.GENERATOR, generatorDescription);
    }
}