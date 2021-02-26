/*
 * Created by JFormDesigner on Sat Feb 27 00:17:29 CST 2021
 */

package control;

import fileManage.File;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Lu Ning
 */
public class createFileDialog extends JDialog {
    public createFileDialog(Window owner) {
        super(owner);
        initComponents();

    }
    public void okButtonActionPerformed(ActionEvent e) {
        try {
            short mode = 0,type = 0,size;
            String temp = (String) fileTypeBox.getSelectedItem();
            if(temp.equals("普通文件")){
                type = 1;
            } else if(temp.equals("目录文件")){
                type = 2;
            } else if(temp.equals("特殊文件")){
                type = 3;
            }
            temp = (String) fileSaveModeBox.getSelectedItem();
            if(temp.equals("无")){
                mode = 0;
            }else if(temp.equals("可运行")){
                mode = 1;
            }else if(temp.equals("可写")){
                mode = 2;
            }else if(temp.equals("可运行、写")){
                mode = 3;
            }else if(temp.equals("可读")){
                mode = 4;
            }else if(temp.equals("可读、运行")){
                mode = 5;
            }else if(temp.equals("可读、写")){
                mode = 6;
            }else if(temp.equals("可读、写、运行")){
                mode = 7;
            }
            String name = inputFileName.getText();
            if(name.equals("")){
                throw new Exception("未输入文件名");
            }
            size = Short.parseShort(inputFileSize.getText());
            File f =File.createFile(name,mode, type,size);
            System.out.println(name);
            OS.topFile = f;
            f.showFileInfomation();
        }catch (Exception e1){
            JOptionPane.showMessageDialog(null,e1.getMessage(),"创建时发生错误",JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
        dispose();
    }

    public void cancelButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        panel1 = new JPanel();
        label35 = new JLabel();
        inputFileName = new JTextField();
        label36 = new JLabel();
        fileTypeBox = new JComboBox<>();
        label37 = new JLabel();
        label38 = new JLabel();
        label39 = new JLabel();
        fileSaveModeBox = new JComboBox<>();
        label40 = new JLabel();
        inputFileSize = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("\u521b\u5efa\u6587\u4ef6");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //======== panel1 ========
                {

                    //---- label35 ----
                    label35.setText("\u6587\u4ef6\u540d");

                    //---- label36 ----
                    label36.setText("\u6587\u4ef6\u7c7b\u578b");

                    //---- fileTypeBox ----
                    fileTypeBox.setModel(new DefaultComboBoxModel<>(new String[] {
                        "\u666e\u901a\u6587\u4ef6",
                        "\u76ee\u5f55\u6587\u4ef6",
                        "\u7279\u6b8a\u6587\u4ef6"
                    }));

                    //---- label38 ----
                    label38.setText("\u5b58\u53d6\u6743\u9650");

                    //---- label39 ----
                    label39.setText("\u9884\u7559\u5757\u6570");

                    //---- fileSaveModeBox ----
                    fileSaveModeBox.setModel(new DefaultComboBoxModel<>(new String[] {
                        "\u65e0",
                        "\u53ef\u8fd0\u884c",
                        "\u53ef\u5199",
                        "\u53ef\u8fd0\u884c\u3001\u5199",
                        "\u53ef\u8bfb",
                        "\u53ef\u8bfb\u3001\u8fd0\u884c",
                        "\u53ef\u8bfb\u3001\u5199",
                        "\u53ef\u8bfb\u3001\u5199\u3001\u8fd0\u884c"
                    }));
                    fileSaveModeBox.setSelectedIndex(7);

                    //---- label40 ----
                    label40.setText("\u5f53\u524d\u8def\u5f84");

                    //---- inputFileSize ----
                    inputFileSize.setText("1");

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addContainerGap(21, Short.MAX_VALUE)
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addComponent(label35, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(inputFileName))
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addComponent(label36)
                                            .addGap(18, 18, 18)
                                            .addComponent(fileTypeBox))
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addComponent(label38)
                                            .addGap(18, 18, 18)
                                            .addComponent(fileSaveModeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(label37)
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addComponent(label39)
                                            .addGap(18, 18, 18)
                                            .addComponent(inputFileSize))
                                        .addComponent(label40))
                                    .addGap(0, 15, Short.MAX_VALUE)))
                            .addGap(0, 249, Short.MAX_VALUE)
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(label35)
                                            .addGap(11, 11, 11))
                                        .addComponent(inputFileName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGap(7, 7, 7)
                                            .addComponent(label36)
                                            .addGap(12, 12, 12))
                                        .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                            .addComponent(fileTypeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addGap(6, 6, 6)))
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGap(7, 7, 7)
                                            .addComponent(label38))
                                        .addComponent(fileSaveModeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)
                                    .addComponent(label37)
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGap(12, 12, 12)
                                            .addComponent(label39))
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(inputFileSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                    .addGap(39, 39, 39)
                                    .addComponent(label40)
                                    .addGap(0, 68, Short.MAX_VALUE)))
                            .addGap(0, 267, Short.MAX_VALUE)
                    );
                }
                contentPanel.add(panel1, BorderLayout.SOUTH);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    public static JPanel dialogPane;
    public static JPanel contentPanel;
    public static JPanel panel1;
    public static JLabel label35;
    public static JTextField inputFileName;
    public static JLabel label36;
    public static JComboBox<String> fileTypeBox;
    public static JLabel label37;
    public static JLabel label38;
    public static JLabel label39;
    public static JComboBox<String> fileSaveModeBox;
    public static JLabel label40;
    public static JTextField inputFileSize;
    public static JPanel buttonBar;
    public static JButton okButton;
    public static JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
