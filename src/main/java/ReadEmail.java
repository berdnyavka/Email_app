import java.io.*;
import java.util.Properties;

import javax.mail.*;

public class ReadEmail
{


    private  final  static  String  PROPS_FILE = "src/main/resources/email.properties";

    protected  static String   IMAP_AUTH_EMAIL = null;
    protected  static String   IMAP_AUTH_PWD   = null;
    protected  static String   IMAP_Server     = null;
    protected  static String   IMAP_Port       = null;

    public void fill()
    {
        try (InputStream is = new FileInputStream(PROPS_FILE)){
            Reader reader = new InputStreamReader(is, "UTF-8");
            Properties props = new Properties();
            props.load(reader);
            ReadEmail.IMAP_AUTH_EMAIL  = props.getProperty ("from");
            ReadEmail.IMAP_AUTH_PWD      = props.getProperty ("pass");
            ReadEmail.IMAP_Server     = props.getProperty ("imap_server");
            ReadEmail.IMAP_Port = props.getProperty ("imap_port");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private Session session;
    private Store store;
    private Folder folder;

    public void login()
    {
        fill();
        Properties properties = new Properties();
        properties.put("mail.debug"          , "false"  );
        properties.put("mail.store.protocol" , "imaps"  );
        properties.put("mail.imap.ssl.enable", "true"   );
        properties.put("mail.imap.port"      , IMAP_Port);

        Authenticator auth = new EmailAuthenticator(IMAP_AUTH_EMAIL, IMAP_AUTH_PWD);
        session = Session.getDefaultInstance(properties, auth);
        session.setDebug(false);
        try {
            store = session.getStore();
            store.connect(IMAP_Server, IMAP_AUTH_EMAIL, IMAP_AUTH_PWD);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(0);

        }
    }

    public void logout() {
        if(folder != null) {
            try {
                folder.close(false);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        if(store != null) {
            try {
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        store = null;
        session = null;
    }

    public int getMessageCount() {
        int messageCount = 0;
        try {
            messageCount = folder.getMessageCount();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return messageCount;
    }

    public Message[] getMessages() {
        try {
            return folder.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}