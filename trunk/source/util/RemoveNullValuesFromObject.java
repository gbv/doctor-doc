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

import java.lang.reflect.Method;

import org.grlea.log.SimpleLogger;

public class RemoveNullValuesFromObject {

  private static final SimpleLogger LOG = new SimpleLogger(RemoveNullValuesFromObject.class);

    /**

     * @author Pascal Steiner
     */

  public RemoveNullValuesFromObject() {

  }

    /**
     * Replace all null values from an object in his .getters & .setters with String as GenericReturnType()
     * & GenericParameterTypes
     *
     * @author Pascal Steiner
     * @param Object o
     * @return Object o without null values in his getters and setters methods
     *
     */
    public Object remove(Object o) {

        Method[] methods = o.getClass().getMethods();
        Object[] setvalue = new Object[1];
    setvalue[0] = "";

    // Alle Methoden der Klasse durchlaufen
        for (Method m : methods) {
          // Alle getter welche einen String zurückgeben behandeln
            if (m.getGenericReturnType() ==  "".getClass()) {
              try {
                // liefert get null? (getmethode ausführen)
          if (m.invoke(o) == null) {
            // Name der Membervariable heraussuchen
                  String membervar = m.getName().substring(3);

                  // Falls es eine settermethode mit simplem String als Parameter gibt den wert null durch "" ersetzen
                  Method methode = o.getClass().getMethod("set" + membervar, "".getClass());
            methode.invoke(o, setvalue);
                }
        } catch (Exception e) {
          LOG.error("RemoveNullvaluesFrom.simplyGettterSetterMethods: " + e.toString());
        }
            }
        }
      return o;
    }




}
