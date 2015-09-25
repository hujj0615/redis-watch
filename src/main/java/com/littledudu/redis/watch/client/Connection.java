package com.littledudu.redis.watch.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;


/**
 * @author hujinjun
 * @date 2015-9-24 
 */
public class Connection {


//    For Simple Strings the first byte of the reply is "+"
//    For Errors the first byte of the reply is "-"
//    For Integers the first byte of the reply is ":"
//    For Bulk Strings the first byte of the reply is "$"
//    For Arrays the first byte of the reply is "*"

	private static final byte FIRST_BYTE_SIMPLE_STRING = '+';
	private static final byte FIRST_BYTE_ERRORS = '-';
	private static final byte FIRST_BYTE_INTEGER = ':';
	private static final byte FIRST_BYTE_BULK_STRING = '$';
	private static final byte FIRST_BYTE_ARRAY = '*';
	
	private static final int SIMPLE_STRING_MAX_LENGTH = 256;
	private static final int NUMBER_MAX_LENGTH = 20;
	
	private final static byte[] numberToByte = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private final static int[] numberToByteSize = {-1, 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999,999999999, Integer.MAX_VALUE };
	
	private String host;
	private int port;
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int connectionTimeout = 2000;
	private int soTimeout = 2000;

	public Connection() {
		this(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT);
	}

	public Connection(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		init();
	}

	public void init() {
		try {
	        socket = new Socket();
	        socket.setKeepAlive(true);
	        socket.setTcpNoDelay(true);
	        socket.setSoLinger(true, 0);
	        socket.connect(new InetSocketAddress(host, port), connectionTimeout);
	        socket.setSoTimeout(soTimeout);
	        outputStream = new BufferedOutputStream(socket.getOutputStream());
	        inputStream = new BufferedInputStream(socket.getInputStream());
	      } catch (IOException e) {
	        throw new RedisClientException(e);
	      }
	}
	
	/**
	 * A client sends to the Redis server a RESP Array consisting of just Bulk Strings.
	 * @param command
	 * @param args
	 * @throws IOException
	 * 
	 */
	protected void writeCommand(byte[] command, byte[]... args) throws IOException {
		if(args == null) {
			args = new byte[0][0];
		}
		int total = args.length + 1;
		outputStream.write(FIRST_BYTE_ARRAY);
		outputStream.write(numberToDecimalBytes(total));
		writeCRLF();
		writeBulkString(command);
		for(int i = 0; i < args.length; i++) {
			writeBulkString(args[i]);
		}
		outputStream.flush();
	}
	/**
	 * none negative number
	 * @param num
	 * @return
	 */
	private byte[] numberToDecimalBytes(int num) {
		int size = 0;
		for(; size < numberToByteSize.length; size++) {
			if(num > numberToByteSize[size]) {
				
			} else {
				break;
			}
		}
		byte[] buf = new byte[size];
		for(int i = size - 1; i >= 0; i--) {
			int lastNumber = num % 10;
			buf[i] = numberToByte[lastNumber];
			num = num/10;
		}
		return buf;
	}
	/**
	 * example string:
	 * $4\r\n
	 * LLEN\r\n
	 * @param string
	 * @throws IOException
	 */
	protected void writeBulkString(byte[] string) throws IOException {
		outputStream.write(FIRST_BYTE_BULK_STRING);
		outputStream.write(numberToDecimalBytes(string.length));
		writeCRLF();
		outputStream.write(string);
		writeCRLF();
	}
	protected void writeCRLF() throws IOException {
		outputStream.write('\r');
		outputStream.write('\n');
	}
	
	private void read() throws IOException {
		int firstByte = inputStream.read();
		if(firstByte == -1) {
			throw new RedisClientException("read end of stream");
		} else if((firstByte & 0xFF) == FIRST_BYTE_ARRAY) {
			readRESPArray();
		}
	}
	
	private void readRESPArray() throws IOException {
		//read array length
		int length = (int) readNumber();
		if(length == -1) {
			//null array
		} else if(length == 0) {
			//no further reading
		} else {
			while(length-- > 0) {
				byte typeByte = (byte) inputStream.read();
				switch(typeByte) {
				case FIRST_BYTE_SIMPLE_STRING:
					byte[] simpleString = readSimpleString();
					break;
				case FIRST_BYTE_ERRORS:
					byte[] errMsg = readErrorString();
					break;
				case FIRST_BYTE_INTEGER:
					long num = readNumber();
					break;
				case FIRST_BYTE_BULK_STRING:
					byte[] buldString = readBulkString(true);
					break;
					//TODO array contains another array is possible
					default: throw new RedisClientException("unexpected byte: " + typeByte);
				}
			}
		}
	}
	protected long readNumber() throws IOException {
		int pos = 0;
		long num = 0;
		boolean negative = false;
		boolean firstbyte = true;
		int b = inputStream.read();
		while(((b & 0xFF) != '\r') && b != -1) {
			if(pos >= NUMBER_MAX_LENGTH) {
				throw new RedisClientException("number exceeds max length: " + NUMBER_MAX_LENGTH);
			}
			if(firstbyte) {
				if((b & 0xFF) == '-') {
					negative = true;
					b = inputStream.read();
					continue;
				}
				firstbyte = false;
			}
			pos++;
			num = num * 10 + ((b & 0xFF) - 48);
			b = inputStream.read();
		}
		if(b == -1)
			throw new RedisClientException("expected CRLF but read end of stream");
		
		inputStream.read();//suppose it is '\n'
		
		return negative ? (-num) : num;
	}
	protected byte[] readBulkString(boolean hasReadFirstByte) throws IOException {
		if( ! hasReadFirstByte) {
			int i = inputStream.read();
			if(i != FIRST_BYTE_BULK_STRING) {
				if(i == FIRST_BYTE_ERRORS) {
					String errMsg = new String(readErrorString());
					throw new RedisClientException("server return err msg: " + errMsg);
				} else {
					throw new RedisClientException("expected bulk string byte but not: " + i);
				}
			}
		}
		int length = (int) readNumber();
		if(length < 0) {
			//Null Bulk String, length should equals to -1
			return null;
		} else if(length == 0) {
			//empty bulk string
			readCRLF();
			return new byte[0];
		} else {
			return readBytesAndCRLF(length);
		}
	}
	protected byte[] readErrorString() throws IOException {
		return readSimpleString();
	}
	protected byte[] readSimpleString() throws IOException {
		byte[] buf = new byte[SIMPLE_STRING_MAX_LENGTH];
		int pos = 0;
		int b = inputStream.read();
		while(((b & 0xFF) != '\r') && b != -1) {
			if(pos >= SIMPLE_STRING_MAX_LENGTH) {
				throw new RedisClientException("simple string exceeds max length: " + SIMPLE_STRING_MAX_LENGTH);
			}
			buf[pos++] = (byte) b;
			b = inputStream.read();
		}
		if(b == -1)
			throw new RedisClientException("expected CRLF but read end of stream");
		inputStream.read();//suppose it is '\n'
		return Arrays.copyOfRange(buf, 0, pos);
	}
	protected byte[] readBytesAndCRLF(int len) throws IOException {
		byte[] buf = new byte[len];
		int cnt = inputStream.read(buf);
		if(cnt == -1)
			throw new RedisClientException("read end of stream");
		if(cnt < len) {
			throw new RedisClientException(String.format("expect %d bytes but actually read %d bytes", len, cnt));
		}
		
		//read CRLF
		readCRLF();
		
		return buf;
	}
	protected void readCRLF() throws IOException {
		byte[] crlf = new byte[2];
		int cnt = inputStream.read(crlf);
		if(cnt == -1)
			throw new RedisClientException("read end of stream");
		if(cnt < 2) {
			throw new RedisClientException(String.format("expect 2 bytes but actually read %d bytes", cnt));
		}
		if(crlf[0] == '\r' && crlf[1] == '\n') {
			//correct
		} else {
			throw new RedisClientException(String.format("expected CRLF but actually not: %d, %d", crlf[0], crlf[1]));
		}
	}
}
