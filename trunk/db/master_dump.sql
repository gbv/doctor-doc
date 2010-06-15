--
-- works with  >= Revision 38 (see also dbchanges_forUpdate, if using SVN)
--

-- phpMyAdmin SQL Dump
-- version 3.1.3.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 15. Juni 2010 um 16:27
-- Server Version: 5.1.33
-- PHP-Version: 5.2.9

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Datenbank: `dd_os`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `benutzer`
--

DROP TABLE IF EXISTS `benutzer`;
CREATE TABLE IF NOT EXISTS `benutzer` (
  `UID` bigint(20) NOT NULL AUTO_INCREMENT,
  `institut` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `abteilung` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `anrede` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vorname` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `adr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `adrzus` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `telp` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `telg` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `plz` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ort` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `land` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mail` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `pw` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `librarycard` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `loginopt` tinyint(1) NOT NULL,
  `userbestellung` tinyint(1) NOT NULL,
  `gbvbestellung` tinyint(1) NOT NULL,
  `billing` bigint(20) NOT NULL,
  `kontoval` tinyint(1) NOT NULL,
  `kontostatus` tinyint(1) NOT NULL,
  `rechte` bigint(20) NOT NULL,
  `datum` datetime NOT NULL,
  `lastuse` datetime NOT NULL,
  `gtc` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `gtcdate` datetime NOT NULL,
  PRIMARY KEY (`UID`),
  KEY `institut` (`institut`),
  KEY `abteilung` (`abteilung`),
  KEY `anrede` (`anrede`),
  KEY `plz` (`plz`),
  KEY `ort` (`ort`),
  KEY `land` (`land`),
  KEY `mail` (`mail`),
  KEY `pw` (`pw`),
  KEY `rechte` (`rechte`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Benutzertabelle' AUTO_INCREMENT=4 ;

--
-- Daten für Tabelle `benutzer`
--

INSERT INTO `benutzer` (`UID`, `institut`, `abteilung`, `anrede`, `vorname`, `name`, `adr`, `adrzus`, `telp`, `telg`, `plz`, `ort`, `land`, `mail`, `pw`, `librarycard`, `loginopt`, `userbestellung`, `gbvbestellung`, `billing`, `kontoval`, `kontostatus`, `rechte`, `datum`, `lastuse`, `gtc`, `gtcdate`) VALUES
(1, '', 'My Library', 'Herr', 'Staff', 'Librarian', '', '', '', '', '', '', '', 'staff@doctor-doc.com', '6ccb4b7c39a6e77f76ecfa935a855c6c46ad5611', NULL, 1, 1, 1, 0, 0, 1, 2, '2010-05-15 11:53:37', '2010-05-15 12:19:24', 'Version1', '0000-00-00 00:00:00'),
(2, '', 'My Library', 'Herr', 'User', 'Patron', '', '', '', '', '', '', '', 'user@doctor-doc.com', '12dea96fec20593566ab75692c9949596833adc9', NULL, 1, 0, 0, 0, 0, 1, 1, '2010-05-15 11:53:37', '2010-05-15 12:20:01', 'Version1', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `bestellform_param`
--

DROP TABLE IF EXISTS `bestellform_param`;
CREATE TABLE IF NOT EXISTS `bestellform_param` (
  `BPID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) NOT NULL,
  `TYID` bigint(20) NOT NULL,
  `kennung` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `saveorder` tinyint(1) NOT NULL,
  `institution` tinyint(1) NOT NULL DEFAULT '0',
  `abteilung` tinyint(1) NOT NULL DEFAULT '0',
  `adresse` tinyint(1) NOT NULL DEFAULT '0',
  `strasse` tinyint(1) NOT NULL DEFAULT '0',
  `plz` tinyint(1) NOT NULL DEFAULT '0',
  `ort` tinyint(1) NOT NULL DEFAULT '0',
  `telefon` tinyint(1) NOT NULL DEFAULT '0',
  `benutzernr` tinyint(1) NOT NULL DEFAULT '0',
  `land` tinyint(1) NOT NULL DEFAULT '0',
  `prio` tinyint(1) NOT NULL DEFAULT '1',
  `lieferart` tinyint(1) NOT NULL DEFAULT '0',
  `lieferart_value1` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lieferart_value2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lieferart_value3` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `frei1` tinyint(1) NOT NULL DEFAULT '0',
  `frei2` tinyint(1) NOT NULL DEFAULT '0',
  `frei3` tinyint(1) NOT NULL DEFAULT '0',
  `frei1_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `frei2_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `frei3_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `comment1` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `option` tinyint(1) NOT NULL DEFAULT '0',
  `option_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `option_comment` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `option_linkout` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `option_linkoutname` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `option_value1` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `option_value2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `option_value3` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gebuehren` tinyint(1) NOT NULL DEFAULT '0',
  `gebuehren_link` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `agb` tinyint(1) NOT NULL DEFAULT '0',
  `agb_link` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `inst_required` tinyint(1) NOT NULL DEFAULT '0',
  `abt_required` tinyint(1) NOT NULL DEFAULT '0',
  `frei1_required` tinyint(1) NOT NULL DEFAULT '0',
  `frei2_required` tinyint(1) NOT NULL DEFAULT '0',
  `frei3_required` tinyint(1) NOT NULL DEFAULT '0',
  `adr_required` tinyint(1) NOT NULL DEFAULT '0',
  `str_required` tinyint(1) NOT NULL DEFAULT '0',
  `plz_required` tinyint(1) NOT NULL DEFAULT '0',
  `ort_required` tinyint(1) NOT NULL DEFAULT '0',
  `land_required` tinyint(1) NOT NULL DEFAULT '0',
  `tel_required` tinyint(1) NOT NULL DEFAULT '0',
  `benutzernr_required` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`BPID`),
  KEY `KID` (`KID`),
  KEY `TYID` (`TYID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `bestellform_param`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `bestellstatus`
--

DROP TABLE IF EXISTS `bestellstatus`;
CREATE TABLE IF NOT EXISTS `bestellstatus` (
  `BSID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BID` bigint(20) NOT NULL,
  `TID` bigint(20) NOT NULL,
  `bemerkungen` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bearbeiter` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`BSID`),
  KEY `BID` (`BID`),
  KEY `TID` (`TID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `bestellstatus`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `bestellungen`
--

DROP TABLE IF EXISTS `bestellungen`;
CREATE TABLE IF NOT EXISTS `bestellungen` (
  `BID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) NOT NULL,
  `UID` bigint(20) NOT NULL,
  `LID` bigint(20) NOT NULL,
  `mediatype` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `orderpriority` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `deloptions` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `fileformat` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `zeitschrift` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `autor` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `artikeltitel` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `jahr` varchar(4) COLLATE utf8_unicode_ci NOT NULL,
  `jahrgang` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `heft` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `seiten` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `issn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isbn` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `buchkapitel` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `buchtitel` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `verlag` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `doi` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `pmid` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `subitonr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gbvnr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `trackingnr` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `internenr` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `biblionr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bibliothek` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bestellquelle` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `statedate` datetime DEFAULT NULL,
  `orderdate` datetime DEFAULT NULL,
  `erledigt` tinyint(1) DEFAULT '0',
  `systembemerkung` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `notizen` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `kaufpreis` decimal(6,2) DEFAULT NULL,
  `waehrung` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`BID`),
  KEY `KID` (`KID`),
  KEY `UID` (`UID`),
  KEY `LID` (`LID`),
  KEY `mediatype` (`mediatype`),
  KEY `orderpriority` (`orderpriority`),
  KEY `deloptions` (`deloptions`),
  KEY `fileformat` (`fileformat`),
  KEY `zeitschrift` (`zeitschrift`),
  KEY `artikeltitel` (`artikeltitel`),
  KEY `jahr` (`jahr`),
  KEY `bestellquelle` (`bestellquelle`),
  KEY `state` (`state`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `bestellungen`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `billing`
--

DROP TABLE IF EXISTS `billing`;
CREATE TABLE IF NOT EXISTS `billing` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UID` bigint(20) DEFAULT NULL,
  `KID` bigint(20) DEFAULT NULL,
  `rechnungsgrund` bigint(20) NOT NULL,
  `betrag` decimal(6,2) NOT NULL,
  `waehrung` varchar(4) NOT NULL,
  `rechnungsnummer` varchar(20) NOT NULL,
  `rechnungsdatum` date NOT NULL,
  `zahlungseingang` date DEFAULT NULL,
  `storniert` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `billing`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `c3p0TestTable`
--

DROP TABLE IF EXISTS `c3p0TestTable`;
CREATE TABLE IF NOT EXISTS `c3p0TestTable` (
  `a` char(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `c3p0TestTable`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ddl_billing`
--

DROP TABLE IF EXISTS `ddl_billing`;
CREATE TABLE IF NOT EXISTS `ddl_billing` (
  `DBID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UID` bigint(20) NOT NULL,
  `billingnumber` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `billingdate` date DEFAULT NULL,
  `receiptofpayment` date DEFAULT NULL,
  PRIMARY KEY (`DBID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `ddl_billing`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ddl_billingstatus`
--

DROP TABLE IF EXISTS `ddl_billingstatus`;
CREATE TABLE IF NOT EXISTS `ddl_billingstatus` (
  `BSID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DBID` bigint(20) NOT NULL,
  `state` varchar(30) NOT NULL,
  PRIMARY KEY (`BSID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `ddl_billingstatus`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ddl_positionen`
--

DROP TABLE IF EXISTS `ddl_positionen`;
CREATE TABLE IF NOT EXISTS `ddl_positionen` (
  `pid` bigint(20) NOT NULL AUTO_INCREMENT,
  `UID` bigint(20) NOT NULL COMMENT 'Endkunde / Bibliothekskunde',
  `KID` bigint(20) NOT NULL COMMENT 'Bibliothekskonto',
  `priority` varchar(20) NOT NULL COMMENT 'Normal, Express',
  `deloptions` varchar(20) NOT NULL COMMENT 'Online, Email, Postweg, Fax, Fax to PDF',
  `fileformat` varchar(20) NOT NULL COMMENT 'HTML, PDF, Papierkopie,...',
  `orderdate` datetime NOT NULL COMMENT 'Datum der Bestellung',
  `mediatype` varchar(20) DEFAULT NULL COMMENT 'Artikel, Teilkopie Buch oder Buch',
  `autor` varchar(100) DEFAULT NULL COMMENT 'Autor',
  `zeitschrift_verlag` varchar(100) DEFAULT NULL COMMENT 'Zeitschrift / Verlag',
  `heft` varchar(20) DEFAULT NULL COMMENT 'Heft',
  `jahrgang` varchar(20) DEFAULT NULL,
  `jahr` varchar(4) DEFAULT NULL,
  `titel` varchar(100) DEFAULT NULL COMMENT 'Artikeltiel / Buchtitel',
  `kapitel` varchar(100) DEFAULT NULL COMMENT 'Kapitel',
  `seiten` varchar(20) DEFAULT NULL,
  `waehrung` varchar(10) NOT NULL,
  `preis` decimal(6,2) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `ddl_positionen`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `default_preise`
--

DROP TABLE IF EXISTS `default_preise`;
CREATE TABLE IF NOT EXISTS `default_preise` (
  `DPID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TID_bezeichnung` bigint(20) NOT NULL,
  `lid` bigint(20) NOT NULL,
  `bezeichnung` text NOT NULL,
  `TID_waehrung` bigint(20) DEFAULT NULL,
  `waehrung` text,
  `deloptions` varchar(20) DEFAULT NULL,
  `KID` bigint(20) DEFAULT NULL,
  `preis` decimal(6,2) NOT NULL,
  PRIMARY KEY (`DPID`),
  KEY `TID_bezeichnung` (`TID_bezeichnung`),
  KEY `lid` (`lid`),
  KEY `KID` (`KID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `default_preise`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ezb_id`
--

DROP TABLE IF EXISTS `ezb_id`;
CREATE TABLE IF NOT EXISTS `ezb_id` (
  `EID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ezb_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `zdb_id` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`EID`),
  KEY `zdb_id` (`zdb_id`),
  KEY `ezb_id` (`ezb_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `ezb_id`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fax`
--

DROP TABLE IF EXISTS `fax`;
CREATE TABLE IF NOT EXISTS `fax` (
  `FID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) NOT NULL,
  `popfaxid` varchar(100) NOT NULL,
  `fromnr` varchar(100) DEFAULT NULL,
  `popfaxdate` datetime DEFAULT NULL,
  `pages` varchar(20) DEFAULT NULL,
  `state` varchar(20) NOT NULL,
  `statedate` datetime NOT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `fax`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `faxrun`
--

DROP TABLE IF EXISTS `faxrun`;
CREATE TABLE IF NOT EXISTS `faxrun` (
  `FRID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) NOT NULL,
  `state` varchar(20) NOT NULL,
  `statedate` datetime NOT NULL,
  PRIMARY KEY (`FRID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `faxrun`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `holdings`
--

DROP TABLE IF EXISTS `holdings`;
CREATE TABLE IF NOT EXISTS `holdings` (
  `HOID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) NOT NULL,
  `titel` varchar(200) NOT NULL,
  `coden` varchar(6) DEFAULT NULL,
  `verlag` varchar(100) NOT NULL,
  `ort` varchar(100) NOT NULL,
  `issn` varchar(10) DEFAULT NULL,
  `zdbid` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`HOID`),
  KEY `issn` (`issn`),
  KEY `KID` (`KID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `holdings`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `iso_countries`
--

DROP TABLE IF EXISTS `iso_countries`;
CREATE TABLE IF NOT EXISTS `iso_countries` (
  `rowId` int(11) NOT NULL AUTO_INCREMENT,
  `countryId` int(11) NOT NULL,
  `locale` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'en',
  `countryCode` char(2) COLLATE utf8_unicode_ci NOT NULL,
  `countryName` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `phonePrefix` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`rowId`),
  KEY `countryId` (`countryId`),
  KEY `locale` (`locale`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=302 ;

--
-- Daten für Tabelle `iso_countries`
--

INSERT INTO `iso_countries` (`rowId`, `countryId`, `locale`, `countryCode`, `countryName`, `phonePrefix`) VALUES
(1, 1, 'en', 'AF', 'Afghanistan', '+93'),
(2, 2, 'en', 'AL', 'Albania', '+355'),
(3, 3, 'en', 'DZ', 'Algeria', '+213'),
(4, 4, 'en', 'AD', 'Andorra', '+376'),
(5, 5, 'en', 'AO', 'Angola', '+244'),
(6, 6, 'en', 'AG', 'Antigua and Barbuda', '+1-268'),
(7, 7, 'en', 'AR', 'Argentina', '+54'),
(8, 8, 'en', 'AM', 'Armenia', '+374'),
(9, 9, 'en', 'AU', 'Australia', '+61'),
(10, 10, 'en', 'AT', 'Austria', '+43'),
(11, 11, 'en', 'AZ', 'Azerbaijan', '+994'),
(12, 12, 'en', 'BS', 'Bahamas, The', '+1-242'),
(13, 13, 'en', 'BH', 'Bahrain', '+973'),
(14, 14, 'en', 'BD', 'Bangladesh', '+880'),
(15, 15, 'en', 'BB', 'Barbados', '+1-246'),
(16, 16, 'en', 'BY', 'Belarus', '+375'),
(17, 17, 'en', 'BE', 'Belgium', '+32'),
(18, 18, 'en', 'BZ', 'Belize', '+501'),
(19, 19, 'en', 'BJ', 'Benin', '+229'),
(20, 20, 'en', 'BT', 'Bhutan', '+975'),
(21, 21, 'en', 'BO', 'Bolivia', '+591'),
(22, 22, 'en', 'BA', 'Bosnia and Herzegovina', '+387'),
(23, 23, 'en', 'BW', 'Botswana', '+267'),
(24, 24, 'en', 'BR', 'Brazil', '+55'),
(25, 25, 'en', 'BN', 'Brunei', '+673'),
(26, 26, 'en', 'BG', 'Bulgaria', '+359'),
(27, 27, 'en', 'BF', 'Burkina Faso', '+226'),
(28, 28, 'en', 'BI', 'Burundi', '+257'),
(29, 29, 'en', 'KH', 'Cambodia', '+855'),
(30, 30, 'en', 'CM', 'Cameroon', '+237'),
(31, 31, 'en', 'CA', 'Canada', '+1'),
(32, 32, 'en', 'CV', 'Cape Verde', '+238'),
(33, 33, 'en', 'CF', 'Central African Republic', '+236'),
(34, 34, 'en', 'TD', 'Chad', '+235'),
(35, 35, 'en', 'CL', 'Chile', '+56'),
(36, 36, 'en', 'CN', 'China, People''s Republic of', '+86'),
(37, 37, 'en', 'CO', 'Colombia', '+57'),
(38, 38, 'en', 'KM', 'Comoros', '+269'),
(39, 39, 'en', 'CD', 'Congo, Democratic Republic of the (Congo ? Kinshasa)', '+243'),
(40, 40, 'en', 'CG', 'Congo, Republic of the (Congo ? Brazzaville)', '+242'),
(41, 41, 'en', 'CR', 'Costa Rica', '+506'),
(42, 42, 'en', 'CI', 'Cote d''Ivoire (Ivory Coast)', '+225'),
(43, 43, 'en', 'HR', 'Croatia', '+385'),
(44, 44, 'en', 'CU', 'Cuba', '+53'),
(45, 45, 'en', 'CY', 'Cyprus', '+357'),
(46, 46, 'en', 'CZ', 'Czech Republic', '+420'),
(47, 47, 'en', 'DK', 'Denmark', '+45'),
(48, 48, 'en', 'DJ', 'Djibouti', '+253'),
(49, 49, 'en', 'DM', 'Dominica', '+1-767'),
(50, 50, 'en', 'DO', 'Dominican Republic', '+1-809 and 1-829'),
(51, 51, 'en', 'EC', 'Ecuador', '+593'),
(52, 52, 'en', 'EG', 'Egypt', '+20'),
(53, 53, 'en', 'SV', 'El Salvador', '+503'),
(54, 54, 'en', 'GQ', 'Equatorial Guinea', '+240'),
(55, 55, 'en', 'ER', 'Eritrea', '+291'),
(56, 56, 'en', 'EE', 'Estonia', '+372'),
(57, 57, 'en', 'ET', 'Ethiopia', '+251'),
(58, 58, 'en', 'FJ', 'Fiji', '+679'),
(59, 59, 'en', 'FI', 'Finland', '+358'),
(60, 60, 'en', 'FR', 'France', '+33'),
(61, 61, 'en', 'GA', 'Gabon', '+241'),
(62, 62, 'en', 'GM', 'Gambia, The', '+220'),
(63, 63, 'en', 'GE', 'Georgia', '+995'),
(64, 64, 'en', 'DE', 'Germany', '+49'),
(65, 65, 'en', 'GH', 'Ghana', '+233'),
(66, 66, 'en', 'GR', 'Greece', '+30'),
(67, 67, 'en', 'GD', 'Grenada', '+1-473'),
(68, 68, 'en', 'GT', 'Guatemala', '+502'),
(69, 69, 'en', 'GN', 'Guinea', '+224'),
(70, 70, 'en', 'GW', 'Guinea-Bissau', '+245'),
(71, 71, 'en', 'GY', 'Guyana', '+592'),
(72, 72, 'en', 'HT', 'Haiti', '+509'),
(73, 73, 'en', 'HN', 'Honduras', '+504'),
(74, 74, 'en', 'HU', 'Hungary', '+36'),
(75, 75, 'en', 'IS', 'Iceland', '+354'),
(76, 76, 'en', 'IN', 'India', '+91'),
(77, 77, 'en', 'ID', 'Indonesia', '+62'),
(78, 78, 'en', 'IR', 'Iran', '+98'),
(79, 79, 'en', 'IQ', 'Iraq', '+964'),
(80, 80, 'en', 'IE', 'Ireland', '+353'),
(81, 81, 'en', 'IL', 'Israel', '+972'),
(82, 82, 'en', 'IT', 'Italy', '+39'),
(83, 83, 'en', 'JM', 'Jamaica', '+1-876'),
(84, 84, 'en', 'JP', 'Japan', '+81'),
(85, 85, 'en', 'JO', 'Jordan', '+962'),
(86, 86, 'en', 'KZ', 'Kazakhstan', '+7'),
(87, 87, 'en', 'KE', 'Kenya', '+254'),
(88, 88, 'en', 'KI', 'Kiribati', '+686'),
(89, 89, 'en', 'KP', 'Korea, Democratic People''s Republic of (North Korea)', '+850'),
(90, 90, 'en', 'KR', 'Korea, Republic of  (South Korea)', '+82'),
(91, 91, 'en', 'KW', 'Kuwait', '+965'),
(92, 92, 'en', 'KG', 'Kyrgyzstan', '+996'),
(93, 93, 'en', 'LA', 'Laos', '+856'),
(94, 94, 'en', 'LV', 'Latvia', '+371'),
(95, 95, 'en', 'LB', 'Lebanon', '+961'),
(96, 96, 'en', 'LS', 'Lesotho', '+266'),
(97, 97, 'en', 'LR', 'Liberia', '+231'),
(98, 98, 'en', 'LY', 'Libya', '+218'),
(99, 99, 'en', 'LI', 'Liechtenstein', '+423'),
(100, 100, 'en', 'LT', 'Lithuania', '+370'),
(101, 101, 'en', 'LU', 'Luxembourg', '+352'),
(102, 102, 'en', 'MK', 'Macedonia', '+389'),
(103, 103, 'en', 'MG', 'Madagascar', '+261'),
(104, 104, 'en', 'MW', 'Malawi', '+265'),
(105, 105, 'en', 'MY', 'Malaysia', '+60'),
(106, 106, 'en', 'MV', 'Maldives', '+960'),
(107, 107, 'en', 'ML', 'Mali', '+223'),
(108, 108, 'en', 'MT', 'Malta', '+356'),
(109, 109, 'en', 'MH', 'Marshall Islands', '+692'),
(110, 110, 'en', 'MR', 'Mauritania', '+222'),
(111, 111, 'en', 'MU', 'Mauritius', '+230'),
(112, 112, 'en', 'MX', 'Mexico', '+52'),
(113, 113, 'en', 'FM', 'Micronesia', '+691'),
(114, 114, 'en', 'MD', 'Moldova', '+373'),
(115, 115, 'en', 'MC', 'Monaco', '+377'),
(116, 116, 'en', 'MN', 'Mongolia', '+976'),
(117, 117, 'en', 'ME', 'Montenegro', '+382'),
(118, 118, 'en', 'MA', 'Morocco', '+212'),
(119, 119, 'en', 'MZ', 'Mozambique', '+258'),
(120, 120, 'en', 'MM', 'Myanmar (Burma)', '+95'),
(121, 121, 'en', 'NA', 'Namibia', '+264'),
(122, 122, 'en', 'NR', 'Nauru', '+674'),
(123, 123, 'en', 'NP', 'Nepal', '+977'),
(124, 124, 'en', 'NL', 'Netherlands', '+31'),
(125, 125, 'en', 'NZ', 'New Zealand', '+64'),
(126, 126, 'en', 'NI', 'Nicaragua', '+505'),
(127, 127, 'en', 'NE', 'Niger', '+227'),
(128, 128, 'en', 'NG', 'Nigeria', '+234'),
(129, 129, 'en', 'NO', 'Norway', '+47'),
(130, 130, 'en', 'OM', 'Oman', '+968'),
(131, 131, 'en', 'PK', 'Pakistan', '+92'),
(132, 132, 'en', 'PW', 'Palau', '+680'),
(133, 133, 'en', 'PA', 'Panama', '+507'),
(134, 134, 'en', 'PG', 'Papua New Guinea', '+675'),
(135, 135, 'en', 'PY', 'Paraguay', '+595'),
(136, 136, 'en', 'PE', 'Peru', '+51'),
(137, 137, 'en', 'PH', 'Philippines', '+63'),
(138, 138, 'en', 'PL', 'Poland', '+48'),
(139, 139, 'en', 'PT', 'Portugal', '+351'),
(140, 140, 'en', 'QA', 'Qatar', '+974'),
(141, 141, 'en', 'RO', 'Romania', '+40'),
(142, 142, 'en', 'RU', 'Russia', '+7'),
(143, 143, 'en', 'RW', 'Rwanda', '+250'),
(144, 144, 'en', 'KN', 'Saint Kitts and Nevis', '+1-869'),
(145, 145, 'en', 'LC', 'Saint Lucia', '+1-758'),
(146, 146, 'en', 'VC', 'Saint Vincent and the Grenadines', '+1-784'),
(147, 147, 'en', 'WS', 'Samoa', '+685'),
(148, 148, 'en', 'SM', 'San Marino', '+378'),
(149, 149, 'en', 'ST', 'Sao Tome and Principe', '+239'),
(150, 150, 'en', 'SA', 'Saudi Arabia', '+966'),
(151, 151, 'en', 'SN', 'Senegal', '+221'),
(152, 152, 'en', 'RS', 'Serbia', '+381'),
(153, 153, 'en', 'SC', 'Seychelles', '+248'),
(154, 154, 'en', 'SL', 'Sierra Leone', '+232'),
(155, 155, 'en', 'SG', 'Singapore', '+65'),
(156, 156, 'en', 'SK', 'Slovakia', '+421'),
(157, 157, 'en', 'SI', 'Slovenia', '+386'),
(158, 158, 'en', 'SB', 'Solomon Islands', '+677'),
(159, 159, 'en', 'SO', 'Somalia', '+252'),
(160, 160, 'en', 'ZA', 'South Africa', '+27'),
(161, 161, 'en', 'ES', 'Spain', '+34'),
(162, 162, 'en', 'LK', 'Sri Lanka', '+94'),
(163, 163, 'en', 'SD', 'Sudan', '+249'),
(164, 164, 'en', 'SR', 'Suriname', '+597'),
(165, 165, 'en', 'SZ', 'Swaziland', '+268'),
(166, 166, 'en', 'SE', 'Sweden', '+46'),
(167, 167, 'en', 'CH', 'Switzerland', '+41'),
(168, 168, 'en', 'SY', 'Syria', '+963'),
(169, 169, 'en', 'TJ', 'Tajikistan', '+992'),
(170, 170, 'en', 'TZ', 'Tanzania', '+255'),
(171, 171, 'en', 'TH', 'Thailand', '+66'),
(172, 172, 'en', 'TL', 'Timor-Leste (East Timor)', '+670'),
(173, 173, 'en', 'TG', 'Togo', '+228'),
(174, 174, 'en', 'TO', 'Tonga', '+676'),
(175, 175, 'en', 'TT', 'Trinidad and Tobago', '+1-868'),
(176, 176, 'en', 'TN', 'Tunisia', '+216'),
(177, 177, 'en', 'TR', 'Turkey', '+90'),
(178, 178, 'en', 'TM', 'Turkmenistan', '+993'),
(179, 179, 'en', 'TV', 'Tuvalu', '+688'),
(180, 180, 'en', 'UG', 'Uganda', '+256'),
(181, 181, 'en', 'UA', 'Ukraine', '+380'),
(182, 182, 'en', 'AE', 'United Arab Emirates', '+971'),
(183, 183, 'en', 'GB', 'United Kingdom', '+44'),
(184, 184, 'en', 'US', 'United States', '+1'),
(185, 185, 'en', 'UY', 'Uruguay', '+598'),
(186, 186, 'en', 'UZ', 'Uzbekistan', '+998'),
(187, 187, 'en', 'VU', 'Vanuatu', '+678'),
(188, 188, 'en', 'VA', 'Vatican City', '+379'),
(189, 189, 'en', 'VE', 'Venezuela', '+58'),
(190, 190, 'en', 'VN', 'Viet Nam', '+84'),
(191, 191, 'en', 'YE', 'Yemen', '+967'),
(192, 192, 'en', 'ZM', 'Zambia', '+260'),
(193, 193, 'en', 'ZW', 'Zimbabwe', '+263'),
(194, 194, 'en', 'GE', 'Abkhazia', '+995'),
(195, 195, 'en', 'TW', 'China, Republic of (Taiwan)', '+886'),
(196, 196, 'en', 'AZ', 'Nagorno-Karabakh', '+374-97'),
(197, 197, 'en', 'CY', 'Northern Cyprus', '+90-392'),
(198, 198, 'en', 'MD', 'Pridnestrovie (Transnistria)', '+373-533'),
(199, 199, 'en', 'SO', 'Somaliland', '+252'),
(200, 200, 'en', 'GE', 'South Ossetia', '+995'),
(201, 201, 'en', 'AU', 'Ashmore and Cartier Islands', ''),
(202, 202, 'en', 'CX', 'Christmas Island', '+61'),
(203, 203, 'en', 'CC', 'Cocos (Keeling) Islands', '+61'),
(204, 204, 'en', 'AU', 'Coral Sea Islands', ''),
(205, 205, 'en', 'HM', 'Heard Island and McDonald Islands', ''),
(206, 206, 'en', 'NF', 'Norfolk Island', '+672'),
(207, 207, 'en', 'NC', 'New Caledonia', '+687'),
(208, 208, 'en', 'PF', 'French Polynesia', '+689'),
(209, 209, 'en', 'YT', 'Mayotte', '+269'),
(210, 210, 'en', 'PM', 'Saint Pierre and Miquelon', '+508'),
(211, 211, 'en', 'WF', 'Wallis and Futuna', '+681'),
(212, 212, 'en', 'TF', 'French Southern and Antarctic Lands', ''),
(213, 213, 'en', 'PF', 'Clipperton Island', ''),
(214, 214, 'en', '', 'French Scattered Islands in the Indian Ocean', ''),
(215, 215, 'en', 'BV', 'Bouvet Island', ''),
(216, 216, 'en', 'CK', 'Cook Islands', '+682'),
(217, 217, 'en', 'NU', 'Niue', '+683'),
(218, 218, 'en', 'TK', 'Tokelau', '+690'),
(219, 219, 'en', 'GG', 'Guernsey', '+44'),
(220, 220, 'en', 'IM', 'Isle of Man', '+44'),
(221, 221, 'en', 'JE', 'Jersey', '+44'),
(222, 222, 'en', 'AI', 'Anguilla', '+1-264'),
(223, 223, 'en', 'BM', 'Bermuda', '+1-441'),
(224, 224, 'en', 'IO', 'British Indian Ocean Territory', '+246'),
(225, 225, 'en', '', 'British Sovereign Base Areas', '+357'),
(226, 226, 'en', 'VG', 'British Virgin Islands', '+1-284'),
(227, 227, 'en', 'KY', 'Cayman Islands', '+1-345'),
(228, 228, 'en', 'FK', 'Falkland Islands (Islas Malvinas)', '+500'),
(229, 229, 'en', 'GI', 'Gibraltar', '+350'),
(230, 230, 'en', 'MS', 'Montserrat', '+1-664'),
(231, 231, 'en', 'PN', 'Pitcairn Islands', ''),
(232, 232, 'en', 'SH', 'Saint Helena', '+290'),
(233, 233, 'en', 'GS', 'South Georgia and the South Sandwich Islands', ''),
(234, 234, 'en', 'TC', 'Turks and Caicos Islands', '+1-649'),
(235, 235, 'en', 'MP', 'Northern Mariana Islands', '+1-670'),
(236, 236, 'en', 'PR', 'Puerto Rico', '+1-787 and 1-939'),
(237, 237, 'en', 'AS', 'American Samoa', '+1-684'),
(238, 238, 'en', 'UM', 'Baker Island', ''),
(239, 239, 'en', 'GU', 'Guam', '+1-671'),
(240, 240, 'en', 'UM', 'Howland Island', ''),
(241, 241, 'en', 'UM', 'Jarvis Island', ''),
(242, 242, 'en', 'UM', 'Johnston Atoll', ''),
(243, 243, 'en', 'UM', 'Kingman Reef', ''),
(244, 244, 'en', 'UM', 'Midway Islands', ''),
(245, 245, 'en', 'UM', 'Navassa Island', ''),
(246, 246, 'en', 'UM', 'Palmyra Atoll', ''),
(247, 247, 'en', 'VI', 'U.S. Virgin Islands', '+1-340'),
(248, 248, 'en', 'UM', 'Wake Island', ''),
(249, 249, 'en', 'HK', 'Hong Kong', '+852'),
(250, 250, 'en', 'MO', 'Macau', '+853'),
(251, 251, 'en', 'FO', 'Faroe Islands', '+298'),
(252, 252, 'en', 'GL', 'Greenland', '+299'),
(253, 253, 'en', 'GF', 'French Guiana', '+594'),
(254, 254, 'en', 'GP', 'Guadeloupe', '+590'),
(255, 255, 'en', 'MQ', 'Martinique', '+596'),
(256, 256, 'en', 'RE', 'Reunion', '+262'),
(257, 257, 'en', 'AX', 'Aland', '+358-18'),
(258, 258, 'en', 'AW', 'Aruba', '+297'),
(259, 259, 'en', 'AN', 'Netherlands Antilles', '+599'),
(260, 260, 'en', 'SJ', 'Svalbard', '+47'),
(261, 261, 'en', 'AC', 'Ascension', '+247'),
(262, 262, 'en', 'TA', 'Tristan da Cunha', ''),
(263, 263, 'en', 'AQ', 'Antarctica', ''),
(264, 264, 'en', 'CS', 'Kosovo', '+381'),
(265, 265, 'en', 'PS', 'Palestinian Territories (Gaza Strip and West Bank)', '+970'),
(266, 266, 'en', 'EH', 'Western Sahara', '+212'),
(267, 267, 'en', 'AQ', 'Australian Antarctic Territory', ''),
(268, 268, 'en', 'AQ', 'Ross Dependency', ''),
(269, 269, 'en', 'AQ', 'Peter I Island', ''),
(270, 270, 'en', 'AQ', 'Queen Maud Land', ''),
(271, 271, 'en', 'AQ', 'British Antarctic Territory', ''),
(301, 300, 'de', '', 'andere / other', NULL),
(276, 64, 'de', 'DE', 'Deutschland', NULL),
(277, 74, 'de', 'HU', 'Ungarn', NULL),
(299, 99, 'de', 'LI', 'Liechtenstein', NULL),
(279, 82, 'de', 'IT', 'Italien', NULL),
(280, 124, 'de', 'NL', 'Holland', NULL),
(298, 47, 'de', 'DK', 'Dänemark', NULL),
(282, 138, 'de', 'PL', 'Polen', NULL),
(283, 142, 'de', 'RU', 'Russland', NULL),
(300, 300, 'en', '', 'other', NULL),
(285, 161, 'de', 'ES', 'Spanien', NULL),
(286, 167, 'de', 'CH', 'Schweiz', NULL),
(287, 184, 'de', 'US', 'USA', NULL),
(288, 17, 'de', 'BE', 'Belgien', NULL),
(290, 60, 'de', 'FR', 'Frankreich', NULL),
(291, 59, 'de', 'FI', 'Finnland', NULL),
(292, 124, 'de', 'NL', 'Niederlande', NULL),
(293, 129, 'de', 'NO', 'Norwegen', NULL),
(294, 139, 'de', 'PT', 'Portugal', NULL),
(295, 166, 'de', 'SE', 'Schweden', NULL),
(296, 183, 'de', 'GB', 'England', NULL),
(297, 10, 'de', 'AT', 'Österreich', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `issn`
--

DROP TABLE IF EXISTS `issn`;
CREATE TABLE IF NOT EXISTS `issn` (
  `IID` bigint(20) NOT NULL AUTO_INCREMENT,
  `identifier` varchar(10) CHARACTER SET utf8 NOT NULL,
  `identifier_id` bigint(20) NOT NULL,
  `issn` varchar(9) CHARACTER SET utf8 NOT NULL,
  `eissn` tinyint(1) NOT NULL,
  `lissn` tinyint(1) NOT NULL,
  `coden` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `titel` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `verlag` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `ort` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `sprache` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`IID`),
  KEY `identifier_id` (`identifier_id`),
  KEY `issn` (`issn`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `issn`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `konto`
--

DROP TABLE IF EXISTS `konto`;
CREATE TABLE IF NOT EXISTS `konto` (
  `KID` bigint(20) NOT NULL AUTO_INCREMENT,
  `biblioname` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `isil` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `adresse` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `adresszusatz` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `plz` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `ort` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `land` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `timezone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `telefon` varchar(20) COLLATE utf8_unicode_ci DEFAULT '0',
  `faxno` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `faxusername` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `faxpassword` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `popfaxend` date DEFAULT NULL,
  `fax2` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bibliomail` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `dbsmail` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `dbsmailpw` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `gbvbn` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gbvpw` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gbv_requester_id` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ezbid` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `zdb` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Ist Bibliothek ZDB-Teilnehmer?',
  `billing` bigint(20) DEFAULT NULL,
  `billingtype` bigint(20) DEFAULT NULL,
  `accounting_rhythmvalue` bigint(20) DEFAULT NULL,
  `accounting_rhythmday` bigint(20) DEFAULT NULL,
  `accounting_rhythmtimeout` bigint(20) DEFAULT NULL,
  `billingschwellwert` int(3) DEFAULT NULL,
  `maxordersu` bigint(20) DEFAULT NULL,
  `maxordersutotal` bigint(20) DEFAULT NULL,
  `maxordersj` bigint(20) DEFAULT NULL,
  `orderlimits` tinyint(1) NOT NULL,
  `userlogin` tinyint(1) NOT NULL,
  `userbestellung` tinyint(1) NOT NULL,
  `gbvbestellung` tinyint(1) NOT NULL,
  `kontostatus` tinyint(1) NOT NULL,
  `kontotyp` tinyint(4) NOT NULL DEFAULT '0',
  `default_deloptions` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'post',
  `paydate` date DEFAULT NULL,
  `expdate` date DEFAULT NULL,
  `edatum` date NOT NULL,
  `gtc` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gtcdate` datetime DEFAULT NULL,
  PRIMARY KEY (`KID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Daten für Tabelle `konto`
--

INSERT INTO `konto` (`KID`, `biblioname`, `isil`, `adresse`, `adresszusatz`, `plz`, `ort`, `land`, `timezone`, `telefon`, `faxno`, `faxusername`, `faxpassword`, `popfaxend`, `fax2`, `bibliomail`, `dbsmail`, `dbsmailpw`, `gbvbn`, `gbvpw`, `gbv_requester_id`, `ezbid`, `zdb`, `billing`, `billingtype`, `accounting_rhythmvalue`, `accounting_rhythmday`, `accounting_rhythmtimeout`, `billingschwellwert`, `maxordersu`, `maxordersutotal`, `maxordersj`, `orderlimits`, `userlogin`, `userbestellung`, `gbvbestellung`, `kontostatus`, `kontotyp`, `default_deloptions`, `paydate`, `expdate`, `edatum`, `gtc`, `gtcdate`) VALUES
(1, 'My Library', NULL, 'Adress / Institution', 'Street 10', '10000', 'My City', 'Schweiz', NULL, '+41 (0)43 111 11 11', '+41 (0)43 111 11 12', '', '', NULL, NULL, 'mail@mylibrary.ch', 'mail@mylibrary.ch', '', NULL, NULL, NULL, '', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 'post', NULL, NULL, '2010-05-15', NULL, NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lieferanten`
--

DROP TABLE IF EXISTS `lieferanten`;
CREATE TABLE IF NOT EXISTS `lieferanten` (
  `LID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Lieferanten ID',
  `siegel` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lieferant` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `D` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Deutscher Lieferant',
  `A` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Oestereichischer Lieferant',
  `CH` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Schweizer Lieferant',
  `allgemein` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Diesen Lieferanten sehen alle Bibliothekskonten',
  `KID` bigint(20) DEFAULT NULL COMMENT 'Speziallieferant dieses Kunden',
  PRIMARY KEY (`LID`),
  KEY `siegel` (`siegel`),
  KEY `KID` (`KID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=397 ;

--
-- Daten für Tabelle `lieferanten`
--

INSERT INTO `lieferanten` (`LID`, `siegel`, `lieferant`, `D`, `A`, `CH`, `allgemein`, `KID`) VALUES
(38, '', 'Liechtensteinische Landesbibliothek', 0, 0, 1, 0, NULL),
(33, '', 'IDS Basel/Bern', 0, 0, 1, 0, NULL),
(50, '', 'Réseau valaisan', 0, 0, 1, 0, NULL),
(43, '', 'CERN Document Server', 0, 0, 1, 0, NULL),
(36, '', 'IDS Zürich Universität', 0, 0, 1, 0, NULL),
(37, '', 'Nebis', 0, 0, 1, 0, NULL),
(35, '', 'IDS Luzern', 0, 0, 1, 0, NULL),
(39, '', 'Kunsthaus Zürich', 0, 0, 1, 0, NULL),
(34, '', 'IDS St.Gallen', 0, 0, 1, 0, NULL),
(41, '', 'SGBN - St. Galler Bibliotheksnetz', 0, 0, 1, 0, NULL),
(42, '', 'BGR Bibliotheksverbund Graubünden', 0, 0, 1, 0, NULL),
(56, '', 'GBV - Gemeinsamer Bibliotheksverbund', 0, 0, 0, 1, NULL),
(53, '', 'andere', 0, 0, 0, 1, NULL),
(46, '', 'RPVZ - Schweizerischer Zeitschrfitengesamtkatalog', 0, 0, 1, 0, NULL),
(47, '', 'Réseau des bibliothèques genevoises', 0, 0, 1, 0, NULL),
(48, '', 'Réseau des bibliothèques neuchâteloises et jurassiennes', 0, 0, 1, 0, NULL),
(45, '', 'Schweizerische Nationalbibliothek Bern', 0, 0, 1, 0, NULL),
(49, '', 'Réseau Fribourgeois', 0, 0, 1, 0, NULL),
(44, '', 'Alexandria', 0, 0, 1, 0, NULL),
(51, '', 'Réseau vaudois', 0, 0, 1, 0, NULL),
(52, '', 'British Library', 0, 0, 0, 1, NULL),
(55, '', 'Verlag', 0, 0, 0, 1, NULL),
(32, '', 'Subito', 0, 0, 0, 1, NULL),
(31, '', 'abonniert', 0, 0, 0, 1, NULL),
(30, '', 'Internet', 0, 0, 0, 1, NULL),
(40, '', 'Sbt - Sistema bibliotecario ticinese', 0, 0, 1, 0, NULL),
(167, 'B 106', 'Robert-Koch-Institut Berlin, Hauptbibliothek Wedding', 1, 0, 0, 0, NULL),
(166, 'B 1513', 'Robert-Koch-Institut Berlin, Bibliothek Seestraße', 1, 0, 0, 0, NULL),
(170, '46', 'Staats- und Universitätsbibliothek Bremen', 1, 0, 0, 0, NULL),
(173, '1a', 'Staatsbibliothek zu Berlin - Preußischer Kulturbesitz, Haus Potsdamer Straße', 1, 0, 0, 0, NULL),
(174, '1', 'Staatsbibliothek zu Berlin - Preußischer Kulturbesitz, Haus Unter den Linden', 1, 0, 0, 0, NULL),
(163, 'Ma 14', 'Otto-von-Guericke-Universität Magdeburg, Universitätsbibliothek, Medizinische Zentralbibliothek', 1, 0, 0, 0, NULL),
(171, '18', 'Staats- und Universitätsbibliothek Hamburg Carl von Ossietzky Hamburg', 1, 0, 0, 0, NULL),
(161, 'Gö 116  ', 'Otto-Hahn-Bibliothek des Max-Planck-Instituts für biophysikalische Chemie Göttingen', 1, 0, 0, 0, NULL),
(160, 'Hv 131', 'Niedersächsisches Ministerium für den ländlichen Raum Hannover, Ernährung, Landwirtschaft und Verbraucherschutz', 1, 0, 0, 0, NULL),
(169, '109/720', 'Senatsbibliothek in der Stiftung Zentral- und Landesbibliothek Berlin', 1, 0, 0, 0, NULL),
(162, 'Ma 9', 'Otto-von-Guericke-Universität Magdeburg, Universitätsbibliothek', 1, 0, 0, 0, NULL),
(164, 'Bs 68', 'Physikalisch-Technische Bundesanstalt Braunschweig', 1, 0, 0, 0, NULL),
(175, 'Bv 2', 'Stiftung Alfred-Wegener-Institut (AWI) für Polar- und Meeresforschung in der Helmholtz-Gemeinschaft Bremerhaven', 1, 0, 0, 0, NULL),
(168, 'Ner 1', 'Ruppiner Kliniken GmbH Neuruppin', 1, 0, 0, 0, NULL),
(165, 'B 108', 'Physikalisch-Technische Bundesanstalt, Institut Berlin', 1, 0, 0, 0, NULL),
(158, '7/0001', 'Niedersächsische Staats- und Universitätsbibliothek Göttingen, Forstwissenschaften und Waldökologie', 1, 0, 0, 0, NULL),
(156, '354', 'Medizinische Hochschule Hannover', 1, 0, 0, 0, NULL),
(153, 'Ha 94', 'Max-Planck-Institut für Mikrostrukturphysik Halle/Saale', 1, 0, 0, 0, NULL),
(139, 'Ha 93', 'Leibniz-Institut für Pflanzenbiochemie Halle/Saale', 1, 0, 0, 0, NULL),
(154, 'B 787', 'Max-Planck-Institut für molekulare Genetik Berlin', 1, 0, 0, 0, NULL),
(159, 'Hv 14', 'Niedersächsischer Landtag Hannover', 1, 0, 0, 0, NULL),
(152, 'Gö 134', 'Max-Planck-Institut für experimentelle Medizin Göttingen, Karl-Thomas-Bibliothek', 1, 0, 0, 0, NULL),
(151, 'Ma 54', 'Max-Planck-Institut für Dynamik Komplexer Technischer Systeme Magdeburg', 1, 0, 0, 0, NULL),
(150, 'J 126', 'Max-Planck-Institut für Chemische Ökologie und Max-Planck-Institut für Biogeochemie Jena', 1, 0, 0, 0, NULL),
(149, 'B 1532', 'Max-Planck-Institut für Bildungsforschung Berlin', 1, 0, 0, 0, NULL),
(148, 'B 2225', 'Max-Delbrück-Centrum für Molekulare Medizin Berlin', 1, 0, 0, 0, NULL),
(125, 'R 71', 'Johann Heinrich von Thünen-Institut Hamburg, BFI für Ländliche Räume, Wald, Fischerei, IZ Fischerei, Rostock', 1, 0, 0, 0, NULL),
(143, 'R 134', 'Marineamt Rostock, Fachinformationsstelle', 1, 0, 0, 0, NULL),
(147, 'Ki 29', 'Max Rubner-Institut, Bundesforschungsinstitut für Ernährung und Lebensmittel, Kiel', 1, 0, 0, 0, NULL),
(142, 'B 30', 'Lorberg-Bibliothek des Instituts für Gärungsgewerbe und Biotechnologie Berlin', 1, 0, 0, 0, NULL),
(141, 'Mun 1', 'Leibniz-Zentrum für Agrarlandschaftsforschung (ZALF) e. V. Müncheberg', 1, 0, 0, 0, NULL),
(157, '7', 'Niedersächsische Staats- und Universitätsbibliothek Göttingen', 1, 0, 0, 0, NULL),
(155, 'Po 80', 'Max-Planck-Institut für Molekulare Pflanzenphysiologie Potsdam', 1, 0, 0, 0, NULL),
(146, 'Ka 51', 'Max Rubner-Institut, Bundesforschungsinstitut für Ernährung und Lebensmittel, Karlsruhe', 1, 0, 0, 0, NULL),
(145, 'Wim 21', 'Materialforschungs- und -prüfanstalt (MFPA) an der Bauhaus-Universität Weimar', 1, 0, 0, 0, NULL),
(144, 'Eb 15', 'Martin Gropius Krankenhaus GmbH Eberswalde', 1, 0, 0, 0, NULL),
(140, 'B 2042', 'Leibniz-Institut für Zoo- und Wildtierforschung im Forschungsverbund Berlin e.V. (IZW)', 1, 0, 0, 0, NULL),
(138, 'Ma 45', 'Leibniz-Institut für Neurobiologie Magdeburg', 1, 0, 0, 0, NULL),
(137, 'Ki 109', 'Leibniz-Institut für Meereswissenschaften Kiel, IFM-GEOMAR, Bibliothek Westufer', 1, 0, 0, 0, NULL),
(127, 'B 85', 'Julius Kühn-Institut, Bundesforschungsinstitut für Kulturpflanzen, Berlin-Dahlem', 1, 0, 0, 0, NULL),
(135, 'B 259', 'Leibniz-Institut für Gewässerökologie und Binnenfischerei im Forschungsverbund Berlin e.V.', 1, 0, 0, 0, NULL),
(134, 'Po 61', 'Landeslabor Brandenburg Potsdam', 1, 0, 0, 0, NULL),
(133, 'B 2096', 'Länderinstitut für Bienenkunde Hohen Neuendorf e.V.', 1, 0, 0, 0, NULL),
(132, 'FR 1', 'Klinikum Frankfurt/Oder GmbH', 1, 0, 0, 0, NULL),
(131, 'Po 30', 'Klinikum ''Ernst von Bergmann'' gGmbH Potsdam', 1, 0, 0, 0, NULL),
(130, 'Kle 2', 'Julius Kühn-Institut, Bundesforschungsinstitutfür Kulturpflanzen, Kleinmachnow', 1, 0, 0, 0, NULL),
(129, 'Q 1', 'Julius Kühn-Institut, Bundesforschungsinstitut für Kulturpflanzen, Quedlinburg', 1, 0, 0, 0, NULL),
(120, '11/18', 'Humboldt-Universität zu Berlin, Agrarwissenschaften, Teilbibliothek Tierzucht', 1, 0, 0, 0, NULL),
(122, '11/148', 'Humboldt-Universität zu Berlin, Fremdsprachliche Philologien, Teilbibliothek Anglistik / Amerikanistik', 1, 0, 0, 0, NULL),
(124, 'H 140', 'Johann Heinrich von Thünen-Institut Hamburg, BFI für Ländliche Räume, Wald, Fischerei, IZ Fischerei, Hamburg', 1, 0, 0, 0, NULL),
(123, '204', 'Ibero-Amerikanisches Institut Preußischer Kulturbesitz Berlin', 1, 0, 0, 0, NULL),
(128, 'Bs 66', 'Julius Kühn-Institut, Bundesforschungsinstitut für Kulturpflanzen, Braunschweig', 1, 0, 0, 0, NULL),
(121, '11/84', 'Humboldt-Universität zu Berlin, Zweigbibliothek Biologie', 1, 0, 0, 0, NULL),
(136, 'Ki 130', 'Leibniz-Institut für Meereswissenschaften Kiel, IFM-GEOMAR, Bibliothek Ostufer', 1, 0, 0, 0, NULL),
(119, '11/42', 'Humboldt-Universität zu Berlin, Agrarwissenschaften, Institut für Gartenbauwissenschaften, Pflanzenzüchtung', 1, 0, 0, 0, NULL),
(193, '3/76', 'Universitäts- und Landesbibliothek Sachsen-Anhalt, Halle/Saale, Zweigbibliothek Physiologische Chemie', 1, 0, 0, 0, NULL),
(126, 'H 105', 'Johann Heinrich von Thünen-Institut Hamburg, BFI für Ländliche Räume, Wald, Fischerei, IZ Wald', 1, 0, 0, 0, NULL),
(118, '11', 'Humboldt-Universität zu Berlin, Universitätsbibliothek, Zentralbibliothek', 1, 0, 0, 0, NULL),
(117, '705', 'Helmut-Schmidt-Universität, Universität der Bundeswehr Hamburg', 1, 0, 0, 0, NULL),
(99, 'Eb 1', 'Fachhochschule Eberswalde', 1, 0, 0, 0, NULL),
(106, '188/846', 'Freie Universität Berlin, Fachbereich Veterinärmedizin', 1, 0, 0, 0, NULL),
(114, 'B 113', 'Fritz-Haber-Institut der Max-Planck-Gesellschaft Berlin', 1, 0, 0, 0, NULL),
(113, 'Ce 4', 'Friedrich-Loeffler-Institut, Bundesforschungsinstitut für Tiergesundheit Celle, Institut für Tierschutz und Tierhaltung', 1, 0, 0, 0, NULL),
(112, 'Wus 1', 'Friedrich-Loeffler-Institut Wusterhausen, Bundesforschungsinstitut für Tiergesundheit', 1, 0, 0, 0, NULL),
(111, 'J 120', 'Friedrich-Loeffler-Institut Jena, Bundesforschungsinstitut für Tiergesundheit', 1, 0, 0, 0, NULL),
(115, 'B 1505', 'Hahn-Meitner-Institut Berlin GmbH Berlin, Zentralbibliothek', 1, 0, 0, 0, NULL),
(109, 'Tü 67', 'Friedrich-Loeffler-Institut (FLI) Tübingen, Bundesforschungsinstitut für Tiergesundheit, Institut für Immunologie', 1, 0, 0, 0, NULL),
(108, '188', 'Freie Universität Berlin', 1, 0, 0, 0, NULL),
(105, '188/726', 'Freie Universität Berlin, Fachbereich Erziehungswissenschaft und Psychologie', 1, 0, 0, 0, NULL),
(101, 'H 23', 'Freie und Hansestadt Hamburg, Behörde für Soziales und Gesundheit, Amt für Gesundheit und Verbraucherschutz', 1, 0, 0, 0, NULL),
(103, '188/24', 'Freie Universität Berlin, Botanischer Garten und Botanisches Museum Berlin-Dahlem', 1, 0, 0, 0, NULL),
(102, '188/808', 'Freie Universität Berlin, Fachbereich Rechtswissenschaft', 1, 0, 0, 0, NULL),
(107, '188/820', 'Freie Universität Berlin, Fachbereich Wirtschaftswissenschaft', 1, 0, 0, 0, NULL),
(100, 'H 27', 'Freie und Hansestadt Hamburg, Behörde für Soziales und Gesundheit, Institut für Hygiene und Umwelt', 1, 0, 0, 0, NULL),
(116, 'B 2018', 'HELIOS Zentralbibliothek Berlin', 1, 0, 0, 0, NULL),
(98, 'B 1514', 'Fachbibliothek Umwelt des Umweltbundesamtes, Berlin', 1, 0, 0, 0, NULL),
(97, 'B 1531', 'Deutsches Rheuma-Forschungszentrum und Max-Planck-Institut für Infektionsbiologie Berlin', 1, 0, 0, 0, NULL),
(96, 'Po 6', 'Deutsches Institut für Ernährungsforschung (DIfE) Potsdam-Rehbrücke', 1, 0, 0, 0, NULL),
(110, 'Gr 61', 'Friedrich-Loeffler-Institut, Bundesforschungsinstitut für Tiergesundheit Greifswald', 1, 0, 0, 0, NULL),
(95, '206', 'Deutsche Zentralbibliothek für Wirtschaftswissenschaften (ZBW) / Leibniz-Informationszentrum Wirtschaft, Kiel', 1, 0, 0, 0, NULL),
(89, 'Co 2', 'Carl-Thiem-Klinikum Cottbus gGmbH', 1, 0, 0, 0, NULL),
(87, 'B 12', 'Bundesinstitut für Risikobewertung (BfR)', 1, 0, 0, 0, NULL),
(94, '206 H', 'Deutsche Zentralbibliothek für Wirtschaftswissenschaften (ZBW) / Leibniz-Informationszentrum Wirtschaft, Hamburg', 1, 0, 0, 0, NULL),
(93, '578/897', 'Charité - Universitätsmedizin Berlin, Zentrum für Human- und Gesundheitswissenschaften, Institut für Arbeitsmedizin', 1, 0, 0, 0, NULL),
(91, '578/2', 'Charité - Universitätsmedizin Berlin, Campus Charité Mitte', 1, 0, 0, 0, NULL),
(90, '578/1', 'Charité - Universitätsmedizin Berlin, Campus Benjamin Franklin', 1, 0, 0, 0, NULL),
(92, '578/3', 'Charité - Universitätsmedizin Berlin, Campus Virchow-Klinikum', 1, 0, 0, 0, NULL),
(176, '89', 'Technische Informationsbibliothek und Universitätsbibliothek Hannover (TIB/UB)', 1, 0, 0, 0, NULL),
(88, 'B 1574', 'Bundeswehrkrankenhaus Berlin', 1, 0, 0, 0, NULL),
(86, 'B 43', 'Bundesanstalt für Materialforschung und -prüfung Berlin', 1, 0, 0, 0, NULL),
(207, '83', 'Zentralbibliothek der Technischen Universität Berlin', 1, 0, 0, 0, NULL),
(84, 'B 1565', 'Bundesamt für Verbraucherschutz und Lebensmittelsicherheit (BVL) Berlin', 1, 0, 0, 0, NULL),
(82, 'B 4', 'Berlin-Brandenburgische Akademie der Wissenschaften', 1, 0, 0, 0, NULL),
(83, 'Co 1', 'Brandenburgische Technische Universität Cottbus', 1, 0, 0, 0, NULL),
(81, 'Wim 2', 'Bauhaus-Universität Weimar', 1, 0, 0, 0, NULL),
(80, '287', 'Akademie der Bundeswehr für Information und Telefon Strausberg', 1, 0, 0, 0, NULL),
(85, 'B 424', 'Bundesanstalt für Arbeitsschutz und Arbeitsmedizin Berlin', 1, 0, 0, 0, NULL),
(177, '830', 'Technische Universität Hamburg-Harburg', 1, 0, 0, 0, NULL),
(178, 'J 123', 'Thüringer Landesanstalt für Landwirtschaft Jena', 1, 0, 0, 0, NULL),
(179, 'J 121', 'Thüringer Landesanstalt für Umwelt und Geologie Jena', 1, 0, 0, 0, NULL),
(180, 'Sb 1', 'Thüringer Landesanstalt für Umwelt und Geologie Weinbergen, Staatliche Vogelschutzwarte Seebach', 1, 0, 0, 0, NULL),
(181, '27', 'Thüringer Universitäts- und Landesbibliothek Jena', 1, 0, 0, 0, NULL),
(191, '3/21', 'Universitäts- und Landesbibliothek Sachsen-Anhalt, Halle/Saale, Fachbereich Physik', 1, 0, 0, 0, NULL),
(183, '83/1230', 'TU Berlin, Institut für Chemie, Arbeitsgruppe Biochemie und Molekulare Biologie', 1, 0, 0, 0, NULL),
(184, '83/1032', 'TU Berlin, Institut für Geodäsie und Geoinformationstechnik', 1, 0, 0, 0, NULL),
(185, '83/1111', 'TU Berlin, Universitätsbibliothek, Bereichbibliothek TIB-Gelände, FG Lebensmittelchemie', 1, 0, 0, 0, NULL),
(349, '715', 'Informations-, Bibliotheks- und IT-Dienste (IBIT)Oldenburg', 1, 0, 0, 0, NULL),
(187, '517', 'Universität Potsdam', 1, 0, 0, 0, NULL),
(188, '39', 'Universitäts- und Forschungsbibliothek Erfurt/Gotha, Forschungsbibliothek Gotha', 1, 0, 0, 0, NULL),
(189, '547', 'Universitäts- und Forschungsbibliothek Erfurt/Gotha, Universitätsbibliothek Erfurt', 1, 0, 0, 0, NULL),
(198, '104', 'Universitätsbibliothek Clausthal-Zellerfeld', 1, 0, 0, 0, NULL),
(196, '9', 'Universitätsbibliothek Greifswald', 1, 0, 0, 0, NULL),
(194, '3/11', 'Universitäts- und Landesbibliothek Sachsen-Anhalt, Halle/Saale, Zweigbibliothek Rechtswissenschaft', 1, 0, 0, 0, NULL),
(195, '3/55', 'Universitäts- und Landesbibliothek Sachsen-Anhalt, Merseburg, Zweigbibliothek Technik', 1, 0, 0, 0, NULL),
(190, '3', 'Universitäts- und Landesbibliothek Sachsen-Anhalt, Halle/Saale, Zentrale', 1, 0, 0, 0, NULL),
(197, '28', 'Universitätsbibliothek Rostock', 1, 0, 0, 0, NULL),
(182, '95', 'Tierärztliche Hochschule Hannover', 1, 0, 0, 0, NULL),
(199, '700', 'Universitätsbibliothek Osnabrück', 1, 0, 0, 0, NULL),
(200, 'Lün 4', 'Universitätsbibliothek Lüneburg', 1, 0, 0, 0, NULL),
(203, 'Ilm 1', 'Universitätsbibliothek Ilmenau', 1, 0, 0, 0, NULL),
(186, '83/1001', 'TU Berlin, WiWiDok - Fachbibliothek Wirtschaft, Recht & Statistik', 1, 0, 0, 0, NULL),
(201, '84', 'Universitätsbibliothek Braunschweig', 1, 0, 0, 0, NULL),
(204, '8', 'Universitätsbibliothek Kiel', 1, 0, 0, 0, NULL),
(205, '578/821', 'Universitätsmedizin Berlin, Campus Benjamin Franklin, Klinik und Poliklinik für Zahn-, Mund- und Kieferheilkunde', 1, 0, 0, 0, NULL),
(208, 'Bre 14', 'Zentrum für Marine Tropenökologie an der Universität Bremen (ZMT), ZB der TU Berlin', 1, 0, 0, 0, NULL),
(206, '109', 'Zentral- und Landesbibliothek Berlin, Haus Amerika-Gedenkbibliothek und Haus Berliner Stadt- & Senatsbibliothek', 1, 0, 0, 0, NULL),
(104, '188/823', 'Freie Universität Berlin, Fachbereich Biologie, Chemie, Pharmazie, Mineralogie, Geowissenschaften', 1, 0, 0, 0, NULL),
(345, '100', 'Universitätsbibliothek Hohenheim, Stuttgart', 1, 0, 0, 0, NULL),
(202, 'Hil 2', 'Universitätsbibliothek Hildesheim', 1, 0, 0, 0, NULL),
(357, 'L 97', 'Helmholtz-Zentrum für Umweltforschung GmbH - UFZ, Leipzig', 1, 0, 0, 0, NULL),
(346, '', 'GBV (BVB - Bibliotheksverbund Bayern)', 0, 0, 0, 1, NULL),
(347, '', 'GBV (HeBIS - Hessisches BibliotheksInformationsSystem)', 0, 0, 0, 1, NULL),
(362, '253', 'Johann Heinrich von Thünen-Institut,\nBFI für Ländliche Räume, Wald und Fischerei, Braunschweig', 1, 0, 0, 0, NULL),
(192, '3/ 4', 'Universitäts- und Landesbibliothek Sachsen-Anhalt, Halle/Saale, Zweigbibliothek Landwirtschaft', 1, 0, 0, 0, NULL),
(359, 'Det 2', 'Max Rubner-Institut, Bundesforschungsinstitut für Ernährung und\nLebensmittel, Detmold', 1, 0, 0, 0, NULL),
(355, 'Dm 19', 'Bundesanstalt für Arbeitsschutz und Arbeitsmedizin, Bibliothek', 1, 0, 0, 0, NULL),
(348, '', 'GBV (SWB - Südwestdeutscher Bibliotheksverbund)', 0, 0, 0, 1, NULL),
(358, 'Sra 5', 'Fachhochschule Stralsund, Hochschulbibliothek', 1, 0, 0, 0, NULL),
(356, 'B 1512', 'Bundesinstitut für Arzneimittel und Medizinprodukte (BfArM), Bibliothek', 1, 0, 0, 0, NULL),
(361, 'B 198', 'Deutsche Rentenversicherung Bund, Bibliothek, Berlin', 1, 0, 0, 0, NULL),
(1, NULL, 'k.A.', 0, 0, 0, 0, NULL),
(363, 'F 20', 'Paul-Ehrlich-Institut, Langen', 1, 0, 0, 0, NULL),
(364, '', 'Loansome Doc', 0, 0, 0, 1, NULL),
(388, '66', 'Hochschul- und Landesbibliothek Fulda', 1, 0, 0, 0, NULL),
(389, 'B 15', 'Senckenberg Deutsches Entomologisches Institut', 1, 0, 0, 0, NULL),
(390, '', 'ETH Zürich', 0, 0, 1, 0, NULL),
(391, '', 'ZB Zürich', 0, 0, 1, 0, NULL),
(392, '', 'Carelit-Volltexte', 0, 0, 0, 1, NULL),
(394, '101', 'Deutsche Nationalbibliothek', 1, 0, 0, 0, NULL),
(395, '', 'Deutsche Nationalbibliothek', 0, 0, 1, 0, NULL),
(396, 'Wa 1', 'Leibniz-Institut für Ostseeforschung Warnemünde', 1, 0, 0, 0, NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `stock`
--

DROP TABLE IF EXISTS `stock`;
CREATE TABLE IF NOT EXISTS `stock` (
  `STID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HOID` bigint(20) NOT NULL,
  `startyear` varchar(4) NOT NULL,
  `startvolume` varchar(20) DEFAULT NULL,
  `startissue` varchar(20) DEFAULT NULL,
  `endyear` varchar(4) NOT NULL,
  `endvolume` varchar(20) DEFAULT NULL,
  `endissue` varchar(20) DEFAULT NULL,
  `suppl` tinyint(1) NOT NULL DEFAULT '1',
  `eissue` tinyint(1) NOT NULL DEFAULT '0',
  `standort` bigint(20) NOT NULL,
  `shelfmark` varchar(100) NOT NULL,
  `bemerkungen` varchar(500) NOT NULL,
  `internal` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STID`),
  KEY `HOID` (`HOID`),
  KEY `startyear` (`startyear`),
  KEY `startvolume` (`startvolume`),
  KEY `startissue` (`startissue`),
  KEY `endyear` (`endyear`),
  KEY `endvolume` (`endvolume`),
  KEY `endissue` (`endissue`),
  KEY `standort` (`standort`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `stock`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `stock_missing`
--

DROP TABLE IF EXISTS `stock_missing`;
CREATE TABLE IF NOT EXISTS `stock_missing` (
  `NLID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Negative-List-ID',
  `STID` bigint(20) NOT NULL,
  `startyear` varchar(4) NOT NULL,
  `startvolume` varchar(20) DEFAULT NULL,
  `startissue` varchar(20) DEFAULT NULL,
  `endyear` varchar(4) NOT NULL,
  `endvolume` varchar(20) DEFAULT NULL,
  `endissue` varchar(20) DEFAULT NULL,
  `suppl` tinyint(1) NOT NULL DEFAULT '0',
  `anmerkungen` varchar(500) NOT NULL,
  PRIMARY KEY (`NLID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `stock_missing`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `text`
--

DROP TABLE IF EXISTS `text`;
CREATE TABLE IF NOT EXISTS `text` (
  `TID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) DEFAULT NULL,
  `TYID` bigint(20) NOT NULL,
  `inhalt` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`TID`),
  KEY `TYID` (`TYID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=22 ;

--
-- Daten für Tabelle `text`
--

INSERT INTO `text` (`TID`, `KID`, `TYID`, `inhalt`) VALUES
(1, NULL, 1, 'User'),
(2, NULL, 1, 'Bibliothekar'),
(3, NULL, 1, 'Administrator'),
(4, NULL, 2, 'bestellt'),
(5, NULL, 2, 'reklamiert'),
(6, NULL, 2, 'geliefert'),
(7, NULL, 2, 'nicht lieferbar'),
(8, NULL, 2, 'erledigt'),
(9, NULL, 2, 'zu bestellen'),
(10, NULL, 4, 'aktiv'),
(11, NULL, 4, 'inaktiv'),
(13, NULL, 3, 'erhalten'),
(14, NULL, 3, 'geliefert'),
(15, NULL, 3, 'gelöscht'),
(16, NULL, 3, 'Faxserver aktiv'),
(17, NULL, 3, 'Faxserver Verbindungsfehler'),
(12, NULL, 6, 'Version1'),
(18, NULL, 7, 'EUR'),
(19, NULL, 7, 'USD'),
(20, NULL, 7, 'CHF'),
(21, NULL, 7, 'GBP');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `texttyp`
--

DROP TABLE IF EXISTS `texttyp`;
CREATE TABLE IF NOT EXISTS `texttyp` (
  `TYID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) DEFAULT NULL,
  `inhalt` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`TYID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=15 ;

--
-- Daten für Tabelle `texttyp`
--

INSERT INTO `texttyp` (`TYID`, `KID`, `inhalt`) VALUES
(1, NULL, 'Berechtigungsstufe'),
(2, NULL, 'Status'),
(3, NULL, 'Faxstatus'),
(4, NULL, 'Kontostatus'),
(5, NULL, 'Bestellquelle'),
(6, NULL, 'GTC'),
(7, NULL, 'Waehrungen'),
(8, NULL, 'Rechnungsgrund'),
(9, NULL, 'IP'),
(10, NULL, 'Standorte'),
(11, NULL, 'Brokerkennung'),
(12, NULL, 'Kontokennung'),
(13, NULL, 'Bestellformular eingeloggt'),
(14, NULL, 'DAIA-ID');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `v_konto_benutzer`
--

DROP TABLE IF EXISTS `v_konto_benutzer`;
CREATE TABLE IF NOT EXISTS `v_konto_benutzer` (
  `vkbid` bigint(20) NOT NULL AUTO_INCREMENT,
  `KID` bigint(20) NOT NULL,
  `UID` bigint(20) NOT NULL,
  PRIMARY KEY (`vkbid`),
  KEY `KID` (`KID`),
  KEY `UID` (`UID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Daten für Tabelle `v_konto_benutzer`
--

INSERT INTO `v_konto_benutzer` (`vkbid`, `KID`, `UID`) VALUES
(2, 1, 1),
(3, 1, 2);
