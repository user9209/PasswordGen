/*
    Copyright (C) 2017  Georg Schmidt <gs-develop@gs-sys.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;

/**
 * Mini Password Generator
 * Randomness based on "java.security.SecureRandom"
 *
 * @author Georg Schmidt <gs-develop@gs-sys.de>
 * @version 1.4 on 07.04.2017
 */

public class PasswordGen extends JFrame {
    private final char[] alph = new char[]
            {'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'y', 'x', 'c', 'v', 'b', 'n', 'm',
                    'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    '!', '"', '$', '%', '&', '/', '(', ')', '=', '?', '\\', '}', ']', '[', '{', '+', '*', '#', '-', '_', '.', ':', ',', ';', '@'};

    private final String version = "1.4";

    private int defaultLengthPassword = 15;
    private String iniFileName = "passwordgen.ini";

    private JTextField tf_pw = new JTextField();
    private JButton b_generate = new JButton();
    private JLabel l_version = new JLabel();
    private SecureRandom rand = new SecureRandom();
    private JSpinner js_length = new JSpinner();
    private SpinnerNumberModel js_lengthModel = new SpinnerNumberModel(12, 1, 64, 1);

    public PasswordGen() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int frameWidth = 290;
        int frameHeight = 140;
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setTitle("PasswordGen");
        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);

        tf_pw.setBounds(10, 8, 270, 33);
        tf_pw.setEditable(false);
        tf_pw.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(tf_pw);
        b_generate.setBounds(90, 50, 190, 33);
        b_generate.setText("Generate");
        b_generate.setMargin(new Insets(2, 2, 2, 2));
        b_generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b_generate_ActionPerformed(evt);
            }
        });
        cp.add(b_generate);
        js_length.setBounds(10, 50, 40, 33);
        js_length.setModel(js_lengthModel);
        cp.add(js_length);
        l_version.setBounds(60, 50, 30, 33);
        l_version.setText(version);
        cp.add(l_version);


        // Load password length from file
        if (new File(iniFileName).exists()) {
            try {
                defaultLengthPassword = Integer.parseInt(new String(Files.readAllBytes(Paths.get(iniFileName))));
            } catch (Exception e) {
            }
        }

        // set password length
        js_length.setValue(defaultLengthPassword);

        // generate new password at creation time
        b_generate_ActionPerformed(null);

        setVisible(true);
    }

    public static void main(String[] args) {
        new PasswordGen();
    }

    private void b_generate_ActionPerformed(ActionEvent evt) {
        int size = (Integer) js_length.getValue();

        // save password length to file if it has been changed
        if(size != defaultLengthPassword)
        {
            defaultLengthPassword = size;
            try {
                Files.write(
                    Paths.get(iniFileName),
                    Integer.toString(defaultLengthPassword).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
                );
            } catch (Exception e) {}
        }

        // Generate password
        char[] newPW = new char[size];
        for (int i = 0; i < size; i++) {
            newPW[i] = alph[rand.nextInt(alph.length)];
        }

        tf_pw.setText(new String(newPW));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(tf_pw.getText()), null);
    }
}
