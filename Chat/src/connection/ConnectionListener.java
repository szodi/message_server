package connection;

public interface ConnectionListener
{
	void processMessage(Connection connection, Object data);

	void disposeConnection(Connection connection);
}
