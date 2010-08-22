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

package ch.dbs.interf;

import java.sql.Date;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Konto;

/**
 * Interface zum Handeln der OrderForm/Positionen
 * @author Pascal Steiner
 */
public interface OrderHandler {

  public abstract AbstractBenutzer getBenutzer();
  public abstract Konto getKonto();
  public abstract String getPriority();
  public abstract String getDeloptions();
  public abstract String getFileformat();
  public abstract Date getOrderdate();
  public abstract String getMediatype();
  public abstract String getAutor();
  public abstract String getZeitschrift_verlag();
  public abstract String getHeft();
  public abstract String getJahrgang();
  public abstract String getJahr();
  public abstract String getTitel();
  public abstract String getKapitel();
  public abstract String getSeiten();
  public abstract String getWaehrung();
  public abstract String getPreis();

}
