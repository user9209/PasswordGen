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

package guifx;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ResourceBundle;


public class ControllerMain implements Initializable {

    private final char[] alph = new char[]
            {'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'y', 'x', 'c', 'v', 'b', 'n', 'm',
                    'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M',
                    '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    '!', '"', '$', '%', '&', '/', '(', ')', '=', '?', '\\', '}', ']', '[', '{', '+', '*', '#', '-', '_', '.', ':', ',', ';', '@'};

    private final String versionString = "V 1.6 fx";

    private int defaultLengthPassword = 15;
    private String iniFileName = "passwordgen.ini";

    private SecureRandom rand = new SecureRandom();


    // are true, if key is pressed
    private static boolean isControlPressed = false;
    private static boolean isAltPressed = false;
    private static boolean isShiftPressed = false;

    @FXML
    TextField password_field;

    @FXML
    // http://o7planning.org/en/11185/javafx-spinner-tutorial
    Spinner<Integer> spinner_length;

    @FXML
    Label version;

    @FXML
    Button button_generate;

    @FXML
    void onGenerate() {
        int size = spinner_length.getValue();

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
            } catch (Exception ignored) {}
        }

        // Generate password
        char[] newPW = new char[size];
        for (int i = 0; i < size; i++) {
            newPW[i] = alph[rand.nextInt(alph.length)];
        }

        password_field.setText(new String(newPW));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password_field.getText()), null);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Load password length from file
        if (new File(iniFileName).exists()) {
            try {
                defaultLengthPassword = Integer.parseInt(new String(Files.readAllBytes(Paths.get(iniFileName))));
            }
            catch (Exception ignored) {
            }
        }

        version.setText(versionString );

        // generate new password at creation time
        onGenerate();

        // set password length
        spinner_length.getValueFactory().setValue(defaultLengthPassword);

        spinner_length.getValueFactory().valueProperty().addListener((obs, oldValue, newValue) -> onGenerate());
        //SpinnerValueFactory<Integer> valueFactory = //
        //        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, defaultLengthPassword);
        //spinner_length.setValueFactory(valueFactory);

        // Default in button
        Platform.runLater(() -> button_generate.requestFocus());
    }

    /**
     * Registers the hotkeys
     */
    static void loadHotkeys(Stage stage)
    {
        stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                switch (k.getCode())
                {
                    case M:
                        if(isControlPressed) {
                            System.out.println("M");
                        }
                        break;
                    case S:
                        if(isControlPressed)
                            System.out.println("S");
                        break;
                    case O:
                        if(isControlPressed)
                            System.out.println("O");
                        break;
                    case CONTROL:
                        isControlPressed = true;
                        break;
                    case ALT:
                        isAltPressed = true;
                        break;
                    case SHIFT:
                        isShiftPressed = true;
                        break;
                    default:
                }
            }
        });

        stage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                switch (k.getCode())
                {
                    case CONTROL:
                        isControlPressed = false;
                        break;
                    case ALT:
                        isAltPressed = false;
                        break;
                    case SHIFT:
                        isShiftPressed = false;
                        break;
                    default:
                }
            }
        });
    }
}
