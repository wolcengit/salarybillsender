package wolcen.salarybillsender;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SendMailsPanel extends JPanel {
    private static Logger logger = Logger.getLogger(SendMailsPanel.class);
    private SalarybillSender salarybill;
    private SendTableModel model;
    private boolean isRunning;
    private JButton btnBillSend;
    private JButton btnBillLoad;
    private JLabel lbMsg;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable table;
    private ConfigBillPanel configBillPanel;
    private ConfigSenderPanel configSenderPanel;
    private JButton btnTemplate;

    public SendMailsPanel() {
        super();
        this.isRunning = false;
        this.salarybill = new SalarybillSender();
        this.model = new SendTableModel(this.salarybill);
        this.initComponents();
        this.table.setAutoResizeMode(0);
        this.table.setRowHeight(150);
        this.table.getColumnModel().getColumn(0).setMinWidth(2000);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(2000);
        final TableColumn tableColumn1 = this.table.getColumnModel().getColumn(0);
        tableColumn1.setCellRenderer(new SendDataTableCellRenderer());
        this.load();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(0, 0));
        final JPanel panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(10, 281));
        this.add(panel_2, "North");
        panel_2.setLayout(null);
        this.lbMsg = new JLabel("");
        this.lbMsg.setOpaque(true);
        this.lbMsg.setBackground(SystemColor.info);
        this.lbMsg.setForeground(Color.RED);
        this.lbMsg.setFont(new Font("宋体", 1, 14));
        this.lbMsg.setBounds(10, 250, 798, 21);
        panel_2.add(this.lbMsg);
        this.btnBillLoad = new JButton("2、加载人员");
        this.btnBillLoad.setBounds(696, 65, 112, 84);
        panel_2.add(this.btnBillLoad);
        this.btnBillSend = new JButton("3、发送邮件");
        this.btnBillSend.setEnabled(false);
        this.btnBillSend.setBounds(696, 159, 112, 84);
        panel_2.add(this.btnBillSend);
        this.configSenderPanel = new ConfigSenderPanel();
        this.configSenderPanel.setBounds(0, 133, 690, 117);
        panel_2.add(this.configSenderPanel);
        this.configBillPanel = new ConfigBillPanel();
        this.configBillPanel.setBounds(0, 0, 690, 136);
        panel_2.add(this.configBillPanel);
        
        btnTemplate = new JButton("1、示例模板");
        btnTemplate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		do_btnTemplate_actionPerformed(e);
        	}
        });
        btnTemplate.setBounds(696, 10, 112, 47);
        panel_2.add(btnTemplate);
        this.panel = new JPanel();
        this.panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        this.add(this.panel, "Center");
        this.panel.setLayout(new BorderLayout(0, 0));
        this.scrollPane = new JScrollPane();
        this.panel.add(this.scrollPane);
        this.table = new JTable(this.model);
        this.scrollPane.setViewportView(this.table);
        this.btnBillSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                do_btnSend_actionPerformed(e);
            }
        });
        this.btnBillLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                do_btnLoad_actionPerformed(e);
            }
        });
    }

    protected void load() {
        final File f = new File(SendMails.SALARYBILLSENDER_PROPERTIES);
        if (!f.exists()) {
            try {
                final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)), true);
                pw.println("salarybill.left=A");
                pw.println("salarybill.email=C");
                pw.println("salarybill.title=2");
                pw.println("salarybill.right=AD");
                pw.println("smtp.host=smtp.foxhis.com");
                pw.println("smtp.ssl=false");
                pw.println("smtp.port=25");
                pw.println("smtp.email=xxx@foxhis.com");
                pw.println("email.subject={B} - {A1}");
                pw.println("salarybill=");
                pw.flush();
                pw.close();
            } catch (Exception ex) {
            }
        }
        this.salarybill.load(SendMails.SALARYBILLSENDER_PROPERTIES);
        this.configBillPanel.set(this.salarybill);
        this.configSenderPanel.set(this.salarybill);
    }

    protected void save() {
        this.salarybill.save(SendMails.SALARYBILLSENDER_PROPERTIES);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.configBillPanel.setEnabled(enabled);
        this.configSenderPanel.setEnabled(enabled);
        this.btnBillLoad.setEnabled(enabled);
        this.btnBillSend.setEnabled(enabled && this.salarybill.getEmployees() > 0);
        super.setEnabled(enabled);
    }

    protected void do_btnLoad_actionPerformed(final ActionEvent e) {
        if (!this.configBillPanel.get(this.salarybill)) {
            return;
        }
        this.isRunning = true;
        this.setEnabled(!this.isRunning);
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                salarybill.loadbill(model, new INotifyUI() {
                    @Override
                    public void notifyUI(final String text) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                showMessage(text);
                            }
                        });
                    }
                });
                return null;
            }

            @Override
            protected void done() {
                isRunning = false;
                setEnabled(!isRunning);
                super.done();
            }

        }.execute();
    }

    protected void do_btnSend_actionPerformed(final ActionEvent e) {
        if (!this.configSenderPanel.get(this.salarybill)) {
            return;
        }
        if (this.salarybill.getEmployees() == 0) {
            return;
        }
        this.isRunning = true;
        this.setEnabled(!this.isRunning);
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                salarybill.sendbill(model, new INotifyUI() {
                    @Override
                    public void notifyUI(final String text) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                showMessage(text);
                            }
                        });
                    }
                });
                salarybill.save(SendMails.SALARYBILLSENDER_PROPERTIES);
                return null;
            }

            @Override
            protected void done() {
                isRunning = false;
                setEnabled(!isRunning);
                super.done();
            }
        }.execute();
    }

    public void showMessage(final String text) {
        this.lbMsg.setText(text);
        this.lbMsg.invalidate();
    }

    public boolean canClose() {
        if (this.isRunning) {
            JOptionPane.showMessageDialog(this, "信息处理中，无法退出", SendMails.TITLE, 2);
            return false;
        }
        return true;
    }
	protected void do_btnTemplate_actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            File filePath= fileChooser.getSelectedFile();
            InputStream is = null;
            OutputStream os = null;
            try {
                is = SendMails.class.getResourceAsStream("/template.xls");
                os = new FileOutputStream(new File(filePath,"工资单模板示例.xls"));
                IOUtils.copy(is,os);
            } catch (Exception ex) {
                logger.error("error export template.xls",ex);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                }catch (Exception ex1){

                }
            }
        }
	}
}
