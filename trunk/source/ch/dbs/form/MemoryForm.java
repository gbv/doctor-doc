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

import util.MemoryUtil;


public final class MemoryForm extends ActionForm {
	
	private long freeMemory;
	private long totalMemory;
	private long maxMemory;
	private long memoryUsed;	
    private static final long serialVersionUID = 1L;
    
    public MemoryForm(MemoryUtil mu) {    	
    	freeMemory = mu.getFreeMemory();
    	maxMemory = mu.getMaxMemory();
    	memoryUsed = mu.getMemoryUsed();
    	totalMemory = mu.getTotalMemory();
    }
    
    public MemoryForm() {
    	
    }


	public final long getFreeMemory() {
		return freeMemory;
	}

	public final long getTotalMemory() {
		return totalMemory;
	}

	public final long getMaxMemory() {
		return maxMemory;
	}

	public final long getMemoryUsed() {
		return memoryUsed;
	}

    



}
