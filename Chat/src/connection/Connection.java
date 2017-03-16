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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	public void send(Object object)
	{
		try
		{
			oos.writeObject(object);
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
				Object object = ois.readObject();
				if (object != null)
				{
					connectionListener.processMessage(this, object);
				}
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

	public void setSocketListener(ConnectionListener connectionListener)
	{
		this.connectionListener = connectionListener;
	}

	public boolean isRunnable()
	{
		return runnable;
	}

	public void setRunnable(boolean runnable)
	{
		this.runnable = runnable;
	}
}
