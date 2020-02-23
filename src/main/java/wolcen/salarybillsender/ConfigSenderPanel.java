package wolcen.salarybillsender;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class ConfigSenderPanel extends JPanel {
    private static Logger logger = Logger.getLogger(ConfigSenderPanel.class);
    private JTextField tfSmtpEmail;
    private JPasswordField tfSmtpPass;
    private JTextField tfSmtpHost;
    private JTextField tfSmtpPort;
    private JCheckBox cbxSmtpSsl;
    private JPanel panel;

    public ConfigSenderPanel() {
        super();
        this.initComponents();
        ((AbstractDocument) this.tfSmtpPort.getDocument()).setDocumentFilter(new DocumentFilter4Integer());
    }

    private void initComponents() {
        this.setLayout(null);
        this.panel = new JPanel();
        this.panel.setBounds(10, 10, 670, 101);
        this.add(this.panel);
        this.panel.setLayout(null);
        this.panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "发送人信息", 4, 2, null, Color.BLUE));
        final JLabel label_0 = new JLabel("发送人邮件：");
        label_0.setHorizontalAlignment(4);
        label_0.setBounds(10, 20, 94, 20);
        this.panel.add(label_0);
        this.tfSmtpEmail = new JTextField();
        this.tfSmtpEmail.setBounds(114, 20, 280, 21);
        this.panel.add(this.tfSmtpEmail);
        this.tfSmtpEmail.setColumns(10);
        final JLabel label = new JLabel("邮件密码：");
        label.setHorizontalAlignment(4);
        label.setBounds(403, 20, 94, 20);
        this.panel.add(label);
        this.tfSmtpPass = new JPasswordField();
        this.tfSmtpPass.setBounds(507, 21, 146, 21);
        this.panel.add(this.tfSmtpPass);
        final JLabel label_2 = new JLabel("发送服务器：");
        label_2.setHorizontalAlignment(4);
        label_2.setBounds(10, 50, 94, 20);
        this.panel.add(label_2);
        this.tfSmtpHost = new JTextField();
        this.tfSmtpHost.setColumns(10);
        this.tfSmtpHost.setBounds(114, 50, 280, 21);
        this.panel.add(this.tfSmtpHost);
        final JLabel label_3 = new JLabel("端口：");
        label_3.setHorizontalAlignment(4);
        label_3.setBounds(403, 47, 94, 20);
        this.panel.add(label_3);
        this.tfSmtpPort = new JTextField();
        this.tfSmtpPort.setText("25");
        this.tfSmtpPort.setColumns(10);
        this.tfSmtpPort.setBounds(507, 47, 61, 21);
        this.panel.add(this.tfSmtpPort);
        this.cbxSmtpSsl = new JCheckBox("SSL");
        this.cbxSmtpSsl.setBounds(574, 46, 61, 23);
        this.panel.add(this.cbxSmtpSsl);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.tfSmtpEmail.setEnabled(enabled);
        this.tfSmtpPass.setEnabled(enabled);
        this.tfSmtpHost.setEnabled(enabled);
        this.tfSmtpPort.setEnabled(enabled);
        this.cbxSmtpSsl.setEnabled(enabled);
    }

    public void set(final SalarybillSender salarybill) {
        this.tfSmtpEmail.setText(salarybill.getSmtpEmail());
        this.tfSmtpPass.setText(salarybill.getSmtpPass());
        this.tfSmtpHost.setText(salarybill.getSmtpHost());
        this.tfSmtpPort.setText(salarybill.getSmtpPort());
        this.cbxSmtpSsl.setSelected(salarybill.isSmtpSsl());
    }

    public boolean get(final SalarybillSender salarybill) {
        final String smtpEmail = this.tfSmtpEmail.getText();
        final String smtpPass = new String(this.tfSmtpPass.getPassword());
        final String smtpHost = this.tfSmtpHost.getText();
        final String smtpPort = this.tfSmtpPort.getText();
        final boolean smtpSsl = this.cbxSmtpSsl.isSelected();
        if (smtpEmail == null || smtpEmail.isEmpty() || !DataChecker.isEmail(smtpEmail)) {
            JOptionPane.showMessageDialog(this, "请输入发送人的邮件地址", SendMails.TITLE, 0);
            return false;
        }
        if (smtpPass == null || smtpPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入发送人的邮箱密码", SendMails.TITLE, 0);
            return false;
        }
        if (smtpHost == null || smtpHost.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入发送服务器地址（域名或者IP）", SendMails.TITLE, 0);
            return false;
        }
        if (smtpPort == null || smtpPort.isEmpty() || !DataChecker.isNumeric(smtpPort)) {
            JOptionPane.showMessageDialog(this, "请输入发送服务器端口（数字，一般25）", SendMails.TITLE, 0);
            return false;
        }
        salarybill.setSmtpEmail(smtpEmail);
        salarybill.setSmtpHost(smtpHost);
        salarybill.setSmtpPass(smtpPass);
        salarybill.setSmtpPort(smtpPort);
        salarybill.setSmtpSsl(smtpSsl);
        return true;
    }
}
