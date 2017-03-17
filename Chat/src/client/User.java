package client;

import java.io.Serializable;

public class User implements Serializable
{
	private static final long serialVersionUID = 2831552085796194705L;

	String name;
	String message = "";

	public User(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "User [name=" + name + ", message=" + message + "]";
	}
}
