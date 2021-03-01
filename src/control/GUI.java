package control;

import javax.swing.event.*;

import fileManage.Directory;
import fileManage.File;
import fileManage.Inode;
import memoryManage.FreeBlockManage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Wed Jan 27 17:22:33 CST 2021
 */



/**
 * @author Lu Ning
 */
public class GUI extends JFrame {
    public GUI() {
        initComponents();
        this.setVisible(true);
    }

    public void workButtonActionPerformed(ActionEvent e) {
        cardPanel.removeAll();
        cardPanel.add(workPanel);
        cardPanel.repaint();
        cardPanel.validate();
    }

    public void memoryButtonActionPerformed(ActionEvent e) {
        cardPanel.removeAll();
        cardPanel.add(memoryPanel);
        cardPanel.repaint();
        cardPanel.validate();
    }

    public void fileButtonActionPerformed(ActionEvent e) {
        cardPanel.removeAll();
        cardPanel.add(filePanel);
        cardPanel.repaint();
        cardPanel.validate();
    }

    public void pauseActionPerformed(ActionEvent e) {
        TimerThread.setIfTimerSuspend(true);
    }

    public void continueButtonActionPerformed(ActionEvent e) {
        TimerThread.setIfTimerSuspend(false);
    }

    public void runSystemActionPerformed(ActionEvent e) {
        try {
            OS.runSystem();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null,exception,"开机错误",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reinstallOSActionPerformed(ActionEvent e) {
        OS.reinstallOperatingSystem();
        JOptionPane.showMessageDialog(null,"已在硬盘重装操作系统");
    }

    public void loadOSFileActionPerformed(ActionEvent e) {
        try {
            OS.disk.loadDisk();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null,exception,"加载镜像错误",JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null,"已加载操作系统至硬盘");
    }

    public void saveOSActionPerformed(ActionEvent e) {
        try {
            OS.saveOSMirror();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"已保存当前操作系统镜像");
    }

    public void deviceManageActionPerformed(ActionEvent e) {
        cardPanel.removeAll();
        cardPanel.add(devicePanel);
        cardPanel.repaint();
        cardPanel.validate();
    }

    public void findBlockContextActionPerformed(ActionEvent e) {
        try{
            String tempId = JOptionPane.showInputDialog(devices,"请输入盘块号(0-20479)","0");
            String tempContext = OS.disk.findBlockByDno(Short.parseShort(tempId)).showInFile();
            blockContext.setText(tempContext);
        }catch (Exception e1){
            JOptionPane.showMessageDialog(null,"输入盘块号有误","错误",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void findBlockContext2ActionPerformed(ActionEvent e) {
        try{
            String tempId = JOptionPane.showInputDialog(devices,"请输入盘块号(0-64)","0");
            String tempContext = OS.memory.findBlockByNumber(Short.parseShort(tempId)).showInFile();
            blockContext2.setText(tempContext);
        }catch (Exception e1){
            JOptionPane.showMessageDialog(null,"输入盘块号有误","错误",JOptionPane.ERROR_MESSAGE);
        }
    }


    public void createFileActionPerformed(ActionEvent e)  {
        new createFileDialog(this).setVisible(true);
    }

    public void deleteFileActionPerformed(ActionEvent e) {
        try {
            if(OS.selectedString == null){
                throw new Exception("未选中文件");
            }
            int option = JOptionPane.showConfirmDialog(null,"确认删除文件"+OS.selectedString+"?","删除文件",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(option == 0){
                File.deleteFile(OS.selectedString);
                OS.topFile = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void fileDetailActionPerformed(ActionEvent e) {
        try {
            new Inode().loadInodeToMemory();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void testButtonActionPerformed(ActionEvent e)  {
        try {
            System.out.println(FreeBlockManage.getAUserBlock());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void showDownButtonActionPerformed(ActionEvent e) {
        OS.showDown();

    }

    public void writeFileButtonActionPerformed(ActionEvent e) {
        new WriteFileDialog(this).setVisible(true);
    }

    public void readFileButtonActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, File.readFile((OS.topFile.getFd())));

    }


    public void addFileButtonActionPerformed(ActionEvent e) {
        new addFileDialog(this).setVisible(true);
    }

    public void fileNameListValueChanged(ListSelectionEvent e) {
        try {
            if (e.getValueIsAdjusting()){
                OS.selectedString = (String)fileNameList.getSelectedValue();
                OS.topFile = OS.pathDirectory.findFileInDirectory(OS.selectedString);
                OS.topFile.showFileInfomation();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void getInDirectoryActionPerformed(ActionEvent e) {
        try {
            Directory.getInDirectory(OS.selectedString);

            if(OS.selectedString.equals("..")){
                OS.path.remove(OS.path.size()-1);
            }else if(!OS.selectedString.equals(".")){
                OS.path.add(OS.selectedString);
            }
            OS.selectedString = null;

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null,exception,"追加时发生错误",JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        buttonPanel = new JPanel();
        workButton = new JButton();
        memoryButton = new JButton();
        fileButton = new JButton();
        deviceManage = new JButton();
        reinstallOS = new JButton();
        loadOSFile = new JButton();
        saveOS = new JButton();
        runSystem = new JButton();
        continueButton = new JButton();
        pauseButton = new JButton();
        showDownButton = new JButton();
        timeLabel = new JLabel();
        allTimeLabel = new JLabel();
        cardPanel = new JPanel();
        workPanel = new JPanel();
        memoryPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        filePanel = new JPanel();
        panel1 = new JPanel();
        label9 = new JLabel();
        fileName = new JLabel();
        label10 = new JLabel();
        fileType = new JLabel();
        fileSize = new JLabel();
        label11 = new JLabel();
        fileLink = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        fileCount = new JLabel();
        label14 = new JLabel();
        fileSaveMode = new JLabel();
        label15 = new JLabel();
        fileFID = new JLabel();
        label16 = new JLabel();
        fileStructureP = new JLabel();
        label17 = new JLabel();
        fileStructureL = new JLabel();
        label8 = new JLabel();
        label28 = new JLabel();
        createTime = new JLabel();
        label29 = new JLabel();
        alterTime = new JLabel();
        label30 = new JLabel();
        filePath = new JLabel();
        panel2 = new JPanel();
        label18 = new JLabel();
        directAddress1 = new JLabel();
        label19 = new JLabel();
        directAddress2 = new JLabel();
        label20 = new JLabel();
        directAddress3 = new JLabel();
        label21 = new JLabel();
        directAddress4 = new JLabel();
        label22 = new JLabel();
        directAddress5 = new JLabel();
        directAddress6 = new JLabel();
        label23 = new JLabel();
        directAddress7 = new JLabel();
        label24 = new JLabel();
        directAddress8 = new JLabel();
        label25 = new JLabel();
        indirectAddress1 = new JLabel();
        label26 = new JLabel();
        indirectAddress2 = new JLabel();
        label27 = new JLabel();
        panel3 = new JPanel();
        textField1 = new JTextField();
        fileJump = new JButton();
        deleteFile = new JButton();
        label3 = new JLabel();
        filePathLabel = new JLabel();
        button1 = new JButton();
        writeFileButton = new JButton();
        readFileButton = new JButton();
        addFileButton = new JButton();
        panel8 = new JPanel();
        scrollPane10 = new JScrollPane();
        fileNameList = new JList();
        scrollPane11 = new JScrollPane();
        fileModeList = new JList();
        scrollPane13 = new JScrollPane();
        fileUserList = new JList();
        scrollPane12 = new JScrollPane();
        list3 = new JList();
        createFile = new JButton();
        openFile = new JButton();
        getInDirectory = new JButton();
        panel4 = new JPanel();
        label6 = new JLabel();
        scrollPane3 = new JScrollPane();
        groupLinkList = new JList();
        label7 = new JLabel();
        inodeBarInDisk = new JProgressBar();
        scrollPane4 = new JScrollPane();
        inodeInMemoryList = new JList();
        label31 = new JLabel();
        label32 = new JLabel();
        inodeBarInMemory = new JProgressBar();
        inodeInDiskLabel = new JLabel();
        inodeInMemoryLabel = new JLabel();
        label34 = new JLabel();
        scrollPane5 = new JScrollPane();
        freeBlockList = new JList();
        scrollPane9 = new JScrollPane();
        systemOpenFileTable = new JList();
        label41 = new JLabel();
        label42 = new JLabel();
        testButton = new JButton();
        devicePanel = new JPanel();
        subDevicePanel1 = new JPanel();
        label4 = new JLabel();
        devices = new JPanel();
        panel5 = new JPanel();
        scrollPane1 = new JScrollPane();
        blockContext = new JTextArea();
        label5 = new JLabel();
        diskUsedSituation = new JLabel();
        diskBar = new JProgressBar();
        findBlockContext = new JButton();
        label33 = new JLabel();
        diskUsedSituation2 = new JLabel();
        diskBar2 = new JProgressBar();
        findBlockContext2 = new JButton();
        scrollPane6 = new JScrollPane();
        blockContext2 = new JTextArea();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //======== buttonPanel ========
                {
                    buttonPanel.setLayout(new FlowLayout());

                    //---- workButton ----
                    workButton.setText("\u4f5c\u4e1a\u7ba1\u7406\u9875\u9762");
                    workButton.addActionListener(e -> workButtonActionPerformed(e));
                    buttonPanel.add(workButton);

                    //---- memoryButton ----
                    memoryButton.setText("\u5185\u5b58\u7ba1\u7406\u9875\u9762");
                    memoryButton.addActionListener(e -> memoryButtonActionPerformed(e));
                    buttonPanel.add(memoryButton);

                    //---- fileButton ----
                    fileButton.setText("\u6587\u4ef6\u7ba1\u7406\u9875\u9762");
                    fileButton.addActionListener(e -> fileButtonActionPerformed(e));
                    buttonPanel.add(fileButton);

                    //---- deviceManage ----
                    deviceManage.setText("\u8bbe\u5907\u7ba1\u7406\u9875\u9762");
                    deviceManage.addActionListener(e -> deviceManageActionPerformed(e));
                    buttonPanel.add(deviceManage);

                    //---- reinstallOS ----
                    reinstallOS.setText("\u91cd\u88c5\u64cd\u4f5c\u7cfb\u7edf");
                    reinstallOS.addActionListener(e -> reinstallOSActionPerformed(e));
                    buttonPanel.add(reinstallOS);

                    //---- loadOSFile ----
                    loadOSFile.setText("\u52a0\u8f7d\u64cd\u4f5c\u7cfb\u7edf\u955c\u50cf");
                    loadOSFile.addActionListener(e -> loadOSFileActionPerformed(e));
                    buttonPanel.add(loadOSFile);

                    //---- saveOS ----
                    saveOS.setText("\u4fdd\u5b58\u64cd\u4f5c\u7cfb\u7edf\u955c\u50cf");
                    saveOS.addActionListener(e -> saveOSActionPerformed(e));
                    buttonPanel.add(saveOS);

                    //---- runSystem ----
                    runSystem.setText("\u5f00\u673a");
                    runSystem.addActionListener(e -> runSystemActionPerformed(e));
                    buttonPanel.add(runSystem);

                    //---- continueButton ----
                    continueButton.setText("\u7ee7\u7eed");
                    continueButton.addActionListener(e -> continueButtonActionPerformed(e));
                    buttonPanel.add(continueButton);

                    //---- pauseButton ----
                    pauseButton.setText("\u6682\u505c");
                    pauseButton.addActionListener(e -> pauseActionPerformed(e));
                    buttonPanel.add(pauseButton);

                    //---- showDownButton ----
                    showDownButton.setText("\u5173\u673a");
                    showDownButton.addActionListener(e -> showDownButtonActionPerformed(e));
                    buttonPanel.add(showDownButton);

                    //---- timeLabel ----
                    timeLabel.setText("\u5f53\u524d\u65f6\u95f4\uff1a00");
                    buttonPanel.add(timeLabel);

                    //---- allTimeLabel ----
                    allTimeLabel.setText("\u7d2f\u8ba1\u65f6\u95f4\uff1a0000");
                    buttonPanel.add(allTimeLabel);
                }
                contentPanel.add(buttonPanel, BorderLayout.NORTH);

                //======== cardPanel ========
                {
                    cardPanel.setLayout(new CardLayout());

                    //======== workPanel ========
                    {
                        workPanel.setLayout(new BorderLayout());
                    }
                    cardPanel.add(workPanel, "card1");

                    //======== memoryPanel ========
                    {
                        memoryPanel.setLayout(new BorderLayout());

                        //---- label1 ----
                        label1.setText("text");
                        memoryPanel.add(label1, BorderLayout.CENTER);

                        //---- label2 ----
                        label2.setText("text");
                        memoryPanel.add(label2, BorderLayout.NORTH);
                    }
                    cardPanel.add(memoryPanel, "card2");

                    //======== filePanel ========
                    {

                        //======== panel1 ========
                        {

                            //---- label9 ----
                            label9.setText("\u6587\u4ef6\u540d\uff1a");

                            //---- fileName ----
                            fileName.setText("null");

                            //---- label10 ----
                            label10.setText("\u6587\u4ef6\u7c7b\u578b\uff1a");

                            //---- fileType ----
                            fileType.setText("null");

                            //---- fileSize ----
                            fileSize.setText("null");

                            //---- label11 ----
                            label11.setText("\u6587\u4ef6\u5927\u5c0f\uff1a");

                            //---- fileLink ----
                            fileLink.setText("null");

                            //---- label12 ----
                            label12.setText("\u786c\u94fe\u63a5\u6570\uff1a");

                            //---- label13 ----
                            label13.setText("\u6253\u5f00\u6570\u76ee\uff1a");

                            //---- fileCount ----
                            fileCount.setText("null");

                            //---- label14 ----
                            label14.setText("\u5b58\u53d6\u6743\u9650\uff1a");

                            //---- fileSaveMode ----
                            fileSaveMode.setText("null");

                            //---- label15 ----
                            label15.setText("inode\u53f7\uff1a");

                            //---- fileFID ----
                            fileFID.setText("null");

                            //---- label16 ----
                            label16.setText("\u7269\u7406\u7ed3\u6784\uff1a");

                            //---- fileStructureP ----
                            fileStructureP.setText("null");

                            //---- label17 ----
                            label17.setText("\u903b\u8f91\u7ed3\u6784\uff1a");

                            //---- fileStructureL ----
                            fileStructureL.setText("null");

                            //---- label8 ----
                            label8.setText("\u88ab\u9009\u4e2d\u6587\u4ef6\u7684\u76f8\u5173\u4fe1\u606f");

                            //---- label28 ----
                            label28.setText("\u521b\u5efa\u65f6\u95f4\uff1a");

                            //---- createTime ----
                            createTime.setText("null");

                            //---- label29 ----
                            label29.setText("\u4fee\u6539\u65f6\u95f4\uff1a");

                            //---- alterTime ----
                            alterTime.setText("null");

                            //---- label30 ----
                            label30.setText("\u8def\u5f84\u5168\u540d\uff1a");

                            //---- filePath ----
                            filePath.setText("null");

                            //======== panel2 ========
                            {

                                //---- label18 ----
                                label18.setText("\u76f4\u63a5\u5730\u57401:");

                                //---- directAddress1 ----
                                directAddress1.setText("null");

                                //---- label19 ----
                                label19.setText("\u76f4\u63a5\u5730\u57402:");

                                //---- directAddress2 ----
                                directAddress2.setText("null");

                                //---- label20 ----
                                label20.setText("\u76f4\u63a5\u5730\u57403:");

                                //---- directAddress3 ----
                                directAddress3.setText("null");

                                //---- label21 ----
                                label21.setText("\u76f4\u63a5\u5730\u57404:");

                                //---- directAddress4 ----
                                directAddress4.setText("null");

                                //---- label22 ----
                                label22.setText("\u76f4\u63a5\u5730\u57405:");

                                //---- directAddress5 ----
                                directAddress5.setText("null");

                                //---- directAddress6 ----
                                directAddress6.setText("null");

                                //---- label23 ----
                                label23.setText("\u76f4\u63a5\u5730\u57406:");

                                //---- directAddress7 ----
                                directAddress7.setText("null");

                                //---- label24 ----
                                label24.setText("\u76f4\u63a5\u5730\u57407:");

                                //---- directAddress8 ----
                                directAddress8.setText("null");

                                //---- label25 ----
                                label25.setText("\u76f4\u63a5\u5730\u57408:");

                                //---- indirectAddress1 ----
                                indirectAddress1.setText("null");

                                //---- label26 ----
                                label26.setText("\u4e00\u7ea7\u95f4\u5740  :");

                                //---- indirectAddress2 ----
                                indirectAddress2.setText("null");

                                //---- label27 ----
                                label27.setText("\u4e8c\u7ea7\u95f4\u5740  :");

                                GroupLayout panel2Layout = new GroupLayout(panel2);
                                panel2.setLayout(panel2Layout);
                                panel2Layout.setHorizontalGroup(
                                    panel2Layout.createParallelGroup()
                                        .addGroup(panel2Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(panel2Layout.createParallelGroup()
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addGroup(panel2Layout.createParallelGroup()
                                                        .addComponent(label20)
                                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(label18)
                                                            .addComponent(label19)))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(panel2Layout.createParallelGroup()
                                                        .addComponent(directAddress3)
                                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(directAddress1)
                                                            .addComponent(directAddress2))))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label21)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(directAddress4))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label22)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(directAddress5))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label23)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(directAddress6))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label24)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(directAddress7))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label25)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(directAddress8))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label26)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(indirectAddress1))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label27)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(indirectAddress2)))
                                            .addContainerGap(34, Short.MAX_VALUE))
                                );
                                panel2Layout.setVerticalGroup(
                                    panel2Layout.createParallelGroup()
                                        .addGroup(panel2Layout.createSequentialGroup()
                                            .addGap(23, 23, 23)
                                            .addGroup(panel2Layout.createParallelGroup()
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(label18)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(label19)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(label20))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                    .addComponent(directAddress1)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(directAddress2)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(directAddress3)))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label21)
                                                .addComponent(directAddress4))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label22)
                                                .addComponent(directAddress5))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(label23)
                                                .addComponent(directAddress6))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(label24)
                                                .addComponent(directAddress7))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(label25)
                                                .addComponent(directAddress8))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(label26)
                                                .addComponent(indirectAddress1))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(label27)
                                                .addComponent(indirectAddress2))
                                            .addContainerGap(49, Short.MAX_VALUE))
                                );
                            }

                            GroupLayout panel1Layout = new GroupLayout(panel1);
                            panel1.setLayout(panel1Layout);
                            panel1Layout.setHorizontalGroup(
                                panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label28)
                                                .addGap(12, 12, 12)
                                                .addComponent(createTime))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label11)
                                                .addGap(12, 12, 12)
                                                .addComponent(fileSize))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label12)
                                                .addGap(12, 12, 12)
                                                .addComponent(fileLink))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label13)
                                                .addGap(12, 12, 12)
                                                .addComponent(fileCount))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label14)
                                                .addGap(12, 12, 12)
                                                .addComponent(fileSaveMode))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label16)
                                                .addGap(12, 12, 12)
                                                .addComponent(fileStructureP))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label17)
                                                .addGap(12, 12, 12)
                                                .addComponent(fileStructureL))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label15)
                                                .addGap(18, 18, 18)
                                                .addComponent(fileFID))
                                            .addComponent(label8)
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label29)
                                                .addGap(12, 12, 12)
                                                .addComponent(alterTime))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label30)
                                                .addGap(12, 12, 12)
                                                .addComponent(filePath))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label10)
                                                    .addComponent(label9))
                                                .addGap(12, 12, 12)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(fileName)
                                                    .addComponent(fileType))))
                                        .addGap(18, 18, 18)
                                        .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 20, Short.MAX_VALUE))
                            );
                            panel1Layout.setVerticalGroup(
                                panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(label8)
                                                .addGap(3, 3, 3)
                                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label9)
                                                    .addComponent(fileName))
                                                .addGap(5, 5, 5)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label10)
                                                    .addComponent(fileType))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label11)
                                                    .addComponent(fileSize))
                                                .addGap(6, 6, 6)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label12)
                                                    .addComponent(fileLink))
                                                .addGap(6, 6, 6)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label13)
                                                    .addComponent(fileCount))
                                                .addGap(6, 6, 6)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label14)
                                                    .addComponent(fileSaveMode))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label16)
                                                    .addComponent(fileStructureP))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label17)
                                                    .addComponent(fileStructureL))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label15)
                                                    .addComponent(fileFID))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label28)
                                                    .addComponent(createTime))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label29)
                                                    .addComponent(alterTime))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(label30)
                                                    .addComponent(filePath))))
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            );
                        }

                        //======== panel3 ========
                        {

                            //---- fileJump ----
                            fileJump.setText("\u8df3\u8f6c");

                            //---- deleteFile ----
                            deleteFile.setText("\u5220\u9664\u6587\u4ef6");
                            deleteFile.addActionListener(e -> deleteFileActionPerformed(e));

                            //---- label3 ----
                            label3.setText("\u5f53\u524d\u6587\u4ef6\u4f4d\u7f6e:");

                            //---- filePathLabel ----
                            filePathLabel.setText("/");

                            //---- button1 ----
                            button1.setText("\u5efa\u7acb\u94fe\u63a5");

                            //---- writeFileButton ----
                            writeFileButton.setText("\u91cd\u5199\u6587\u4ef6");
                            writeFileButton.addActionListener(e -> writeFileButtonActionPerformed(e));

                            //---- readFileButton ----
                            readFileButton.setText("\u8bfb\u6587\u4ef6");
                            readFileButton.addActionListener(e -> readFileButtonActionPerformed(e));

                            //---- addFileButton ----
                            addFileButton.setText("\u8ffd\u52a0\u6587\u4ef6");
                            addFileButton.addActionListener(e -> addFileButtonActionPerformed(e));

                            //======== panel8 ========
                            {
                                panel8.setLayout(new GridLayout(1, 4));

                                //======== scrollPane10 ========
                                {

                                    //---- fileNameList ----
                                    fileNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                    fileNameList.addListSelectionListener(e -> fileNameListValueChanged(e));
                                    scrollPane10.setViewportView(fileNameList);
                                }
                                panel8.add(scrollPane10);

                                //======== scrollPane11 ========
                                {
                                    scrollPane11.setViewportView(fileModeList);
                                }
                                panel8.add(scrollPane11);

                                //======== scrollPane13 ========
                                {
                                    scrollPane13.setViewportView(fileUserList);
                                }
                                panel8.add(scrollPane13);

                                //======== scrollPane12 ========
                                {
                                    scrollPane12.setViewportView(list3);
                                }
                                panel8.add(scrollPane12);
                            }

                            //---- createFile ----
                            createFile.setText("\u521b\u5efa\u6587\u4ef6");
                            createFile.addActionListener(e -> createFileActionPerformed(e));

                            //---- openFile ----
                            openFile.setText("\u6253\u5f00\u6587\u4ef6");
                            openFile.addActionListener(e -> fileDetailActionPerformed(e));

                            //---- getInDirectory ----
                            getInDirectory.setText("\u8fdb\u5165\u4e0b\u4e00\u7ea7");
                            getInDirectory.addActionListener(e -> getInDirectoryActionPerformed(e));

                            GroupLayout panel3Layout = new GroupLayout(panel3);
                            panel3.setLayout(panel3Layout);
                            panel3Layout.setHorizontalGroup(
                                panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel3Layout.createParallelGroup()
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addComponent(panel8, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(panel3Layout.createParallelGroup()
                                                    .addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                                                        .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(readFileButton, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                                            .addComponent(addFileButton, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                                            .addComponent(createFile, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(button1, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                                        .addGap(12, 12, 12))
                                                    .addGroup(panel3Layout.createSequentialGroup()
                                                        .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(openFile, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(writeFileButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                                                        .addContainerGap())
                                                    .addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                                                        .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(getInDirectory, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(deleteFile, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(11, 11, 11))))
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addGroup(panel3Layout.createParallelGroup()
                                                    .addGroup(panel3Layout.createSequentialGroup()
                                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(fileJump, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(panel3Layout.createSequentialGroup()
                                                        .addComponent(label3)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(filePathLabel)))
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            );
                            panel3Layout.setVerticalGroup(
                                panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label3)
                                            .addComponent(filePathLabel))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel3Layout.createParallelGroup()
                                            .addComponent(fileJump)
                                            .addComponent(textField1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(createFile)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel3Layout.createParallelGroup()
                                            .addComponent(panel8, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addComponent(openFile)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(writeFileButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(readFileButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(addFileButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(button1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(deleteFile)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(getInDirectory)))
                                        .addContainerGap(45, Short.MAX_VALUE))
                            );
                        }

                        //======== panel4 ========
                        {

                            //---- label6 ----
                            label6.setText("\u786c\u76d8\u6210\u7ec4\u7a7a\u95f2\u5757\u94fe");

                            //======== scrollPane3 ========
                            {
                                scrollPane3.setViewportView(groupLinkList);
                            }

                            //---- label7 ----
                            label7.setText("\u786c\u76d8\u7a7a\u95f2inode\u6570");

                            //---- inodeBarInDisk ----
                            inodeBarInDisk.setMaximum(512);
                            inodeBarInDisk.setOrientation(SwingConstants.VERTICAL);
                            inodeBarInDisk.setStringPainted(true);

                            //======== scrollPane4 ========
                            {
                                scrollPane4.setViewportView(inodeInMemoryList);
                            }

                            //---- label31 ----
                            label31.setText("\u5185\u5b58\u4e2dinode");

                            //---- label32 ----
                            label32.setText("\u5185\u5b58\u5269\u4f59\u53ef\u5b58inode\u6570");

                            //---- inodeBarInMemory ----
                            inodeBarInMemory.setMaximum(32);
                            inodeBarInMemory.setOrientation(SwingConstants.VERTICAL);
                            inodeBarInMemory.setStringPainted(true);

                            //---- inodeInDiskLabel ----
                            inodeInDiskLabel.setText("000/512");

                            //---- inodeInMemoryLabel ----
                            inodeInMemoryLabel.setText("00/32");

                            //---- label34 ----
                            label34.setText("\u5185\u5b58\u7a7a\u95f2\u5757");

                            //======== scrollPane5 ========
                            {
                                scrollPane5.setViewportView(freeBlockList);
                            }

                            //======== scrollPane9 ========
                            {
                                scrollPane9.setViewportView(systemOpenFileTable);
                            }

                            //---- label41 ----
                            label41.setText("\u7cfb\u7edf\u6253\u5f00\u6587\u4ef6\u8868");

                            //---- label42 ----
                            label42.setText("  fd-------------------ino");

                            GroupLayout panel4Layout = new GroupLayout(panel4);
                            panel4.setLayout(panel4Layout);
                            panel4Layout.setHorizontalGroup(
                                panel4Layout.createParallelGroup()
                                    .addGroup(GroupLayout.Alignment.TRAILING, panel4Layout.createSequentialGroup()
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(label41))
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(scrollPane9, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                                    .addComponent(label42, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))))
                                        .addGap(0, 33, Short.MAX_VALUE)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addComponent(label34)
                                            .addComponent(scrollPane5, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGap(9, 9, 9)
                                                .addComponent(label32))
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addGroup(panel4Layout.createParallelGroup()
                                                    .addGroup(panel4Layout.createSequentialGroup()
                                                        .addGap(6, 6, 6)
                                                        .addComponent(inodeInMemoryLabel))
                                                    .addComponent(inodeBarInMemory, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))))
                                        .addGap(18, 18, 18)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label31))
                                        .addGap(33, 33, 33)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addComponent(label6)
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addComponent(label7)
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addComponent(inodeInDiskLabel)
                                                    .addComponent(inodeBarInDisk, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))))
                                        .addContainerGap())
                            );
                            panel4Layout.setVerticalGroup(
                                panel4Layout.createParallelGroup()
                                    .addGroup(panel4Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label7)
                                            .addComponent(label6)
                                            .addComponent(label31)
                                            .addComponent(label32)
                                            .addComponent(label41)
                                            .addComponent(label34))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGroup(panel4Layout.createParallelGroup()
                                                    .addComponent(scrollPane3)
                                                    .addComponent(scrollPane4)
                                                    .addComponent(scrollPane5)
                                                    .addGroup(panel4Layout.createSequentialGroup()
                                                        .addComponent(inodeBarInDisk, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(inodeInDiskLabel)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                    .addGroup(GroupLayout.Alignment.TRAILING, panel4Layout.createSequentialGroup()
                                                        .addGap(0, 82, Short.MAX_VALUE)
                                                        .addGroup(panel4Layout.createParallelGroup()
                                                            .addGroup(GroupLayout.Alignment.TRAILING, panel4Layout.createSequentialGroup()
                                                                .addComponent(inodeBarInMemory, GroupLayout.PREFERRED_SIZE, 370, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(inodeInMemoryLabel))
                                                            .addComponent(scrollPane9, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 376, GroupLayout.PREFERRED_SIZE))))
                                                .addGap(33, 33, 33))
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addComponent(label42)
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            );
                        }

                        //---- testButton ----
                        testButton.setText("\u6d4b\u8bd5\u6309\u94ae");
                        testButton.addActionListener(e -> testButtonActionPerformed(e));

                        GroupLayout filePanelLayout = new GroupLayout(filePanel);
                        filePanel.setLayout(filePanelLayout);
                        filePanelLayout.setHorizontalGroup(
                            filePanelLayout.createParallelGroup()
                                .addGroup(filePanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panel3, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addGroup(filePanelLayout.createSequentialGroup()
                                            .addGap(61, 61, 61)
                                            .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, filePanelLayout.createSequentialGroup()
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 519, Short.MAX_VALUE)
                                            .addComponent(testButton)
                                            .addGap(285, 285, 285))))
                        );
                        filePanelLayout.setVerticalGroup(
                            filePanelLayout.createParallelGroup()
                                .addGroup(filePanelLayout.createSequentialGroup()
                                    .addGap(36, 36, 36)
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addComponent(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(filePanelLayout.createSequentialGroup()
                                            .addGap(11, 11, 11)
                                            .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 75, Short.MAX_VALUE)))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(filePanelLayout.createSequentialGroup()
                                            .addGap(123, 123, 123)
                                            .addComponent(testButton)))
                                    .addGap(66, 66, 66))
                        );
                    }
                    cardPanel.add(filePanel, "card3");

                    //======== devicePanel ========
                    {
                        devicePanel.setLayout(new GridLayout(1, 2));

                        //======== subDevicePanel1 ========
                        {

                            //---- label4 ----
                            label4.setText("spooling");

                            GroupLayout subDevicePanel1Layout = new GroupLayout(subDevicePanel1);
                            subDevicePanel1.setLayout(subDevicePanel1Layout);
                            subDevicePanel1Layout.setHorizontalGroup(
                                subDevicePanel1Layout.createParallelGroup()
                                    .addGroup(subDevicePanel1Layout.createSequentialGroup()
                                        .addGap(178, 178, 178)
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(168, Short.MAX_VALUE))
                            );
                            subDevicePanel1Layout.setVerticalGroup(
                                subDevicePanel1Layout.createParallelGroup()
                                    .addGroup(subDevicePanel1Layout.createSequentialGroup()
                                        .addGap(156, 156, 156)
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(478, Short.MAX_VALUE))
                            );
                        }
                        devicePanel.add(subDevicePanel1);

                        //======== devices ========
                        {

                            //======== panel5 ========
                            {

                                //======== scrollPane1 ========
                                {
                                    scrollPane1.setViewportView(blockContext);
                                }

                                //---- label5 ----
                                label5.setText("\u786c\u76d8\u4f7f\u7528\u60c5\u51b5");

                                //---- diskUsedSituation ----
                                diskUsedSituation.setText("\u517110485760B,\u5df2\u4f7f\u7528");

                                //---- diskBar ----
                                diskBar.setMaximum(10485760);
                                diskBar.setStringPainted(true);

                                //---- findBlockContext ----
                                findBlockContext.setText("\u67e5\u8be2\u76d8\u5757\u4e2d\u5185\u5bb9");
                                findBlockContext.addActionListener(e -> findBlockContextActionPerformed(e));

                                //---- label33 ----
                                label33.setText("\u5185\u5b58\u4f7f\u7528\u60c5\u51b5");

                                //---- diskUsedSituation2 ----
                                diskUsedSituation2.setText("\u517132768B,\u5df2\u4f7f\u7528");

                                //---- diskBar2 ----
                                diskBar2.setMaximum(32768);
                                diskBar2.setStringPainted(true);

                                //---- findBlockContext2 ----
                                findBlockContext2.setText("\u67e5\u8be2\u76d8\u5757\u4e2d\u5185\u5bb9");
                                findBlockContext2.addActionListener(e -> findBlockContext2ActionPerformed(e));

                                //======== scrollPane6 ========
                                {
                                    scrollPane6.setViewportView(blockContext2);
                                }

                                GroupLayout panel5Layout = new GroupLayout(panel5);
                                panel5.setLayout(panel5Layout);
                                panel5Layout.setHorizontalGroup(
                                    panel5Layout.createParallelGroup()
                                        .addGroup(panel5Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(panel5Layout.createParallelGroup()
                                                .addGroup(panel5Layout.createSequentialGroup()
                                                    .addComponent(label5)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(diskUsedSituation, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(diskBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                                                    .addComponent(findBlockContext)
                                                    .addGap(26, 26, 26))
                                                .addGroup(panel5Layout.createSequentialGroup()
                                                    .addComponent(label33)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(diskUsedSituation2, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(diskBar2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                                                    .addComponent(findBlockContext2)
                                                    .addGap(23, 23, 23))
                                                .addGroup(GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
                                                    .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(scrollPane6, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                                                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE))
                                                    .addContainerGap())))
                                );
                                panel5Layout.setVerticalGroup(
                                    panel5Layout.createParallelGroup()
                                        .addGroup(panel5Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label5, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(diskUsedSituation)
                                                    .addComponent(diskBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(findBlockContext, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label33, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(diskUsedSituation2)
                                                .addComponent(diskBar2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(findBlockContext2, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap())
                                );
                            }

                            GroupLayout devicesLayout = new GroupLayout(devices);
                            devices.setLayout(devicesLayout);
                            devicesLayout.setHorizontalGroup(
                                devicesLayout.createParallelGroup()
                                    .addGroup(devicesLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(panel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            );
                            devicesLayout.setVerticalGroup(
                                devicesLayout.createParallelGroup()
                                    .addGroup(devicesLayout.createSequentialGroup()
                                        .addComponent(panel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 720, Short.MAX_VALUE))
                            );
                        }
                        devicePanel.add(devices);
                    }
                    cardPanel.add(devicePanel, "card4");
                }
                contentPanel.add(cardPanel, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    public static JPanel dialogPane;
    public static JPanel contentPanel;
    public static JPanel buttonPanel;
    public static JButton workButton;
    public static JButton memoryButton;
    public static JButton fileButton;
    public static JButton deviceManage;
    public static JButton reinstallOS;
    public static JButton loadOSFile;
    public static JButton saveOS;
    public static JButton runSystem;
    public static JButton continueButton;
    public static JButton pauseButton;
    public static JButton showDownButton;
    public static JLabel timeLabel;
    public static JLabel allTimeLabel;
    public static JPanel cardPanel;
    public static JPanel workPanel;
    public static JPanel memoryPanel;
    public static JLabel label1;
    public static JLabel label2;
    public static JPanel filePanel;
    public static JPanel panel1;
    public static JLabel label9;
    public static JLabel fileName;
    public static JLabel label10;
    public static JLabel fileType;
    public static JLabel fileSize;
    public static JLabel label11;
    public static JLabel fileLink;
    public static JLabel label12;
    public static JLabel label13;
    public static JLabel fileCount;
    public static JLabel label14;
    public static JLabel fileSaveMode;
    public static JLabel label15;
    public static JLabel fileFID;
    public static JLabel label16;
    public static JLabel fileStructureP;
    public static JLabel label17;
    public static JLabel fileStructureL;
    public static JLabel label8;
    public static JLabel label28;
    public static JLabel createTime;
    public static JLabel label29;
    public static JLabel alterTime;
    public static JLabel label30;
    public static JLabel filePath;
    public static JPanel panel2;
    public static JLabel label18;
    public static JLabel directAddress1;
    public static JLabel label19;
    public static JLabel directAddress2;
    public static JLabel label20;
    public static JLabel directAddress3;
    public static JLabel label21;
    public static JLabel directAddress4;
    public static JLabel label22;
    public static JLabel directAddress5;
    public static JLabel directAddress6;
    public static JLabel label23;
    public static JLabel directAddress7;
    public static JLabel label24;
    public static JLabel directAddress8;
    public static JLabel label25;
    public static JLabel indirectAddress1;
    public static JLabel label26;
    public static JLabel indirectAddress2;
    public static JLabel label27;
    public static JPanel panel3;
    public static JTextField textField1;
    public static JButton fileJump;
    public static JButton deleteFile;
    public static JLabel label3;
    public static JLabel filePathLabel;
    public static JButton button1;
    public static JButton writeFileButton;
    public static JButton readFileButton;
    public static JButton addFileButton;
    public static JPanel panel8;
    public static JScrollPane scrollPane10;
    public static JList fileNameList;
    public static JScrollPane scrollPane11;
    public static JList fileModeList;
    public static JScrollPane scrollPane13;
    public static JList fileUserList;
    public static JScrollPane scrollPane12;
    public static JList list3;
    public static JButton createFile;
    public static JButton openFile;
    public static JButton getInDirectory;
    public static JPanel panel4;
    public static JLabel label6;
    public static JScrollPane scrollPane3;
    public static JList groupLinkList;
    public static JLabel label7;
    public static JProgressBar inodeBarInDisk;
    public static JScrollPane scrollPane4;
    public static JList inodeInMemoryList;
    public static JLabel label31;
    public static JLabel label32;
    public static JProgressBar inodeBarInMemory;
    public static JLabel inodeInDiskLabel;
    public static JLabel inodeInMemoryLabel;
    public static JLabel label34;
    public static JScrollPane scrollPane5;
    public static JList freeBlockList;
    public static JScrollPane scrollPane9;
    public static JList systemOpenFileTable;
    public static JLabel label41;
    public static JLabel label42;
    public static JButton testButton;
    public static JPanel devicePanel;
    public static JPanel subDevicePanel1;
    public static JLabel label4;
    public static JPanel devices;
    public static JPanel panel5;
    public static JScrollPane scrollPane1;
    public static JTextArea blockContext;
    public static JLabel label5;
    public static JLabel diskUsedSituation;
    public static JProgressBar diskBar;
    public static JButton findBlockContext;
    public static JLabel label33;
    public static JLabel diskUsedSituation2;
    public static JProgressBar diskBar2;
    public static JButton findBlockContext2;
    public static JScrollPane scrollPane6;
    public static JTextArea blockContext2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    /**
     * @Description: main
     * @param: [args]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/27 18:02
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI window = new GUI();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
