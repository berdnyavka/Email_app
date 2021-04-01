import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;
import javax.activation.FileDataSource;

public class SendEmail
{

    private  final  static  String  PROPS_FILE = "src/main/resources/email.properties";

    public void fill()
    {
        try (InputStream is = new FileInputStream(PROPS_FILE)){
            Reader reader = new InputStreamReader(is, "UTF-8");
            Properties props = new Properties();
            props.load(reader);
            SMTP_SERVER    = props.getProperty ("smtp_server" );
            SMTP_Port      = props.getProperty ("smtp_port"   );

            EMAIL_FROM     = props.getProperty ("from"   );
            SMTP_AUTH_USER = props.getProperty ("user"   );
            SMTP_AUTH_PWD  = props.getProperty ("pass"   );


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message message = null;
    protected  static  String   SMTP_SERVER    = null;
    protected  static  String   SMTP_Port      = null;
    protected  static  String   SMTP_AUTH_USER = null;
    protected  static  String   SMTP_AUTH_PWD  = null;
    protected  static  String   EMAIL_FROM     = null;
    protected  static  String   FILE_PATH      = null;
    protected  static  String   REPLY_TO       = null;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public SendEmail(String emailTo, String thema, String text, String filePath)
    {
        fill();
        if(filePath != null){
            FILE_PATH = filePath;
        }
        // Настройка SMTP SSL
        Properties properties = new Properties();
        properties.put("mail.smtp.host"               , SMTP_SERVER);
        properties.put("mail.smtp.port"               , SMTP_Port  );
        properties.put("mail.smtp.auth"               , "true"     );
        properties.put("mail.smtp.ssl.enable"         , "true"     );
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        try {
            Authenticator auth = new EmailAuthenticator(SMTP_AUTH_USER,
                    SMTP_AUTH_PWD);
            Session session = Session.getDefaultInstance(properties,auth);
            session.setDebug(false);

            InternetAddress email_from = new InternetAddress(EMAIL_FROM);
            InternetAddress email_to   = new InternetAddress(emailTo   );
            InternetAddress reply_to   = (REPLY_TO != null) ?
                    new InternetAddress(REPLY_TO) : null;
            message = new MimeMessage(session);
            message.setFrom(email_from);
            message.setRecipient(Message.RecipientType.TO, email_to);
            message.setSubject(thema);
            if (reply_to != null)
                message.setReplyTo (new Address[] {reply_to});
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        }

        sendMessage(text);
    }



    private MimeBodyPart createFileAttachment(String filepath) throws MessagingException {
        // Создание MimeBodyPart
        MimeBodyPart mbp = new MimeBodyPart();

        // Определение файла в качестве контента
        FileDataSource fds = new FileDataSource(filepath);
        mbp.setDataHandler(new DataHandler(fds));
        mbp.setFileName(fds.getName());
        return mbp;
    }


    public void sendMessage (final String text)
    {

        boolean result = false;
        try {
            // Содержимое сообщения
            Multipart mmp = new MimeMultipart();
            // Текст сообщения
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(text, "text/plain; charset=utf-8");
            mmp.addBodyPart(bodyPart);
            // Вложение файла в сообщение
            if (FILE_PATH != null) {
                MimeBodyPart mbr = createFileAttachment(FILE_PATH);
                mmp.addBodyPart(mbr);
            }
            // Определение контента сообщения
            message.setContent(mmp);
            // Отправка сообщения
            Transport.send(message);
            result = true;
        } catch (MessagingException e){
            // Ошибка отправки сообщения
            System.err.println(e.getMessage());
        }
    }
}