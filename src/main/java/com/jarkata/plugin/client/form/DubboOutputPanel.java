package com.jarkata.plugin.client.form;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class DubboOutputPanel extends JPanel {


    private final JTextArea dubboOutputTextArea = new JTextArea();

    public DubboOutputPanel() {
        super(new BorderLayout());
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
        add(outputLabel, BorderLayout.NORTH);
        add(responseScrollPane, BorderLayout.CENTER);

    }

    public JTextArea getDubboOutputTextArea() {
        return dubboOutputTextArea;
    }
}
