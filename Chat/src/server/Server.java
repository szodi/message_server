package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import connection.Connection;
import connection.ConnectionListener;

public class Server implements Runnable, ConnectionListener
{
	public static final int PORT = 6789;

	ServerSocket server;
	Set<Connection> connections = new HashSet<>();

	public Server(int port)
	{
		try
		{
			server = new ServerSocket(port);
			new Thread(this).start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Socket socket = server.accept();
				connections.add(new Connection(socket, this));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void processMessage(Connection connection, Object data)
	{
		connections.parallelStream().forEach((c) -> c.send(data));
	}

	@Override
	public void disposeConnection(Connection connection)
	{
		connections.remove(connection);
	}

	public static void main(String[] args)
	{
		new Server(PORT);
	}

}
