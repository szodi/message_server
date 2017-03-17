package client;

import java.net.Socket;
import java.util.Scanner;

import common.User;
import connection.Connection;
import connection.ConnectionListener;

public class ConsoleClient implements ConnectionListener
{
	public static final int PORT = 6789;
	User user = new User("Bill");

	public ConsoleClient(String host, int port)
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
					connection.dispose();
					break;
				}
				user.setMessage(message);
				connection.send(user);
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
		new ConsoleClient("localhost", PORT);
	}

	@Override
	public void processMessage(Connection connection, Object data)
	{
		if ((data != null) && (data instanceof User))
		{
			User user = (User) data;
			System.out.println("[" + user.getName() + "] " + user.getMessage());
		}
	}

	@Override
	public void disposeConnection(Connection connection)
	{
		System.out.println("Server is down");
	}
}
