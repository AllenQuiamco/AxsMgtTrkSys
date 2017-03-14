package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;

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
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ReceivingDialog extends Dialog
{
	private Monitoring item;
	private Monitoring itemEditable;
	protected boolean result;
	protected Shell shell;
	private Text txtId;
	private Text txtFolder;
	private Text txtReceivedBy;
	private DateTime dtwReceivedOn;
	private Text txtRequestType;
	private Text txtRequestedBy;
	private Button btnSave;
	private Text txtRemarks;
	private Composite cmpSave;
	private Composite cmpDetails;
	
	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		final ReceivingDialog dialog = new ReceivingDialog(shell, SWT.NONE);
		
		Monitoring item = Monitoring.getSamples().getFirst();
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
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean open()
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(450, 312);
		shell.setText("Receiving Details");
		shell.setLayout(new FormLayout());
		
		cmpSave = new Composite(shell, SWT.NONE);
		FormData fd_cmpSave = new FormData();
		fd_cmpSave.bottom = new FormAttachment(100);
		fd_cmpSave.left = new FormAttachment(50, 0 - (cmpSave.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2));
		cmpSave.setLayoutData(fd_cmpSave);
		cmpSave.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		btnSave = new Button(cmpSave, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.newEntry(shell, itemEditable);
			}
		});
		btnSave.setText("Save");
		
		cmpDetails = new Composite(shell, SWT.NONE);
		cmpDetails.setLayout(new FormLayout());
		FormData fd_cmpDetails = new FormData();
		fd_cmpDetails.bottom = new FormAttachment(cmpSave);
		fd_cmpDetails.left = new FormAttachment(0);
		fd_cmpDetails.right = new FormAttachment(100);
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
		
		Label lblFolder = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblFolder = new FormData();
		fd_lblFolder.top = new FormAttachment(0, 34);
		fd_lblFolder.left = new FormAttachment(0, 36);
		lblFolder.setLayoutData(fd_lblFolder);
		lblFolder.setText("Folder");
		
		txtFolder = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtFolder = new FormData();
		fd_txtFolder.right = new FormAttachment(100);
		fd_txtFolder.top = new FormAttachment(0, 31);
		fd_txtFolder.left = new FormAttachment(0, 74);
		txtFolder.setLayoutData(fd_txtFolder);
		txtFolder.setText("Folder");
		
		Label lblReceivedOn = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblReceivedOn = new FormData();
		fd_lblReceivedOn.top = new FormAttachment(0, 60);
		fd_lblReceivedOn.left = new FormAttachment(0, 5);
		lblReceivedOn.setLayoutData(fd_lblReceivedOn);
		lblReceivedOn.setText("Received on");
		
		Label lblReceivedOnSub = new Label(cmpDetails, SWT.NONE);
		lblReceivedOnSub.setFont(SWTResourceManager.getFont("Segoe UI", 6, SWT.BOLD));
		FormData fd_lblReceivedOnSub = new FormData();
		fd_lblReceivedOnSub.right = new FormAttachment(lblReceivedOn, lblReceivedOn.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		fd_lblReceivedOnSub.top = new FormAttachment(lblReceivedOn, -3);
		lblReceivedOnSub.setLayoutData(fd_lblReceivedOnSub);
		lblReceivedOnSub.setText("( m / d / yyyy )");
		
		dtwReceivedOn = new DateTime(cmpDetails, SWT.BORDER | SWT.DROP_DOWN);
		dtwReceivedOn.setEnabled(false);
		dtwReceivedOn.setToolTipText("M / D / YYYY");
		dtwReceivedOn.setDate(1990, 1, 1);
		FormData fd_dtwReceivedOn = new FormData();
		fd_dtwReceivedOn.right = new FormAttachment(100);
		fd_dtwReceivedOn.top = new FormAttachment(0, 56);
		fd_dtwReceivedOn.left = new FormAttachment(0, 74);
		dtwReceivedOn.setLayoutData(fd_dtwReceivedOn);		
		
		Label lblReceivedBy = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblReceivedBy = new FormData();
		fd_lblReceivedBy.top = new FormAttachment(0, 86);
		fd_lblReceivedBy.left = new FormAttachment(0, 6);
		lblReceivedBy.setLayoutData(fd_lblReceivedBy);
		lblReceivedBy.setText("Received by");
		
		txtReceivedBy = new Text(cmpDetails, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtReceivedBy = new FormData();
		fd_txtReceivedBy.right = new FormAttachment(100);
		fd_txtReceivedBy.top = new FormAttachment(0, 83);
		fd_txtReceivedBy.left = new FormAttachment(0, 74);
		txtReceivedBy.setLayoutData(fd_txtReceivedBy);
		txtReceivedBy.setText("Received by");
		
		txtRequestType = new Text(cmpDetails, SWT.BORDER);
		FormData fd_txtRequestType = new FormData();
		fd_txtRequestType.right = new FormAttachment(100);
		fd_txtRequestType.top = new FormAttachment(0, 109);
		fd_txtRequestType.left = new FormAttachment(0, 74);
		txtRequestType.setLayoutData(fd_txtRequestType);
		txtRequestType.setText("Request Type");
		
		Label lblType = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblType = new FormData();
		fd_lblType.right = new FormAttachment(txtRequestType, -5);
		fd_lblType.top = new FormAttachment(0, 112);
		lblType.setLayoutData(fd_lblType);
		lblType.setText("Request");
		
		Label lblFrom = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblFrom = new FormData();
		fd_lblFrom.top = new FormAttachment(0, 138);
		fd_lblFrom.left = new FormAttachment(0, 41);
		lblFrom.setLayoutData(fd_lblFrom);
		lblFrom.setText("From");
		
		txtRequestedBy = new Text(cmpDetails, SWT.BORDER);
		FormData fd_txtRequestedBy = new FormData();
		fd_txtRequestedBy.right = new FormAttachment(100);
		fd_txtRequestedBy.top = new FormAttachment(0, 135);
		fd_txtRequestedBy.left = new FormAttachment(0, 74);
		txtRequestedBy.setLayoutData(fd_txtRequestedBy);
		txtRequestedBy.setText("Requested by");
		
		Label lblRemarks = new Label(cmpDetails, SWT.NONE);
		FormData fd_lblRemarks = new FormData();
		fd_lblRemarks.top = new FormAttachment(0, 164);
		fd_lblRemarks.left = new FormAttachment(0, 24);
		lblRemarks.setLayoutData(fd_lblRemarks);
		lblRemarks.setText("Remarks");
		
		txtRemarks = new Text(cmpDetails, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_txtRemarks = new FormData();
		fd_txtRemarks.bottom = new FormAttachment(100);
		fd_txtRemarks.right = new FormAttachment(100);
		fd_txtRemarks.top = new FormAttachment(0, 161);
		fd_txtRemarks.left = new FormAttachment(0, 74);
		txtRemarks.setLayoutData(fd_txtRemarks);
		txtRemarks.setText("Remarks\r\n2\r\n3\r\n4\r\n5\r\n6");
		initDataBindings();
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
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtIdObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtId);
		IObservableValue iDItemObserveValue = PojoProperties.value("ID").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtIdObserveWidget, iDItemObserveValue, null, null);
		//
		IObservableValue observeTextTxtFolderObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtFolder);
		IObservableValue folderItemObserveValue = PojoProperties.value("folder").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtFolderObserveWidget, folderItemObserveValue, null, null);
		//
		IObservableValue observeSelectionDtwReceivedOnObserveWidget = WidgetProperties.selection().observe(dtwReceivedOn);
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
		bindingContext.bindValue(observeTextTxtRequestedByObserveWidget, requestTypeItemEditableObserveValue, null, null);
		//
		IObservableValue observeTextTxtRemarksObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtRemarks);
		IObservableValue remarksItemEditableObserveValue = PojoProperties.value("remarks").observe(itemEditable);
		bindingContext.bindValue(observeTextTxtRemarksObserveWidget, remarksItemEditableObserveValue, null, null);
		//
		return bindingContext;
	}
}
