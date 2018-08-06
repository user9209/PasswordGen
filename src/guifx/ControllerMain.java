/*
    Copyright (C) 2017, 2018  Georg Schmidt <gs-develop@gs-sys.de>

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

import de.gs_sys.lib.crypto.passwords.PasswordStrength;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ResourceBundle;


public class ControllerMain implements Initializable {

    enum ALPHABET {AZaz09S, AZaz09, AZaz, AZ09, az09, a09}

    private final String versionString = "V 1.8 fx";
    // Todo: no possibility to save ini file non-portable
    private final String[] dirList = new String[]{System.getProperty("user.home") + "/", System.getenv("APPDATA") + "/", "./"};

    private int defaultLengthPassword = 15;
    private ALPHABET currentAlphabet = ALPHABET.AZaz09;
    private String iniFileName = "passwordgen.ini";
    private String iniDir = "";

    private SecureRandom rand = new SecureRandom();
    private static ControllerMain instance;

    // are true, if key is pressed
    private static boolean isControlPressed = false;
    private static boolean isAltPressed = false;
    private static boolean isShiftPressed = false;

    @FXML
    private TextField password_field;

    @FXML
    // http://o7planning.org/en/11185/javafx-spinner-tutorial
    private Spinner<Integer> spinner_length;

    @FXML
    private ChoiceBox<String> choicebox_charset;

    @FXML
    private Button b_version;

    @FXML
    private Label passwordQuality;

    @FXML
    private Button button_generate;

    @FXML
    void onButtonVersion() {
        password_field.setText("Copyright (c) 2018 Georg Schmidt");
    }

    @FXML
    private void onGenerate() {
        int size = spinner_length.getValue();

        // save password length to file if it has been changed
        if(size != defaultLengthPassword || currentAlphabet != ALPHABET.AZaz09S)
        {
            defaultLengthPassword = size;
            writeDataToIni(defaultLengthPassword, choicebox_charset.getSelectionModel().getSelectedIndex());
        }

        // Generate password
        char[] newPW = new char[size];

        setAlphabet();

        for (int i = 0; i < size; i++) {
            newPW[i] = alpha[rand.nextInt(alphaLength)];
        }

        password_field.setText(new String(newPW));

        setQuality(password_field.getText());

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password_field.getText()), null);
    }

    private void setQuality(String password) {
        passwordQuality.setText("Corresponds to " + PasswordStrength.complexity(password).getBit() + " bit.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        instance = this;

        String[] config = new String[0];

        // Load password length from file
        if (findIni()) {
            config = loadIni();
        }

        b_version.setText(versionString );

        // set password length
        spinner_length.getValueFactory().setValue(defaultLengthPassword);

        spinner_length.getValueFactory().valueProperty().addListener((obs, oldValue, newValue) -> onGenerate());
        //SpinnerValueFactory<Integer> valueFactory = //
        //        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, defaultLengthPassword);
        //spinner_length.setValueFactory(valueFactory);

        choicebox_charset.getItems().addAll(Arrays.asList(
                "[0-9]",
                "[A-Za-z]",
                "[A-Z0-9]",
                "[a-z0-9]",
                "[A-Za-z0-9]",
                "[A-Za-z0-9] + Special chars"
        ));

        choicebox_charset.valueProperty().addListener((obs, oldValue, newValue) -> {
            setAlphabetByString(newValue);
            onGenerate();
        });
        choicebox_charset .getSelectionModel().select(config.length == 2 ? Integer.parseInt(config[1]): 4);

        // generate new password at creation time
        onGenerate();

        // Default in button
        Platform.runLater(() -> button_generate.requestFocus());
    }

    /**
     * Registers the hotkeys
     */
    public static void loadHotkeys(Stage stage)
    {
        stage.getScene().setOnKeyPressed(keyEvent1 -> {
            switch (keyEvent1.getCode())
            {
                case ESCAPE:
                    System.exit(0);
                    break;
                case ENTER:
                    instance.onGenerate();
                    break;
                case O:
                    if(isControlPressed)
                        System.out.println("CTRL + O");
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
        });

        stage.getScene().setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode())
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
        });
    }


    /* Multi-Alphabets */
    private char[] alpha;
    private int alphaLength;

    private final char[] alp09 = new char[]
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final char[] alphaz09 = new char[]
            {'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'y', 'x', 'c', 'v', 'b', 'n', 'm',
                    '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final char[] alphAZ09 = new char[]
            {'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M',
                    '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final char[] alphAZaz = new char[]
            {'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'y', 'x', 'c', 'v', 'b', 'n', 'm',
                    'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M'};

    private final char[] alphAZaz09 = new char[]
            {'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'y', 'x', 'c', 'v', 'b', 'n', 'm',
                    'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M',
                    '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final char[] alphAZaz09S = new char[]
            {'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'y', 'x', 'c', 'v', 'b', 'n', 'm',
                    'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M',
                    '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    '!', '$', '%', '&', '/', '(', ')', '=', '?', '+', '*', '-', '_', '.', ':', '@'};

    private void setAlphabet() {
        switch (currentAlphabet) {
            case AZaz09S:
                alpha = alphAZaz09S;
                alphaLength = alpha.length;
                break;
            case AZaz:
                alpha = alphAZaz;
                alphaLength = alpha.length;
                break;
            case AZ09:
                alpha = alphAZ09;
                alphaLength = alpha.length;
                break;
            case az09:
                alpha = alphaz09;
                alphaLength = alpha.length;
                break;
            case a09:
                alpha = alp09;
                alphaLength = alpha.length;
                break;
            case AZaz09:
            default:
                alpha = alphAZaz09;
                alphaLength = alpha.length;
        }
    }

    private void setAlphabetByString(String newValue) {
        switch (newValue) {
            case "[0-9]":
                currentAlphabet = ALPHABET.a09;
                break;
            case "[A-Za-z]":
                currentAlphabet = ALPHABET.AZaz;
                break;
            case "[A-Z0-9]":
                currentAlphabet = ALPHABET.AZ09;
                break;
            case "[a-z0-9]":
                currentAlphabet = ALPHABET.az09;
                break;
            case "[A-Za-z0-9] + Special chars":
                currentAlphabet = ALPHABET.AZaz09S;
                break;
            case "[A-Za-z0-9]":
            default:
                currentAlphabet = ALPHABET.AZaz09;
        }
    }

    private boolean findIni() {

        for(String x : dirList) {

            System.out.println(Paths.get(x + iniFileName).toAbsolutePath()  + " " + Files.exists(Paths.get(x + iniFileName)));
            if(Files.exists(Paths.get(x + iniFileName)))
            {
                iniDir =  x;
                return true;
            }
        }
        return false;
    }

    private String[] loadIni() {
        String[] config = new String[0];
        try {
            config = new String(Files.readAllBytes(Paths.get(iniDir + iniFileName)), StandardCharsets.UTF_8).split("\\|");

            defaultLengthPassword = Integer.parseInt(config[0]);
        }
        catch (Exception ignored) {
        }
        return config;
    }

    public void writeDataToIni(int passwordlength, int alphabet){
        try {
            Files.write(
                    Paths.get(iniDir + iniFileName),
                    (Integer.toString(passwordlength) + "|" + alphabet).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
            );
        } catch (Exception ignored) {}
    }
}
