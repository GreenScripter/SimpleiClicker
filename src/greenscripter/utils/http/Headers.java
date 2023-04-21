package greenscripter.utils.http;

import java.util.ArrayList;
import java.util.List;

public class Headers {
	
	public List<String> inHeaders = new ArrayList<>();
	public List<String> inValues = new ArrayList<>();
	
	/**
	 * Add a header and value pair.
	 * 
	 * @param header the new header
	 * @param value the header's value
	 */
	public Headers add(String header, String value) {
		inHeaders.add(header);
		inValues.add(value);
		return this;
	}
	
	/**
	 * Get the first occurrence of a header. Some headers may appear multiply times.
	 * 
	 * @param header the header string
	 * @return the value of the first occurrence
	 */
	public String get(String header) {
		for (int i = 0; i < inHeaders.size(); i++) {
			if (inHeaders.get(i).equalsIgnoreCase(header)) {
				return inValues.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Get all occurrences of a header. Some headers may appear multiply times.
	 * 
	 * @param header the header string
	 * @return a list of all values under that header
	 */
	public List<String> getAll(String header) {
		List<String> h = new ArrayList<>();
		for (int i = 0; i < inHeaders.size(); i++) {
			if (inHeaders.get(i).equalsIgnoreCase(header)) {
				h.add(inValues.get(i));
			}
		}
		return null;
	}
	
	/**
	 * Remove all occurrences of a specified header.
	 * 
	 * @param header the header to remove
	 */
	public void remove(String header) {
		for (int i = 0; i < inHeaders.size(); i++) {
			if (inHeaders.get(i).equalsIgnoreCase(header)) {
				inValues.remove(i);
				inHeaders.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Create a copy such that modifications to this header will remain distinct, by performing a
	 * deep copy.
	 * 
	 * @return a copy of this header
	 */
	public Headers copy() {
		Headers h = new Headers();
		h.inHeaders = new ArrayList<>(inHeaders);
		h.inValues = new ArrayList<>(inValues);
		return h;
	}
}