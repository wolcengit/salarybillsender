package wolcen.salarybillsender;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ConfigBillPanel extends JPanel {
    private static Logger logger = Logger.getLogger(ConfigBillPanel.class);

    private JTextField tfBillFile;
    private JLabel label_4;
    private JTextField tfBillTitleRow;
    private JLabel label_5;
    private JTextField tfBillEmailColumn;
    private JLabel label_6;
    private JTextField tfBillStartColumn;
    private JLabel label_7;
    private JTextField tfBillEndColumn;
    private JLabel label_8;
    private JTextField tfEmailSubject;
    private JPanel panel_1;
    private JLabel label_9;
    private JButton btnFile;

    public ConfigBillPanel() {
        super();
        this.initComponents();
        ((AbstractDocument) this.tfBillTitleRow.getDocument()).setDocumentFilter(new DocumentFilter4Integer());
        ((AbstractDocument) this.tfBillEmailColumn.getDocument()).setDocumentFilter(new DocumentFilter4UpperCharacter());
        this.tfBillEmailColumn.addKeyListener(new KeyAdapter4InputLength(2, this.tfBillEmailColumn));
        ((AbstractDocument) this.tfBillStartColumn.getDocument()).setDocumentFilter(new DocumentFilter4UpperCharacter());
        this.tfBillStartColumn.addKeyListener(new KeyAdapter4InputLength(2, this.tfBillStartColumn));
        ((AbstractDocument) this.tfBillEndColumn.getDocument()).setDocumentFilter(new DocumentFilter4UpperCharacter());
        this.tfBillEndColumn.addKeyListener(new KeyAdapter4InputLength(2, this.tfBillEndColumn));
    }

    private void initComponents() {
        this.setLayout(null);
        this.panel_1 = new JPanel();
        this.panel_1.setBounds(10, 10, 670, 118);
        this.panel_1.setLayout(null);
        this.panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "邮件信息", 4, 2, null, new Color(0, 0, 0)));
        this.tfBillFile = new JTextField();
        this.tfBillFile.setText("工资单发送.xls");
        this.tfBillFile.setColumns(10);
        this.tfBillFile.setBounds(114, 20, 468, 21);
        this.panel_1.add(this.tfBillFile);
        this.label_4 = new JLabel("标题行：");
        this.label_4.setHorizontalAlignment(4);
        this.label_4.setBounds(10, 48, 94, 20);
        this.panel_1.add(this.label_4);
        this.tfBillTitleRow = new JTextField();
        this.tfBillTitleRow.setText("2");
        this.tfBillTitleRow.setColumns(10);
        this.tfBillTitleRow.setBounds(114, 48, 61, 21);
        this.panel_1.add(this.tfBillTitleRow);
        this.label_5 = new JLabel("邮箱列：");
        this.label_5.setHorizontalAlignment(4);
        this.label_5.setBounds(185, 48, 75, 20);
        this.panel_1.add(this.label_5);
        this.tfBillEmailColumn = new JTextField();
        this.tfBillEmailColumn.setText("C");
        this.tfBillEmailColumn.setColumns(10);
        this.tfBillEmailColumn.setBounds(270, 48, 61, 21);
        this.panel_1.add(this.tfBillEmailColumn);
        this.label_6 = new JLabel("起始列：");
        this.label_6.setHorizontalAlignment(4);
        this.label_6.setBounds(352, 48, 75, 20);
        this.panel_1.add(this.label_6);
        this.tfBillStartColumn = new JTextField();
        this.tfBillStartColumn.setText("A");
        this.tfBillStartColumn.setColumns(10);
        this.tfBillStartColumn.setBounds(437, 48, 61, 21);
        this.panel_1.add(this.tfBillStartColumn);
        this.label_7 = new JLabel("终止列：");
        this.label_7.setHorizontalAlignment(4);
        this.label_7.setBounds(507, 48, 75, 20);
        this.panel_1.add(this.label_7);
        this.tfBillEndColumn = new JTextField();
        this.tfBillEndColumn.setText("AD");
        this.tfBillEndColumn.setColumns(10);
        this.tfBillEndColumn.setBounds(592, 48, 61, 21);
        this.panel_1.add(this.tfBillEndColumn);
        this.label_8 = new JLabel("邮件主题：");
        this.label_8.setHorizontalAlignment(4);
        this.label_8.setBounds(10, 78, 94, 20);
        this.panel_1.add(this.label_8);
        this.tfEmailSubject = new JTextField();
        this.tfEmailSubject.setText("{cell:2}{date:yyyy年mm月}工资单");
        this.tfEmailSubject.setColumns(10);
        this.tfEmailSubject.setBounds(114, 78, 539, 21);
        this.panel_1.add(this.tfEmailSubject);
        this.label_9 = new JLabel("名单文件：");
        this.label_9.setHorizontalAlignment(4);
        this.label_9.setBounds(12, 23, 92, 15);
        this.panel_1.add(this.label_9);
        this.btnFile = new JButton("选择");
        this.btnFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                do_btnFile_actionPerformed(e);
            }
        });
        this.btnFile.setBounds(589, 19, 64, 23);
        this.panel_1.add(this.btnFile);
        this.add(this.panel_1);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.btnFile.setEnabled(enabled);
        this.tfBillFile.setEnabled(enabled);
        this.tfBillTitleRow.setEnabled(enabled);
        this.tfBillEmailColumn.setEnabled(enabled);
        this.tfBillStartColumn.setEnabled(enabled);
        this.tfBillEndColumn.setEnabled(enabled);
        this.tfEmailSubject.setEnabled(enabled);
    }

    public void set(final SalarybillSender salarybill) {
        this.tfBillFile.setText(salarybill.getSalarybillFile());
        this.tfBillTitleRow.setText(salarybill.getSalarybillTitleRow());
        this.tfBillEmailColumn.setText(salarybill.getSalarybillEmailColumn());
        this.tfBillStartColumn.setText(salarybill.getSalarybillStartColumn());
        this.tfBillEndColumn.setText(salarybill.getSalarybillEndColumn());
        this.tfEmailSubject.setText(salarybill.getEmailSubject());
    }

    public boolean get(final SalarybillSender salarybill) {
        final String salarybillFile = this.tfBillFile.getText();
        final String salarybillTitleRow = this.tfBillTitleRow.getText();
        final String salarybillEmailColumn = this.tfBillEmailColumn.getText();
        final String salarybillStartColumn = this.tfBillStartColumn.getText();
        final String salarybillEndColumn = this.tfBillEndColumn.getText();
        final String emailSubject = this.tfEmailSubject.getText();
        if (!DataChecker.isFile(salarybillFile)) {
            JOptionPane.showMessageDialog(this, "请选择人员清单文件", SendMails.TITLE, 0);
            return false;
        }
        if (!DataChecker.isExcelRow(salarybillTitleRow)) {
            JOptionPane.showMessageDialog(this, "请输入人员清单文件的 标题行（数字）", SendMails.TITLE, 0);
            return false;
        }
        if (!DataChecker.isExcelColumn(salarybillEmailColumn)) {
            JOptionPane.showMessageDialog(this, "请输入人员清单文件的邮箱列（数字）", SendMails.TITLE, 0);
            return false;
        }
        if (!DataChecker.isExcelColumn(salarybillStartColumn)) {
            JOptionPane.showMessageDialog(this, "请输入人员清单文件的起始列（数字）", SendMails.TITLE, 0);
            return false;
        }
        if (!DataChecker.isExcelColumn(salarybillEndColumn)) {
            JOptionPane.showMessageDialog(this, "请输入人员清单文件的结束列（数字）", SendMails.TITLE, 0);
            return false;
        }
        if (emailSubject == null || emailSubject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入邮件主题", SendMails.TITLE, 0);
            return false;
        }
        salarybill.setSalarybillFile(salarybillFile);
        salarybill.setSalarybillTitleRow(salarybillTitleRow);
        salarybill.setSalarybillEmailColumn(salarybillEmailColumn);
        salarybill.setSalarybillStartColumn(salarybillStartColumn);
        salarybill.setSalarybillEndColumn(salarybillEndColumn);
        salarybill.setEmailSubject(emailSubject);
        return true;
    }

    protected void do_btnFile_actionPerformed(final ActionEvent e) {
        final JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File("."));
        jfc.setFileSelectionMode(0);
        jfc.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "Excel File";
            }

            @Override
            public boolean accept(final File f) {
                if (f.isDirectory()) {
                    return true;
                }
                final String name = f.getName();
                return name.endsWith(".xls");
            }
        });
        final int r = jfc.showDialog(this, this.btnFile.getText());
        if (r == 0) {
            final File file = jfc.getSelectedFile();
            this.tfBillFile.setText(file.getAbsolutePath());
        }
    }
}
