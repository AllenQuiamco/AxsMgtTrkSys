package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBox;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxButtons;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxIcon;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxResult;

public class ProcessingDialog extends Dialog
{
	private static final String[] statusSelection = new String[] { "PROCESSED" };
	
	private Monitoring item;
	private Monitoring itemEditable;
	protected boolean result;
	protected Shell shell;
	private Composite cmpSave;
	private Button btnSave;
	private Composite cmpDetails;
	private Text txtId;
	private Text txtType;
	private Text txtFrom;
	private DateTime dtwAssignedOnDate;
	private DateTime dtwAssignedOnTime;
	private Text txtAssignedBy;
	private Combo cbxStatus;
	private Label lblStatusAsterisk;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ProcessingDialog(Shell parent, int style)
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
		shell.setText("Assignment Details");
		shell.setLayout(new FormLayout());
		
		cmpSave = new Composite(shell, SWT.NONE);
		FormData fd_cmpSave = new FormData();
		fd_cmpSave.bottom = new FormAttachment(100);
		fd_cmpSave.left = new FormAttachment(50, 0 - (cmpSave.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2));
		cmpSave.setLayoutData(fd_cmpSave);
		cmpSave.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		btnSave = new Button(cmpSave, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				result = true;
				shell.close();
			}
		});
		btnSave.setText("Save");
		
		cmpDetails = new Composite(shell, SWT.NONE);
		cmpDetails.setLayout(new FormLayout());
		FormData fd_cmpDetails = new FormData();
		fd_cmpDetails.bottom = new FormAttachment(cmpSave);
		fd_cmpDetails.left = new FormAttachment(0);
		fd_cmpDetails.right = new FormAttachment(100, -6);
		fd_cmpDetails.top = new FormAttachment(0);
		cmpDetails.setLayoutData(fd_cmpDetails);
		
		Label lblId = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblId = new FormData();
		fd_lblId.top = new FormAttachment(0, 8);
		fd_lblId.left = new FormAttachment(0, 58);
		lblId.setLayoutData(fd_lblId);
		lblId.setText("ID");
		
		txtId = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtId = new FormData();
		fd_txtId.right = new FormAttachment(100);
		fd_txtId.top = new FormAttachment(0, 5);
		fd_txtId.left = new FormAttachment(0, 74);
		txtId.setLayoutData(fd_txtId);
		txtId.setText("ID");
		
		txtType = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtType = new FormData();
		fd_txtType.right = new FormAttachment(100);
		fd_txtType.top = new FormAttachment(0, 31);
		fd_txtType.left = new FormAttachment(0, 74);
		txtType.setLayoutData(fd_txtType);
		txtType.setText("Type");
		
		Label lblType = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblType = new FormData();
		fd_lblType.top = new FormAttachment(0, 34);
		fd_lblType.right = new FormAttachment(txtType, -4);
		lblType.setLayoutData(fd_lblType);
		lblType.setText("Request");
		
		txtFrom = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtFrom = new FormData();
		fd_txtFrom.right = new FormAttachment(100);
		fd_txtFrom.top = new FormAttachment(0, 57);
		fd_txtFrom.left = new FormAttachment(0, 74);
		txtFrom.setLayoutData(fd_txtFrom);
		txtFrom.setText("From");
		
		Label lblFrom = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblFrom = new FormData();
		fd_lblFrom.top = new FormAttachment(0, 60);
		fd_lblFrom.right = new FormAttachment(txtType, -4);
		lblFrom.setLayoutData(fd_lblFrom);
		lblFrom.setText("From");
		
		Label lblAssignedOn = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblAssignedOn = new FormData();
		fd_lblAssignedOn.top = new FormAttachment(0, 86);
		fd_lblAssignedOn.left = new FormAttachment(0, 5);
		lblAssignedOn.setLayoutData(fd_lblAssignedOn);
		lblAssignedOn.setText("Assigned on");
		
		Label lblAssignedOnSub = new Label(cmpDetails, SWT.NONE);
		lblAssignedOnSub.setFont(SWTResourceManager.getFont("Segoe UI", 6, SWT.BOLD));
		FormData fd_lblAssignedOnSub = new FormData();
		fd_lblAssignedOnSub.right = new FormAttachment(lblAssignedOn, lblAssignedOn.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		fd_lblAssignedOnSub.top = new FormAttachment(lblAssignedOn, 0);
		lblAssignedOnSub.setLayoutData(fd_lblAssignedOnSub);
		lblAssignedOnSub.setText("( m / d / yyyy )");
		
		dtwAssignedOnDate = new DateTime(cmpDetails, SWT.BORDER);
		dtwAssignedOnDate.setEnabled(false);
		FormData fd_dtwAssignedOnDate = new FormData();
		fd_dtwAssignedOnDate.right = new FormAttachment(0, 174);
		fd_dtwAssignedOnDate.top = new FormAttachment(0, 82);
		fd_dtwAssignedOnDate.left = new FormAttachment(0, 74);
		dtwAssignedOnDate.setLayoutData(fd_dtwAssignedOnDate);
		
		dtwAssignedOnTime = new DateTime(cmpDetails, SWT.BORDER | SWT.TIME);
		dtwAssignedOnTime.setEnabled(false);
		FormData fd_dtwAssignedOnTime = new FormData();
		fd_dtwAssignedOnTime.left = new FormAttachment(0, 177);
		fd_dtwAssignedOnTime.right = new FormAttachment(0, 277);
		fd_dtwAssignedOnTime.top = new FormAttachment(0, 82);
		dtwAssignedOnTime.setLayoutData(fd_dtwAssignedOnTime);
		
		Label lblAssignedBy = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblAssignedBy = new FormData();
		fd_lblAssignedBy.top = new FormAttachment(0, 112);
		fd_lblAssignedBy.left = new FormAttachment(0, 6);
		lblAssignedBy.setLayoutData(fd_lblAssignedBy);
		lblAssignedBy.setText("Assigned by");
		
		txtAssignedBy = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtAssignedBy = new FormData();
		fd_txtAssignedBy.right = new FormAttachment(100);
		fd_txtAssignedBy.top = new FormAttachment(0, 109);
		fd_txtAssignedBy.left = new FormAttachment(0, 74);
		txtAssignedBy.setLayoutData(fd_txtAssignedBy);
		txtAssignedBy.setText("Assigned by");
		
		cbxStatus = new Combo(cmpDetails, SWT.READ_ONLY);
		cbxStatus.setItems(statusSelection);
		FormData fd_cbxStatus = new FormData();
		fd_cbxStatus.top = new FormAttachment(txtAssignedBy, 4);
		fd_cbxStatus.left = new FormAttachment(txtId, 0, SWT.LEFT);
		cbxStatus.setLayoutData(fd_cbxStatus);
		cbxStatus.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				MsgBoxResult dialogResult = MsgBox.show(shell, String.format("Are you sure you want to set this item as PROCESSED?%n%nThis may not be changed once set."), "Continue?", MsgBoxButtons.YES_NO, MsgBoxIcon.WARNING);
				if (dialogResult.isYes())
				{
					getParent().getDisplay().asyncExec(new Runnable()
					{
						@Override
						public void run()
						{
							if (Utilities.equals(item.getStatus(), itemEditable.getStatus()))
							{
								lblStatusAsterisk.setText("");
							}
							else
							{
								lblStatusAsterisk.setText("*");
							}
						}
					});
				}
			}
		});
		
		Label lblStatus = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblStatus = new FormData();
		fd_lblStatus.top = new FormAttachment(0, 138);
		fd_lblStatus.right = new FormAttachment(cbxStatus, -4);
		lblStatus.setLayoutData(fd_lblStatus);
		lblStatus.setText("Set Status");
		
		lblStatusAsterisk = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblStatusAsterisk = new FormData();
		fd_lblStatusAsterisk.top = new FormAttachment(0, 138);
		fd_lblStatusAsterisk.left = new FormAttachment(cbxStatus, 2);
		lblStatusAsterisk.setLayoutData(fd_lblStatusAsterisk);
		lblStatusAsterisk.setText("  ");
	}
	
}
