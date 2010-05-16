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

package ch.dbs.entity;

/**
 * Basisklasse für Tabellen mit einem Primärschlüssel {@link Long} 
 * <p/>
 * @author Pascal Steiner
 */
public class AbstractIdEntity extends AbstractEntity {
  /**
   * The unique identifier of the entity.
   */
  private Long id;

  /**
   * Default constructor which is only package visible for hibernate reasons.
   */
  public AbstractIdEntity() { }

  /**
   * @return The unique identifier of the entity.
   */
  public Long getId() {
    return id;
  }
  /**
   * @param id The unique identifier of the entity.
   */
  public void setId(Long id) {
    this.id = id;
  }
}
