package client;

import java.net.Socket;
import java.util.Scanner;

import connection.Connection;
import connection.ConnectionListener;

public class Client implements ConnectionListener
{
	public static final int PORT = 6789;

	public Client(String host, int port)
	{
		try
		{
			Socket socket = new Socket(host, port);
			Connection connection = new Connection(socket, this);
			Scanner scanner = new Scanner(System.in);
			while (true)
			{
				String message = scanner.nextLine();
				if ("/q".equalsIgnoreCase(message))
				{
					System.out.println("quit");
					connection.setRunnable(false);
					connection.send(message);
					break;
				}
				connection.send(message);
			}
			scanner.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new Client("localhost", PORT);
	}

	@Override
	public void processMessage(Connection connection, Object data)
	{
		System.out.println(data);
	}

	@Override
	public void disposeConnection(Connection connection)
	{
		System.out.println("Server shut down");
	}
}
