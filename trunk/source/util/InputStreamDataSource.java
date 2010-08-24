//  Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; version 2 of the License.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//  Contact: info@doctor-doc.com

package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class InputStreamDataSource implements DataSource {

    private String name;
    private String contentType;
    private ByteArrayOutputStream baos;

    InputStreamDataSource(String stName, String contentTyp, InputStream inputStream) throws IOException {
        this.name = stName;
        this.contentType = contentTyp;

        baos = new ByteArrayOutputStream();

        int read;
        byte[] buff = new byte[256];
        while ((read = inputStream.read(buff)) != -1) {
            baos.write(buff, 0, read);
        }
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public String getName() {
        return name;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Cannot write to this read-only resource");
    }
}
