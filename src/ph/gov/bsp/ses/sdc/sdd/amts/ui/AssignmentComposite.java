package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Monitoring;
import ph.gov.bsp.ses.sdc.sdd.util.Utilities;

import org.eclipse.swt.widgets.Combo;

public class AssignmentComposite extends Composite
{
	private static final String[] statusSelection = new String[] { "ALL", "NEW", "APPROVED", "DISALLOWED", "CANCELLED" };
	private PaginationComposite xcmpPagination;
	private Shell parentShell;
	private AssignmentComposite self;
	private Button btnRefresh;
	private Combo cbxStatus;
	private Text txtType;
	private Text txtFrom;
	private Table table;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AssignmentComposite(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new FormLayout());
		
		setParentShell(parent);
		self = this;
		
		xcmpPagination = new PaginationComposite(this, SWT.NONE);
		FormData fd_paginationComposite = new FormData();
		fd_paginationComposite.left = new FormAttachment(50, -165);
		fd_paginationComposite.bottom = new FormAttachment(100);
		xcmpPagination.setLayoutData(fd_paginationComposite);
		xcmpPagination.setPageChangeAction(new Runnable()
		{
			@Override
			public void run()
			{
				Program.refreshAssignmentTab(self, true);
			}
		});
		
		Button btnNewEntry = new Button(this, SWT.NONE);
		FormData fd_btnNewEntry = new FormData();
		fd_btnNewEntry.top = new FormAttachment(0, 18);
		btnNewEntry.setLayoutData(fd_btnNewEntry);
		btnNewEntry.setText("New Entry");
		btnNewEntry.setVisible(false); // Helps alignment with Refresh button
		
		btnRefresh = new Button(this, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.refreshAssignmentTab(self, true);
			}
		});
		
		FormData fd_btnRefresh = new FormData();
		fd_btnRefresh.top = new FormAttachment(btnNewEntry, 1);
		fd_btnRefresh.right = new FormAttachment(btnNewEntry, 0, SWT.RIGHT);
		fd_btnRefresh.left = new FormAttachment(0);
		btnRefresh.setLayoutData(fd_btnRefresh);
		btnRefresh.setText("Refresh");
		
		Group grpFilter = new Group(this, SWT.NONE);
		grpFilter.setText("Filter");
		grpFilter.setLayout(new GridLayout(2, false));
		FormData fd_grpFilter = new FormData();
		fd_grpFilter.top = new FormAttachment(0);
		fd_grpFilter.left = new FormAttachment(btnRefresh, 6);
		fd_grpFilter.right = new FormAttachment(100, -10);
		grpFilter.setLayoutData(fd_grpFilter);
		
		Label lblStatus = new Label(grpFilter, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatus.setText("Status");
		
		cbxStatus = new Combo(grpFilter, SWT.READ_ONLY);
		cbxStatus.setItems(statusSelection);
		cbxStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cbxStatus.select(1);
		cbxStatus.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Program.refreshAssignmentTab(self, true);
				table.setFocus();
			}
		});
		
		Label lblType = new Label(grpFilter, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type");
		
		txtType = new Text(grpFilter, SWT.BORDER);
		txtType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtType.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.character == SWT.CR)
				{
					Program.refreshAssignmentTab(self, true);
					btnRefresh.setFocus();
				}
			}
		});
		
		Label lblFrom = new Label(grpFilter, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From");
		
		txtFrom = new Text(grpFilter, SWT.BORDER);
		txtFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFrom.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.character == SWT.CR)
				{
					Program.refreshAssignmentTab(self, true);
					btnRefresh.setFocus();
				}
			}
		});
		
		table = new Table(this, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(grpFilter, 6);
		fd_table.left = new FormAttachment(0);
		fd_table.bottom = new FormAttachment(100, -33);
		fd_table.right = new FormAttachment(100);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				if (TableItem.class.isInstance(event.item))
				{
					final TableItem item = (TableItem)event.item;
					
					if (item.getChecked())
					{
						getDisplay().syncExec(new Runnable()
						{
							@Override
							public void run()
							{
								item.setChecked(false);
							}
						});
						
						String textId = item.getText();
						int id = Integer.parseInt(textId);
						
						Program.updateAssignment(parentShell, item, id);
					}
				}
			}
		});
		
		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("        ID");
		
		TableColumn tblclmnStatus = new TableColumn(table, SWT.NONE);
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");
		
		TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(100);
		tblclmnType.setText("Type");
		
		TableColumn tblclmnFrom = new TableColumn(table, SWT.NONE);
		tblclmnFrom.setWidth(100);
		tblclmnFrom.setText("From");
		
		TableColumn tblclmnReceivedOn = new TableColumn(table, SWT.NONE);
		tblclmnReceivedOn.setWidth(100);
		tblclmnReceivedOn.setText("Received on");
		
		TableColumn tblclmnAssignedTo = new TableColumn(table, SWT.NONE);
		tblclmnAssignedTo.setWidth(100);
		tblclmnAssignedTo.setText("Assigned to");
		
		TableColumn tblclmnAssignedOn = new TableColumn(table, SWT.NONE);
		tblclmnAssignedOn.setWidth(100);
		tblclmnAssignedOn.setText("Assigned on");
		
		TableColumn tblclmnAssignedBy = new TableColumn(table, SWT.NONE);
		tblclmnAssignedBy.setWidth(100);
		tblclmnAssignedBy.setText("Assigned by");
		
		TableColumn tblclmnRemarks = new TableColumn(table, SWT.NONE);
		tblclmnRemarks.setWidth(100);
		tblclmnRemarks.setText("Remarks");
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void setParentShell(Composite parent)
	{
		while (parent != null && !Shell.class.isInstance(parent))
		{
			parent = parent.getParent();
		}
		
		if (parent == null) throw new NullPointerException("Composite does not have a parent.");
		this.parentShell = (Shell)parent;
	}
	
	public void display(final List<Monitoring> rows, final int rowStart, final int rowEnd, final int rowTotal)
	{
		getDisplay().syncExec(new Runnable()
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
		this.table.setEnabled(true);
		
		TableItem item;
		
		for (Monitoring row : rows)
		{
			item = new TableItem(table, SWT.NONE);
			setText(item, row);
		}
	}
	
	public void clear()
	{
		this.table.removeAll();
	}
	
	public static void setText(TableItem item, Monitoring row)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy hh:mm a");
		
		item.setText(
			Utilities.toArray(
				String.format("%d", row.getID()), 
				row.getStatus(), 
				row.getRequestType(), 
				row.getRequestedBy(), 
				(row.getReceivedOn() == null) ? "" : formatter.format(row.getReceivedOn()), 
				row.getAssignedTo(), 
				(row.getAssignedOn() == null) ? "" : formatter.format(row.getAssignedOn()), 
				row.getAssignedBy(), 
				row.getRemarks()));
	}
	
	public void displayEmpty()
	{
		getDisplay().syncExec(new Runnable()
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
		this.table.setEnabled(false);
		
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
	
	public String getFilterStatus()
	{
		String text = this.cbxStatus.getText();
		if (text.equals("ALL")) return "";
		else return text;
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
