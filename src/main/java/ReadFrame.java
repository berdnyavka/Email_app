import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ReadFrame extends JFrame implements ActionListener {

    private int numberOfMessage;

    public ReadFrame(int number){
        this.numberOfMessage = number - 1;
        try {
            run();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }
    private void createFrame(JTextArea area){
        setTitle("Прочитать письмо");

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(area));
        add(panel,BorderLayout.NORTH);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel btnPanel = setButtons(gridbag,gbc);

        add(btnPanel,BorderLayout.CENTER);
        setBounds(100,100,1000,600);
        setVisible(true);

    }

    protected JPanel setButtons(GridBagLayout gridbag, GridBagConstraints gbc){
        JPanel btnPanel = new JPanel();
        btnPanel.add(createButton(gridbag,gbc,"Скачать файлы","download"));
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
            case "download":
                download();
                break;
        }
    }

    private void download() {

        ReadEmail mailService = new ReadEmail();
        mailService.login();

        try {
            Message[] messages = mailService.getMessages();
                Message message = messages[numberOfMessage];
                Object content = message.getContent();

                if (content instanceof Multipart) {
                    Multipart multiPart = (Multipart) content;
                    int multiPartCount = multiPart.getCount();
                    for (int j = 0; j < multiPartCount; j++) {
                        BodyPart bodyPart = multiPart.getBodyPart(j);

                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {

                            String fileName = MimeUtility.decodeText(bodyPart.getFileName());

                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(j);
                            part.saveFile("C:/Users/79006/IdeaProjects/Email_app/src/" + fileName);

                        }
                    }
                }
            mailService.logout();

        }catch (IOException|MessagingException ex){
            ex.printStackTrace();
        }
    }

    public void run() throws MessagingException, IOException {

        JTextArea area = new JTextArea(30,60);

        ReadEmail mailService = new ReadEmail();
        mailService.login();

        Message[] messages = mailService.getMessages();

        StringBuilder buffer = new StringBuilder();

            buffer.append("Message #"+(numberOfMessage)+"\n");
            String subject = "";
            if (messages[numberOfMessage].getSubject() != null)
                subject = messages[numberOfMessage].getSubject();
            Address[] fromAddress = messages[numberOfMessage].getFrom();
            buffer.append("FROM:"+(fromAddress[0].toString())+"\nDATE:"+messages[numberOfMessage].getReceivedDate() +"\nSUBJECT:"+subject);

            Message message = messages[numberOfMessage];
            Object content = message.getContent();

            if (content instanceof String) {
                buffer.append("TEXT:"+content);
            } else if (content instanceof Multipart) {
                Multipart multiPart = (Multipart) content;
                int multiPartCount = multiPart.getCount();
                for (int j = 0; j < multiPartCount; j++) {
                    BodyPart bodyPart = multiPart.getBodyPart(j);
                    Object o = bodyPart.getContent();

                    if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())){

                        String fileName = MimeUtility.decodeText(bodyPart.getFileName());

                        buffer.append("\nFILE "+(j+1)+":"+fileName);
                        buffer.append("\n-------------------\n");
                        buffer.append("\nDownload?");
                        if(false){
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(j);
                            part.saveFile("C:/Users/79006/IdeaProjects/Email_app/src/"+fileName);
                        }
                        break;
                    }
                    if (o instanceof String) {
                        buffer.append("\nTEXT:" + o);

                    }
                }
            }



        area.setText(buffer.toString());
        createFrame(area);

        mailService.logout();
    }
}
