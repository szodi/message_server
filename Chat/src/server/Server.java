package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import connection.Connection;
import connection.ConnectionListener;

public class Server implements Runnable, ConnectionListener
{
	ServerSocket server;
	List<Connection> connections = new ArrayList<>();

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
				Connection connection = new Connection(socket, this);
				connections.add(connection);
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
		for (Connection con : connections)
		{
			con.send(data);
		}
	}

	@Override
	public void disposeConnection(Connection connection)
	{
		connections.remove(connection);
	}

	public static void main(String[] args)
	{
		new Server(6789);
	}

}
