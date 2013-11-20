package io.project;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.Element;

/**
 * Reads the specifications of individual processes from the project
 * loader and provides them to the process factory for construction.
 * 
 * @untested
 * @author dbborens
 *
 */
public class ProcessLoader {

	HashMap<Integer, Element> elemsByID;
	
	public ProcessLoader(Element root) {
		elemsByID = new HashMap<Integer, Element>();
		
		for (Object o : root.elements()) {
			Element e = (Element) o;
			
			Integer id = Integer.valueOf(get(e, "id"));
			
			if (elemsByID.containsKey(id)) {
				String msg = "Duplicate process ID " + id + ".";
				throw new IllegalArgumentException(msg);
			}
			
			elemsByID.put(id, e);
		}
	}
	
	/**
	 * Retrieves a process element from the processes sub-tree, or throws
	 * an exception if the element is not found.
	 * @param elementName
	 * @param id
	 * @return
	 */
	public Element getProcess(int id) {
		return elemsByID.get(id);
	}

	public Integer[] getProcesses() {
		// Get set of process IDs. These are assumed to be unsorted.
		Set<Integer> processes = elemsByID.keySet();
		
		// Sort them.
		TreeSet<Integer> sortedProcesses = new TreeSet<Integer>(processes);
		
		// Make an array from the sorted set.
		Integer[] processArr = sortedProcesses.toArray(new Integer[0]);
		
		// Return the array.
		return processArr;
	}
	
	// Pull in a single-datum element
	protected String get(Element g, String key) {
		Element vElem = g.element(key);
		if (vElem == null) {
			throw new IllegalArgumentException("Process " + 
					key + " missing. XML as follows: \n" + g.asXML());
		}
		
		Object value = vElem.getData();
		
		return value.toString();
	}
}

