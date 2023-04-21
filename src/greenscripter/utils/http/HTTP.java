package greenscripter.utils.http;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class HTTP {

	public static String readChar(InputStream in) throws IOException {
		int theByte = in.read();
		if (theByte == -1) {
			throw new EOFException();

		}
		boolean a = (theByte & 128) != 0;
		boolean b = (theByte & 64) != 0;
		boolean c = (theByte & 32) != 0;
		boolean d = (theByte & 16) != 0;
		int ai = a ? 1 : 0;
		int bi = b ? 1 : 0;
		int ci = c ? 1 : 0;
		int di = d ? 1 : 0;
		if (ai == 1 & bi == 1) {

			if (ci == 1) {
				if (di == 1) {

					int b2 = in.read();
					int b3 = in.read();
					int b4 = in.read();
					if (b2 == -1 || b3 == -1 || b4 == -1) {
						throw new EOFException();
					}

					return new String(new byte[] { (byte) theByte, (byte) b2, (byte) b3, (byte) b4 }, StandardCharsets.UTF_8);
				} else {
					int b2 = in.read();
					int b3 = in.read();
					if (b2 == -1 || b3 == -1) {
						throw new EOFException();
					}
					return new String(new byte[] { (byte) theByte, (byte) b2, (byte) b3 }, StandardCharsets.UTF_8);
				}
			} else {
				int b2 = in.read();
				if (b2 == -1) {
					throw new EOFException();
				}
				return new String(new byte[] { (byte) theByte, (byte) b2 }, StandardCharsets.UTF_8);
			}

		} else {
			return new String(new byte[] { (byte) theByte }, StandardCharsets.UTF_8);
		}
	}

	public static int lastBits(int by, int n) {
		int theByte = by;
		boolean a = (theByte & 128) != 0;
		boolean b = (theByte & 64) != 0;
		boolean c = (theByte & 32) != 0;
		boolean d = (theByte & 16) != 0;
		boolean e = (theByte & 8) != 0;
		boolean f = (theByte & 4) != 0;
		boolean g = (theByte & 2) != 0;
		boolean h = (theByte & 1) != 0;
		int ai = a ? 1 : 0;
		int bi = b ? 1 : 0;
		int ci = c ? 1 : 0;
		int di = d ? 1 : 0;
		int ei = e ? 1 : 0;
		int fi = f ? 1 : 0;
		int gi = g ? 1 : 0;
		int hi = h ? 1 : 0;
		int[] number = new int[] { ai, bi, ci, di, ei, fi, gi, hi };
		int out = 0;
		int count = n - 1;
		for (int at = 8 - n; at < 8; at++) {
			out += number[at] << count;
			count -= 1;

		}
		return out;
	}

	public static String readLine(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();

		String c = readChar(is);
		if (c.equals("\r")) {
			c = readChar(is);
		}
		try {
			while (!c.equals("\n")) {
				sb.append(c);
				c = readChar(is);
				if (c.equals("\r")) {
					c = readChar(is);
				}
			}
		} catch (EOFException e) {

		}
		return sb.toString();
	}

	public static Response sendRequest(String method, String url, Headers headers, byte[] content) throws IOException {
		InputStream in = null;
		OutputStream out = System.out;
		String host;
		String path = "/";
		int port = -1;
		if (headers.inHeaders.size() != headers.inValues.size()) {
			throw new IOException("Must have the same number of headers and values. " + headers.inHeaders.size() + " != " + headers.inValues.size());
		}
		try {
			host = url.substring(url.indexOf("//") + 2);

			if (host.contains("/")) {
				path = host.substring(host.indexOf("/"));
				if (path.endsWith("/") && !path.equals("/")) {
					path = path.substring(0, path.length() - 1);
				}
				host = host.substring(0, host.indexOf("/"));

			}
			if (host.contains(":")) {
				port = Integer.parseInt(host.substring(host.indexOf(":") + 1));
				host = host.substring(0, host.indexOf(":"));
			}
		} catch (Exception e) {
			throw new IOException("Invalid URL " + url, e);
		}

		if (url.startsWith("https://")) {

			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port == -1 ? 443 : port);

			sslsocket.startHandshake();
			in = new BufferedInputStream(sslsocket.getInputStream());
			out = sslsocket.getOutputStream();

		} else if (url.startsWith("http://")) {
			@SuppressWarnings("resource")
			Socket s = new Socket(host, port == -1 ? 80 : port);
			in = new BufferedInputStream(s.getInputStream());
			out = s.getOutputStream();
		} else {
			throw new IOException("Invalid URL " + url);
		}

		Request request = new Request();
		request.body = content;
		request.headers = headers;
		request.method = method;
		request.path = path;
		request.version = "HTTP/1.1";
		request.useGzipCompression();
		request.headers.remove("host");
		request.headers.add("host", host + (port == -1 ? "" : ":" + port));

		out.write(request.getBytes());
		Response response = readResponse(in);
		return response;

	}

	public static Response sendRequest(String url, Request r) throws IOException {
		InputStream in = null;
		OutputStream out = System.out;
		String host;
		String path = "/";
		int port = -1;
		try {
			host = url.substring(url.indexOf("//") + 2);

			if (host.contains("/")) {
				path = host.substring(host.indexOf("/"));
				if (path.endsWith("/") && !path.equals("/")) {
					path = path.substring(0, path.length() - 1);
				}
				host = host.substring(0, host.indexOf("/"));

			}
			if (host.contains(":")) {
				port = Integer.parseInt(host.substring(host.indexOf(":") + 1));
				host = host.substring(0, host.indexOf(":"));
			}
		} catch (Exception e) {
			throw new IOException("Invalid URL " + url, e);
		}

		if (url.startsWith("https://")) {

			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port == -1 ? 443 : port);

			sslsocket.startHandshake();
			in = new BufferedInputStream(sslsocket.getInputStream());
			out = sslsocket.getOutputStream();

		} else if (url.startsWith("http://")) {
			@SuppressWarnings("resource")
			Socket s = new Socket(host, port == -1 ? 80 : port);
			in = new BufferedInputStream(s.getInputStream());
			out = s.getOutputStream();
		} else {
			throw new IOException("Invalid URL " + url);
		}

		Request request = r;
		r.path = path;
		request.useGzipCompression();
		request.headers.remove("host");
		request.headers.add("host", host + (port == -1 ? "" : ":" + port));

		out.write(request.getBytes());
		Response response = readResponse(in);

		if (response.responseCode / 100 != 2) {
			throw new IOException(response.responseCode + " " + response.responseText);
		}

		return response;

	}

	public static boolean decompressGzip = true;

	public static Response readResponse(InputStream in) throws IOException {
		String inputLine;
		List<String> inHeaders = new ArrayList<>();
		List<String> inValues = new ArrayList<>();
		int responseCode = 0;
		String responseText = "";
		int c = 0;
		while ((inputLine = readLine(in)) != null) {
			if (c == 0) {
				responseCode = Integer.parseInt(inputLine.substring(inputLine.indexOf(" ") + 1, inputLine.indexOf(" ") + 4));
				try {
					responseText = inputLine.substring(inputLine.indexOf(" ") + 5);
				} catch (Exception e) {

				}
			} else {
				if (inputLine.length() == 0) {
					break;
				}
				String headerLine = inputLine.substring(0, inputLine.indexOf(": "));
				String valueLine = inputLine.substring(inputLine.indexOf(": ") + 2);
				inHeaders.add(headerLine);
				inValues.add(valueLine);
			}
			c++;
		}
		int length = -1;
		for (int i = 0; i < inHeaders.size(); i++) {
			if (inHeaders.get(i).equalsIgnoreCase("Content-Length")) {
				length = Integer.parseInt(inValues.get(i).trim());
				break;
			}
		}
		byte[] body = new byte[0];

		Response r = new Response();

		r.body = body;
		r.headers.inHeaders = inHeaders;
		r.headers.inValues = inValues;
		r.responseCode = responseCode;
		r.responseText = responseText;
		boolean compression = false;
		if (r.getHeader("Content-Encoding") != null && r.getHeader("Content-Encoding").equals("gzip")) {
			compression = true;
		}
		if (r.getHeader("Transfer-Encoding") != null && r.getHeader("Transfer-Encoding").equals("chunked")) {
			int chunklength = Integer.parseInt(readLine(in), 16);

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			DataInputStream ind = new DataInputStream(in);

			while (chunklength != 0) {
				body = new byte[chunklength];
				ind.readFully(body);

				out.write(body);

				readLine(in);

				chunklength = Integer.parseInt(readLine(in), 16);

			}
			readLine(in);

			r.body = out.toByteArray();
			for (int i = 0; i < inHeaders.size(); i++) {
				if (inHeaders.get(i).equalsIgnoreCase("Transfer-Encoding")) {
					inHeaders.remove(i);
					inValues.remove(i);
					break;
				}
			}
			inHeaders.add("Content-Length");
			inValues.add(r.body.length + "");
		} else if (length == -1) {

		} else {
			body = new byte[length];
			r.body = body;

			DataInputStream ind = new DataInputStream(in);
			ind.readFully(body);

		}
		r.body = (compression && decompressGzip) ? gunzip(r.body) : r.body;

		return r;
	}

	public static Request readRequest(InputStream in) throws IOException {
		String inputLine;
		List<String> inHeaders = new ArrayList<>();
		List<String> inValues = new ArrayList<>();
		String method = "";
		String path = "";
		String version = "";
		int c = 0;
		while ((inputLine = readLine(in)) != null) {
			if (c == 0) {
				String[] parts = inputLine.split(" ");
				if (parts.length >= 3) {
					method = parts[0];

					path = parts[1];
					version = parts[2];
				}
			} else {
				if (inputLine.length() == 0) {
					break;
				}
				String headerLine = inputLine.substring(0, inputLine.indexOf(": "));
				String valueLine = inputLine.substring(inputLine.indexOf(": ") + 2);
				inHeaders.add(headerLine);
				inValues.add(valueLine);
			}
			c++;
		}
		int length = -1;
		for (int i = 0; i < inHeaders.size(); i++) {
			if (inHeaders.get(i).equalsIgnoreCase("Content-Length")) {
				length = Integer.parseInt(inValues.get(i));
				break;
			}
		}

		Request r = new Request();

		r.body = null;
		r.headers.inHeaders = inHeaders;
		r.headers.inValues = inValues;
		r.method = method;
		r.path = path;
		r.version = version;
		if (r.getHeader("Transfer-Encoding") != null && r.getHeader("Transfer-Encoding").equals("chunked")) {
			int chunklength = Integer.parseInt(readLine(in), 16);

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			DataInputStream ind = new DataInputStream(in);
			byte[] body = null;

			while (chunklength != 0) {
				body = new byte[chunklength];
				ind.readFully(body);

				out.write(body);

				readLine(in);

				chunklength = Integer.parseInt(readLine(in), 16);

			}
			readLine(in);

			r.body = out.toByteArray();
			for (int i = 0; i < inHeaders.size(); i++) {
				if (inHeaders.get(i).equalsIgnoreCase("Transfer-Encoding")) {
					inHeaders.remove(i);
					inValues.remove(i);
					break;
				}
			}
			inHeaders.add("Content-Length");
			inValues.add(r.body.length + "");
		} else if (length == -1) {

		} else {
			r.body = new byte[length];
			DataInputStream ind = new DataInputStream(in);
			ind.readFully(r.body);
		}

		return r;
	}

	public static byte[] gzip(byte[] input) {
		GZIPOutputStream gzipOS = null;
		try {
			ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
			gzipOS = new GZIPOutputStream(byteArrayOS);
			gzipOS.write(input);
			gzipOS.flush();
			gzipOS.close();
			return byteArrayOS.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (gzipOS != null) {
				try {
					gzipOS.close();
				} catch (Exception ignored) {
				}
			}
		}
	}

	public static byte[] gunzip(byte[] input) {
		GZIPInputStream gzipOS = null;
		try {
			ByteArrayInputStream byteArrayOS = new ByteArrayInputStream(input);
			gzipOS = new GZIPInputStream(byteArrayOS);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] b = new byte[8192];
			int read = 0;
			while ((read = gzipOS.read(b)) != -1) {
				out.write(b, 0, read);
			}
			gzipOS.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (gzipOS != null) {
				try {
					gzipOS.close();
				} catch (Exception ignored) {
				}
			}
		}
	}
}