package com.jarkata.plugin.client.form;

import com.intellij.openapi.ui.ComboBox;
import com.jarkata.plugin.client.PluginConstants;
import com.jarkata.plugin.client.domain.DubboRequestVo;
import com.jarkata.plugin.client.enums.CommandEnums;
import com.jarkata.plugin.client.service.BeanFactory;
import com.jarkata.plugin.client.service.impl.DubboServiceImpl;
import com.jarkata.plugin.client.utils.ClassUtils;
import com.jarkata.plugin.client.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Dubbo配置的url界面
 */
public class DubboTestUrlPanel extends JPanel {
    private final JComboBox<String> protocolComBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{"dubbo"}));
    private final JComboBox<String> hostComboBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{"127.0.0.1:20880"}));
    private final JComboBox<String> urlComboBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{}));
    private final JComboBox<String> methodComBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{}));
    private final JComboBox<String> versionComBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{PluginConstants.DUBBO_VERSION_DEFAULT}));

    private JTextArea inputTextArea = null;
    private JTextArea outputTextArea = null;
    private static final String SELLECT_FLAG = "-------";

    /**
     * Dubbo url 界面
     *
     * @param inputTextArea
     * @param outputTextArea
     */
    public DubboTestUrlPanel(JTextArea inputTextArea, JTextArea outputTextArea) {
        super(new BorderLayout());
        this.inputTextArea = inputTextArea;
        this.outputTextArea = outputTextArea;
        //地址信息赋值
        hostComboBox.setEditable(true);
        urlComboBox.setEditable(true);
        versionComBox.setEditable(true);
        //执行请求
        JButton goButton = new JButton("发送");
        goButton.setActionCommand(CommandEnums.COMMAND_SEND.getCommand());
        goButton.addActionListener(this::submitRequest);

        JLabel hostLabel = new JLabel("IP:PORT地址:");
        hostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel hostPanel = new JPanel(new BorderLayout());
        hostPanel.add(hostLabel, BorderLayout.WEST);
        hostPanel.add(hostComboBox, BorderLayout.CENTER);

        //接口信息
        JLabel interfaceLabel = new JLabel("接口名称:");
        interfaceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.add(interfaceLabel, BorderLayout.WEST);
        pathPanel.add(urlComboBox, BorderLayout.CENTER);
        urlComboBox.addActionListener(this::chooseClazzNameAction);

        //方法信息
        JLabel methodLabel = new JLabel("   方法:");
        methodLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel methodPanel = new JPanel(new BorderLayout());
        methodPanel.add(methodLabel, BorderLayout.WEST);
        methodPanel.add(methodComBox, BorderLayout.CENTER);
        methodPanel.setPreferredSize(new Dimension(320, 30));
        methodComBox.addItem(SELLECT_FLAG);
        methodComBox.addActionListener(this::chooseMethodAction);
        //版本号
        JLabel versionLabel = new JLabel("Version:");
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel versionPanel = new JPanel(new BorderLayout());
        versionPanel.add(versionLabel, BorderLayout.WEST);
        versionPanel.add(versionComBox, BorderLayout.CENTER);
        versionPanel.setPreferredSize(new Dimension(160, 30));


        JPanel hostMethodPanel = new JPanel(new BorderLayout());
        hostMethodPanel.add(pathPanel, BorderLayout.CENTER);
        hostMethodPanel.add(methodPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(hostPanel, BorderLayout.WEST);
        centerPanel.add(hostMethodPanel, BorderLayout.CENTER);
        centerPanel.add(versionPanel, BorderLayout.EAST);

        //Panel的布局
        add(protocolComBox, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(goButton, BorderLayout.EAST);
    }

    /**
     * 获取类名
     *
     * @param event
     */
    void chooseClazzNameAction(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        if (!"comboBoxChanged".equals(actionCommand)) {
            return;
        }
        Object selectedItem = urlComboBox.getSelectedItem();
        try {
            Class<?> selectClazz = ClassUtils.getClass(Objects.toString(selectedItem, null));
            if (selectClazz == null) {
                JOptionPane.showMessageDialog(null, selectedItem + "输入的类不存在");
                return;
            }
            Method[] declaredMethods = selectClazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                methodComBox.addItem(declaredMethod.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * 选择方法mplements ActionListener
     *
     * @param event
     */
    void chooseMethodAction(ActionEvent event) {
        if (!"comboBoxChanged".equals(event.getActionCommand())) {
            return;
        }
        String method = Objects.toString(methodComBox.getSelectedItem(), "");
        if (SELLECT_FLAG.equals(method)) {
            return;
        }
        String clazzName = Objects.toString(urlComboBox.getSelectedItem(), null);
        try {
            Class<?> selectClazz = ClassUtils.getClass(clazzName);
            Method clazzMethod = ClassUtils.getMethod(selectClazz, method);
            if (clazzMethod == null) {
                return;
            }
            Class<?> returnType = clazzMethod.getReturnType();
            Object object = returnType.newInstance();
            Class<?>[] parameterTypes = clazzMethod.getParameterTypes();
            List<Object> paramsList = new ArrayList<>();
            for (Class<?> parameterType : parameterTypes) {
                paramsList.add(parameterType.newInstance());
            }
            outputTextArea.setText(JsonUtils.toJsonWithPretty(object));
            inputTextArea.setText(JsonUtils.toJsonWithPretty(paramsList));
        } catch (Exception ex) {
            outputTextArea.setText(ex.getMessage());
        }
    }

    /**
     * 提交请求
     *
     * @param event
     */
    void submitRequest(ActionEvent event) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            outputTextArea.setText("正在处理中....");
            new Thread(() -> {
                try {
                    Thread.currentThread().setContextClassLoader(classLoader);
                    DubboServiceImpl dubboService = BeanFactory.getBean(DubboServiceImpl.class);
                    Object objectMap = dubboService.getService(getDubboRequest());
                    outputTextArea.setText(JsonUtils.toJsonWithPretty(objectMap));
                } catch (Exception e) {
                    outputTextArea.setText(e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            outputTextArea.setText(JsonUtils.toJsonWithPretty(e));
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * 获取dubbo请求对象
     *
     * @return
     */
    private DubboRequestVo getDubboRequest() {
        String dubboProtocol = Objects.toString(protocolComBox.getSelectedItem(), "");
        String hostAddr = Objects.toString(hostComboBox.getSelectedItem(), "");
        hostAddr = StringUtils.trimToEmpty(hostAddr);
        String clazzUrl = Objects.toString(urlComboBox.getSelectedItem(), "");
        clazzUrl = StringUtils.trimToEmpty(clazzUrl);
        String clazzMethod = Objects.toString(methodComBox.getSelectedItem(), "");
        String dubboVersion = Objects.toString(versionComBox.getSelectedItem(), "");
        DubboRequestVo dubboRequestVo = new DubboRequestVo(dubboProtocol, hostAddr, clazzUrl, clazzMethod);
        dubboRequestVo.setVersion(StringUtils.trimToEmpty(dubboVersion));
        dubboRequestVo.setRequestJson(StringUtils.trimToEmpty(inputTextArea.getText()));
        return dubboRequestVo;
    }


}
