package com.jarkata.plugin.client.form;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.domain.DubboRequestVo;
import com.jarkata.plugin.client.enums.CommandEnums;
import com.jarkata.plugin.client.service.ServiceFactory;
import com.jarkata.plugin.client.service.impl.DubboServiceImpl;
import com.jarkata.plugin.client.utils.ClassUtils;
import com.jarkata.plugin.client.utils.FileUtils;
import com.jarkata.plugin.client.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * DubboForm插件
 */
public class DubboPanelForm extends JPanel implements ActionListener {


    //测试请求参数
    private final JTextArea dubboParamTextArea = new JTextArea();
    private final JTextArea dubboOutputTextArea = new JTextArea();
    private final JComboBox<String> protocolComBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{"Dubbo"}));
    private final JComboBox<String> hostComboBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{"127.0.0.1:20880"}));
    private final JComboBox<String> urlComboBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{}));
    private final JComboBox<String> methodComBox = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{}));
    //配置
    private final JTextField appnameTextField = new JTextField("dubbo-app");
    private final JTextField contextPathTextField = new JTextField();
    private final JTextField registryTextField = new JTextField();
    private DefaultTreeModel defaultTreeModel = null;
    private DefaultMutableTreeNode rootNode = null;


    public DubboPanelForm() {
        super(new BorderLayout());
        //Dubbo面板
        JTabbedPane httpBodyPanel = new JBTabbedPane();
        httpBodyPanel.addTab("请求/响应", getRequestResponsePanel());
        httpBodyPanel.addTab("Dubbo配置", getConfigurationPanel());
        add(httpBodyPanel, BorderLayout.CENTER);
    }


    public JPanel getConfigurationPanel() {

        //应用名称
        JLabel hostLabel = new JLabel("应用名称:");
        hostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        hostLabel.setPreferredSize(new Dimension(100, 30));

        appnameTextField.setEditable(true);

        JPanel appnamePanel = new JPanel(new BorderLayout());
        appnamePanel.add(hostLabel, BorderLayout.WEST);
        appnamePanel.add(appnameTextField, BorderLayout.CENTER);

        //ContextPath
        JLabel contextPathLabel = new JLabel("上下文路径:");
        contextPathLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contextPathLabel.setPreferredSize(new Dimension(100, 30));
        //文本框
        contextPathTextField.setEditable(true);
        contextPathTextField.setText("/");
        JPanel contextPathPanel = new JPanel(new BorderLayout());
        contextPathPanel.add(contextPathLabel, BorderLayout.WEST);
        contextPathPanel.add(contextPathTextField, BorderLayout.CENTER);

        JButton saveConfigButton = new JButton();
        saveConfigButton.setText("保存");
        saveConfigButton.setActionCommand(CommandEnums.COMMAND_SAVE.getCommand());
        saveConfigButton.setPreferredSize(new Dimension(60, 30));
        saveConfigButton.addActionListener(this);
        JPanel saveConfigurationPanel = new JPanel(new BorderLayout());
        saveConfigurationPanel.add(saveConfigButton, BorderLayout.EAST);


        GridLayout gridLayout = new GridLayout(4, 1);
        JPanel configurationPanel = new JPanel(gridLayout);
        configurationPanel.setPreferredSize(new Dimension(500, 120));
        configurationPanel.add(appnamePanel);
        configurationPanel.add(contextPathPanel);
        configurationPanel.add(saveConfigurationPanel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(configurationPanel, BorderLayout.NORTH);

        JPanel parentPanel = new JPanel(new GridLayout(1, 2));
        parentPanel.add(panel);
        JPanel fileTreePanel = new JPanel(new BorderLayout());

        //JarUpload
        JLabel jarfileLabel = new JLabel("Jar文件上传:");
        jarfileLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        jarfileLabel.setPreferredSize(new Dimension(100, 30));
        //上传文件的按钮
        JButton jarfileUploadButton = new JButton();
        jarfileUploadButton.setText("文件上传");
        jarfileUploadButton.setActionCommand(CommandEnums.COMMAND_UPLOAD.getCommand());
        jarfileUploadButton.addActionListener(listener -> {
            JFileChooser jarFileChooser = new JFileChooser();
            jarFileChooser.showOpenDialog(jarfileUploadButton);
            File selectedFile = jarFileChooser.getSelectedFile();
            try {
                FileUtils.saveFileContent(FileUtils.FILE_DUBBO, selectedFile.getName(), selectedFile);
                rootNode.add(new DefaultMutableTreeNode(selectedFile.getName()));
                defaultTreeModel.reload();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        JPanel uploadButtonPanel = new JPanel(new BorderLayout());
        uploadButtonPanel.add(jarfileUploadButton, BorderLayout.EAST);

        JPanel jarFilePanel = new JPanel(new BorderLayout());
        jarFilePanel.add(jarfileLabel, BorderLayout.WEST);
        jarFilePanel.add(uploadButtonPanel, BorderLayout.CENTER);
        fileTreePanel.add(jarFilePanel, BorderLayout.NORTH);
        JTree jTree = getJTree();
        fileTreePanel.add(jTree, BorderLayout.CENTER);
        parentPanel.add(fileTreePanel);

        return parentPanel;
    }


    public JTree getJTree() {
        DefaultMutableTreeNode rootTree = getRootTree();
        this.defaultTreeModel = new DefaultTreeModel(rootTree);

        Tree treeRoot = new Tree(defaultTreeModel);// 利用根节点直接创建树
        treeRoot.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeRoot.setRootVisible(true);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("删除");
        menuItem.addActionListener(listener -> {
            DefaultMutableTreeNode selectionPath = (DefaultMutableTreeNode) treeRoot.getLastSelectedPathComponent();
            System.out.println("action:" + selectionPath);
            JOptionPane.showMessageDialog(null, "" + selectionPath.getUserObject(), "消息", JOptionPane.INFORMATION_MESSAGE);
        });

        popupMenu.add(menuItem);

        treeRoot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();
                if (button == MouseEvent.BUTTON3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });


        return treeRoot;
    }


    private DefaultMutableTreeNode getRootTree() {
        rootNode = new DefaultMutableTreeNode("/");//创建根节点
        List<File> fileList = FileUtils.getFileList(FileUtils.FILE_DUBBO);
        for (File file : fileList) {
            rootNode.add(new DefaultMutableTreeNode(file.getName()));//创建一级节点
        }
        return rootNode;
    }

    /**
     * 请求响应面板
     *
     * @return
     */
    private JPanel getRequestResponsePanel() {
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
        JPanel requestResponsePanel = new JPanel(new BorderLayout());
        requestResponsePanel.add(getUrlPanel(), BorderLayout.NORTH);
        requestResponsePanel.add(requestCenterPanel, BorderLayout.CENTER);
        return requestResponsePanel;
    }

    /**
     * Url 部分的Panel
     *
     * @return UrlPanel
     */
    private JPanel getUrlPanel() {
        hostComboBox.setEditable(true);
        urlComboBox.setEditable(true);
        JButton goButton = new JButton("发送");
        goButton.setActionCommand(CommandEnums.COMMAND_SEND.getCommand());
        goButton.addActionListener(this);

        JLabel hostLabel = new JLabel("IP:Port地址:");
        hostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel hostPanel = new JPanel(new BorderLayout());
        hostPanel.add(hostLabel, BorderLayout.WEST);
        hostPanel.add(hostComboBox, BorderLayout.CENTER);


        JLabel interfaceLabel = new JLabel("接口名称:");
        interfaceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.add(interfaceLabel, BorderLayout.WEST);
        pathPanel.add(urlComboBox, BorderLayout.CENTER);
        urlComboBox.addActionListener(event -> {
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
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        JLabel methodLabel = new JLabel("方法:");
        methodLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel methodPanel = new JPanel(new BorderLayout());
        methodPanel.add(methodLabel, BorderLayout.WEST);
        methodPanel.add(methodComBox, BorderLayout.CENTER);
        methodComBox.addItem("请选择");
        methodComBox.addActionListener(event -> {
            if (!"comboBoxChanged".equals(event.getActionCommand())) {
                return;
            }
            String method = Objects.toString(methodComBox.getSelectedItem(), "");
            if ("请选择".equals(method)) {
                return;
            }
            DubboRequestVo dubboRequest = getDubboRequest();
            try {
                Class<?> selectClazz = ClassUtils.getClass(dubboRequest.getClazzUrl());
                Method clazzMethod = ClassUtils.getMethod(selectClazz, method);
                if (clazzMethod == null) {
                    dubboOutputTextArea.setText("方法不存在");
                    return;
                }
                Class<?> returnType = clazzMethod.getReturnType();
                Object object = returnType.newInstance();
                String responseBody = JsonUtils.toJson(object);
                dubboOutputTextArea.setText(responseBody);

                Class<?>[] parameterTypes = clazzMethod.getParameterTypes();
                List<Object> paramsList = new ArrayList<>();
                for (Class<?> parameterType : parameterTypes) {
                    paramsList.add(parameterType.newInstance());
                }
                dubboParamTextArea.setText(JsonUtils.toJson(paramsList));
            } catch (Exception e) {
                e.printStackTrace();
                dubboOutputTextArea.setText(e.getMessage());
            }
        });

        //url panel
        JPanel centPanel = new JPanel(new GridLayout(1, 3));
        centPanel.add(hostPanel);
        centPanel.add(pathPanel);
        centPanel.add(methodPanel);

        JPanel urlPane = new JPanel(new BorderLayout());
        urlPane.add(protocolComBox, BorderLayout.WEST);
        urlPane.add(centPanel, BorderLayout.CENTER);
        urlPane.add(goButton, BorderLayout.EAST);
        return urlPane;
    }


    /**
     * 发送Http请求的Url
     *
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        System.out.println("command:" + actionCommand);
        if (CommandEnums.COMMAND_SAVE.getCommand().equals(actionCommand)) {
            DubboConfigVo saveVo = getDubboConfigVo();
            System.out.println(saveVo);
        } else if (CommandEnums.COMMAND_SEND.getCommand().equals(actionCommand)) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader classLoader = this.getClass().getClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                DubboServiceImpl dubboService = ServiceFactory.getService("dubboService", DubboServiceImpl.class);
                System.out.println(dubboService);

                Object objectMap = dubboService.getService(getDubboRequest(), getDubboConfigVo());
                dubboOutputTextArea.setText(JsonUtils.toJson(objectMap));
            } catch (Exception e) {
                dubboOutputTextArea.setText(JsonUtils.toJson(e));
            } finally {
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
        }

    }


    private DubboConfigVo getDubboConfigVo() {
        String appnameText = appnameTextField.getText();
        return new DubboConfigVo(appnameText, contextPathTextField.getText());
    }

    private DubboRequestVo getDubboRequest() {
        String dubboProtocol = Objects.toString(protocolComBox.getSelectedItem(), "");
        String hostAddr = Objects.toString(hostComboBox.getSelectedItem(), "");
        String clazzUrl = Objects.toString(urlComboBox.getSelectedItem(), "");
        String clazzMethod = Objects.toString(methodComBox.getSelectedItem(), "");
        DubboRequestVo dubboRequestVo = new DubboRequestVo(dubboProtocol, hostAddr, clazzUrl, clazzMethod);
        dubboRequestVo.setRequestJson(dubboParamTextArea.getText());
        return dubboRequestVo;
    }
}