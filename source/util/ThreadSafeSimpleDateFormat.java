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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ThreadSafeSimpleDateFormat {

    private final transient DateFormat df;
    //   private static final String TIMEZONE = "GTM+01";

    public ThreadSafeSimpleDateFormat(final String format) {
        this.df = new SimpleDateFormat(format);
    }

    public synchronized String format(final Date date, final String timezone) {
        df.setTimeZone(TimeZone.getTimeZone(timezone));
        return df.format(date);
    }

    public synchronized Date parse(final String string) throws ParseException {
        return df.parse(string);
    }

    public synchronized void setTimeZone(final TimeZone tz) {
        df.setTimeZone(tz);
    }

    //  public static String getTIMEZONE() {
    //    return TIMEZONE;
    //  }


}
