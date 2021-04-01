import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ReadDialog extends JDialog implements ActionListener {

    final JTextPane countOfMes = new JTextPane();


    private final int PAD = 25;
    private final int W_L = 100;
    private final int W_T = 100;
    private final int W_B = 120;
    private final int H_B = 25;



    public ReadDialog(){
        setTitle("Введите номер письма");

        buildFields("Номер:",countOfMes,1);

        buildBtn();

        setLayout(null);
        setModal(true);
        setBounds(500, 200, 300, 350);
        setVisible(true);

    }

    protected void buildFields(String name,JTextPane pane,int counter) {
        JLabel lbl = new JLabel(name);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setBounds(new Rectangle(PAD, counter * H_B + PAD, W_L+20, H_B));
        add(lbl);
        pane.setBounds(new Rectangle(3 * PAD, counter * H_B + PAD, W_B, H_B));
        pane.setBorder(BorderFactory.createEtchedBorder());
        add(pane);

    }

    protected void buildBtn() {
        JButton btnSave = new JButton("Сохранить");
        btnSave.setActionCommand("SAVE");
        btnSave.addActionListener(this);
        btnSave.setBounds(new Rectangle(3 * PAD, 6*H_B + PAD, W_B, H_B));
        add(btnSave);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
            setVisible(false);
    }
}

