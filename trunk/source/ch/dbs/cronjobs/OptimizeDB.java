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

package ch.dbs.cronjobs;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import ch.dbs.entity.Text;

public final class OptimizeDB extends DispatchAction {
    
    /**
     * Allows to optimize the database upon a regular cronjob. Use with caution:
     * the tables will be locked, while optimizing and depending on the table size
     * this may take a while. 
     */
    public void optimizeDB(ActionMapping mp,
            				ActionForm form,
            				HttpServletRequest rq,
            				HttpServletResponse rp){
    	
    	Text cn = new Text();                
        PreparedStatement pstmt = null;
        
    	try {
            pstmt = cn.getConnection().prepareStatement("OPTIMIZE TABLE `benutzer` , `bestellform_param` , `bestellungen` , `bestellstatus` , `default_preise` , `holdings` , `konto` , `lieferanten` , `stock` , `text` , `v_konto_benutzer`");            
            pstmt.executeUpdate();            
        } catch (Exception e) {
        	log.error("public void optimize)" + e.toString());
        } finally {
        	if (pstmt != null) {
        		try {
        			pstmt.close();
        		} catch (SQLException e) {
        			System.out.println(e);
        		}
        	}
        	cn.close();
        }
    }
}
