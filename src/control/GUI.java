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
import workManage.Primitives;
import workManage.Process;
import workManage.Queues;
import workManage.RequestFile;
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
        cardPanel.add(resourcePanel);
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
        try {
            OS.saveOSMirror();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        OS.showDown();

    }

    public void writeFileButtonActionPerformed(ActionEvent e) {
        new WriteFileDialog(this).setVisible(true);
    }

    public void readFileButtonActionPerformed(ActionEvent e) {
        new ReadFileDialog(this).setVisible(true);

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

    public void runFileActionPerformed(ActionEvent e) {
        try {

            Primitives.init(OS.topFile, Short.parseShort(JOptionPane.showInputDialog("请输入启动优先级（0-10），0最大",5)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void button2ActionPerformed(ActionEvent e) {
        Process p = Queues.readyQueue.get(0);
        Primitives.block(p,(short) 3);
    }

    public void button3ActionPerformed(ActionEvent e) {
        Process p = Queues.readyQueue.get(0);
        try {
            Primitives.hangup(p);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void button4ActionPerformed(ActionEvent e) {
        Process p = Queues.hangUpReadyQueue.get(0);
        try {
            Primitives.destroy(p);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void showProcessActionPerformed(ActionEvent e) {
        try {
            new processInfomationDialog(this).setVisible(true);
        }catch (Exception e1){
            JOptionPane.showMessageDialog(null,"没有在内存中进程选中一个进程");
        }

    }


    public void processInMemoryValueChanged(ListSelectionEvent e) {

        try {
            if (e.getValueIsAdjusting()){
                for(Process process : Process.processInMemory){
                    if (process.equals(processInMemory.getSelectedValue())){
                        OS.chooseProcess = process;
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void hangupReadyQueueValueChanged(ListSelectionEvent e) {
        try {
            if (e.getValueIsAdjusting()){
                for(Process process : Queues.hangUpReadyQueue){
                    if (process.equals(hangupReadyQueue.getSelectedValue())){
                        OS.chooseProcess = process;
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void hangupBlockQueueValueChanged(ListSelectionEvent e) {
        try {
            if (e.getValueIsAdjusting()){
                for(Process process : Queues.hangUpBlockedQueue){
                    if (process.equals(hangupBlockQueue.getSelectedValue())){
                        OS.chooseProcess = process;
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void runRequestFileActionPerformed(ActionEvent e) {
        try{
            RequestFile.readRequestFile(OS.topFile);
        }catch (Exception e2){
//            JOptionPane.showMessageDialog(null,"打开的不是作业请求文件或者文件内容不规范或运行程序数量不匹配");
            e2.printStackTrace();
        }
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        buttonPanel = new JPanel();
        fileButton = new JButton();
        deviceManage = new JButton();
        reinstallOS = new JButton();
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
        createFile = new JButton();
        openFile = new JButton();
        getInDirectory = new JButton();
        runFile = new JButton();
        runRequestFile = new JButton();
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
        panel6 = new JPanel();
        scrollPane7 = new JScrollPane();
        pcbPool = new JList();
        label35 = new JLabel();
        panel7 = new JPanel();
        scrollPane15 = new JScrollPane();
        systemPageTable = new JList();
        panel9 = new JPanel();
        label38 = new JLabel();
        label36 = new JLabel();
        label43 = new JLabel();
        panel10 = new JPanel();
        label39 = new JLabel();
        systemTime = new JLabel();
        label40 = new JLabel();
        IR = new JLabel();
        label45 = new JLabel();
        PC = new JLabel();
        label46 = new JLabel();
        workingProcessNum = new JLabel();
        label47 = new JLabel();
        cpuState = new JLabel();
        label60 = new JLabel();
        PSW = new JLabel();
        label61 = new JLabel();
        R0 = new JLabel();
        R1 = new JLabel();
        label62 = new JLabel();
        R2 = new JLabel();
        label63 = new JLabel();
        R3 = new JLabel();
        label64 = new JLabel();
        label44 = new JLabel();
        chosenProcess = new JLabel();
        scrollPane2 = new JScrollPane();
        processInMemory = new JList();
        label37 = new JLabel();
        showProcess = new JButton();
        label4 = new JLabel();
        scrollPane12 = new JScrollPane();
        jobReadyQueue = new JList();
        resourcePanel = new JPanel();
        processPanel = new JPanel();
        panel11 = new JPanel();
        label48 = new JLabel();
        scrollPane16 = new JScrollPane();
        readyQueue = new JList();
        panel12 = new JPanel();
        label49 = new JLabel();
        scrollPane17 = new JScrollPane();
        blockedQueue1 = new JList();
        panel13 = new JPanel();
        label50 = new JLabel();
        scrollPane18 = new JScrollPane();
        blockedQueue2 = new JList();
        panel14 = new JPanel();
        label51 = new JLabel();
        scrollPane19 = new JScrollPane();
        blockedQueue3 = new JList();
        panel15 = new JPanel();
        label52 = new JLabel();
        scrollPane20 = new JScrollPane();
        blockedQueue4 = new JList();
        panel16 = new JPanel();
        label53 = new JLabel();
        scrollPane21 = new JScrollPane();
        blockedQueue5 = new JList();
        panel17 = new JPanel();
        label54 = new JLabel();
        scrollPane22 = new JScrollPane();
        blockedQueue6 = new JList();
        panel18 = new JPanel();
        label55 = new JLabel();
        scrollPane23 = new JScrollPane();
        blockedQueue7 = new JList();
        panel19 = new JPanel();
        label56 = new JLabel();
        scrollPane24 = new JScrollPane();
        blockedQueue8 = new JList();
        panel21 = new JPanel();
        label58 = new JLabel();
        scrollPane26 = new JScrollPane();
        hangupReadyQueue = new JList();
        panel22 = new JPanel();
        label59 = new JLabel();
        scrollPane27 = new JScrollPane();
        hangupBlockQueue = new JList();
        scrollPane8 = new JScrollPane();
        outInfoArea = new JTextArea();
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
        panel20 = new JPanel();
        label66 = new JLabel();
        label75 = new JLabel();
        label71 = new JLabel();
        label74 = new JLabel();
        label67 = new JLabel();
        label70 = new JLabel();
        d01 = new JLabel();
        d11 = new JLabel();
        d21 = new JLabel();
        d31 = new JLabel();
        label77 = new JLabel();
        d02 = new JLabel();
        d12 = new JLabel();
        d22 = new JLabel();
        d32 = new JLabel();
        label81 = new JLabel();
        d03 = new JLabel();
        d13 = new JLabel();
        d23 = new JLabel();
        d33 = new JLabel();
        scrollPane14 = new JScrollPane();
        inputWell = new JTextArea();
        label57 = new JLabel();
        scrollPane25 = new JScrollPane();
        outputWell = new JTextArea();
        label65 = new JLabel();

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

                    //---- fileButton ----
                    fileButton.setText("\u6587\u4ef6\u7ba1\u7406\u9875\u9762");
                    fileButton.addActionListener(e -> fileButtonActionPerformed(e));
                    buttonPanel.add(fileButton);

                    //---- deviceManage ----
                    deviceManage.setText("\u8fdb\u7a0b\u8be6\u60c5\u548c\u8bbe\u5907\u7ba1\u7406");
                    deviceManage.addActionListener(e -> deviceManageActionPerformed(e));
                    buttonPanel.add(deviceManage);

                    //---- reinstallOS ----
                    reinstallOS.setText("\u91cd\u88c5\u64cd\u4f5c\u7cfb\u7edf");
                    reinstallOS.addActionListener(e -> reinstallOSActionPerformed(e));
                    buttonPanel.add(reinstallOS);

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
                            readFileButton.setText("\u8bfb\u53d6\u6587\u4ef6");
                            readFileButton.addActionListener(e -> readFileButtonActionPerformed(e));

                            //---- addFileButton ----
                            addFileButton.setText("\u8ffd\u52a0\u5185\u5bb9");
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
                            }

                            //---- createFile ----
                            createFile.setText("\u521b\u5efa\u6587\u4ef6");
                            createFile.addActionListener(e -> createFileActionPerformed(e));

                            //---- openFile ----
                            openFile.setText("\u6253\u5f00\u6587\u4ef6");
                            openFile.addActionListener(e -> fileDetailActionPerformed(e));

                            //---- getInDirectory ----
                            getInDirectory.setText("\u8fdb\u5165\u4e0b\u7ea7");
                            getInDirectory.addActionListener(e -> getInDirectoryActionPerformed(e));

                            //---- runFile ----
                            runFile.setText("\u8fd0\u884c\u6587\u4ef6");
                            runFile.addActionListener(e -> runFileActionPerformed(e));

                            //---- runRequestFile ----
                            runRequestFile.setText("\u8fd0\u884c\u4f5c\u4e1a\u8bf7\u6c42\u6587\u4ef6");
                            runRequestFile.addActionListener(e -> runRequestFileActionPerformed(e));

                            GroupLayout panel3Layout = new GroupLayout(panel3);
                            panel3.setLayout(panel3Layout);
                            panel3Layout.setHorizontalGroup(
                                panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel3Layout.createParallelGroup()
                                            .addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                                                .addComponent(panel8, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                                .addGroup(panel3Layout.createParallelGroup()
                                                    .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(panel3Layout.createParallelGroup()
                                                            .addComponent(deleteFile, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(panel3Layout.createSequentialGroup()
                                                                .addGroup(panel3Layout.createParallelGroup()
                                                                    .addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(readFileButton, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                                                        .addComponent(addFileButton, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                                                        .addComponent(createFile, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(button1, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(openFile, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(writeFileButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(1, 1, 1)))
                                                        .addComponent(runFile, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                                    .addGroup(panel3Layout.createSequentialGroup()
                                                        .addGap(1, 1, 1)
                                                        .addComponent(getInDirectory, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(14, 14, 14))
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addGroup(panel3Layout.createParallelGroup()
                                                    .addGroup(panel3Layout.createSequentialGroup()
                                                        .addComponent(label3)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(filePathLabel))
                                                    .addGroup(panel3Layout.createSequentialGroup()
                                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(fileJump, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(runRequestFile, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(97, Short.MAX_VALUE))))
                            );
                            panel3Layout.setVerticalGroup(
                                panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addGroup(panel3Layout.createParallelGroup()
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addGap(8, 8, 8)
                                                .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label3)
                                                    .addComponent(filePathLabel))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(fileJump)
                                                    .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                                .addGap(42, 42, 42)
                                                .addComponent(panel8, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addGap(69, 69, 69)
                                                .addComponent(createFile)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
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
                                                .addComponent(getInDirectory)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(runFile)))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(runRequestFile)
                                        .addContainerGap(8, Short.MAX_VALUE))
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
                                                .addGroup(panel4Layout.createParallelGroup()
                                                    .addComponent(label42, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(scrollPane9))))
                                        .addGap(7, 7, 7)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addComponent(label34)
                                            .addComponent(scrollPane5, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addComponent(label32)
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addGap(38, 38, 38)
                                                .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addComponent(inodeBarInMemory, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(inodeInMemoryLabel))))
                                        .addGap(18, 18, 18)
                                        .addGroup(panel4Layout.createParallelGroup()
                                            .addComponent(label31)
                                            .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
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
                                            .addComponent(label42)
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addComponent(inodeBarInDisk, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(inodeInDiskLabel))
                                            .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(scrollPane9, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(scrollPane5, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 412, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(scrollPane3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                                                .addComponent(scrollPane4, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
                                            .addGroup(panel4Layout.createSequentialGroup()
                                                .addComponent(inodeBarInMemory, GroupLayout.PREFERRED_SIZE, 367, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(inodeInMemoryLabel)))
                                        .addContainerGap(12, Short.MAX_VALUE))
                            );
                        }

                        //======== panel6 ========
                        {

                            //======== scrollPane7 ========
                            {
                                scrollPane7.setViewportView(pcbPool);
                            }

                            //---- label35 ----
                            label35.setText("PCB\u6c60");

                            //======== panel7 ========
                            {
                                panel7.setLayout(new BorderLayout());

                                //======== scrollPane15 ========
                                {

                                    //---- systemPageTable ----
                                    systemPageTable.setPreferredSize(new Dimension(38, 49));
                                    scrollPane15.setViewportView(systemPageTable);
                                }
                                panel7.add(scrollPane15, BorderLayout.CENTER);

                                //======== panel9 ========
                                {
                                    panel9.setOpaque(false);
                                    panel9.setMinimumSize(new Dimension(179, 40));

                                    //---- label38 ----
                                    label38.setText("\u903b\u8f91\u9875\u53f7");

                                    //---- label36 ----
                                    label36.setText("\u7cfb\u7edf\u7ea7\u9875\u8868");

                                    //---- label43 ----
                                    label43.setText("\u7269\u7406\u9875\u53f7");

                                    GroupLayout panel9Layout = new GroupLayout(panel9);
                                    panel9.setLayout(panel9Layout);
                                    panel9Layout.setHorizontalGroup(
                                        panel9Layout.createParallelGroup()
                                            .addGroup(panel9Layout.createSequentialGroup()
                                                .addComponent(label38)
                                                .addGap(71, 71, 71)
                                                .addComponent(label43)
                                                .addGap(0, 38, Short.MAX_VALUE))
                                            .addGroup(panel9Layout.createSequentialGroup()
                                                .addGap(82, 82, 82)
                                                .addComponent(label36)
                                                .addContainerGap(63, Short.MAX_VALUE))
                                    );
                                    panel9Layout.setVerticalGroup(
                                        panel9Layout.createParallelGroup()
                                            .addGroup(GroupLayout.Alignment.TRAILING, panel9Layout.createSequentialGroup()
                                                .addContainerGap(27, Short.MAX_VALUE)
                                                .addComponent(label36)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(panel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label38)
                                                    .addComponent(label43))
                                                .addGap(16, 16, 16))
                                    );
                                }
                                panel7.add(panel9, BorderLayout.NORTH);
                            }

                            //======== panel10 ========
                            {

                                //---- label39 ----
                                label39.setText("\u7cfb\u7edf\u65f6\u95f4\uff1a");

                                //---- systemTime ----
                                systemTime.setText("00");

                                //---- label40 ----
                                label40.setText("IR:");

                                //---- IR ----
                                IR.setText("00");

                                //---- label45 ----
                                label45.setText("PC:");

                                //---- PC ----
                                PC.setText("00");

                                //---- label46 ----
                                label46.setText("\u5f53\u524d\u8fd0\u884c\u8fdb\u7a0b\u53f7");

                                //---- workingProcessNum ----
                                workingProcessNum.setText("00");

                                //---- label47 ----
                                label47.setText("CPU\u72b6\u6001\uff1a");

                                //---- cpuState ----
                                cpuState.setText("00");

                                //---- label60 ----
                                label60.setText("PSW:");

                                //---- PSW ----
                                PSW.setText("00");

                                //---- label61 ----
                                label61.setText("R0:");

                                //---- R0 ----
                                R0.setText("00");

                                //---- R1 ----
                                R1.setText("00");

                                //---- label62 ----
                                label62.setText("R1:");

                                //---- R2 ----
                                R2.setText("00");

                                //---- label63 ----
                                label63.setText("R2:");

                                //---- R3 ----
                                R3.setText("00");

                                //---- label64 ----
                                label64.setText("R3:");

                                //---- label44 ----
                                label44.setText("\u5f53\u524d\u9009\u4e2d\u8fdb\u7a0b\u53f7");

                                //---- chosenProcess ----
                                chosenProcess.setText("text");

                                GroupLayout panel10Layout = new GroupLayout(panel10);
                                panel10.setLayout(panel10Layout);
                                panel10Layout.setHorizontalGroup(
                                    panel10Layout.createParallelGroup()
                                        .addGroup(panel10Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(panel10Layout.createParallelGroup()
                                                .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addGroup(GroupLayout.Alignment.LEADING, panel10Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(GroupLayout.Alignment.LEADING, panel10Layout.createSequentialGroup()
                                                            .addComponent(label60)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(PSW))
                                                        .addGroup(GroupLayout.Alignment.LEADING, panel10Layout.createSequentialGroup()
                                                            .addComponent(label45)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(PC))
                                                        .addGroup(GroupLayout.Alignment.LEADING, panel10Layout.createSequentialGroup()
                                                            .addComponent(label40)
                                                            .addGap(39, 39, 39)
                                                            .addComponent(IR)))
                                                    .addGroup(panel10Layout.createSequentialGroup()
                                                        .addGroup(panel10Layout.createParallelGroup()
                                                            .addGroup(panel10Layout.createSequentialGroup()
                                                                .addComponent(label39)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(systemTime))
                                                            .addGroup(panel10Layout.createSequentialGroup()
                                                                .addComponent(label47)
                                                                .addGap(6, 6, 6)
                                                                .addComponent(cpuState)))
                                                        .addGap(124, 124, 124)))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addComponent(label61)
                                                    .addGap(34, 34, 34)
                                                    .addComponent(R0))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addComponent(label62)
                                                    .addGap(34, 34, 34)
                                                    .addComponent(R1))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addComponent(label63)
                                                    .addGap(34, 34, 34)
                                                    .addComponent(R2))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addComponent(label64)
                                                    .addGap(34, 34, 34)
                                                    .addComponent(R3))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addComponent(label46)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(workingProcessNum))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addComponent(label44)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(chosenProcess))))
                                );
                                panel10Layout.setVerticalGroup(
                                    panel10Layout.createParallelGroup()
                                        .addGroup(panel10Layout.createSequentialGroup()
                                            .addGap(14, 14, 14)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label39)
                                                .addComponent(systemTime))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup()
                                                .addComponent(label47)
                                                .addComponent(cpuState))
                                            .addGap(13, 13, 13)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label40)
                                                .addComponent(IR, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label45)
                                                .addComponent(PC))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label60)
                                                .addComponent(PSW))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(label61, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(R0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup()
                                                .addComponent(label62)
                                                .addComponent(R1))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup()
                                                .addComponent(label63)
                                                .addComponent(R2))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup()
                                                .addComponent(label64)
                                                .addComponent(R3))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label46)
                                                .addComponent(workingProcessNum))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label44)
                                                .addComponent(chosenProcess))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );
                            }

                            //======== scrollPane2 ========
                            {

                                //---- processInMemory ----
                                processInMemory.addListSelectionListener(e -> processInMemoryValueChanged(e));
                                scrollPane2.setViewportView(processInMemory);
                            }

                            //---- label37 ----
                            label37.setText("\u5185\u5b58\u4e2d\u8fdb\u7a0b");

                            //---- showProcess ----
                            showProcess.setText("\u67e5\u770b\u9009\u4e2d\u8fdb\u7a0b\u8be6\u7ec6\u4fe1\u606f");
                            showProcess.addActionListener(e -> showProcessActionPerformed(e));

                            //---- label4 ----
                            label4.setText("\u540e\u5907\u4f5c\u4e1a\u961f\u5217");

                            //======== scrollPane12 ========
                            {
                                scrollPane12.setViewportView(jobReadyQueue);
                            }

                            GroupLayout panel6Layout = new GroupLayout(panel6);
                            panel6.setLayout(panel6Layout);
                            panel6Layout.setHorizontalGroup(
                                panel6Layout.createParallelGroup()
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addComponent(panel10, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(showProcess))
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addGap(54, 54, 54)
                                                .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addGap(70, 70, 70)
                                                .addComponent(label37)))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addGroup(GroupLayout.Alignment.TRAILING, panel6Layout.createSequentialGroup()
                                                .addComponent(scrollPane7, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(scrollPane12, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(GroupLayout.Alignment.TRAILING, panel6Layout.createSequentialGroup()
                                                .addComponent(label35)
                                                .addGap(64, 64, 64)
                                                .addComponent(label4)
                                                .addGap(14, 14, 14)))
                                        .addGap(26, 26, 26)
                                        .addComponent(panel7, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
                            );
                            panel6Layout.setVerticalGroup(
                                panel6Layout.createParallelGroup()
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(panel10, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(showProcess)
                                        .addContainerGap(127, Short.MAX_VALUE))
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(panel7, GroupLayout.PREFERRED_SIZE, 361, GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label37)
                                                    .addComponent(label35)
                                                    .addComponent(label4))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                                    .addComponent(scrollPane12, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                                    .addComponent(scrollPane7, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))))
                                        .addContainerGap(99, Short.MAX_VALUE))
                            );
                        }

                        GroupLayout filePanelLayout = new GroupLayout(filePanel);
                        filePanel.setLayout(filePanelLayout);
                        filePanelLayout.setHorizontalGroup(
                            filePanelLayout.createParallelGroup()
                                .addGroup(filePanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGap(58, 58, 58)
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addComponent(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(filePanelLayout.createSequentialGroup()
                                            .addComponent(panel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE)))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        );
                        filePanelLayout.setVerticalGroup(
                            filePanelLayout.createParallelGroup()
                                .addGroup(filePanelLayout.createSequentialGroup()
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addGroup(filePanelLayout.createSequentialGroup()
                                            .addGap(19, 19, 19)
                                            .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(filePanelLayout.createSequentialGroup()
                                            .addGap(47, 47, 47)
                                            .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(filePanelLayout.createParallelGroup()
                                        .addGroup(filePanelLayout.createSequentialGroup()
                                            .addGap(43, 43, 43)
                                            .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap())
                                        .addGroup(GroupLayout.Alignment.TRAILING, filePanelLayout.createSequentialGroup()
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(panel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                        );
                    }
                    cardPanel.add(filePanel, "card3");

                    //======== resourcePanel ========
                    {
                        resourcePanel.setLayout(new GridLayout(1, 2));

                        //======== processPanel ========
                        {

                            //======== panel11 ========
                            {
                                panel11.setLayout(new BorderLayout(1, 1));

                                //---- label48 ----
                                label48.setText("   \u5c31\u7eea\u961f\u5217");
                                panel11.add(label48, BorderLayout.NORTH);

                                //======== scrollPane16 ========
                                {
                                    scrollPane16.setViewportView(readyQueue);
                                }
                                panel11.add(scrollPane16, BorderLayout.CENTER);
                            }

                            //======== panel12 ========
                            {
                                panel12.setLayout(new BorderLayout(1, 1));

                                //---- label49 ----
                                label49.setText("\u963b\u585e\u961f\u52171");
                                panel12.add(label49, BorderLayout.NORTH);

                                //======== scrollPane17 ========
                                {
                                    scrollPane17.setViewportView(blockedQueue1);
                                }
                                panel12.add(scrollPane17, BorderLayout.CENTER);
                            }

                            //======== panel13 ========
                            {
                                panel13.setLayout(new BorderLayout(1, 1));

                                //---- label50 ----
                                label50.setText("\u963b\u585e\u961f\u52172");
                                panel13.add(label50, BorderLayout.NORTH);

                                //======== scrollPane18 ========
                                {
                                    scrollPane18.setViewportView(blockedQueue2);
                                }
                                panel13.add(scrollPane18, BorderLayout.CENTER);
                            }

                            //======== panel14 ========
                            {
                                panel14.setLayout(new BorderLayout(1, 1));

                                //---- label51 ----
                                label51.setText(" \u963b\u585e\u961f\u52173");
                                panel14.add(label51, BorderLayout.NORTH);

                                //======== scrollPane19 ========
                                {
                                    scrollPane19.setViewportView(blockedQueue3);
                                }
                                panel14.add(scrollPane19, BorderLayout.CENTER);
                            }

                            //======== panel15 ========
                            {
                                panel15.setLayout(new BorderLayout(1, 1));

                                //---- label52 ----
                                label52.setText(" \u963b\u585e\u961f\u52174");
                                panel15.add(label52, BorderLayout.NORTH);

                                //======== scrollPane20 ========
                                {
                                    scrollPane20.setViewportView(blockedQueue4);
                                }
                                panel15.add(scrollPane20, BorderLayout.CENTER);
                            }

                            //======== panel16 ========
                            {
                                panel16.setLayout(new BorderLayout(1, 1));

                                //---- label53 ----
                                label53.setText(" \u963b\u585e\u961f\u52175");
                                panel16.add(label53, BorderLayout.NORTH);

                                //======== scrollPane21 ========
                                {
                                    scrollPane21.setViewportView(blockedQueue5);
                                }
                                panel16.add(scrollPane21, BorderLayout.CENTER);
                            }

                            //======== panel17 ========
                            {
                                panel17.setLayout(new BorderLayout(1, 1));

                                //---- label54 ----
                                label54.setText(" \u963b\u585e\u961f\u52176");
                                panel17.add(label54, BorderLayout.NORTH);

                                //======== scrollPane22 ========
                                {
                                    scrollPane22.setViewportView(blockedQueue6);
                                }
                                panel17.add(scrollPane22, BorderLayout.CENTER);
                            }

                            //======== panel18 ========
                            {
                                panel18.setLayout(new BorderLayout(1, 1));

                                //---- label55 ----
                                label55.setText(" \u963b\u585e\u961f\u52177");
                                panel18.add(label55, BorderLayout.NORTH);

                                //======== scrollPane23 ========
                                {
                                    scrollPane23.setViewportView(blockedQueue7);
                                }
                                panel18.add(scrollPane23, BorderLayout.CENTER);
                            }

                            //======== panel19 ========
                            {
                                panel19.setLayout(new BorderLayout(1, 1));

                                //---- label56 ----
                                label56.setText(" \u963b\u585e\u961f\u52178");
                                panel19.add(label56, BorderLayout.NORTH);

                                //======== scrollPane24 ========
                                {
                                    scrollPane24.setViewportView(blockedQueue8);
                                }
                                panel19.add(scrollPane24, BorderLayout.CENTER);
                            }

                            //======== panel21 ========
                            {
                                panel21.setLayout(new BorderLayout(1, 1));

                                //---- label58 ----
                                label58.setText("\u6302\u8d77\u5c31\u7eea");
                                panel21.add(label58, BorderLayout.NORTH);

                                //======== scrollPane26 ========
                                {

                                    //---- hangupReadyQueue ----
                                    hangupReadyQueue.addListSelectionListener(e -> hangupReadyQueueValueChanged(e));
                                    scrollPane26.setViewportView(hangupReadyQueue);
                                }
                                panel21.add(scrollPane26, BorderLayout.CENTER);
                            }

                            //======== panel22 ========
                            {
                                panel22.setLayout(new BorderLayout(1, 1));

                                //---- label59 ----
                                label59.setText("\u6302\u8d77\u963b\u585e");
                                panel22.add(label59, BorderLayout.NORTH);

                                //======== scrollPane27 ========
                                {

                                    //---- hangupBlockQueue ----
                                    hangupBlockQueue.addListSelectionListener(e -> hangupBlockQueueValueChanged(e));
                                    scrollPane27.setViewportView(hangupBlockQueue);
                                }
                                panel22.add(scrollPane27, BorderLayout.CENTER);
                            }

                            //======== scrollPane8 ========
                            {
                                scrollPane8.setViewportView(outInfoArea);
                            }

                            GroupLayout processPanelLayout = new GroupLayout(processPanel);
                            processPanel.setLayout(processPanelLayout);
                            processPanelLayout.setHorizontalGroup(
                                processPanelLayout.createParallelGroup()
                                    .addGroup(processPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(processPanelLayout.createParallelGroup()
                                            .addComponent(scrollPane8, GroupLayout.PREFERRED_SIZE, 584, GroupLayout.PREFERRED_SIZE)
                                            .addGroup(processPanelLayout.createSequentialGroup()
                                                .addGroup(processPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addGroup(processPanelLayout.createSequentialGroup()
                                                        .addComponent(panel21, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(panel22, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(processPanelLayout.createSequentialGroup()
                                                        .addComponent(panel11, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(panel12, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(12, 12, 12)
                                                        .addComponent(panel13, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(processPanelLayout.createParallelGroup()
                                                    .addGroup(processPanelLayout.createSequentialGroup()
                                                        .addComponent(panel14, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(panel15, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(panel16, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(panel17, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(processPanelLayout.createSequentialGroup()
                                                        .addComponent(panel18, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(panel19, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))))
                                        .addContainerGap(24, Short.MAX_VALUE))
                            );
                            processPanelLayout.setVerticalGroup(
                                processPanelLayout.createParallelGroup()
                                    .addGroup(processPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(processPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(panel17, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel16, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel15, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel14, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel13, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel12, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel11, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(processPanelLayout.createParallelGroup()
                                            .addComponent(panel21, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel18, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel19, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(panel22, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addComponent(scrollPane8, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                            );
                        }
                        resourcePanel.add(processPanel);

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
                                                        .addComponent(scrollPane6, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
                                                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE))
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

                            //======== panel20 ========
                            {
                                panel20.setLayout(new GridLayout(4, 5, 5, 5));

                                //---- label66 ----
                                label66.setText("\u8bbe\u5907\u53f7:");
                                panel20.add(label66);

                                //---- label75 ----
                                label75.setText("1");
                                panel20.add(label75);

                                //---- label71 ----
                                label71.setText("2");
                                panel20.add(label71);

                                //---- label74 ----
                                label74.setText("3");
                                panel20.add(label74);

                                //---- label67 ----
                                label67.setText("4");
                                panel20.add(label67);

                                //---- label70 ----
                                label70.setText("\u8bbe\u5907\u540d:");
                                panel20.add(label70);

                                //---- d01 ----
                                d01.setText("text");
                                panel20.add(d01);

                                //---- d11 ----
                                d11.setText("text");
                                panel20.add(d11);
                                panel20.add(d21);

                                //---- d31 ----
                                d31.setText("text");
                                panel20.add(d31);

                                //---- label77 ----
                                label77.setText("\u5904\u7406\u6240\u9700\u65f6\u95f4:");
                                panel20.add(label77);

                                //---- d02 ----
                                d02.setText("text");
                                panel20.add(d02);
                                panel20.add(d12);

                                //---- d22 ----
                                d22.setText("text");
                                panel20.add(d22);

                                //---- d32 ----
                                d32.setText("text");
                                panel20.add(d32);

                                //---- label81 ----
                                label81.setText("\u4e2d\u65ad\u5411\u91cf\u5730\u5740");
                                panel20.add(label81);

                                //---- d03 ----
                                d03.setText("text");
                                panel20.add(d03);

                                //---- d13 ----
                                d13.setText("text");
                                panel20.add(d13);

                                //---- d23 ----
                                d23.setText("text");
                                panel20.add(d23);

                                //---- d33 ----
                                d33.setText("text");
                                panel20.add(d33);
                            }

                            //======== scrollPane14 ========
                            {
                                scrollPane14.setViewportView(inputWell);
                            }

                            //---- label57 ----
                            label57.setText("Spooling\u8f93\u5165\u4e95");

                            //======== scrollPane25 ========
                            {
                                scrollPane25.setViewportView(outputWell);
                            }

                            //---- label65 ----
                            label65.setText("Spooling\u8f93\u51fa\u4e95");

                            GroupLayout devicesLayout = new GroupLayout(devices);
                            devices.setLayout(devicesLayout);
                            devicesLayout.setHorizontalGroup(
                                devicesLayout.createParallelGroup()
                                    .addGroup(devicesLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(devicesLayout.createParallelGroup()
                                            .addGroup(devicesLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(panel20, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(scrollPane14, GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
                                            .addGroup(devicesLayout.createSequentialGroup()
                                                .addComponent(panel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(devicesLayout.createSequentialGroup()
                                                .addComponent(scrollPane25, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(GroupLayout.Alignment.TRAILING, devicesLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(devicesLayout.createParallelGroup()
                                                    .addGroup(GroupLayout.Alignment.TRAILING, devicesLayout.createSequentialGroup()
                                                        .addComponent(label57)
                                                        .addGap(301, 301, 301))
                                                    .addGroup(GroupLayout.Alignment.TRAILING, devicesLayout.createSequentialGroup()
                                                        .addComponent(label65)
                                                        .addGap(299, 299, 299))))))
                            );
                            devicesLayout.setVerticalGroup(
                                devicesLayout.createParallelGroup()
                                    .addGroup(devicesLayout.createSequentialGroup()
                                        .addComponent(panel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(panel20, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(label57)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane14, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(label65, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane25, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 302, Short.MAX_VALUE))
                            );
                        }
                        resourcePanel.add(devices);
                    }
                    cardPanel.add(resourcePanel, "card4");
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
    public static JButton fileButton;
    public static JButton deviceManage;
    public static JButton reinstallOS;
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
    public static JButton createFile;
    public static JButton openFile;
    public static JButton getInDirectory;
    public static JButton runFile;
    public static JButton runRequestFile;
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
    public static JPanel panel6;
    public static JScrollPane scrollPane7;
    public static JList pcbPool;
    public static JLabel label35;
    public static JPanel panel7;
    public static JScrollPane scrollPane15;
    public static JList systemPageTable;
    public static JPanel panel9;
    public static JLabel label38;
    public static JLabel label36;
    public static JLabel label43;
    public static JPanel panel10;
    public static JLabel label39;
    public static JLabel systemTime;
    public static JLabel label40;
    public static JLabel IR;
    public static JLabel label45;
    public static JLabel PC;
    public static JLabel label46;
    public static JLabel workingProcessNum;
    public static JLabel label47;
    public static JLabel cpuState;
    public static JLabel label60;
    public static JLabel PSW;
    public static JLabel label61;
    public static JLabel R0;
    public static JLabel R1;
    public static JLabel label62;
    public static JLabel R2;
    public static JLabel label63;
    public static JLabel R3;
    public static JLabel label64;
    public static JLabel label44;
    public static JLabel chosenProcess;
    public static JScrollPane scrollPane2;
    public static JList processInMemory;
    public static JLabel label37;
    public static JButton showProcess;
    public static JLabel label4;
    public static JScrollPane scrollPane12;
    public static JList jobReadyQueue;
    public static JPanel resourcePanel;
    public static JPanel processPanel;
    public static JPanel panel11;
    public static JLabel label48;
    public static JScrollPane scrollPane16;
    public static JList readyQueue;
    public static JPanel panel12;
    public static JLabel label49;
    public static JScrollPane scrollPane17;
    public static JList blockedQueue1;
    public static JPanel panel13;
    public static JLabel label50;
    public static JScrollPane scrollPane18;
    public static JList blockedQueue2;
    public static JPanel panel14;
    public static JLabel label51;
    public static JScrollPane scrollPane19;
    public static JList blockedQueue3;
    public static JPanel panel15;
    public static JLabel label52;
    public static JScrollPane scrollPane20;
    public static JList blockedQueue4;
    public static JPanel panel16;
    public static JLabel label53;
    public static JScrollPane scrollPane21;
    public static JList blockedQueue5;
    public static JPanel panel17;
    public static JLabel label54;
    public static JScrollPane scrollPane22;
    public static JList blockedQueue6;
    public static JPanel panel18;
    public static JLabel label55;
    public static JScrollPane scrollPane23;
    public static JList blockedQueue7;
    public static JPanel panel19;
    public static JLabel label56;
    public static JScrollPane scrollPane24;
    public static JList blockedQueue8;
    public static JPanel panel21;
    public static JLabel label58;
    public static JScrollPane scrollPane26;
    public static JList hangupReadyQueue;
    public static JPanel panel22;
    public static JLabel label59;
    public static JScrollPane scrollPane27;
    public static JList hangupBlockQueue;
    public static JScrollPane scrollPane8;
    public static JTextArea outInfoArea;
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
    public static JPanel panel20;
    public static JLabel label66;
    public static JLabel label75;
    public static JLabel label71;
    public static JLabel label74;
    public static JLabel label67;
    public static JLabel label70;
    public static JLabel d01;
    public static JLabel d11;
    public static JLabel d21;
    public static JLabel d31;
    public static JLabel label77;
    public static JLabel d02;
    public static JLabel d12;
    public static JLabel d22;
    public static JLabel d32;
    public static JLabel label81;
    public static JLabel d03;
    public static JLabel d13;
    public static JLabel d23;
    public static JLabel d33;
    public static JScrollPane scrollPane14;
    public static JTextArea inputWell;
    public static JLabel label57;
    public static JScrollPane scrollPane25;
    public static JTextArea outputWell;
    public static JLabel label65;
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
