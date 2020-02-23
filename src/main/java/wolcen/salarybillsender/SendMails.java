package wolcen.salarybillsender;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SendMails {
    public static final String TITLE = "salary bill sender by mail v1.7.2";
    public static String SALARYBILLSENDER_PROPERTIES = System.getProperty("user.dir") + File.separator + "salarybillsender.properties";
    private static Logger logger = Logger.getLogger(SendMails.class);
    private JFrame frame;
    private SendMailsPanel panel;

    public SendMails() {
        super();
        this.initialize();
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    String configFilename = System.getProperty("user.dir") + File.separator + "log4j.properties";
                    File f = new File(configFilename);
                    if (!f.exists()) {
                        try {
                            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)), true);
                            pw.println("#rootLogger");
                            pw.println("log4j.rootLogger=debug,stdout,file");
                            pw.println("#console");
                            pw.println("log4j.appender.stdout=org.apache.log4j.ConsoleAppender");
                            pw.println("log4j.appender.stdout.Target=System.out");
                            pw.println("log4j.appender.stdout.layout=org.apache.log4j.PatternLayout");
                            pw.println("log4j.appender.stdout.layout.ConversionPattern=%d [%5p] %l - %m%n");
                            pw.println("#file");
                            pw.println("log4j.appender.file=org.apache.log4j.DailyRollingFileAppender");
                            pw.println("log4j.appender.file.File=${user.dir}//logs//error.log");
                            pw.println("log4j.appender.file.DatePattern = '.'yyyy-MM-dd ");
                            pw.println("log4j.appender.file.layout=org.apache.log4j.PatternLayout");
                            pw.println("log4j.appender.file.layout.ConversionPattern=%d [%5p] %l - %m%n");
                            pw.flush();
                            pw.close();
                        } catch (Exception ex) {
                        }
                    }
                    PropertyConfigurator.configure(configFilename);
                    configFilename = System.getProperty("user.dir") + File.separator + "salarybillsender.properties";
                    f = new File(configFilename);
                    if (!f.exists()) {
                        try {
                            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)), true);
                            pw.println("salarybill.left=A");
                            pw.println("salarybill.email=C");
                            pw.println("salarybill.title=2");
                            pw.println("salarybill.right=AH");
                            pw.println("smtp.host=smtp.sample.com");
                            pw.println("smtp.ssl=false");
                            pw.println("smtp.port=25");
                            pw.println("smtp.email=master@sample.com");
                            pw.println("email.subject={B} - {A1}");
                            pw.flush();
                            pw.close();
                        } catch (Exception ex) {
                        }
                    }
                    final SendMails window = new SendMails();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    logger.error("error main", e);
                }
            }
        });
    }

    private void initialize() {
        this.panel = new SendMailsPanel();
        (this.frame = new JFrame(SendMails.TITLE)).setBounds(100, 100, 840, 600);
        this.frame.setDefaultCloseOperation(0);
        this.frame.getContentPane().add(this.panel);
        this.frame.setLocationRelativeTo(null);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (panel.canClose()) {
                    System.exit(0);
                }
            }
        });
    }
}
