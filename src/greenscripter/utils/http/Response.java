package greenscripter.utils.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Response {
	public byte[] body;
	public Headers headers = new Headers();
	public int responseCode;
	public String responseText;
	
	public byte[] getBytes() {
		byte[] body = this.body;
		if (body != null) {
			String encoding = getHeader("Content-Encoding");
			body = ((encoding != null && encoding.equals("gzip")) ? HTTP.gzip(body) : body);
			headers.remove("Content-Length");
			headers.add("Content-Length", body.length + "");
		} else {
			headers.remove("Content-Length");
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StringBuffer header = new StringBuffer();
		
		header.append("HTTP/1.1");
		header.append(" ");
		header.append(responseCode + "");
		header.append(" ");
		header.append(responseText);
		
		header.append("\r\n");
		
		for (int i = 0; i < headers.inHeaders.size(); i++) {
			header.append(headers.inHeaders.get(i));
			header.append(": ");
			header.append(headers.inValues.get(i));
			header.append("\r\n");
			
		}
		header.append("\r\n");
		try {
			out.write(header.toString().getBytes());
			
			if (body != null) out.write(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out.toByteArray();
		
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public void disableCompression() {
		headers.remove("Content-Encoding");
	}
	
	public void useGzipCompression() {
		headers.remove("Content-Encoding");
		headers.add("Content-Encoding", "gzip");
	}
	
}