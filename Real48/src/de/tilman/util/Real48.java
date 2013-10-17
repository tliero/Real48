/*
 * Borland Real48 in Java
 * 
 * When writing a converter tool for a binary file format, I had problems 
 * reading some of the floating point numbers. After a little bit of research 
 * I found out that this very special format was used in programs written in 
 * Borland Pascal/Delphi. Since I couldn't find any Java class that's able to 
 * read this kind of number I wrote it myself.
 * As far as I remember these are basically reverse IEEE floating point 
 * numbers of six bytes. (It's been a while since I wrote this. See the PDF 
 * file by Richard Biffl for a detailed description of the format.)
 * 
 * References:
 * 
 * http://www.wotsit.org/list.asp?search=Borland+Pascal+Real&button=GO!
 * http://mail.python.org/pipermail/python-list/2001-December/117947.html
 */

package de.tilman.util;

import java.math.BigDecimal;

/**
 * A class for representation and conversion of 6-byte/48-bit floating point
 * numbers used in Borland Pascal/Delphi.
 * 
 * @author Tilman Liero
 */
public class Real48 {

	char[] bytes;
	double value;

	/**
	 * Represents a number in Real48 format.
	 * 
	 * @param bytes
	 *            the byte values of the number
	 */
	public Real48(char[] bytes) {
		this.bytes = bytes;
		this.value = Real48.getDoubleFromBytes(bytes);
	}

	/**
	 * Returns the value of the number as double.
	 * 
	 * @return the number as double
	 */
	public double getDoubleValue() {
		return value;
	}

	/**
	 * Returns the byte values of the Real48 as char[].
	 * 
	 * @return the byte values
	 */
	public char[] getBytes() {
		return bytes;
	}

	/**
	 * Converts a Real48 in a double.
	 * 
	 * @param bytes
	 *            the number in Real48 format
	 * @return the number as double
	 */
	public static double getDoubleFromBytes(char[] bytes) {
		long[] real48 = new long[6];
		real48[0] = bytes[0];
		real48[1] = bytes[1];
		real48[2] = bytes[2];
		real48[3] = bytes[3];
		real48[4] = bytes[4];
		real48[5] = bytes[5];

		// get sign
		long sign = (real48[0] & 0x80) >> 7;

		// get significand
		long significand = ((real48[0] % 0x80) << 32) + (real48[1] << 24)
				+ (real48[2] << 16) + (real48[3] << 8) + (real48[4]);

		// get exponent
		long exponent = bytes[5];

		// if the exponent is zero the real represents zero, too
		if (exponent == 0) {
			return 0.0;
		}

		// convert exponent (bias for Real48 is 129, bias for IEEE 754-double is 1023)
		exponent += 894;

		long bits = (sign << 63) + (exponent << 52) + (significand << 13);

		double value = Double.longBitsToDouble(bits);

		// since Real48 has only a precision of 11 we need to set all digits > 11 to zero
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(11, BigDecimal.ROUND_DOWN);

		return bd.doubleValue();
	}

}