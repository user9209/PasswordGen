package de.gs_sys.lib.crypto.passwords;

import java.math.BigInteger;

import static de.gs_sys.lib.crypto.passwords.Complexity.SPECIALCHARS_REGEX;

/*
    Copyright (C) 2018  Georg Schmidt <gs-develop@gs-sys.de>

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

public class PasswordStrength {

    /**
     * Charset and length of a password
     * @param password
     */
    public static Complexity complexity(String password) {
        Complexity complexity = new Complexity();
        int chars = 0;
        if(password.matches(".*[0-9].*"))
        {
            chars = 10;
            complexity.setNumbers();
        }

        if(password.matches(".*[A-Z].*"))
        {
            chars += 26;
            complexity.setUppercase();
        }

        if(password.matches(".*[a-z].*"))
        {
            chars += 26;
            complexity.setLowercase();
        }

        if(password.matches(".*[" + SPECIALCHARS_REGEX + "].*"))
        {
            chars += 17;
            complexity.setSpecialcahrs();
        }

        if(chars == 0)
        {
            System.out.println("Can not analyse " + password);
            return new Complexity();
        }

        complexity.setCharsetSize(chars);
        complexity.setLength(password.length());
        complexity.setBit(bitStrength(chars,complexity.getLength()));

        return complexity;
    }

    /**
     * Returns the corresponding bits of a password complexity
     * @param base
     * @param exponent
     * @return
     */
    private static int bitStrength(int base, int exponent) {
        BigInteger b = BigInteger.valueOf(base);

        BigInteger r = b.pow(exponent);
        int res =  (int) (Math.log(r.doubleValue()) / Math.log(2));
        if (res > 20000)
            return 1024;
        return res;
    }
}
