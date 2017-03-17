package connection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable
{
	ConnectionListener connectionListener;

	ObjectInputStream ois;
	ObjectOutputStream oos;

	boolean runnable = true;

	public Connection(Socket socket, ConnectionListener connectionListener)
	{
		this.connectionListener = connectionListener;
		try
		{
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new Thread(this).start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void send(Object object)
	{
		try
		{
			oos.writeObject(object);
			oos.reset();
		}
		catch (SocketException e)
		{
			connectionListener.disposeConnection(this);
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
			while (runnable)
			{
				connectionListener.processMessage(this, ois.readObject());
			}
			oos.close();
			ois.close();
		}
		catch (SocketException | EOFException e)
		{
			connectionListener.disposeConnection(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setRunnable(boolean runnable)
	{
		this.runnable = runnable;
	}

	public synchronized void dispose()
	{
		runnable = false;
		send(null);
	}
}
