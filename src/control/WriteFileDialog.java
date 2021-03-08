/*
 * Created by JFormDesigner on Fri Feb 26 23:48:54 CST 2021
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
public class WriteFileDialog extends JDialog {
    public WriteFileDialog(Window owner) {
        super(owner);
        initComponents();
//        writeFileArea.setText("");
    }

    public void writeFileSureActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    public void writeFileCancelActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    public void okButtonActionPerformed(ActionEvent e) {
        try {
            File.writeFile(OS.topFile.getFd(),writeFileArea.getText());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null,exception,"写入时发生错误",JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    }

    public void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        writeFileArea = new JTextArea();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("\u91cd\u5199\u6587\u4ef6");
        setResizable(false);
        setMinimumSize(new Dimension(800, 600));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setMinimumSize(new Dimension(1200, 800));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setMinimumSize(new Dimension(1200, 800));
                contentPanel.setLayout(new BorderLayout());

                //======== scrollPane1 ========
                {
                    scrollPane1.setMinimumSize(new Dimension(1200, 800));

                    //---- writeFileArea ----
                    writeFileArea.setMinimumSize(new Dimension(1200, 800));
                    scrollPane1.setViewportView(writeFileArea);
                }
                contentPanel.add(scrollPane1, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("\u786e\u5b9a");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("\u53d6\u6d88");
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
    public static JScrollPane scrollPane1;
    public static JTextArea writeFileArea;
    public static JPanel buttonBar;
    public static JButton okButton;
    public static JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
