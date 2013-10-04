package structural;

import java.util.Map;

/**
 * Read-only viewer for the state map object.
 *
 */
public class StateMapViewer {

	private Map<Integer, Integer> stateMap;

	public StateMapViewer(Map<Integer, Integer> stateMap) {
		this.stateMap = stateMap;
	}
	
	public Integer[] getStates() {
		Integer[] states = stateMap.keySet().toArray(new Integer[0]);
		return states;
	}
	
	public Integer getCount(Integer state) {
		
		// If it doesn't have the key, there are no cells of that state
		if (!stateMap.containsKey(state)) {
			return 0;
		}
		
		return stateMap.get(state);
	}
}
