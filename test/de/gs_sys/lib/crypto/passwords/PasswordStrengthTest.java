package de.gs_sys.lib.crypto.passwords;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordStrengthTest {

    @Test
    public void complexity1() {
        Complexity com = PasswordStrength.complexity("a!84");
        assertEquals("Complexity{bit=22, length=4, charsetSize=53}", com.toString());
    }
    @Test
    public void complexity2() {
        Complexity com = PasswordStrength.complexity("a!8A4");
        assertEquals("Complexity{bit=31, length=5, charsetSize=79}", com.toString());
    }
    @Test
    public void complexity3() {
        Complexity com = PasswordStrength.complexity("aaaa");
        assertEquals("Complexity{bit=18, length=4, charsetSize=26}", com.toString());
    }
    @Test
    public void complexity4() {
        Complexity com = PasswordStrength.complexity("AAAA");
        assertEquals("Complexity{bit=18, length=4, charsetSize=26}", com.toString());
    }
    @Test
    public void complexity5() {
        Complexity com = PasswordStrength.complexity("0359");
        assertEquals("Complexity{bit=13, length=4, charsetSize=10}", com.toString());
    }
}