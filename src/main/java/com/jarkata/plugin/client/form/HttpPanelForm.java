package com.jarkata.plugin.client.form;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import com.jarkata.plugin.client.utils.HttpUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * HttpForm插件
 */
public class HttpPanelForm extends JPanel implements ActionListener {


    private final JTextArea headTextArea = new JTextArea();
    private final JTextArea paramTextArea = new JTextArea();
    private final JComboBox<String> methodBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{"GET", "POST", "PUT", "DELETE"}));
    private final JComboBox<String> urlComboBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{}));
    private final JTextArea outputTextArea = new JTextArea();

    public HttpPanelForm() {
        super(new BorderLayout());

        urlComboBox.setEditable(true);
        JButton goButton = new JButton("发送");
        goButton.addActionListener(this);
        //url panel
        JPanel urlPane = new JPanel(new BorderLayout());
        urlPane.add(methodBox, BorderLayout.WEST);
        urlPane.add(urlComboBox, BorderLayout.CENTER);
        urlPane.add(goButton, BorderLayout.EAST);
        add(urlPane, BorderLayout.NORTH);

        //RequestPanel
        //Http Header
        headTextArea.setText("ContentType:application/json;charset=UTF-8");
        headTextArea.setTabSize(2);
        headTextArea.setLineWrap(true);
        headTextArea.setVisible(true);
        headTextArea.setAutoscrolls(true);
        headTextArea.setInheritsPopupMenu(true);
        headTextArea.setToolTipText("http header信息，以JSON格式设置");
        headTextArea.setSize(new Dimension(500, 200));
        headTextArea.setBorder(new MatteBorder(1, 1, 1, 1, JBColor.GRAY.brighter()));

        JBScrollPane requestHeaderScrollPane = new JBScrollPane(headTextArea);
        requestHeaderScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        JLabel httpHeaderLabel = new JLabel("请求头信息");

        JPanel requestHeadPanel = new JPanel(new BorderLayout());
        requestHeadPanel.add(httpHeaderLabel, BorderLayout.NORTH);
        requestHeadPanel.add(requestHeaderScrollPane, BorderLayout.CENTER);

        // Http Param
        paramTextArea.setLineWrap(true);
        paramTextArea.setTabSize(2);
        paramTextArea.setAutoscrolls(true);
        paramTextArea.setToolTipText("http parameter, 以http格式需求");
        paramTextArea.setMargin(JBUI.insets(30));
        paramTextArea.setBorder(new MatteBorder(1, 1, 1, 1, JBColor.GRAY.brighter()));

        JBScrollPane requestScrollPane = new JBScrollPane(paramTextArea);
        requestScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel httpParamLabel = new JLabel("请求参数");

        JPanel requestParamPanel = new JPanel(new BorderLayout());
        requestParamPanel.add(httpParamLabel, BorderLayout.NORTH);
        requestParamPanel.add(requestScrollPane, BorderLayout.CENTER);


        //请求信息
        JPanel requestPanel = new JPanel(new GridLayout(1, 2));
        requestPanel.add(requestHeadPanel);
        requestPanel.add(requestParamPanel);

        //响应信息
        //响应输出面板
        outputTextArea.setBorder(new MatteBorder(1, 1, 1, 1, JBColor.GRAY.brighter()));
        outputTextArea.setLineWrap(true);
        outputTextArea.setAutoscrolls(true);
        outputTextArea.setMargin(JBUI.insets(30));

        JBScrollPane outputScrollPane = new JBScrollPane(outputTextArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        //Request Tab
        JTabbedPane httpBodyPanel = new JBTabbedPane();
        httpBodyPanel.addTab("请求面板", requestPanel);
        httpBodyPanel.addTab("响应面板", outputPanel);
        add(httpBodyPanel, BorderLayout.CENTER);
    }


    /**
     * 发送Http请求的Url
     *
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object methodValue = methodBox.getSelectedItem();
        Object urlValue = urlComboBox.getSelectedItem();
        String url = Objects.toString(urlValue, "");
        String headText = headTextArea.getText();
        String paramText = paramTextArea.getText();

        try {
            String responseResult = null;
            if ("POST".equals(methodValue)) {
                responseResult = HttpUtils.httpPost(url, paramText);
            } else {
                responseResult = HttpUtils.httpGet(url + "?" + paramText);
            }
            outputTextArea.setText(responseResult);
        } catch (IOException e) {
            outputTextArea.setText(e.getMessage());
        }

    }


}