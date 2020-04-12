package com.jarkata.plugin.client.form;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class DubboResponsePanel extends JPanel {


    private final JTextArea dubboResponseTextArea = new JTextArea();

    public DubboResponsePanel() {
        super(new BorderLayout());
        //Dubbo Output
        dubboResponseTextArea.setText("{}");
        dubboResponseTextArea.setTabSize(2);
        dubboResponseTextArea.setLineWrap(true);
        dubboResponseTextArea.setAutoscrolls(true);
        dubboResponseTextArea.setToolTipText("输出请求结果");
        dubboResponseTextArea.setMargin(JBUI.insets(30));
        dubboResponseTextArea.setBorder(new MatteBorder(1, 1, 1, 1, JBColor.GRAY.brighter()));

        JBScrollPane responseScrollPane = new JBScrollPane(dubboResponseTextArea);
        responseScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(responseScrollPane, BorderLayout.CENTER);

    }

    public JTextArea getDubboResponseTextArea() {
        return dubboResponseTextArea;
    }
}
