package wolcen.salarybillsender;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SalarybillSender {
    private static Logger logger = Logger.getLogger(SalarybillSender.class);
    boolean smtpSsl;
    private String salarybillFile;
    private String salarybillTitleRow;
    private String salarybillEmailColumn;
    private String salarybillStartColumn;
    private String salarybillEndColumn;
    private String emailSubject;
    private String smtpEmail;
    private String smtpPass;
    private String smtpHost;
    private String smtpPort;
    private String emailSubjectFormat;
    private String emailContentTitle;
    private int emailColumns;
    private List<MailInfo> mails;
    private Properties sendlast;

    public SalarybillSender() {
        super();
        this.mails = new ArrayList<MailInfo>();
        this.sendlast = new Properties();
    }

    public String getSalarybillFile() {
        return this.salarybillFile;
    }

    public void setSalarybillFile(final String salarybillFile) {
        this.salarybillFile = salarybillFile;
    }

    public String getSalarybillTitleRow() {
        return this.salarybillTitleRow;
    }

    public void setSalarybillTitleRow(final String salarybillTitleRow) {
        this.salarybillTitleRow = salarybillTitleRow;
    }

    public String getSalarybillEmailColumn() {
        return this.salarybillEmailColumn;
    }

    public void setSalarybillEmailColumn(final String salarybillEmailColumn) {
        this.salarybillEmailColumn = salarybillEmailColumn;
    }

    public String getSalarybillStartColumn() {
        return this.salarybillStartColumn;
    }

    public void setSalarybillStartColumn(final String salarybillStartColumn) {
        this.salarybillStartColumn = salarybillStartColumn;
    }

    public String getSalarybillEndColumn() {
        return this.salarybillEndColumn;
    }

    public void setSalarybillEndColumn(final String salarybillEndColumn) {
        this.salarybillEndColumn = salarybillEndColumn;
    }

    public String getEmailSubject() {
        return this.emailSubject;
    }

    public void setEmailSubject(final String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getSmtpEmail() {
        return this.smtpEmail;
    }

    public void setSmtpEmail(final String smtpEmail) {
        this.smtpEmail = smtpEmail;
    }

    public String getSmtpPass() {
        return this.smtpPass;
    }

    public void setSmtpPass(final String smtpPass) {
        this.smtpPass = smtpPass;
    }

    public String getSmtpHost() {
        return this.smtpHost;
    }

    public void setSmtpHost(final String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPort() {
        return this.smtpPort;
    }

    public void setSmtpPort(final String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public boolean isSmtpSsl() {
        return this.smtpSsl;
    }

    public void setSmtpSsl(final boolean smtpSsl) {
        this.smtpSsl = smtpSsl;
    }

    public void load(final String file) {
        final Properties prop = new Properties();
        try {
            final FileInputStream inStream = new FileInputStream(new File(file));
            prop.load(inStream);
            inStream.close();
            this.setSalarybillTitleRow(prop.getProperty("salarybill.title"));
            this.setSalarybillEmailColumn(prop.getProperty("salarybill.email"));
            this.setSalarybillStartColumn(prop.getProperty("salarybill.left"));
            this.setSalarybillEndColumn(prop.getProperty("salarybill.right"));
            this.setEmailSubject(prop.getProperty("email.subject"));
            this.setSmtpEmail(prop.getProperty("smtp.email"));
            this.setSmtpPass(prop.getProperty("smtp.pass"));
            this.setSmtpHost(prop.getProperty("smtp.host"));
            this.setSmtpPort(prop.getProperty("smtp.port"));
            this.setSmtpSsl(Boolean.valueOf(prop.getProperty("smtp.ssl")));
        } catch (Exception e) {
            logger.error("error load properties", e);
        }
    }

    public void save(final String file) {
        final Properties prop = new Properties();
        try {
            prop.setProperty("smtp.email", this.smtpEmail);
            prop.setProperty("smtp.host", this.smtpHost);
            prop.setProperty("smtp.port", this.smtpPort);
            prop.setProperty("smtp.ssl", String.valueOf(this.smtpSsl));
            prop.setProperty("salarybill.title", this.salarybillTitleRow);
            prop.setProperty("salarybill.email", this.salarybillEmailColumn);
            prop.setProperty("salarybill.left", this.salarybillStartColumn);
            prop.setProperty("salarybill.right", this.salarybillEndColumn);
            prop.setProperty("email.subject", this.emailSubject);
            final FileOutputStream out = new FileOutputStream(new File(file));
            prop.store(out, null);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("error save properties", e);
        }
    }

    public int strToRow(final String str) {
        final int row = Integer.valueOf(str) - 1;
        return row;
    }

    public int strToColumn(final String str) {
        final char[] ch = str.toCharArray();
        if (ch.length == 1) {
            return ch[0] - 'A';
        }
        return 26 * (ch[0] - '@') + (ch[1] - 'A');
    }

    public String intToColumn(final int column) {
        if (column <= 26) {
            return String.valueOf((char) (65 + column));
        }
        final int i1 = column / 26;
        final int i2 = column % 26;
        return String.valueOf((char) (64 + i1)) + (char) (65 + i2);
    }

    public void loadbill(final SendTableModel model, final INotifyUI ui) {
        Workbook workbook = null;
        try {
            this.mails.clear();
            model.fireTableDataChanged();
            this.sendlast = new Properties();
            final File propfile = new File(this.salarybillFile + ".sendlast");
            if (propfile.exists()) {
                try {
                    final FileInputStream inStream = new FileInputStream(propfile);
                    this.sendlast.load(inStream);
                    inStream.close();
                    final int n = JOptionPane.showConfirmDialog(null, "发现该名单已经不是第一次发送，若是确认是第一次，请选择【是】，然后删除下面提示文件后，再次点击【加载人员】\r\n 上次日志：【" + propfile.getAbsolutePath() + "】", SendMails.TITLE, 0);
                    if (n == 0) {
                        ui.notifyUI("上次日志：【" + propfile.getAbsolutePath() + "】");
                        return;
                    }
                } catch (Exception e) {
                    logger.error("error load sendlast", e);
                }
            }
            workbook = Workbook.getWorkbook(new File(this.salarybillFile));
            final Sheet sheet = workbook.getSheet(0);
            this.emailSubjectFormat = this.emailSubject;
            if (logger.isDebugEnabled()) {
                logger.debug("emailSubjectFormat:" + this.emailSubjectFormat);
            }
            int fromIndex = 0;
            while (this.emailSubjectFormat.indexOf("{", fromIndex) != -1) {
                final int s = this.emailSubjectFormat.indexOf("{", fromIndex);
                final int e2 = this.emailSubjectFormat.indexOf("}", s);
                final String rcs = this.emailSubjectFormat.substring(s + 1, e2);
                if (DataChecker.isExcelCell(rcs)) {
                    final Cell cell = sheet.getCell(rcs);
                    final String text = (cell != null) ? cell.getContents() : "";
                    this.emailSubjectFormat = this.emailSubjectFormat.substring(0, s) + text + this.emailSubjectFormat.substring(e2 + 1);
                    fromIndex = s + text.length();
                } else {
                    fromIndex = e2 + 1;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("emailSubjectFormat(after {xxx}):" + this.emailSubjectFormat);
            }
            this.emailContentTitle = "<tr>";
            final int titleRow = this.strToRow(this.salarybillTitleRow);
            final int mailColumn = this.strToColumn(this.salarybillEmailColumn);
            final int startColumn = this.strToColumn(this.salarybillStartColumn);
            int endColumn = this.strToColumn(this.salarybillEndColumn);
            endColumn = ((endColumn < sheet.getColumns()) ? endColumn : sheet.getColumns());
            this.emailColumns = endColumn - startColumn + 1;
            for (int c = startColumn; c < endColumn; ++c) {
                final Cell cell2 = sheet.getCell(c, titleRow);
                final String text2 = (cell2 != null) ? cell2.getContents() : "";
                if (DataChecker.isEmail(text2) || DataChecker.isNumeric(text2)) {
                    workbook.close();
                    ui.notifyUI("发现标题异常，请检查标题配置");
                    return;
                }
                this.emailContentTitle = this.emailContentTitle + "<td width= 5%>" + text2 + "</td>";
            }
            this.emailContentTitle += "</tr>";
            if (logger.isDebugEnabled()) {
                logger.debug("emailContentTitle:" + this.emailContentTitle);
            }
            this.mails.clear();
            for (int r = titleRow + 1; r < sheet.getRows(); ++r) {
                final String sendTo = sheet.getCell(mailColumn, r).getContents();
                if (sendTo == null || sendTo.isEmpty() || !DataChecker.isEmail(sendTo)) {
                    logger.warn("warn row:" + r);
                } else {
                    ui.notifyUI("正在处理 " + sendTo);
                    String subject = this.emailSubjectFormat;
                    String content = "" + this.emailContentTitle + "<tr>";
                    for (int c2 = startColumn; c2 < endColumn; ++c2) {
                        final Cell cell3 = sheet.getCell(c2, r);
                        final String text3 = (cell3 != null) ? cell3.getContents() : "";
                        content = content + "<td width= 5%>" + text3 + "</td>";
                        subject = subject.replace("{" + this.intToColumn(c2) + "}", text3);
                    }
                    content += "</tr>";
                    final MailInfo info = new MailInfo(r + 1, sendTo, subject, content);
                    if (this.sendlast.containsKey(sendTo)) {
                        String last = this.sendlast.getProperty(sendTo).trim();
                        if (last.startsWith("T:")) {
                            info.success = true;
                            if (last.length() > 2) {
                                last = last.substring(2);
                                try {
                                    info.sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(last);
                                } catch (Exception ex) {
                                }
                            }
                        }
                    }
                    this.mails.add(info);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Job:[" + (r + 1) + "]" + subject + "[" + sendTo + "]");
                    }
                    model.fireTableDataChanged();
                }
            }
            workbook.close();
            ui.notifyUI("载入名单完成");
        } catch (Exception e3) {
            if (workbook != null) {
                workbook.close();
            }
            logger.error("error load bill", e3);
            ui.notifyUI("载入名单失败：" + e3.getMessage());
        }
    }

    public void sendbill(final SendTableModel model, final INotifyUI ui) {
        boolean over = false;
        String lasterror = "";
        for (int index = 0; index < this.mails.size(); ++index) {
            final MailInfo info = this.mails.get(index);
            if (info.success) {
                if (logger.isDebugEnabled()) {
                    logger.debug("last send over [" + info.sendto + "]");
                }
            } else {
                ui.notifyUI("正在处理  【" + info.index + "】" + info.subject);
                final String text = "<html><table border=1 width=100%>" + info.content + "</table></html>";
                if (logger.isDebugEnabled()) {
                    logger.debug("bengin send [" + info.sendto + "]");
                }
                try {
                    final HtmlEmail email = new HtmlEmail();
                    email.setHostName(this.smtpHost);
                    email.setAuthentication(this.smtpEmail, this.smtpPass);
                    email.setSSL(this.smtpSsl);
                    email.setCharset("UTF-8");
                    email.addTo(info.sendto);
                    email.setFrom(this.smtpEmail);
                    email.setSubject(info.subject);
                    email.setHtmlMsg(text);
                    email.send();
                    info.sendTime = new Date();
                    info.success = true;
                    info.errormessage = "发送成功";
                    if (logger.isDebugEnabled()) {
                        logger.debug("bengin send success");
                    }
                    ui.notifyUI("【" + info.index + "】" + info.errormessage);
                    lasterror = info.errormessage;
                } catch (EmailException e1) {
                    logger.error("error send mail ", e1);
                    info.sendTime = new Date();
                    info.success = false;
                    info.errormessage = e1.getLocalizedMessage();
                    if (e1.getCause() != null) {
                        Throwable cause = e1.getCause();
                        if (cause instanceof AuthenticationFailedException) {
                            final StringBuilder sb = new StringBuilder();
                            final MailInfo mailInfo = info;
                            mailInfo.errormessage = sb.append(mailInfo.errormessage).append("<-- ").append(cause.getLocalizedMessage()).toString();
                            over = true;
                            ui.notifyUI("发送人帐号或者密码错误，请修改配置后再试");
                        } else if (cause instanceof MessagingException) {
                            cause = cause.getCause();
                            if (cause != null && cause instanceof UnknownHostException) {
                                final StringBuilder sb2 = new StringBuilder();
                                final MailInfo mailInfo2 = info;
                                mailInfo2.errormessage = sb2.append(mailInfo2.errormessage).append("<--").append(cause.getLocalizedMessage()).toString();
                                over = true;
                                ui.notifyUI("发送服务器配置错误，请修改配置后再试");
                            }
                        }
                    }
                    if (!over) {
                        StringBuilder sb3;
                        MailInfo mailInfo3;
                        for (Throwable cause = e1; cause.getCause() != null; cause = cause.getCause(), sb3 = new StringBuilder(), mailInfo3 = info, mailInfo3.errormessage = sb3.append(mailInfo3.errormessage).append("<-- ").append((cause.getLocalizedMessage() != null) ? cause.getLocalizedMessage() : cause.getClass().getName()).toString()) {
                        }
                    }
                    if (lasterror.equals(info.errormessage)) {
                        ui.notifyUI("连续出现重复性错误，请检查, 目前序号【" + info.index + "】");
                        over = true;
                    }
                    lasterror = info.errormessage;
                }
                info.over = true;
                this.sendlast.put(info.sendto, info.getLog());
                model.fireTableDataChanged();
                if (over) {
                    break;
                }
            }
        }
        File propfile = null;
        try {
            propfile = new File(this.salarybillFile + ".sendlast");
            final FileOutputStream out = new FileOutputStream(propfile);
            this.sendlast.store(out, null);
            out.flush();
            out.close();
        } catch (Exception e2) {
            logger.error("error save sendlast", e2);
        }
        if (!over) {
            ui.notifyUI("发送完成，日志：" + ((propfile != null) ? propfile.getAbsolutePath() : "(写入异常)"));
        }
    }

    public int getEmployees() {
        return this.mails.size();
    }

    public String getTableCellContents(final int index) {
        final MailInfo info = this.mails.get(index);
        final String textFormat = "<html><table border=1 width=100%><tr><td colspan=\"{0}\"  bgcolor=\"{1}\" >【{2}】 {3}  {4}</td></tr>{5}</table></html>";
        final String text = MessageFormat.format(textFormat, this.emailColumns, info.getColor(), info.index, info.subject, info.getMessage(), info.content);
        return text;
    }


    class MailInfo {
        public int index;
        public String sendto;
        public String subject;
        public String content;
        public boolean success;
        public String errormessage;
        public Date sendTime;
        public boolean over;

        public MailInfo(final int index, final String sendto, final String subject, final String content) {
            this.success = false;
            this.errormessage = "";
            this.sendTime = null;
            this.over = false;
            this.index = index;
            this.sendto = sendto;
            this.subject = subject;
            this.content = content;
            this.success = false;
            this.sendTime = null;
            this.errormessage = "";
            this.over = false;
        }

        public String getColor() {
            return this.success ? "#9ACD32" : (this.over ? "#FF0000" : "#FFFFFF");
        }

        public String getMessage() {
            if (!this.success) {
                return this.errormessage.isEmpty() ? "" : ("===>发送失败： " + this.errormessage);
            }
            if (this.sendTime != null) {
                return "===>已经发送：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.sendTime);
            }
            return "===>已经发送：(时间未记录)";
        }

        public String getLog() {
            if (!this.success) {
                return this.errormessage.isEmpty() ? "F:" : ("F:" + this.errormessage);
            }
            if (this.sendTime != null) {
                return "T:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.sendTime);
            }
            return "T:";
        }
    }
}
