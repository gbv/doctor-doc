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

package test.dbs.entity;


import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Administrator;
import ch.dbs.entity.Benutzer;
import ch.dbs.entity.Bibliothekar;
import ch.dbs.entity.Konto;

public class TestBenutzer extends TestCase{

    private String anrede = "Testanrede";
    private String vorname = "Testvorname";
    private String name= "Testname";
    private String email= "Test@mail.ch";
    private String telefonnrg = "1234567";
    private String telefonnrp = "1234568";
    private String institut = "Testinstitut";
    private String abteilung = "Testabteilung";
    private String adresse = "Testadresse";
    private String adresszusatz = "Testadresszusatz";
    private String plz = "Testplz";
    private String ort = "Testort";
    private String land = "Testland";
    private String password = "Testpasswort";
    private boolean loginopt = false;
    private boolean userbestellung = false; // darf bei SUBITO bestellen
    private boolean gbvbestellung = false; // darf bei GBV bestellen
    private boolean kontostatus = false;
    private boolean kontovalidation = false;
    private boolean validation = false;
    private int rechte = 1;
//    private Long status = Long.valueOf(1);
    private Long billing = Long.valueOf(1);
    private String gtc = "Testgtc";
    private String gtcdate = "01.01.2009";
    private Date lastuse = new Date();
    private String datum = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(lastuse);




  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testSaveBenutzer() throws SQLException {
    //Benutzervalues vorbereiten
    AbstractBenutzer u = this.setUserValues();
    Konto tz = new Konto(); // we need this for setting a default timezone
    Date d = new Date();
    ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datum = fmt.format(d, tz.getTimezone());
        u.setDatum(datum);
    assertNotNull("Es konnte keine Verbindung zur Datenbank hergestellt werden", u.getSingleConnection());
    u.saveNewUser(u, tz, u.getSingleConnection());
    u.close();
  }
  @Test
  public void testLoadBenutzer() throws SQLException {
    //Benutzervalues vorbereiten
    AbstractBenutzer b = new AbstractBenutzer();
    Connection cn = b.getSingleConnection();
    b = b.getUserFromEmail(email, cn);
    cn.close();
    assertEquals(b.getAnrede(), anrede);
    assertEquals(b.getVorname(),vorname);
    assertEquals(b.getName(),name);
    assertEquals(b.getEmail(),email);
    assertEquals(b.getTelefonnrg(),telefonnrg);
    assertEquals(b.getTelefonnrp(),telefonnrp);
    assertEquals(b.getInstitut(),institut);
    assertEquals(b.getAbteilung(),abteilung);
    assertEquals(b.getAdresse(),adresse);
    assertEquals(b.getAdresszusatz(),adresszusatz);
    assertEquals(b.getPlz(),plz);
    assertEquals(b.getOrt(),ort);
    assertEquals(b.getLand(),land);
    assertEquals(b.getPassword(),password);
    assertEquals(b.getDatum(), datum);
    assertEquals(b.getGtc(),gtc);
    assertFalse(b.isLoginopt());
    assertFalse(b.isUserbestellung());
    assertFalse(b.isGbvbestellung());
    assertFalse(b.isKontostatus());
    assertFalse(b.isKontovalidation());
    assertEquals(b.getRechte(),rechte);
    assertEquals(b.getBilling(),billing);
    assertFalse(b.getGtcdate().equals(gtcdate));
    System.out.println("b: "+ b.getLastuse()+" lastuse: "+ lastuse);
    assertEquals(new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(b.getLastuse()),datum);

  }

  @Test
  public void testChangeBenutzer() throws SQLException {
    //Benutzervalues vorbereiten
    AbstractBenutzer b = new AbstractBenutzer();
    Konto tz = new Konto(); // we need this for setting a default timezone
    Connection cn = b.getSingleConnection();
    b = b.getUserFromEmail(email, cn);
    b.setName(name+"1");
    b.setVorname(vorname + "1");
    b.updateUser(b, tz, cn);
    b = b.getUserFromEmail(email, cn);
    cn.close();
    assertEquals(b.getVorname(),vorname+"1");
    assertEquals(b.getName(),name+"1");
  }

  @Test
  public void testDeleteBenutzer() throws SQLException {
    //Benutzervalues vorbereiten
    AbstractBenutzer b = new AbstractBenutzer();
    Connection cn = b.getSingleConnection();
    b.deleteUser(b.getUserFromEmail(email, cn), cn);
    b = null;
    AbstractBenutzer ab = new AbstractBenutzer();
    b = ab.getUserFromEmail(email, cn);
    cn.close();
    assertNull("Noch Testbenutzer im System! Manuell löschen und Test wiederholen ob immer noch fehlschlägt", b);
  }

  @Test
  public void testSave2BenutzerWithSameEmail() throws SQLException {

//    Benutzervalues vorbereiten
    AbstractBenutzer u = this.setUserValues();
    Konto tz = new Konto(); // we need this for setting a default timezone
    Date d = new Date();
    ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datum = fmt.format(d, tz.getTimezone());
        u.setDatum(datum);
    assertNotNull("Es konnte keine Verbindung zur Datenbank hergestellt werden", u.getSingleConnection());
    u.saveNewUser(u, tz, u.getSingleConnection());
    u.saveNewUser(u, tz, u.getSingleConnection());

    List <AbstractBenutzer> abl = u.getAllUserFromEmail(email, u.getSingleConnection());
    assertEquals("Es befinden sich nicht genau 2 Testbenutzer in der DB!",2, abl.size());

    u.deleteUser(u.getUserFromEmail(email, u.getSingleConnection()), u.getSingleConnection());
    u.deleteUser(u.getUserFromEmail(email, u.getSingleConnection()), u.getSingleConnection());

    AbstractBenutzer b = u.getUserFromEmail(email, u.getSingleConnection());
    u.close();
    assertNull("Noch Testbenutzer im System! Manuell löschen und Test wiederholen ob immer noch fehlschlägt", b);
  }

//  @Test
//  public void testSave2BibliothekareWithSameEmail() throws SQLException {
//
////    Benutzervalues vorbereiten
//    AbstractBenutzer u = this.setUserValues();
//    assertNotNull("Es konnte keine Verbindung zur Datenbank hergestellt werden", u.getConnection());
//    u.saveNewUser(u, u.getConnection());
//    u.saveNewUser(u, u.getConnection());
//
//    List <AbstractBenutzer> abl = u.getAllUserFromEmail(email, u.getConnection());
//    assertEquals("Es befinden sich nicht genau 2 Testbenutzer in der DB!",2, abl.size());
//
//    AbstractBenutzer.deleteUser(AbstractBenutzer.getUserFromEmail(email, u.getConnection()), u.getConnection());
//    AbstractBenutzer.deleteUser(AbstractBenutzer.getUserFromEmail(email, u.getConnection()), u.getConnection());
//
//    AbstractBenutzer b = AbstractBenutzer.getUserFromEmail(email, u.getConnection());
//    u.close();
//    assertNull("Noch Testbenutzer im System! Manuell löschen und Test wiederholen ob immer noch fehlschlägt", b);
//  }

  /*
   * Füllt die in den Membervariabeln dieser Klasse gesetzten Werte in den AbstractBenutzer
   * (Benutzer / Bibliothekar / Anministrator) ein
   */
  private AbstractBenutzer setUserValues() {

    AbstractBenutzer u = null;

    if (rechte == 3) {
      u = new Administrator();
    }
    if (rechte == 2) {
      u = new Bibliothekar();
    }
    if (rechte == 1) {
      u = new Benutzer();
    }
    if (u != null) {
      u.setInstitut(institut);
          u.setAbteilung(abteilung);
          u.setAnrede(anrede);
          u.setVorname(vorname);
          u.setName(name);
          u.setAdresse(adresse);
          u.setAdresszusatz(adresszusatz);
          u.setTelefonnrp(telefonnrp);
          u.setTelefonnrg(telefonnrg);
          u.setPlz(plz);
          u.setOrt(ort);
          u.setLand(land);
          u.setEmail(email);
          u.setPassword(password);
          u.setLoginopt(loginopt);
          u.setUserbestellung(userbestellung);
          u.setGbvbestellung(gbvbestellung);
          u.setBilling(billing);
          u.setKontovalidation(kontovalidation);
          u.setValidation(validation);
          u.setKontostatus(kontostatus);
//          u.setRechte(rechte);
//          u.setStatus(status);
          u.setGtc(gtc);
          u.setGtcdate(gtcdate);
          u.setDatum(datum);
          u.setLastuse(lastuse);
    }


        return u;

  }
}