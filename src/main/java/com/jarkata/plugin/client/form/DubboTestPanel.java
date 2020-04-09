package com.jarkata.plugin.client.form;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Dubbo请求测试发起界面
 */
public class DubboTestPanel extends JPanel {

    //测试请求参数
    private final JTextArea dubboParamTextArea = new JTextArea();
    private final JTextArea dubboOutputTextArea = new JTextArea();

    public DubboTestPanel() {
        super(new BorderLayout());
        //Dubbo Request
        dubboParamTextArea.setText("[]");
        dubboParamTextArea.setTabSize(2);
        dubboParamTextArea.setLineWrap(true);
        dubboParamTextArea.setAutoscrolls(true);
        dubboParamTextArea.setToolTipText("以JsonArray格式设置");
        dubboParamTextArea.setMargin(JBUI.insets(30));
        dubboParamTextArea.setBorder(new MatteBorder(1, 1, 1, 1, JBColor.GRAY.brighter()));

        JBScrollPane requestScrollPane = new JBScrollPane(dubboParamTextArea);
        requestScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JLabel httpHeaderLabel = new JLabel("请求参数");

        JPanel requestParamPanel = new JPanel(new BorderLayout());
        requestParamPanel.add(httpHeaderLabel, BorderLayout.NORTH);
        requestParamPanel.add(requestScrollPane, BorderLayout.CENTER);

        //Dubbo Output
        dubboOutputTextArea.setText("{}");
        dubboOutputTextArea.setTabSize(2);
        dubboOutputTextArea.setLineWrap(true);
        dubboOutputTextArea.setAutoscrolls(true);
        dubboOutputTextArea.setToolTipText("输出请求结果");
        dubboOutputTextArea.setMargin(JBUI.insets(30));
        dubboOutputTextArea.setBorder(new MatteBorder(1, 1, 1, 1, JBColor.GRAY.brighter()));

        JBScrollPane responseScrollPane = new JBScrollPane(dubboOutputTextArea);
        responseScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel outputLabel = new JLabel("输出结果");

        JPanel responseOutputPanel = new JPanel(new BorderLayout());
        responseOutputPanel.add(outputLabel, BorderLayout.NORTH);
        responseOutputPanel.add(responseScrollPane, BorderLayout.CENTER);

        JPanel requestCenterPanel = new JPanel(new GridLayout(1, 2, 1, 3));
        requestCenterPanel.add(requestParamPanel);
        requestCenterPanel.add(responseOutputPanel);

        //请求信息
        DubboTestUrlPanel testUrlPanel = new DubboTestUrlPanel(dubboParamTextArea, dubboOutputTextArea);
        add(testUrlPanel, BorderLayout.NORTH);
        add(requestCenterPanel, BorderLayout.CENTER);
    }
}
