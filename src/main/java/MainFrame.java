import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainFrame extends JFrame implements ActionListener {

    public MainFrame(){
        run();
    }

    private void createFrame(JTextArea area){
        setTitle("Email_app");

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(area));

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(500,500,700,500);
        JPanel btnPanel = setButtons(gridbag,gbc);

        add(btnPanel,BorderLayout.CENTER);
        add(panel,BorderLayout.SOUTH);

        setBounds(100,100,1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    protected JPanel setButtons(GridBagLayout gridbag, GridBagConstraints gbc){
        JPanel btnPanel = new JPanel();
        btnPanel.add(createButton(gridbag,gbc,"Отправить письмо","send"));
        btnPanel.add(createButton(gridbag,gbc,"Прочитать письмо","read"));
        return btnPanel;
    }

    protected JButton createButton(GridBagLayout gridbag, GridBagConstraints gbc, String title, String action){
        JButton button = new JButton(title);
        button.setActionCommand(action);
        button.addActionListener(this);
        gridbag.setConstraints(button,gbc);
        return button;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch(action){
            case "send":
                send();
                break;
            case "read":
                read();
                break;
        }
    }
    private void send(){
        SendDialog sendf = new SendDialog();
        if(sendf.isSave()){
            String to = sendf.emailTo.getText();
            String th = sendf.theme.getText();
            String txt = sendf.text.getText();
            String fp = sendf.filePath.getText();
            SendEmail se = new SendEmail(to,th,txt,fp);
            JOptionPane.showMessageDialog(this,"Сообщение отправлено");
        }
    }

    private void read(){
        ReadDialog rd = new ReadDialog();

        try {
            ReadFrame rf = new ReadFrame(Integer.parseInt(rd.countOfMes.getText()));
        }catch (Exception ex){

        }
    }

    public void run()  {

        JTextArea area = new JTextArea(30,60);
        ReadEmail mailService = new ReadEmail();
        mailService.login();

        int messageCount = mailService.getMessageCount();
        Message[] messages = mailService.getMessages();

        StringBuilder buffer = new StringBuilder();
        for (int i = messageCount - 1 ; i >= 0; i--) {

            buffer.append("Message #"+(i+1)+"\n");
            String subject = "";
            try {
                if (messages[i].getSubject() != null)
                    subject = messages[i].getSubject();
                Address[] fromAddress = messages[i].getFrom();
                buffer.append("FROM:" + (fromAddress[0].toString()) + "\nDATE:" + messages[i].getReceivedDate() + "\nSUBJECT:" + subject);
                buffer.append("\n-------------------\n");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        area.setText(buffer.toString());
        createFrame(area);

        mailService.logout();
    }


}
