package com.jarkata.plugin.client.form;

import com.intellij.ui.components.JBTabbedPane;
import com.jarkata.plugin.client.service.BeanFactory;

import javax.swing.*;
import java.awt.*;

/**
 * DubboForm插件
 */
public class DubboPanelForm extends JPanel {

    /**
     * 初始化
     */
    public DubboPanelForm() {
        super(new BorderLayout());
        //Dubbo面板
        DubboOutputPanel dubboOutputPanel = new DubboOutputPanel();
        JTabbedPane httpBodyPanel = new JBTabbedPane();
        httpBodyPanel.addTab("请求/响应", new DubboTestPanel(dubboOutputPanel.getDubboOutputTextArea()));
        httpBodyPanel.addTab("响应", dubboOutputPanel);

        httpBodyPanel.addTab("Dubbo配置", new DubboConfigurationPanel());
        add(httpBodyPanel, BorderLayout.CENTER);
    }
}