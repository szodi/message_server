package gui;

import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import common.User;
import connection.Connection;
import connection.ConnectionListener;

public class ChatGUI implements ConnectionListener
{
	public static final int PORT = 6789;

	protected Shell shell;
	private Text history;
	private Text message;

	private Connection connection;

	String msgHistory = "";

	User user = new User("Bill");

	public ChatGUI(String host, int port)
	{
		try
		{
			Socket socket = new Socket(host, port);
			connection = new Connection(socket, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		try
		{
			ChatGUI window = new ChatGUI("localhost", PORT);
			window.open();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		shell = new Shell();
		shell.addDisposeListener(new DisposeListener()
		{
			@Override
			public void widgetDisposed(DisposeEvent arg0)
			{
				connection.dispose();
			}
		});
		shell.setSize(450, 300);
		shell.setText(user.getName());

		history = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		history.setEditable(false);
		history.setBounds(10, 10, 414, 215);

		message = new Text(shell, SWT.BORDER);
		message.addTraverseListener(new TraverseListener()
		{
			@Override
			public void keyTraversed(TraverseEvent event)
			{
				if (event.detail == SWT.TRAVERSE_RETURN && !"".equals(message.getText()))
				{
					user.setMessage(message.getText());
					connection.send(user);
				}
			}
		});
		message.setBounds(10, 230, 333, 26);
		message.setFocus();

		Button btnSend = new Button(shell, SWT.NONE);
		btnSend.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDown(MouseEvent e)
			{
				user.setMessage(message.getText());
				connection.send(user);
			}
		});
		btnSend.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				user.setMessage(message.getText());
				connection.send(user);
			}
		});
		btnSend.setBounds(349, 231, 75, 25);
		btnSend.setText("Send");

	}

	private void appendMessage(String msg)
	{
		msgHistory += msg + '\n';
		shell.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				history.setText(msgHistory);
				message.setText("");
			}
		});
	}

	@Override
	public void processMessage(Connection connection, Object data)
	{
		if (data != null)
		{
			if (data instanceof User)
			{
				User user = (User) data;
				appendMessage("[" + user.getName() + "] " + user.getMessage());
			}

		}
	}

	@Override
	public void disposeConnection(Connection connection)
	{
		appendMessage("Server is down !");
	}
}
