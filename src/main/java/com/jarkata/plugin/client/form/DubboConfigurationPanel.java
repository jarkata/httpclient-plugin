package com.jarkata.plugin.client.form;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.treeStructure.Tree;
import com.jarkata.plugin.client.PluginConstants;
import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.enums.CommandEnums;
import com.jarkata.plugin.client.enums.LinkModelEnum;
import com.jarkata.plugin.client.utils.FileUtils;
import com.jarkata.plugin.client.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DubboConfigurationPanel extends JPanel {
    private final JTextField appnameTextField = new JTextField("dubbo-app");
    private final JTextField contextPathTextField = new JTextField();
    private final JTextField registryAddressTextField = new JTextField();
    private final JComboBox<String> linkModel = new ComboBox<>(new DefaultComboBoxModel<>(new String[]{LinkModelEnum.P2P.getCode(), LinkModelEnum.REGISTRY.getCode()}));
    private JButton jarfileUploadButton = new JButton();
    private DefaultTreeModel defaultTreeModel = null;
    private DefaultMutableTreeNode rootNode = null;

    /**
     * 配置面板
     */
    public DubboConfigurationPanel() {
        super(new GridLayout(1, 2));
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

        //连接方式
        JLabel linkModelLabel = new JLabel("连接方式:");
        linkModelLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        linkModelLabel.setPreferredSize(new Dimension(100, 30));
        JPanel linkModelPanel = new JPanel(new BorderLayout());
        linkModelPanel.add(linkModelLabel, BorderLayout.WEST);
        linkModelPanel.add(linkModel, BorderLayout.CENTER);

        //注册中心地址
        JLabel registryLabel = new JLabel("注册中心地址:");
        registryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        registryLabel.setPreferredSize(new Dimension(100, 30));
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.add(registryLabel, BorderLayout.WEST);
        registerPanel.add(registryAddressTextField, BorderLayout.CENTER);

        //保存按钮
        JButton saveConfigButton = new JButton();
        saveConfigButton.setText("保存");
        saveConfigButton.setActionCommand(CommandEnums.COMMAND_SAVE.getCommand());
        saveConfigButton.setPreferredSize(new Dimension(60, 30));
        saveConfigButton.addActionListener(this::saveConfigAction);
        JPanel saveConfigurationPanel = new JPanel(new BorderLayout());
        saveConfigurationPanel.add(saveConfigButton, BorderLayout.EAST);


        GridLayout gridLayout = new GridLayout(10, 1);
        JPanel configurationPanel = new JPanel(gridLayout);
        configurationPanel.setPreferredSize(new Dimension(500, 300));
        configurationPanel.add(appnamePanel);
        configurationPanel.add(contextPathPanel);
        configurationPanel.add(linkModelPanel);
        configurationPanel.add(registerPanel);
        configurationPanel.add(saveConfigurationPanel);

        JPanel panel = new JPanel();
        panel.add(configurationPanel);

        JPanel fileTreePanel = new JPanel(new BorderLayout());

        //JarUpload
        JLabel jarfileLabel = new JLabel("Jar文件上传:");
        jarfileLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        jarfileLabel.setPreferredSize(new Dimension(100, 30));
        //上传文件的按钮

        jarfileUploadButton.setText("文件上传");
        jarfileUploadButton.setActionCommand(CommandEnums.COMMAND_UPLOAD.getCommand());
        jarfileUploadButton.addActionListener(this::uploadJarFileAction);

        JPanel uploadButtonPanel = new JPanel(new BorderLayout());
        uploadButtonPanel.add(jarfileUploadButton, BorderLayout.EAST);

        JPanel jarFilePanel = new JPanel(new BorderLayout());
        jarFilePanel.add(jarfileLabel, BorderLayout.WEST);
        jarFilePanel.add(uploadButtonPanel, BorderLayout.CENTER);
        fileTreePanel.add(jarFilePanel, BorderLayout.NORTH);
        JTree jTree = getJTree();
        fileTreePanel.add(jTree, BorderLayout.CENTER);
        add(panel);
        add(fileTreePanel);
        init();
    }

    /**
     * 获取文件树信息
     *
     * @return
     */
    private JTree getJTree() {
        DefaultMutableTreeNode rootTree = getRootTree();
        this.defaultTreeModel = new DefaultTreeModel(rootTree);

        Tree treeRoot = new Tree(defaultTreeModel);// 利用根节点直接创建树
        treeRoot.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeRoot.setRootVisible(true);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("删除");
        menuItem.addActionListener(listener -> {
            DefaultMutableTreeNode selectionPath = (DefaultMutableTreeNode) treeRoot.getLastSelectedPathComponent();
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

    /**
     * 获取根节点
     *
     * @return
     */
    private DefaultMutableTreeNode getRootTree() {
        rootNode = new DefaultMutableTreeNode("/");//创建根节点
        List<File> fileList = FileUtils.getFileList(FileUtils.FILE_DUBBO);
        for (File file : fileList) {
            rootNode.add(new DefaultMutableTreeNode(file.getName()));//创建一级节点
        }
        return rootNode;
    }

    /**
     * 上传按钮触发的事件
     *
     * @param event
     */
    void uploadJarFileAction(ActionEvent event) {
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
    }

    /**
     * 保存配置
     *
     * @param event
     */
    void saveConfigAction(ActionEvent event) {
        DubboConfigVo configVo = getConfigVo();
        try {
            FileUtils.saveConfig(PluginConstants.PLUGIN_DUBBO_CONFIG, JsonUtils.toJson(configVo));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "保存dubbo配置信息", "消息", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public void init() {
        DubboConfigVo instance = DubboConfigVo.getInstance();
        appnameTextField.setText(instance.getAppname());
        contextPathTextField.setText(instance.getContextPath());
        linkModel.setSelectedItem(instance.getLinkModel());
        registryAddressTextField.setText(instance.getRegisterAddress());
    }

    /**
     * 配置信息
     *
     * @return
     */
    private DubboConfigVo getConfigVo() {
        DubboConfigVo configVo = new DubboConfigVo(StringUtils.defaultIfEmpty(appnameTextField.getText(), "ideaplugin"));
        configVo.setContextPath(StringUtils.trimToEmpty(contextPathTextField.getText()));
        configVo.setLinkModel(StringUtils.trimToEmpty(Objects.toString(linkModel.getSelectedItem(), null)));
        configVo.setRegisterAddress(StringUtils.trimToEmpty(registryAddressTextField.getText()));
        return configVo;
    }
}
