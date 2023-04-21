package greenscripter.utils.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Request {
	public byte[] body;
	public Headers headers = new Headers();
	
	public String method;
	public String version = "HTTP/1.1";
	public String path;
	
	public byte[] getBytes() {
		for (int i = 0; i < headers.inHeaders.size(); i++) {
			if (headers.inHeaders.get(i).equalsIgnoreCase("Content-Length")) {
				headers.inValues.set(i, body.length + "");
				break;
			}
		}
		
		if (body == null || body.length == 0) {
			for (int i = 0; i < headers.inHeaders.size(); i++) {
				if (headers.inHeaders.get(i).equalsIgnoreCase("Content-Length")) {
					headers.inValues.remove(i);
					headers.inHeaders.remove(i);
					break;
				}
			}
		} else {
			for (int i = 0; i < headers.inHeaders.size(); i++) {
				if (headers.inHeaders.get(i).equalsIgnoreCase("Content-Length")) {
					headers.inValues.remove(i);
					headers.inHeaders.remove(i);
					break;
				}
			}
			headers.add("content-length", body.length + "");
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StringBuffer header = new StringBuffer();
		
		header.append(method);
		header.append(" ");
		header.append(path);
		header.append(" ");
		header.append(version);
		
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
	
	public Request setContent(byte[] b) {
		body = b;
		return this;
	}
	
	public String getHeader(String name) {
		
		return headers.get(name);
	}
	
	public void disableCompression() {
		headers.remove("accept-encoding");
	}
	
	public void useGzipCompression() {
		headers.remove("accept-encoding");
		headers.add("accept-encoding", "gzip");
	}
	
	public void useGzipCompressionIfAllowed() {
		if ((headers.get("accept-encoding") + "").contains("gzip")) {
			headers.remove("accept-encoding");
			headers.add("accept-encoding", "gzip");
		}
	}
}