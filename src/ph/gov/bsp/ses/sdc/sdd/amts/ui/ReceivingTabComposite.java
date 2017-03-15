package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ReceivingTabComposite extends Composite
{
	private Shell parentShell;
	private Table table;
	private PaginationComposite xcmpPagination;
	private Text txtType;
	private Text txtFrom;
	protected ReceivingTabComposite self;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ReceivingTabComposite(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new FormLayout());
		
		setParentShell(parent);
		self = this;
		
		Button btnNewEntry = new Button(this, SWT.NONE);
		btnNewEntry.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.receive(parentShell, e);
				Program.refreshReceivingTab(self, true);
			}
		});
		FormData fd_btnNewEntry = new FormData();
		fd_btnNewEntry.top = new FormAttachment(0, 18);
		btnNewEntry.setLayoutData(fd_btnNewEntry);
		btnNewEntry.setText("New Entry");
		
		Button btnRefresh = new Button(this, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.refreshReceivingTab(self, true);
			}
		});
		FormData fd_btnRefresh = new FormData();
		fd_btnRefresh.top = new FormAttachment(btnNewEntry, 1);
		fd_btnRefresh.right = new FormAttachment(btnNewEntry, 0, SWT.RIGHT);
		fd_btnRefresh.left = new FormAttachment(0);
		btnRefresh.setLayoutData(fd_btnRefresh);
		btnRefresh.setText("Refresh");
		
		table = new Table(this, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.left = new FormAttachment(0);
		fd_table.bottom = new FormAttachment(100, -33);
		fd_table.right = new FormAttachment(100);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("        ID");
		
		TableColumn tblclmnFolder = new TableColumn(table, SWT.NONE);
		tblclmnFolder.setWidth(100);
		tblclmnFolder.setText("Folder");
		
		TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(100);
		tblclmnType.setText("Type");
		
		TableColumn tblclmnFrom = new TableColumn(table, SWT.NONE);
		tblclmnFrom.setWidth(100);
		tblclmnFrom.setText("From");
		
		TableColumn tblclmnReceivedOn = new TableColumn(table, SWT.NONE);
		tblclmnReceivedOn.setWidth(100);
		tblclmnReceivedOn.setText("Received on");
		
		xcmpPagination = new PaginationComposite(this, SWT.NONE);
		FormData fd_paginationComposite = new FormData();
		fd_paginationComposite.left = new FormAttachment(50, -165);
		fd_paginationComposite.bottom = new FormAttachment(100);
		xcmpPagination.setLayoutData(fd_paginationComposite);
		
		Group grpFilter = new Group(this, SWT.NONE);
		fd_table.top = new FormAttachment(grpFilter, 6);
		
		TableColumn tblclmnReceivedby = new TableColumn(table, SWT.NONE);
		tblclmnReceivedby.setWidth(100);
		tblclmnReceivedby.setText("ReceivedBy");
		grpFilter.setText("Filter");
		grpFilter.setLayout(new GridLayout(2, false));
		FormData fd_grpFilter = new FormData();
		fd_grpFilter.top = new FormAttachment(0);
		fd_grpFilter.left = new FormAttachment(btnNewEntry, 6);
		fd_grpFilter.right = new FormAttachment(100, -10);
		grpFilter.setLayoutData(fd_grpFilter);
		
		Label lblType = new Label(grpFilter, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type");
		
		txtType = new Text(grpFilter, SWT.BORDER);
		txtType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFrom = new Label(grpFilter, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From");
		
		txtFrom = new Text(grpFilter, SWT.BORDER);
		txtFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	private void setParentShell(Composite parent)
	{
		while (parent != null && !Shell.class.isInstance(parent))
		{
			parent = parent.getParent();
		}
		
		if (parent == null) throw new NullPointerException("Composite does not have a parent.");
		this.parentShell = (Shell) parent;
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}

	public void clear()
	{
		this.table.removeAll();
	}

	public void display(final List<Monitoring> rows, final int rowStart, final int rowEnd, final int rowTotal)
	{
		getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				displayAsync(rows);
			}
		});
		
		this.xcmpPagination.display(rowStart, rowEnd, rowTotal);
	}

	protected void displayAsync(List<Monitoring> rows)
	{
		this.table.removeAll();
		
		TableItem item;
		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy hh:mm a");
		
		for (Monitoring row : rows)
		{
			item = new TableItem(table, SWT.NONE);
			item.setText(Utilities.toArray(
					String.format("%d", row.getID()), 
					row.getFolder(), 
					row.getRequestType(), 
					row.getRequestedBy(), 
					(row.getReceivedOn() == null) ? "" : formatter.format(row.getReceivedOn()), 
					row.getRequestedBy()));
		}
	}

	public void displayEmpty()
	{
		getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				displayEmptyAsync();
			}
		});
		
		this.xcmpPagination.displayEmpty();
	}

	protected void displayEmptyAsync()
	{
		this.table.removeAll();
		
		TableItem item;
		
		item = new TableItem(table, SWT.NONE);
		item.setText("No data");
	}

	public int getRowStart()
	{
		return this.xcmpPagination.getRowStart();
	}
	
	public int getRowEnd()
	{
		return this.xcmpPagination.getRowEnd();
	}
	
	public String getFilterType()
	{
		return this.txtType.getText();
	}
	
	public String getFilterFrom()
	{
		return this.txtFrom.getText();
	}
}
