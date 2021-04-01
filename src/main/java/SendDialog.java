import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SendDialog extends JDialog implements ActionListener {
    final JTextPane emailTo = new JTextPane();
    final JTextPane theme = new JTextPane();
    final JTextPane text = new JTextPane();
    final JTextPane filePath = new JTextPane();


    private final int PAD = 25;
    private final int W_L = 100;
    private final int W_T = 300;
    private final int W_B = 120;
    private final int H_B = 25;

    public boolean save = false;


    public SendDialog(){
        setTitle("Отправить письмо");

        buildFields("Почта получателя:",emailTo,0);
        buildFields("Тема сообщения:", theme,1);
        buildFields("Файл:",filePath,12);


        JLabel lbl = new JLabel("Текст:");
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setBounds(new Rectangle(PAD, 2 * H_B + PAD+75, W_L+20, H_B));
        add(lbl);
        text.setBounds(new Rectangle(W_L + 3 * PAD, 4 * H_B + PAD, W_T, H_B+150));
        text.setBorder(BorderFactory.createEtchedBorder());
        add(text);

        buildBtn();

        setLayout(null);
        setModal(true);
        setBounds(500, 200, 600, 550);
        setVisible(true);



    }

    protected void buildFields(String name,JTextPane pane,int counter) {
        JLabel lbl = new JLabel(name);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setBounds(new Rectangle(PAD, counter * H_B + PAD, W_L+20, H_B));
        add(lbl);
        pane.setBounds(new Rectangle(W_L + 3 * PAD, counter * H_B + PAD, W_T, H_B));
        pane.setBorder(BorderFactory.createEtchedBorder());
        add(pane);

    }

    protected void buildBtn() {
        JButton btnSave = new JButton("Отправить");
        btnSave.setActionCommand("SAVE");
        btnSave.addActionListener(this);
        btnSave.setBounds(new Rectangle(5 * PAD, 17 * H_B + PAD, W_B, H_B));
        add(btnSave);

        JButton btnCancel = new JButton("Отменить");
        btnCancel.setActionCommand("CANCEL");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(new Rectangle(W_B + 10 * PAD, 17 * H_B + PAD, W_B, H_B));
        add(btnCancel);

        JButton btnFile = new JButton("Добавить файл");
        btnFile.setActionCommand("ADDFILE");
        btnFile.addActionListener(this);
        btnFile.setBounds(new Rectangle(W_B + 7 * PAD, 13 * H_B + PAD, W_B+20, H_B));
        add(btnFile);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        String action = ae.getActionCommand();
        if(action == "ADDFILE"){
            addFile();
        }
        else {
            switch (action) {
                case "SAVE":
                    save = true;
                    break;
                case "CANCEL":
                    save = false;
                    break;
            }
            setVisible(false);
        }
    }

    private void addFile(){
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            filePath.setText(file.getAbsolutePath());
        }
    }

    public boolean isSave() {
        return save;
    }
}

