package de.gs_sys.lib.crypto.passwords;

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

public class Complexity {

    public static final String SPECIALCHARS = "!$%&/()=?-_@+*.:#";
    public static final String SPECIALCHARS_REGEX = "!$%&/()=?\\-_@+*.:#";

    private int bit = -1;
    private int length = -1;
    private int charsetSize = -1;
    private boolean uppercase = false;
    private boolean lowercase = false;
    private boolean numbers = false;
    private boolean specialchars = false;

    public void setUppercase() {
        this.uppercase = true;
    }

    public void setLowercase() {
        this.lowercase = true;
    }

    public void setNumbers() {
        this.numbers = true;
    }

    public void setSpecialcahrs()  {
        this.specialchars = true;
    }

    @Override
    public String toString() {
        return "Complexity{" +
                "bit=" + bit +
                ", length=" + length +
                ", charsetSize=" + charsetSize +
                '}';
    }

    public int getBit() {
        return bit;
    }

    public void setBit(int bit) {
        this.bit = bit;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setCharsetSize(int charsetSize) {
        this.charsetSize = charsetSize;
    }
}
