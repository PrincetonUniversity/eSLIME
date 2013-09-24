package processes;

import org.dom4j.Element;

import structural.halt.HaltCondition;

import io.project.ProcessLoader;
import io.project.ProjectLoader;

public abstract class Process {

	int id;
	int period;
	
	

	
	/* Constructors */
	
	public Process(ProcessLoader loader, int id) {
		this.id = id;
		Element e = loader.getProcess(id);
		period = Integer.valueOf(get(e, "period"));
	}

	/* Process identifiers */
	
	protected abstract String getElementName();

	protected abstract String getProcessClass();

	public int getID() {
		return id;
	}
	
	/* Process events */
	
	public abstract void iterate(StepState state) throws HaltCondition;
	
	/**
	 *  Pull in a single-datum element
	 * @param g
	 * @param key
	 * @return
	 */
	protected String get(Element g, String key) {
		Element vElem = g.element(key);
		if (vElem == null) {
			throw new IllegalArgumentException("Parameter " + 
					key + " not defined for process " +
					getProcessClass() + " (id=" + getID() +").");
		}
		
		Object value = vElem.getData();
		
		return value.toString();
	}

	public int getPeriod() {
		return period;
	}
	

}
