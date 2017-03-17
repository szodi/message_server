package connection;

import java.net.Socket;
import java.net.SocketException;

import connection.Connection;
import connection.ConnectionListener;

public class ConnectionFactory
{
	public static Connection getConnection(String host, int port, ConnectionListener connectionListener) throws SocketException
	{
		try
		{
			Socket socket = new Socket(host, port);
			return new Connection(socket, connectionListener);
		}
		catch (SocketException e)
		{
			throw new SocketException("Connection refused by the server. (Maybe full)");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
