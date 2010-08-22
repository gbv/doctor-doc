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

package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.Encrypt;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Benutzer;
import ch.dbs.entity.Bibliothekar;
import ch.dbs.entity.Konto;
import ch.dbs.entity.VKontoBenutzer;
import ch.dbs.form.KontoForm;
import ch.dbs.form.LoginForm;
import ch.dbs.form.UserForm;
import ch.ddl.entity.Position;

public class PrepareTestObjects {

  /** Passwort welches zum Login benötigt wird */
  public static final String LOGINPW = "testpw";

  /** E-Mail welche zum Login benötigt wird */
  public static final String BNEMAIL = "testitestmail@eldali.ch";


  public static final Long KONTOID = Long.valueOf(1);

  public static final String BIBLIONAME1 = "Biblioname1";
  public static final String BIBLIONAME2 = "Biblioname2";

  /**
   * Erstellt ein Loginform mit Linkresolver-Angaben
   *
   * @return LoginForm
   */
  public static LoginForm getloginForm() {
    LoginForm lf = new LoginForm();

    lf.setMediatype("Artikel");
    lf.setIssn("0803-5253");
    lf.setJahr("1995");
    lf.setJahrgang("84");
    lf.setHeft("9");
    lf.setSeiten("1019-28");
    lf.setArtikeltitel("Swedish+population+reference+standards+for+height%2C+weight+and+body+mass+index+attained+at+6+to+16+years+%28girls%29+or+19+years+%28boys%29");
    lf.setZeitschriftentitel("Acta+paediatrica+%28Oslo%2C+Norway+%3A+1992%29");
    lf.setAuthor("Lindgren+G");
    lf.setGenre("Journal+Article");
    lf.setPmid("info%3Apmid%2F8652953");
    lf.setResolver(true);

    return lf;
  }

  /**
   * Erstellt ein Test-Kontoform
   * @return KontoForm
   */
  public static KontoForm getKontoValues() {

    KontoForm kf = new KontoForm();

    kf.setBiblioname(BIBLIONAME1);
    kf.setAdresse("adresse");
    kf.setPLZ("1234");
    kf.setLand("Schweiz");
    kf.setTelefon("12345678");
    kf.setBibliotheksmail("info@doctor-doc.com");
    kf.setDbsmail("dbs@mail.bl");
    kf.setUserlogin(false);

    kf.setEzbid(""); //verhindert NullPointer in KontoAction

    return kf;

  }

  public static ArrayList <Konto> getTestkonto() {

    ArrayList <Konto> kl = new ArrayList<Konto>();
    Konto cn = new Konto();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
            pstmt = cn.getSingleConnection().prepareStatement("SELECT * FROM konto WHERE `biblioname` = ? or `biblioname` = ?");
            pstmt.setString(1, BIBLIONAME1);
            pstmt.setString(2, BIBLIONAME2);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                kl.add(new Konto(rs));
            }

        } catch (Exception e) {
            System.out.println("getKontoValues() trat folgender Fehler auf: \012"
                    + e);
        } finally {
            if (rs != null) {
              try {
                rs.close();
              } catch (SQLException e) {
                System.out.println(e);
              }
            }
            if (pstmt != null) {
              try {
                pstmt.close();
              } catch (SQLException e) {
                System.out.println(e);
              }
            }
            cn.close();
          }
        return kl;
  }

  public static void clearTestObjects() {
    // UserForm vorbereiten
//    UserForm uf = PrepareTestObjects.getUserForm();
    ArrayList<Konto> oldkl = PrepareTestObjects.getTestkonto();

    Konto cn = new Konto();

    for (Konto k : oldkl) {
      System.out.println("Konto ID = " + k.getId());
      k.deleteSelf(cn.getSingleConnection());
    }

    VKontoBenutzer vkb = new VKontoBenutzer();
    ArrayList<AbstractBenutzer> alb = new AbstractBenutzer().getAllUserFromEmail(BNEMAIL, cn.getSingleConnection());
    for (AbstractBenutzer b : alb) {
      vkb.deleteAllKontoEntries(b, cn.getSingleConnection());
      b.deleteUser(b, cn.getSingleConnection());
    }
    cn.close();
  }

  /**
   * Erstellt ein Test-UserForm
   */
  public static UserForm getUserForm() {

    UserForm uf = new UserForm();

    uf.setAnrede("Herr");
    uf.setName("Testname"); //Mussfeld beim Bibliothekar erstellen
    uf.setVorname("Testvorname"); //Mussfeld beim Bibliothekar erstellen
    uf.setEmail(BNEMAIL); //Mussfeld beim Bibliothekar erstellen
    uf.setKontostatus(true);
    Encrypt e = new Encrypt();
    uf.setPassword(e.makeSHA(LOGINPW)); //Das Passwort muss mindestens 7 Zeichen lang sein

    return uf;
  }

  /**
   *  Erstellt ein Testkontoobjekt
   *
   * @return Konto
   */
  public static Konto getKonto() {

    Konto k = new Konto(getKontoValues());
    return k;

  }

  /**
   * Erstellt ein TestBenutzerObjekt
   *
   * @return Benutzer
   */
  public static Benutzer getBenutzer() {
    Benutzer b = new Benutzer(getUserForm());
    return b;
  }

  /**
   * Holt eine Testbenutzer aus der Datenbank
   * @param cn
   * @return
   */
  public static Benutzer getTestBenutzerFromDb(Connection cn) {
    Benutzer b = null;
    AbstractBenutzer ab = new AbstractBenutzer();
    Encrypt e = new Encrypt();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
            pstmt = cn.prepareStatement(
            "SELECT * FROM `benutzer` AS b " +
            "WHERE b.mail = ? AND b.pw = ? " +
            "AND b.rechte = 1 ");
            pstmt.setString(1, BNEMAIL);
            pstmt.setString(2, e.makeSHA(LOGINPW));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                b = (Benutzer) ab.getUser(rs);
            }
    } catch (Exception err) {
          String f = "In PrepareTestObjects.getTestBenutzerFromDb(Connection cn) trat folgender Fehler auf:\n";
      System.out.println(f + err);
        } finally {
          if (rs != null) {
            try {
              rs.close();
            } catch (SQLException ex) {
              System.out.println(ex);
            }
          }
          if (pstmt != null) {
            try {
              pstmt.close();
            } catch (SQLException ex) {
              System.out.println(ex);
            }
          }
        }

    return b;
  }

  /**
   * Holt eine Testbibliothekar aus der Datenbank
   * @param cn
   * @return
   */
  public static Bibliothekar getTestBibliothekarFromDb(Connection cn) {
    Bibliothekar b = null;
    AbstractBenutzer ab = new AbstractBenutzer();
    Encrypt e = new Encrypt();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
            pstmt = cn.prepareStatement(
            "SELECT * FROM `benutzer` AS b " +
            "WHERE b.mail = ? AND b.pw = ? " +
            "AND b.rechte = 2 ");
            pstmt.setString(1, BNEMAIL);
            pstmt.setString(2, e.makeSHA(LOGINPW));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                b = (Bibliothekar) ab.getUser(rs);
            }
    } catch (Exception err) {
          String f = "In PrepareTestObjects.getTestBenutzerFromDb(Connection cn) trat folgender Fehler auf:\n";
      System.out.println(f + err);
        } finally {
          if (rs != null) {
            try {
              rs.close();
            } catch (SQLException ex) {
              System.out.println(ex);
            }
          }
          if (pstmt != null) {
            try {
              pstmt.close();
            } catch (SQLException ex) {
              System.out.println(ex);
            }
          }
        }

    return b;
  }

  /**
   * Holt ein Konto aus der Datenbank
   * @param cn
   * @return
   */
  public static Konto getTestKontoFromDb(String kontoname, Connection cn) {
    Konto k = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
            pstmt = cn.prepareStatement(
            "SELECT * FROM `konto` AS k " +
            "WHERE k.biblioname = ?");
            pstmt.setString(1, kontoname);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                k = new Konto(rs);
            }
    } catch (Exception err) {
          String f = "getTestKontoFromDb(String kontoname, Connection cn) trat folgender Fehler auf:\n";
      System.out.println(f + err);
        } finally {
          if (rs != null) {
            try {
              rs.close();
            } catch (SQLException e) {
              System.out.println(e);
            }
          }
          if (pstmt != null) {
            try {
              pstmt.close();
            } catch (SQLException e) {
              System.out.println(e);
            }
          }
        }

    return k;
  }

  /**
   * Erstellt ein TestBibliothekarObjekt
   *
   * @return Bibliothekar
   */
  public static Bibliothekar getBibliothekar() {
    Bibliothekar b = new Bibliothekar(getUserForm());
    return b;
  }

  /**
   * Erstellt ein TestPositionenObjekt
   *
   * @return Position
   */
  public static Position getPosition() {
    Position p = new Position();
    p.setAutor("autor");
    p.setBenutzer(getTestBibliothekarFromDb(p.getSingleConnection()));
    p.setDeloptions("deloptions");
    p.setFileformat("fileformat");
    p.setHeft("heft");
    p.setJahr("jahr");
    p.setJahrgang("jahrgang");
    p.setKapitel("kapitel");
    p.setKonto(getTestKontoFromDb("Bibliothek soH/BZ-GS",p.getSingleConnection()));
    p.setMediatype("mediatype");
    p.setOrderdate(new java.sql.Date(1));
    p.setPreis("preis");
    p.setPriority("priority");
    p.setSeiten("seiten");
    p.setTitel("titel");
    p.setWaehrung("waehrung");
    p.setZeitschrift_verlag("zeitschrift_verlag");

    p.close();
    return p;
  }


}