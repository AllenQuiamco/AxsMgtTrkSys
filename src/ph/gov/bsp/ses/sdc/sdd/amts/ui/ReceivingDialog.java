package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Common;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Log;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBox;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxButtons;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxIcon;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxResult;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public class ReceivingDialog extends Dialog
{
	private Monitoring item;
	private Monitoring itemEditable;
	protected boolean result;
	protected Shell shell;
	private Text txtId;
	private Text txtFolder;
	private Text txtReceivedBy;
	private DateTime dtwReceivedOnDate;
	private DateTime dtwReceivedOnTime;
	private Text txtRequestType;
	private Text txtRequestedBy;
	private Button btnSave;
	private Text txtRemarks;
	private Composite cmpSave;
	private Composite cmpDetails;
	private Hashtable<String, Color> defaultColors = new Hashtable<String, Color>();
	
	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		final ReceivingDialog dialog = new ReceivingDialog(shell, SWT.NONE);
		
		Monitoring item = Monitoring.getSamples(1).getFirst();
		dialog.setItem(item);
		
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable()
		{
			@Override
			public void run()
			{
				dialog.open();
			}
		});		
	}
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ReceivingDialog(Shell parent, int style)
	{
		super(parent, style);
		setText("SWT Dialog");
		result = false;
	}
	
	/**
	 * Open the dialog. If the Save button is clicked, the return value is 
	 * <code>true</code>. If t
	 * @return the result
	 */
	public boolean open()
	{
		createContents();
		setWindowState(Program.getSetting("ui.receivingdialog.windowstate"));
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
		shell.setImage(SWTResourceManager.getImage(ReceivingDialog.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/java-16x16-32bit.png"));
		shell.setMinimumSize(new Point(450, 326));
		shell.setSize(450, 326);
		shell.setText("Receiving Details");
		shell.setLayout(new FormLayout());
		shell.addShellListener(new ShellAdapter()
		{
			@Override
			public void shellClosed(ShellEvent e)
			{
				String windowState = getWindowState();
				Program.setSetting("ui.receivingdialog.windowstate", windowState);
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
				String oldRemarks = item.getRemarks();
				String newRemarks = itemEditable.getRemarks();
				if (!Utilities.isNullOrBlank(oldRemarks))
				{
					if (Utilities.isNullOrBlank(newRemarks) || !newRemarks.startsWith(oldRemarks))
					{
						MsgBoxResult r = MsgBox.show(shell,
								String.format("You have edited a previous remark.%n%nAre you sure you want proceed with the changes?"),
								"Confirm",
								MsgBoxButtons.YES_NO,
								MsgBoxIcon.WARNING);
						
						if (!r.isYes()) return;
					}
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
		
		txtFolder = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtFolder = new FormData();
		fd_txtFolder.right = new FormAttachment(100);
		fd_txtFolder.top = new FormAttachment(0, 31);
		fd_txtFolder.left = new FormAttachment(0, 80);
		txtFolder.setLayoutData(fd_txtFolder);
		txtFolder.setText("Folder");
		
		Label lblFolder = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblFolder = new FormData();
		fd_lblFolder.top = new FormAttachment(0, 34);
		fd_lblFolder.right = new FormAttachment(txtFolder, -5);
		lblFolder.setLayoutData(fd_lblFolder);
		lblFolder.setText("Folder");
		
		dtwReceivedOnDate = new DateTime(cmpDetails, SWT.BORDER | SWT.DROP_DOWN);
		FormData fd_dtwReceivedOnDate = new FormData();
		fd_dtwReceivedOnDate.right = new FormAttachment(0, 180);
		fd_dtwReceivedOnDate.top = new FormAttachment(0, 56);
		fd_dtwReceivedOnDate.left = new FormAttachment(0, 80);
		dtwReceivedOnDate.setLayoutData(fd_dtwReceivedOnDate);
		
		dtwReceivedOnTime = new DateTime(cmpDetails, SWT.BORDER | SWT.DROP_DOWN | SWT.TIME);
		FormData fd_dtwReceivedOnTime = new FormData();
		fd_dtwReceivedOnTime.left = new FormAttachment(dtwReceivedOnDate, 3);
		fd_dtwReceivedOnTime.right = new FormAttachment(dtwReceivedOnDate, 103, SWT.RIGHT);
		fd_dtwReceivedOnTime.top = new FormAttachment(0, 56);
		dtwReceivedOnTime.setLayoutData(fd_dtwReceivedOnTime);
		
		Label lblReceivedOn = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblReceivedOn = new FormData();
		fd_lblReceivedOn.top = new FormAttachment(0, 60);
		fd_lblReceivedOn.right = new FormAttachment(dtwReceivedOnDate, -5);
		lblReceivedOn.setLayoutData(fd_lblReceivedOn);
		lblReceivedOn.setText("Received on");
		
		Label lblReceivedOnSub = new Label(cmpDetails, SWT.NONE);
		lblReceivedOnSub.setFont(SWTResourceManager.getFont("Segoe UI", 6, SWT.BOLD));
		FormData fd_lblReceivedOnSub = new FormData();
		fd_lblReceivedOnSub.right = new FormAttachment(lblReceivedOn, lblReceivedOn.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		fd_lblReceivedOnSub.top = new FormAttachment(lblReceivedOn, -3);
		lblReceivedOnSub.setLayoutData(fd_lblReceivedOnSub);
		lblReceivedOnSub.setText("( m / d / yyyy )");
		
		txtReceivedBy = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtReceivedBy = new FormData();
		fd_txtReceivedBy.right = new FormAttachment(100);
		fd_txtReceivedBy.top = new FormAttachment(0, 83);
		fd_txtReceivedBy.left = new FormAttachment(0, 80);
		txtReceivedBy.setLayoutData(fd_txtReceivedBy);
		txtReceivedBy.setText("Received by");
		
		Label lblReceivedBy = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblReceivedBy = new FormData();
		fd_lblReceivedBy.right = new FormAttachment(txtReceivedBy, -5);
		fd_lblReceivedBy.top = new FormAttachment(0, 86);
		lblReceivedBy.setLayoutData(fd_lblReceivedBy);
		lblReceivedBy.setText("Received by");
		
		txtRequestType = new Text(cmpDetails, SWT.BORDER);
		FormData fd_txtRequestType = new FormData();
		fd_txtRequestType.right = new FormAttachment(100);
		fd_txtRequestType.top = new FormAttachment(0, 109);
		fd_txtRequestType.left = new FormAttachment(0, 80);
		txtRequestType.setLayoutData(fd_txtRequestType);
		txtRequestType.setText("Request Type");
		this.defaultColors.put("txtRequestType.background", txtRequestType.getBackground());
		txtRequestType.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				getParent().getDisplay().asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						if (Utilities.equals(item.getRequestType(), itemEditable.getRequestType()))
						{
							txtRequestType.setBackground(getDefaultColor("txtRequestType.background"));
						}
						else 
						{
							txtRequestType.setBackground(getColor_BG_CHANGED_SETTING());
						}
					}
				});
			}
		});
		
		Label lblType = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblType = new FormData();
		fd_lblType.right = new FormAttachment(txtRequestType, -5);
		fd_lblType.top = new FormAttachment(0, 112);
		lblType.setLayoutData(fd_lblType);
		lblType.setText("Request type");
		
		Label lblFrom = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblFrom = new FormData();
		fd_lblFrom.top = new FormAttachment(0, 138);
		lblFrom.setLayoutData(fd_lblFrom);
		lblFrom.setText("Requested by");
		
		txtRequestedBy = new Text(cmpDetails, SWT.BORDER);
		fd_lblFrom.right = new FormAttachment(txtRequestedBy, -5);
		FormData fd_txtRequestedBy = new FormData();
		fd_txtRequestedBy.right = new FormAttachment(100);
		fd_txtRequestedBy.top = new FormAttachment(0, 135);
		fd_txtRequestedBy.left = new FormAttachment(0, 80);
		txtRequestedBy.setLayoutData(fd_txtRequestedBy);
		txtRequestedBy.setText("Requested by");
		this.defaultColors.put("txtRequestedBy.background", txtRequestedBy.getBackground());
		txtRequestedBy.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				getParent().getDisplay().asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						if (Utilities.equals(item.getRequestedBy(), itemEditable.getRequestedBy()))
						{
							txtRequestedBy.setBackground(getDefaultColor("txtRequestedBy.background"));
						}
						else 
						{
							txtRequestedBy.setBackground(getColor_BG_CHANGED_SETTING());
						}
					}
				});
			}
		});
		
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
	}
	
	protected DataBindingContext initDataBindings()
	{
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtIdObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtId);
		IObservableValue iDItemObserveValue = PojoProperties.value("id").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtIdObserveWidget, iDItemObserveValue, null, null);
		//
		IObservableValue observeTextTxtFolderObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtFolder);
		IObservableValue folderItemObserveValue = PojoProperties.value("folder").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtFolderObserveWidget, folderItemObserveValue, null, null);
		//
		IObservableValue observeSelectionDtwReceivedOnDateObserveWidget = WidgetProperties.selection().observe(dtwReceivedOnDate);
		IObservableValue observeSelectionDtwReceivedOnTimeObserveWidget = WidgetProperties.selection().observe(dtwReceivedOnTime);
		IObservableValue observeSelectionDtwReceivedOnObserveWidget = new DateAndTimeObservableValue(observeSelectionDtwReceivedOnDateObserveWidget, observeSelectionDtwReceivedOnTimeObserveWidget) ;
		IObservableValue receivedOnItemEditableObserveValue = PojoProperties.value("receivedOn").observe(itemEditable);
		bindingContext.bindValue(observeSelectionDtwReceivedOnObserveWidget, receivedOnItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtReceivedByObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtReceivedBy);
		IObservableValue receivedByItemEditableObserveValue = PojoProperties.value("receivedBy").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtReceivedByObserveWidget, receivedByItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtRequestTypeObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtRequestType);
		IObservableValue requestTypeItemEditableObserveValue = PojoProperties.value("requestType").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtRequestTypeObserveWidget, requestTypeItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtRequestedByObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtRequestedBy);
		IObservableValue requestedByItemEditableObserveValue = PojoProperties.value("requestedBy").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtRequestedByObserveWidget, requestedByItemEditableObserveValue, null, null);
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
		
		if (item.getReceivedOn().compareTo(itemEditable.getReceivedOn()) != 0)
		{
			log = new Log();
			log.setAction("update");
			log.setTableName("MONITORING");
			log.setFieldName("ReceivedOn");
			log.setRowId(item.getId());
			log.setOldValue(Common.morphDate(item.getReceivedOn()));
			log.setNewValue(Common.morphDate(itemEditable.getReceivedOn()));
			logs.add(log);
		}
		
		if (!Utilities.equals(item.getRequestType(), itemEditable.getRequestType()))
		{
			log = new Log();
			log.setAction("update");
			log.setTableName("MONITORING");
			log.setFieldName("RequestType");
			log.setRowId(item.getId());
			log.setOldValue(item.getRequestType());
			log.setNewValue(itemEditable.getRequestType());
			logs.add(log);
		}
		
		if (!Utilities.equals(item.getRequestedBy(), itemEditable.getRequestedBy()))
		{
			log = new Log();
			log.setAction("update");
			log.setTableName("MONITORING");
			log.setFieldName("RequestedBy");
			log.setRowId(item.getId());
			log.setOldValue(item.getRequestedBy());
			log.setNewValue(itemEditable.getRequestedBy());
			logs.add(log);
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
