//	Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//	This program is free software; you can redistribute it and/or
//	modify it under the terms of the GNU General Public License
//	as published by the Free Software Foundation; version 2 of the License.
//
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with this program; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//	Contact: info@doctor-doc.com

package util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class SimpleLineReader {
	private FilterInputStream s;

	/**
	 * Erstellt die Klasse SimpleLineReader, welche einen InputStream als FilterInputStream abspeichert.
	 * 
	 * @param anIS InputStream
	 */
	public SimpleLineReader(InputStream anIS) {
		s = new DataInputStream(anIS);
	}

	/**
	 * Kopiert die erste Zeile des FilterInputStream aus der private Variablen s und gibt diese als String zurueck
	 * 
	 * @return
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		char[] buffer = new char[100];
		int offset = 0;
		byte thisByte;

		try {
			loop: while (offset < buffer.length) {
				switch (thisByte = (byte) s.read()) {
				case '\n':
					break loop;
				case '\r':
					byte nextByte = (byte) s.read();

					if (nextByte != '\n') {
						if (!(s instanceof PushbackInputStream)) {
							s = new PushbackInputStream(s);
						}
						((PushbackInputStream) s).unread(nextByte);
					}
					break loop;
				default:
					buffer[offset++] = (char) thisByte;
					break;
				}
			}
		} catch (EOFException e) {
			if (offset == 0)
				return null;
		}

		return String.copyValueOf(buffer, 0, offset);
	}
}
