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
	private ServerSocket server;
	private Set<Connection> connections = new HashSet<>();

	int connectionCountLimit = -1;

	int port;

	public Server(int port)
	{
		this.port = port;
		try
		{
			server = new ServerSocket(port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void start()
	{
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				if (canHaveMoreConnections())
				{
					if (server.isClosed())
					{
						server = new ServerSocket(port);
					}
					Socket socket = server.accept();
					connections.add(new Connection(socket, this));
				}
				else
				{
					server.close();
				}
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

	public final int getConnectionCountLimit()
	{
		return connectionCountLimit;
	}

	public final void setConnectionCountLimit(int connectionCountLimit)
	{
		this.connectionCountLimit = connectionCountLimit;
	}

	public Set<Connection> getConnections()
	{
		return connections;
	}

	public boolean canHaveMoreConnections()
	{
		return connectionCountLimit < 0 || connections.size() < connectionCountLimit;
	}
}
