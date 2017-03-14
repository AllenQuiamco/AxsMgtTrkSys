package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class ReceivingDialog extends Dialog
{
	private Monitoring item;
	protected Object result;
	protected Shell shell;
	private Text txtId;
	private Text txtFolder;
	private Text txtReceivedBy;
	private Text txtReceivedOn;
	private Text txtRequestType;
	private Text txtRequestedBy;
	private Text txtRemarks;
	private Label label;
	private Label label_1;
	private Label label_2;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ReceivingDialog(Shell parent, int style)
	{
		super(parent, style);
		setText("SWT Dialog");
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open()
	{
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		return result;
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents()
	{
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		
		Label lblId = new Label(shell, SWT.NONE);
		lblId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblId.setText("ID");
		
		txtId = new Text(shell, SWT.BORDER);
		txtId.setText("ID");
		txtId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFolder = new Label(shell, SWT.NONE);
		lblFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFolder.setText("Folder");
		
		txtFolder = new Text(shell, SWT.BORDER);
		txtFolder.setText("Folder");
		txtFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblReceivedOn = new Label(shell, SWT.NONE);
		lblReceivedOn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReceivedOn.setText("Received on");
		
		txtReceivedOn = new Text(shell, SWT.BORDER);
		txtReceivedOn.setText("Received on");
		txtReceivedOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblReceivedBy = new Label(shell, SWT.NONE);
		lblReceivedBy.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReceivedBy.setText("Received by");
		
		txtReceivedBy = new Text(shell, SWT.BORDER);
		txtReceivedBy.setText("Received by");
		txtReceivedBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(shell, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type");
		
		txtRequestType = new Text(shell, SWT.BORDER);
		txtRequestType.setText("Request Type");
		txtRequestType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFrom = new Label(shell, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From");
		
		txtRequestedBy = new Text(shell, SWT.BORDER);
		txtRequestedBy.setText("Requested by");
		txtRequestedBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRemarks = new Label(shell, SWT.NONE);
		lblRemarks.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRemarks.setText("Remarks");
		
		txtRemarks = new Text(shell, SWT.BORDER | SWT.MULTI);
		txtRemarks.setText("Remarks");
		txtRemarks.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 4));
		
		label = new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		label_1 = new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		label_2 = new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
	}

	public Monitoring getItem()
	{
		return item;
	}

	public void setItem(Monitoring item)
	{
		this.item = item;
	}	
}
