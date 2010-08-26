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

package ch.dbs.form;

import org.apache.struts.action.ActionForm;

public final class Message extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String message; // should be used only with property-message-keys because of translational reasons
    //may be any given freetext string as: additional informations, error codes...
    private String systemMessage;
    private String link;


    public Message() {

    }
    public Message(final String msg, final String mLink) {
        this.message = msg;
        this.link = mLink;
    }

    public Message(final String msg, final String systemMsg, final String mLink) {
        this.message = msg;
        this.systemMessage = systemMsg;
        this.link = mLink;
    }


    public Message(final String msg) {
        this.message = msg;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(final String message) {
        this.message = message;
    }
    public String getLink() {
        return link;
    }
    public void setLink(final String link) {
        this.link = link;
    }
    public String getSystemMessage() {
        return systemMessage;
    }
    public void setSystemMessage(final String systemMessage) {
        this.systemMessage = systemMessage;
    }

}
