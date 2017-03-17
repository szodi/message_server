package client;

import java.net.SocketException;
import java.util.Scanner;

import connection.Connection;
import connection.ConnectionFactory;
import connection.ConnectionListener;

public class ConsoleClient implements ConnectionListener
{
	public static final int PORT = 6789;
	static User user = null;

	public ConsoleClient(String host, int port)
	{
		try
		{
			Connection connection = ConnectionFactory.getConnection(host, port, this);
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
		catch (SocketException e)
		{
			System.out.println("Connection refused by the server. (Maybe full)");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			user = new User(args[0]);
		}
		else
		{
			user = new User("User");
		}
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
