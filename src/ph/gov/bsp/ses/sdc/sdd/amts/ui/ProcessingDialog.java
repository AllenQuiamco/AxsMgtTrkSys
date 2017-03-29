package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.sql.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
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

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Common;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Log;
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
	private Text txtRemarks;
	private Hashtable<String, Color> defaultColors = new Hashtable<String, Color>();
	
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
	public boolean open()
	{
		createContents();
		setWindowState(Program.getSetting("ui.processingdialog.windowstate"));
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
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shell.setImage(SWTResourceManager.getImage(AssignmentDialog.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/java-16x16-32bit.png"));
		shell.setMinimumSize(new Point(450, 326));
		shell.setSize(450, 326);
		shell.setText("Processing Details");
		shell.setLayout(new FormLayout());
		shell.addShellListener(new ShellAdapter()
		{
			@Override
			public void shellClosed(ShellEvent e)
			{
				String windowState = getWindowState();
				Program.setSetting("ui.processingdialog.windowstate", windowState);
			}
		});
		
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
				if (!itemEditable.getRemarks().startsWith(item.getRemarks()))
				{
					MsgBoxResult r = MsgBox.show(shell,
							String.format("You have edited a previous remark.%n%nAre you sure you want proceed with the changes?"),
							"Confirm",
							MsgBoxButtons.YES_NO,
							MsgBoxIcon.WARNING);
					
					if (!r.isYes()) return;
				}
				
				result = true;
				shell.close();
			}
		});
		btnSave.setText("Update");
		
		cmpDetails = new Composite(shell, SWT.NONE);
		cmpDetails.setLayout(new FormLayout());
		FormData fd_cmpDetails = new FormData();
		fd_cmpDetails.bottom = new FormAttachment(cmpSave);
		fd_cmpDetails.left = new FormAttachment(0);
		fd_cmpDetails.right = new FormAttachment(100, -6);
		fd_cmpDetails.top = new FormAttachment(0);
		cmpDetails.setLayoutData(fd_cmpDetails);
		
		txtId = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtId = new FormData();
		fd_txtId.right = new FormAttachment(100);
		fd_txtId.top = new FormAttachment(0, 5);
		fd_txtId.left = new FormAttachment(0, 80);
		txtId.setLayoutData(fd_txtId);
		txtId.setText("ID");
		
		Label lblId = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblId = new FormData();
		fd_lblId.top = new FormAttachment(0, 8);
		fd_lblId.right = new FormAttachment(txtId, -5);
		lblId.setLayoutData(fd_lblId);
		lblId.setText("ID");
		
		txtType = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtType = new FormData();
		fd_txtType.right = new FormAttachment(100);
		fd_txtType.top = new FormAttachment(0, 31);
		fd_txtType.left = new FormAttachment(0, 80);
		txtType.setLayoutData(fd_txtType);
		txtType.setText("Type");
		
		Label lblType = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblType = new FormData();
		fd_lblType.top = new FormAttachment(0, 34);
		fd_lblType.right = new FormAttachment(txtType, -5);
		lblType.setLayoutData(fd_lblType);
		lblType.setText("Request type");
		
		txtFrom = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtFrom = new FormData();
		fd_txtFrom.right = new FormAttachment(100);
		fd_txtFrom.top = new FormAttachment(0, 57);
		fd_txtFrom.left = new FormAttachment(0, 80);
		txtFrom.setLayoutData(fd_txtFrom);
		txtFrom.setText("From");
		
		Label lblFrom = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblFrom = new FormData();
		fd_lblFrom.top = new FormAttachment(0, 60);
		fd_lblFrom.right = new FormAttachment(txtType, -5);
		lblFrom.setLayoutData(fd_lblFrom);
		lblFrom.setText("From");
		
		dtwAssignedOnDate = new DateTime(cmpDetails, SWT.BORDER);
		dtwAssignedOnDate.setEnabled(false);
		FormData fd_dtwAssignedOnDate = new FormData();
		fd_dtwAssignedOnDate.right = new FormAttachment(0, 180);
		fd_dtwAssignedOnDate.top = new FormAttachment(0, 82);
		fd_dtwAssignedOnDate.left = new FormAttachment(0, 80);
		dtwAssignedOnDate.setLayoutData(fd_dtwAssignedOnDate);
		
		dtwAssignedOnTime = new DateTime(cmpDetails, SWT.BORDER | SWT.TIME);
		dtwAssignedOnTime.setEnabled(false);
		FormData fd_dtwAssignedOnTime = new FormData();
		fd_dtwAssignedOnTime.left = new FormAttachment(dtwAssignedOnDate, 3);
		fd_dtwAssignedOnTime.right = new FormAttachment(dtwAssignedOnDate, 103, SWT.RIGHT);
		fd_dtwAssignedOnTime.top = new FormAttachment(0, 82);
		dtwAssignedOnTime.setLayoutData(fd_dtwAssignedOnTime);
		
		Label lblAssignedOn = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblAssignedOn = new FormData();
		fd_lblAssignedOn.top = new FormAttachment(0, 86);
		fd_lblAssignedOn.right = new FormAttachment(dtwAssignedOnDate, -5);
		lblAssignedOn.setLayoutData(fd_lblAssignedOn);
		lblAssignedOn.setText("Assigned on");
		
		Label lblAssignedOnSub = new Label(cmpDetails, SWT.NONE);
		lblAssignedOnSub.setFont(SWTResourceManager.getFont("Segoe UI", 6, SWT.BOLD));
		FormData fd_lblAssignedOnSub = new FormData();
		fd_lblAssignedOnSub.right = new FormAttachment(lblAssignedOn, lblAssignedOn.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		fd_lblAssignedOnSub.top = new FormAttachment(lblAssignedOn, 0);
		lblAssignedOnSub.setLayoutData(fd_lblAssignedOnSub);
		lblAssignedOnSub.setText("( m / d / yyyy )");
		
		txtAssignedBy = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtAssignedBy = new FormData();
		fd_txtAssignedBy.right = new FormAttachment(100);
		fd_txtAssignedBy.top = new FormAttachment(0, 109);
		fd_txtAssignedBy.left = new FormAttachment(0, 80);
		txtAssignedBy.setLayoutData(fd_txtAssignedBy);
		txtAssignedBy.setText("Assigned by");
		
		Label lblAssignedBy = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblAssignedBy = new FormData();
		fd_lblAssignedBy.top = new FormAttachment(0, 112);
		fd_lblAssignedBy.right = new FormAttachment(txtAssignedBy, -5);
		lblAssignedBy.setLayoutData(fd_lblAssignedBy);
		lblAssignedBy.setText("Assigned by");
		
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
				if (cbxStatus.getText().equals("PROCESSED"))
				{
					if (item.getStatus().equals("APPROVED"))
					{
						MsgBoxResult dialogResult = MsgBox.show(shell, String.format("Are you sure you want to set this item as PROCESSED?%n%nThis may not be changed once set."), "Continue?", MsgBoxButtons.YES_NO, MsgBoxIcon.WARNING);
						if (dialogResult.isYes())
						{
							getParent().getDisplay().syncExec(new Runnable()
							{
								@Override
								public void run()
								{
									lblStatusAsterisk.setText("*");
								}
							});
						}
						else
						{
							getParent().getDisplay().syncExec(new Runnable()
							{
								@Override
								public void run()
								{
									cbxStatus.deselectAll();
								}
							});
						}
					}
					else
					{
						MsgBox.show(shell, "This item is not APPROVED.", "Invalid selection", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
						getParent().getDisplay().syncExec(new Runnable()
						{
							@Override
							public void run()
							{
								cbxStatus.deselectAll();
							}
						});
					}
				}
			}
		});
		
		Label lblStatus = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblStatus = new FormData();
		fd_lblStatus.top = new FormAttachment(0, 138);
		fd_lblStatus.right = new FormAttachment(cbxStatus, -5);
		lblStatus.setLayoutData(fd_lblStatus);
		lblStatus.setText("Set Status");
		
		lblStatusAsterisk = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblStatusAsterisk = new FormData();
		fd_lblStatusAsterisk.top = new FormAttachment(0, 138);
		fd_lblStatusAsterisk.left = new FormAttachment(cbxStatus, 2);
		lblStatusAsterisk.setLayoutData(fd_lblStatusAsterisk);
		lblStatusAsterisk.setText("  ");
		
		Button btnDownload = new Button(cmpDetails, SWT.NONE);
		btnDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.createLocalCopy(shell, itemEditable.getId());
			}
		});
		FormData fd_btnDownload = new FormData();
		fd_btnDownload.top = new FormAttachment(txtAssignedBy, 3);
		fd_btnDownload.right = new FormAttachment(100, 0);
		btnDownload.setLayoutData(fd_btnDownload);
		btnDownload.setText("Download files");
		
		txtRemarks = new Text(cmpDetails, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_txtRemarks = new FormData();
		fd_txtRemarks.bottom = new FormAttachment(100);
		fd_txtRemarks.right = new FormAttachment(100);
		fd_txtRemarks.top = new FormAttachment(0, 161);
		fd_txtRemarks.left = new FormAttachment(0, 80);
		txtRemarks.setLayoutData(fd_txtRemarks);
		txtRemarks.setText("Remarks\r\n2\r\n3\r\n4\r\n5\r\n6");
		this.defaultColors.put("txtRemarks.background", txtRemarks.getBackground());
		txtRemarks.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				getParent().getDisplay().asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						if (Utilities.equals(item.getRemarks(), itemEditable.getRemarks()))
						{
							txtRemarks.setBackground(getDefaultColor("txtRemarks.background"));
						}
						else
						{
							txtRemarks.setBackground(getColor_BG_CHANGED_SETTING());
						}
					}
				});
			}
		});
		
		Label lblRemarks = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblRemarks = new FormData();
		fd_lblRemarks.top = new FormAttachment(0, 164);
		fd_lblRemarks.right = new FormAttachment(txtRemarks, -5);
		lblRemarks.setLayoutData(fd_lblRemarks);
		lblRemarks.setText("Remarks");
		
		initDataBindings();
		
		// TODO EnteredOn
	}
	
	protected DataBindingContext initDataBindings() 
	{
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtIdObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtId);
		IObservableValue iDItemEditableObserveValue = PojoProperties.value("id").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtIdObserveWidget, iDItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtTypeObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtType);
		IObservableValue requestTypeItemEditableObserveValue = PojoProperties.value("requestType").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtTypeObserveWidget, requestTypeItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtFromObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtFrom);
		IObservableValue requestedByItemEditableObserveValue = PojoProperties.value("requestedBy").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtFromObserveWidget, requestedByItemEditableObserveValue, null, null);
		//
		IObservableValue observeSelectionDtwAssignedOnDateObserveWidget = WidgetProperties.selection().observe(dtwAssignedOnDate);
		IObservableValue assignedOnItemEditableObserveValue = PojoProperties.value("assignedOn").observe(itemEditable);
		bindingContext.bindValue(observeSelectionDtwAssignedOnDateObserveWidget, assignedOnItemEditableObserveValue, null, null);
		//
		IObservableValue observeSelectionDtwAssignedOnTimeObserveWidget = WidgetProperties.selection().observe(dtwAssignedOnTime);
		bindingContext.bindValue(observeSelectionDtwAssignedOnTimeObserveWidget, assignedOnItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtAssignedByObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtAssignedBy);
		IObservableValue assignedByItemEditableObserveValue = PojoProperties.value("assignedBy").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtAssignedByObserveWidget, assignedByItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextCbxStatusObserveWidget = WidgetProperties.text().observe(cbxStatus);
		IObservableValue statusItemEditableObserveValue = PojoProperties.value("status").observe(itemEditable);
		bindingContext.bindValue(observeTextCbxStatusObserveWidget, statusItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtRemarksObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtRemarks);
		IObservableValue remarksItemEditableObserveValue = PojoProperties.value("remarks").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtRemarksObserveWidget, remarksItemEditableObserveValue, null, null);
		//
		return bindingContext;
	}
	
	public Monitoring getItem()
	{
		return item;
	}
	
	public void setItem(Monitoring item)
	{
		this.item = item;
		this.itemEditable = item.clone();
	}
	
	public Monitoring getEditedItem()
	{
		return this.itemEditable;
	}
	
	public List<Log> getChangeLog()
	{
		List<Log> logs = new LinkedList<Log>();
		
		Log log = null;
		
		if (!Utilities.isNullOrBlank(itemEditable.getStatus()))
		{
			if (!Utilities.equals(item.getStatus(), itemEditable.getStatus()))
			{
				log = new Log();
				log.setAction("update");
				log.setTableName("MONITORING");
				log.setFieldName("Status");
				log.setRowId(item.getId());
				log.setOldValue(item.getStatus());
				log.setNewValue(itemEditable.getStatus());
				logs.add(log);
				
				String status = itemEditable.getStatus();
				
				if (status.equals("PROCESSED"))
				{
					if (!Utilities.equals(item.getAssignedTo(), itemEditable.getAssignedTo()))
					{
						log = new Log();
						log.setAction("update");
						log.setTableName("MONITORING");
						log.setFieldName("ProcessedBy");
						log.setRowId(item.getId());
						log.setOldValue(item.getProcessedBy());
						log.setNewValue(Program.USER); // XXX Direct reference to Program.USER
						logs.add(log);
						
						Date date = new Date(System.currentTimeMillis());
						
						log = new Log();
						log.setAction("update");
						log.setTableName("MONITORING");
						log.setFieldName("ProcessedOn");
						log.setRowId(item.getId());
						log.setOldValue(Common.morphDate(item.getProcessedOn()));
						log.setNewValue(Common.morphDate(date));
						logs.add(log);
						
						log = new Log();
						log.setAction("update");
						log.setTableName("MONITORING");
						log.setFieldName("ResolvedOn");
						log.setRowId(item.getId());
						log.setOldValue(Common.morphDate(item.getResolvedOn()));
						log.setNewValue(Common.morphDate(date));
						logs.add(log);
					}
				}
			}
		}
		
		if (!Utilities.equals(item.getRemarks(), itemEditable.getRemarks()))
		{
			log = new Log();
			log.setAction("update");
			log.setTableName("MONITORING");
			log.setFieldName("Remarks");
			log.setRowId(item.getId());
			log.setOldValue(item.getRemarks());
			log.setNewValue(itemEditable.getRemarks());
			logs.add(log);
		}
		
		return logs;
	}
	
	private Color getColor_BG_CHANGED_SETTING()
	{
		return new Color(this.getParent().getDisplay(), 255, 255, 225);
	}
	
	private Color getDefaultColor(String element)
	{
		return this.defaultColors.get(element);
	}

	public void setWindowState(String string)
	{
		if (Utilities.isNullOrBlank(string)) return;
		
		String[] split = string.trim().split("\\s+");
		String state = Utilities.getArrayItem(split, 0); if (state == null) state = ""; state = state.toUpperCase();
		String posX = Utilities.getArrayItem(split, 1);
		String posY = Utilities.getArrayItem(split, 2);
		String sizeX = Utilities.getArrayItem(split, 3);
		String sizeY = Utilities.getArrayItem(split, 4);
		
		if (state.equals("MAXIMIZED"))
		{
			shell.setMaximized(true);
			//shell.setMinimized(false);
		}
		else if (state.equals("RESTORED"))
		{
			shell.setMaximized(false);
			shell.setMinimized(false);
			
			if ((posX != null) && (posY != null))
			{
				try
				{
					int x = Integer.parseInt(posX);
					int y = Integer.parseInt(posY);
					
					if (x < 0) x = 0;
					if (y < 0) y = 0;
					
					shell.setLocation(new Point(x, y));
				}
				catch (NumberFormatException e) { }
			}
			
			if ((sizeX != null) && (sizeY != null))
			{
				try
				{
					int x = Integer.parseInt(sizeX);
					int y = Integer.parseInt(sizeY);
					
					if (x < 0) x = 0;
					if (y < 0) y = 0;
					
					shell.setSize(x, y);
				}
				catch (NumberFormatException e) { }
			}
		}
	}
	
	public String getWindowState()
	{
		boolean max = shell.getMaximized();
		boolean min = shell.getMinimized();
		Point location = shell.getLocation();
		Point size = shell.getSize();
		String state = "";
		if (max && min) state = "MAXIMIZED";
		else if(max && !min) state = "MAXIMIZED";
		else if(!max && min) state = "RESTORED";
		else state = "RESTORED";
		return String.format("%s %d %d %d %d", state, location.x, location.y, size.x, size.y);
	}
}
