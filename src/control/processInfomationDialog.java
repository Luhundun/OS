/*
 * Created by JFormDesigner on Wed Mar 03 20:15:30 CST 2021
 */

package control;

import java.awt.event.*;
import workManage.Process;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Lu
 */
public class processInfomationDialog extends JDialog {
    public processInfomationDialog(Window owner) {
        super(owner);
        initComponents();
        Process p = OS.chooseProcess;
        label1.setText("进程号:"+p.getPcb().getPid());
        label2.setText("进程优先级:"+p.getPcb().getProcessPriority());
        label3.setText("内存位置:"+(16+4*p.getPcb().getIndexInMemory())+"-"+(16+4*p.getPcb().getIndexInMemory()+3));
        label4.setText("交换区ino:"+p.getPcb().getVirtualMemoryInDisk());
        label5.setText("创建时间:"+p.getPcb().getInTimes());
        label6.setText("运行时间:"+p.getPcb().getRunTimes());
        label7.setText("剩余时间片:"+p.getPcb().getTimeSliceLeft());
        label8.setText("进程状态:"+p.getPcb().getProcessState());
        label9.setText("所在目录:"+p.getPcb().getDirectoryIno());
        label10.setText("进入相应队列时间:"+p.getPcb().getInQueueTime());
        label11.setText("psw:"+p.getPcb().getPsw());
        label12.setText("r0:"+p.getPcb().getR0());
        label13.setText("r1:"+p.getPcb().getR1());
        label14.setText("r2:"+p.getPcb().getR2());
        label15.setText("r3:"+p.getPcb().getR3());
        label16.setText("ir:"+p.getPcb().getIr());
        label17.setText("pc:"+p.getPcb().getPc());
//        label18.setText("内存中第一页块号:"+p.getBlocksInMemory());
        label22.setText("最近访问页面:"+p.getUserStack());
        label23.setText("指令总数:"+p.getPcb().getInstructionNum());
        label24.setText("当前执行指令数cx:"+p.getPcb().getCx());
        label25.setText("当前系统时间:"+OS.getTime());
    }

    public void okButtonActionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        label14 = new JLabel();
        label15 = new JLabel();
        label16 = new JLabel();
        label17 = new JLabel();
        label18 = new JLabel();
        label19 = new JLabel();
        label20 = new JLabel();
        label21 = new JLabel();
        label22 = new JLabel();
        label23 = new JLabel();
        label24 = new JLabel();
        label25 = new JLabel();
        label26 = new JLabel();
        label27 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setMinimumSize(new Dimension(500, 400));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setMinimumSize(new Dimension(500, 400));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setMinimumSize(new Dimension(500, 400));
                contentPanel.setLayout(new GridLayout(9, 3));

                //---- label1 ----
                label1.setText("\u8fdb\u7a0b\u53f7:");
                contentPanel.add(label1);

                //---- label2 ----
                label2.setText("\u8fdb\u7a0b\u4f18\u5148\u7ea7:");
                contentPanel.add(label2);

                //---- label3 ----
                label3.setText("\u8fdb\u7a0b\u4f4d\u7f6e\uff1a");
                contentPanel.add(label3);

                //---- label4 ----
                label4.setText("\u4ea4\u6362\u533aino:");
                contentPanel.add(label4);

                //---- label5 ----
                label5.setText("\u521b\u5efa\u65f6\u95f4\uff1a");
                contentPanel.add(label5);

                //---- label6 ----
                label6.setText("\u8fd0\u884c\u65f6\u95f4\uff1a");
                contentPanel.add(label6);

                //---- label7 ----
                label7.setText("\u5269\u4f59\u65f6\u95f4\u7247:");
                contentPanel.add(label7);

                //---- label8 ----
                label8.setText("\u8fdb\u7a0b\u72b6\u6001:");
                contentPanel.add(label8);

                //---- label9 ----
                label9.setText("\u8fdb\u7a0b\u6240\u5728\u76ee\u5f55:");
                contentPanel.add(label9);

                //---- label10 ----
                label10.setText("\u8fdb\u5165\u76f8\u5e94\u961f\u5217\u65f6\u957f:");
                contentPanel.add(label10);

                //---- label11 ----
                label11.setText("psw:");
                contentPanel.add(label11);

                //---- label12 ----
                label12.setText("r0:");
                contentPanel.add(label12);

                //---- label13 ----
                label13.setText("r1:");
                contentPanel.add(label13);

                //---- label14 ----
                label14.setText("r2:");
                contentPanel.add(label14);

                //---- label15 ----
                label15.setText("r3:");
                contentPanel.add(label15);

                //---- label16 ----
                label16.setText("ir:");
                contentPanel.add(label16);

                //---- label17 ----
                label17.setText("pc:");
                contentPanel.add(label17);

                //---- label18 ----
                label18.setText("\u5185\u5b58\u7b2c\u4e00\u5757\u7269\u7406\u9875\u53f7\uff1a");
                contentPanel.add(label18);

                //---- label19 ----
                label19.setText("\u5185\u5b58\u7b2c\u4e8c\u5757\u7269\u7406\u9875\u53f7\uff1a");
                contentPanel.add(label19);

                //---- label20 ----
                label20.setText("\u5185\u5b58\u7b2c\u4e09\u5757\u7269\u7406\u9875\u53f7\uff1a");
                contentPanel.add(label20);

                //---- label21 ----
                label21.setText("\u5185\u5b58\u7b2c\u56db\u5757\u7269\u7406\u9875\u53f7\uff1a");
                contentPanel.add(label21);

                //---- label22 ----
                label22.setText("\u6700\u8fd1\u8bbf\u95ee\u9875\u9762:");
                contentPanel.add(label22);

                //---- label23 ----
                label23.setText("\u6307\u4ee4\u6570\u76ee\uff1a");
                contentPanel.add(label23);

                //---- label24 ----
                label24.setText("\u5f53\u524d\u6267\u884c\u6307\u4ee4\u6570cx:");
                contentPanel.add(label24);

                //---- label25 ----
                label25.setText("\u5f53\u524d\u7cfb\u7edf\u65f6\u95f4\u70b9\uff1a");
                contentPanel.add(label25);

                //---- label26 ----
                label26.setText("text");
                contentPanel.add(label26);

                //---- label27 ----
                label27.setText("text");
                contentPanel.add(label27);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
    public static JLabel label1;
    public static JLabel label2;
    public static JLabel label3;
    public static JLabel label4;
    public static JLabel label5;
    public static JLabel label6;
    public static JLabel label7;
    public static JLabel label8;
    public static JLabel label9;
    public static JLabel label10;
    public static JLabel label11;
    public static JLabel label12;
    public static JLabel label13;
    public static JLabel label14;
    public static JLabel label15;
    public static JLabel label16;
    public static JLabel label17;
    public static JLabel label18;
    public static JLabel label19;
    public static JLabel label20;
    public static JLabel label21;
    public static JLabel label22;
    public static JLabel label23;
    public static JLabel label24;
    public static JLabel label25;
    public static JLabel label26;
    public static JLabel label27;
    public static JPanel buttonBar;
    public static JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
