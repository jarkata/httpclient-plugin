package com.jarkata.plugin.client;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.jarkata.plugin.client.form.DubboPanelForm;
import com.jarkata.plugin.client.form.HttpPanelForm;
import org.jetbrains.annotations.NotNull;

/**
 * Http项目组件
 */
public class HttpProjectComponent implements ProjectComponent {
    private final Project project;
    private HttpPanelForm httpPanelForm;
    private DubboPanelForm dubboPanelForm;

    public HttpProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(this.project);
        this.httpPanelForm = new HttpPanelForm();
        this.dubboPanelForm = new DubboPanelForm();

        ToolWindow myToolWindow = toolWindowManager.registerToolWindow(getComponentName(), false, ToolWindowAnchor.BOTTOM);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        Content content = contentFactory.createContent(this.httpPanelForm, "Http", false);
        Content dubboContent = contentFactory.createContent(this.dubboPanelForm, "Dubbo", false);
        ContentManager contentManager = myToolWindow.getContentManager();
        contentManager.addContent(dubboContent);
        contentManager.addContent(content);

    }

    @Override
    public void projectClosed() {
        httpPanelForm = null;
        dubboPanelForm = null;
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(this.project);
        toolWindowManager.unregisterToolWindow(getComponentName());
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "HttpClient";
    }
}
