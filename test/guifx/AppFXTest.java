package guifx;


import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Rule;
import org.junit.Test;

import org.junit.experimental.theories.Theories;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

// import static org.testfx.assertions.api.Assertions.assertThat;


public class AppFXTest extends ApplicationTest {
    AppFX appFX;


    @Rule
    public TestName name = new TestName();

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Override
    public void start(Stage stage) throws Exception {
        AppFX.underTest = true;
        appFX = new AppFX();
        appFX.start(null);

        stage = appFX.getRoot();
        stage.show();

        Thread.sleep(2);
    }

    public NodeQuery getNodeQuery() {
        return from(appFX.getRoot().getScene().getRoot());
    }

/*
    @Test
    public void testChangeCharsetMouse() {
        ChoiceBox<String> c = getNodeQuery().lookup("#choicebox_charset").queryAs(ChoiceBox.class);

        c.getSelectionModel().select(4);

        String password1 = getNodeQuery().lookup("#password_field").queryAs(TextField.class).getText();

        System.out.println(password1);

        assertTrue(password1.matches("[a-z0-9]+"));
    }
*/
    @Test
    public void testChangeCharset() {
        ChoiceBox c = getNodeQuery().lookup("#choicebox_charset").queryAs(ChoiceBox.class);

        assertEquals(4,c.getSelectionModel().getSelectedIndex());

//        c.getSelectionModel().select(5);

        clickOn(c);
        press(KeyCode.UP);
        press(KeyCode.ENTER);

        // clickOn(getNodeQuery().lookup("#button_generate").queryAs(Button.class));

        String password1 = getNodeQuery().lookup("#password_field").queryAs(TextField.class).getText();

        System.out.println(password1);

        assertTrue(password1.matches("[a-z0-9]+"));
        assertEquals("[a-z0-9]", c.getSelectionModel().getSelectedItem());

        clickOn(c);
        press(KeyCode.DOWN);
        press(KeyCode.ENTER);
    }

    @Test
    public void testPasswordsDiffer() {

        String password1 = getNodeQuery().lookup("#password_field").queryAs(TextField.class).getText();
        // clickOn(root.lookup("#button_generate").queryAs(Button.class));

        //moveTo(getNodeQuery().lookup("#button_generate").queryAs(Button.class));

        clickOn(getNodeQuery().lookup("#button_generate").queryAs(Button.class));
        /*
        try {
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {getNodeQuery().lookup("#button_generate").queryAs(Button.class).fire(); return  true;});
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        */
        // press(KeyCode.ENTER);
        //WaitForAsyncUtils.waitForFxEvents();
        sleep(2, SECONDS);


        String password2 = getNodeQuery().lookup("#password_field").queryAs(TextField.class).getText();

        System.out.println(name.getMethodName() + ": assertions 1\nPasswords not equal:");
        System.out.println(password1);
        System.out.println(password2);

        assertNotEquals(password1,password2);


        // like clickOn bit direct fire
        //getNodeQuery().lookup("#button_generate").queryAs(Button.class).fire();
        clickOn(getNodeQuery().lookup("#button_generate").queryAs(Button.class));
        sleep(2, SECONDS);

        String password3 = getNodeQuery().lookup("#password_field").queryAs(TextField.class).getText();

        System.out.println(name.getMethodName() + ": assertions 2");
        assertNotEquals(password1,password3);

        /*
        try {
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> false);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        */


        //assertThat(lookup("#password_field").queryAs(TextField.class)).hasText(password1);


        /*
        // given:
        rightClickOn("#desktop").moveTo("New").clickOn("Text Document");
        write("myTextfile.txt").push(KeyCode.ENTER);

        // when:
        drag(".file").dropTo("#trash-can");

        // then:
        assertThat(desktopPane).hasChildren(0, ".file");
        // or (lookup by css id):
        assertThat(lookup("#desktop").queryAs(DesktopPane.class)).hasChildren(0, ".file");
        // or (look up css class):
        assertThat(lookup(".desktop-pane").queryAs(DesktopPane.class)).hasChildren(0, ".file");
        */
    }

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void main() {
    }

    @org.junit.Test
    public void start1() {
    }
}