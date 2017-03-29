package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Filter;

import org.eclipse.wb.swt.SWTResourceManager;

public class RawMonitoringOutputGroup extends Group
{
	private Label lblEnteredOn;
	private DateFilterComposite xcdfEnteredOn;
	private Label lblRequestType;
	private TextFilterComposite xctfRequestType;
	private Label lblRequestedBy;
	private TextFilterComposite xctfRequestedBy;
	private Label lblReceivedOn;
	private DateFilterComposite xcdfReceivedOn;
	private Label lblReceivedBy;
	private TextFilterComposite xctfReceivedBy;
	private Label lblStatus;
	private TextFilterComposite xctfStatus;
	private Label lblAssignedBy;
	private TextFilterComposite xctfAssignedBy;
	private Label lblAssignedTo;
	private TextFilterComposite xctfAssignedTo;
	private Label lblAssignedOn;
	private DateFilterComposite xcdfAssignedOn;
	private Label lblProcessedBy;
	private TextFilterComposite xctfProcessedBy;
	private Label lblProcessedOn;
	private DateFilterComposite xcdfProcessedOn;
	private Label lblResolvedOn;
	private DateFilterComposite xcdfResolvedOn;
	private Button btnCreate;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RawMonitoringOutputGroup(Composite parent, int style)
	{
		super(parent, style);
		setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		setText("Raw Data (CSV) - Monitoring");
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginBottom = 5;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		lblEnteredOn = new Label(this, SWT.NONE);
		GridData gd_lblEnteredOn = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblEnteredOn.verticalIndent = 19;
		lblEnteredOn.setLayoutData(gd_lblEnteredOn);
		lblEnteredOn.setText("Entered on");
		
		xcdfEnteredOn = new DateFilterComposite(this, SWT.NONE);
		
		lblRequestType = new Label(this, SWT.NONE);
		GridData gd_lblRequestType = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblRequestType.verticalIndent = 18;
		lblRequestType.setLayoutData(gd_lblRequestType);
		lblRequestType.setText("Request type");
		
		xctfRequestType = new TextFilterComposite(this, SWT.NONE);
		xctfRequestType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xctfRequestType.setLoadItemsHandler(new LoadItemsHandler()
		{	
			@Override
			public String[] loadItems()
			{
				return Program.getMonitoringFilterItems(getShell(), "RequestType");
			}
		});
		
		lblRequestedBy = new Label(this, SWT.NONE);
		GridData gd_lblRequestedBy = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblRequestedBy.verticalIndent = 18;
		lblRequestedBy.setLayoutData(gd_lblRequestedBy);
		lblRequestedBy.setText("Requested by");
		
		xctfRequestedBy = new TextFilterComposite(this, SWT.NONE);
		xctfRequestedBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xctfRequestedBy.setLoadItemsHandler(new LoadItemsHandler()
		{	
			@Override
			public String[] loadItems()
			{
				return Program.getMonitoringFilterItems(getShell(), "RequestedBy");
			}
		});
		
		lblReceivedOn = new Label(this, SWT.NONE);
		GridData gd_lblReceivedOn = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblReceivedOn.verticalIndent = 19;
		lblReceivedOn.setLayoutData(gd_lblReceivedOn);
		lblReceivedOn.setText("Received on");
		
		xcdfReceivedOn = new DateFilterComposite(this, SWT.NONE);
		
		lblReceivedBy = new Label(this, SWT.NONE);
		GridData gd_lblReceivedBy = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblReceivedBy.verticalIndent = 18;
		lblReceivedBy.setLayoutData(gd_lblReceivedBy);
		lblReceivedBy.setText("Received by");
		
		xctfReceivedBy = new TextFilterComposite(this, SWT.NONE);
		xctfReceivedBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xctfReceivedBy.setLoadItemsHandler(new LoadItemsHandler()
		{	
			@Override
			public String[] loadItems()
			{
				return Program.getMonitoringFilterItems(getShell(), "ReceivedBy");
			}
		});
		
		lblStatus = new Label(this, SWT.NONE);
		GridData gd_lblStatus = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblStatus.verticalIndent = 18;
		lblStatus.setLayoutData(gd_lblStatus);
		lblStatus.setText("Status");
		
		xctfStatus = new TextFilterComposite(this, SWT.NONE);
		xctfStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xctfStatus.setLoadItemsHandler(new LoadItemsHandler()
		{	
			@Override
			public String[] loadItems()
			{
				return Program.getMonitoringFilterItems(getShell(), "Status");
			}
		});
		
		lblAssignedBy = new Label(this, SWT.NONE);
		GridData gd_lblAssignedBy = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblAssignedBy.verticalIndent = 18;
		lblAssignedBy.setLayoutData(gd_lblAssignedBy);
		lblAssignedBy.setText("Assigned by");
		
		xctfAssignedBy = new TextFilterComposite(this, SWT.NONE);
		xctfAssignedBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xctfAssignedBy.setLoadItemsHandler(new LoadItemsHandler()
		{	
			@Override
			public String[] loadItems()
			{
				return Program.getMonitoringFilterItems(getShell(), "AssignedBy");
			}
		});
		
		lblAssignedTo = new Label(this, SWT.NONE);
		GridData gd_lblAssignedTo = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblAssignedTo.verticalIndent = 18;
		lblAssignedTo.setLayoutData(gd_lblAssignedTo);
		lblAssignedTo.setText("Assigned to");
		
		xctfAssignedTo = new TextFilterComposite(this, SWT.NONE);
		xctfAssignedTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xctfAssignedTo.setLoadItemsHandler(new LoadItemsHandler()
		{	
			@Override
			public String[] loadItems()
			{
				return Program.getMonitoringFilterItems(getShell(), "AssignedTo");
			}
		});
		
		lblAssignedOn = new Label(this, SWT.NONE);
		GridData gd_lblAssignedOn = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblAssignedOn.verticalIndent = 19;
		lblAssignedOn.setLayoutData(gd_lblAssignedOn);
		lblAssignedOn.setText("Assigned on");
		
		xcdfAssignedOn = new DateFilterComposite(this, SWT.NONE);
		
		lblProcessedBy = new Label(this, SWT.NONE);
		GridData gd_lblProcessedBy = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblProcessedBy.verticalIndent = 18;
		lblProcessedBy.setLayoutData(gd_lblProcessedBy);
		lblProcessedBy.setText("Processed by");
		
		xctfProcessedBy = new TextFilterComposite(this, SWT.NONE);
		xctfProcessedBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xctfProcessedBy.setLoadItemsHandler(new LoadItemsHandler()
		{	
			@Override
			public String[] loadItems()
			{
				return Program.getMonitoringFilterItems(getShell(), "ProcessedBy");
			}
		});
		
		lblProcessedOn = new Label(this, SWT.NONE);
		GridData gd_lblProcessedOn = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblProcessedOn.verticalIndent = 18;
		lblProcessedOn.setLayoutData(gd_lblProcessedOn);
		lblProcessedOn.setText("Processed on");
		
		xcdfProcessedOn = new DateFilterComposite(this, SWT.NONE);
		
		lblResolvedOn = new Label(this, SWT.NONE);
		GridData gd_lblResolvedOn = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblResolvedOn.verticalIndent = 18;
		lblResolvedOn.setLayoutData(gd_lblResolvedOn);
		lblResolvedOn.setText("Resolved on");
		
		xcdfResolvedOn = new DateFilterComposite(this, SWT.NONE);
		
		btnCreate = new Button(this, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				create();
			}
		});
		btnCreate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnCreate.setText("Create");
		
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void create()
	{
		Date enteredOnFrom = xcdfEnteredOn.getFilter().getFrom();
		Date enteredOnTo = xcdfEnteredOn.getFilter().getTo();
		
		String requestType = xctfRequestType.getFilter();
		
		String requestedBy = xctfRequestedBy.getFilter();
		
		Date receivedOnFrom = xcdfReceivedOn.getFilter().getFrom();
		Date receivedOnTo = xcdfReceivedOn.getFilter().getTo();
		
		String receivedBy = xctfReceivedBy.getFilter();
		
		String status = xctfStatus.getFilter();
		
		String assignedBy = xctfAssignedBy.getFilter();
		
		String assignedTo = xctfAssignedTo.getFilter();
		
		Date assignedOnFrom = xcdfAssignedOn.getFilter().getFrom();
		Date assignedOnTo = xcdfAssignedOn.getFilter().getTo();
		
		String processedBy = xctfProcessedBy.getFilter();
		
		Date processedOnFrom = xcdfProcessedOn.getFilter().getFrom();
		Date processedOnTo = xcdfProcessedOn.getFilter().getTo();
		
		Date resolvedOnFrom = xcdfResolvedOn.getFilter().getFrom();
		Date resolvedOnTo = xcdfResolvedOn.getFilter().getTo();
		
		Filter filter = new Filter();
		filter.add("EnteredOn", enteredOnFrom, enteredOnTo);
		filter.add("RequestType", requestType);
		filter.add("RequestedBy", requestedBy);
		filter.add("ReceivedOn", receivedOnFrom, receivedOnTo);
		filter.add("ReceivedBy", receivedBy);
		filter.add("Status", status);
		filter.add("AssignedBy", assignedBy);
		filter.add("AssignedTo", assignedTo);
		filter.add("AssignedOn", assignedOnFrom, assignedOnTo);
		filter.add("ProcessedBy", processedBy);
		filter.add("ProcessedOn", processedOnFrom, processedOnTo);
		filter.add("ResolvedOn", resolvedOnFrom, resolvedOnTo);
		
		Program.createOutputMonitoringRaw(getShell(), filter);
	}
}
