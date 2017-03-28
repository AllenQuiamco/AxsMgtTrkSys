package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

import ph.gov.bsp.ses.sdc.sdd.amts.data.Common;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBox;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxButtons;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxIcon;

public class RawDataOutputGroup extends Group
{
	private Group grpTable;
	private Button btnMonitoring;
	private Button btnLog;
	private Group grpRange;
	private Button btnAll;
	private Button btnDate;
	private Group grpDateFrom;
	private Button btnDateEarliest;
	private Label lblEarliest;
	private Button btnDateFrom;
	private DateTime dtwFrom;
	private Group grpDateTo;
	private Button btnDateLatest;
	private Label lblLatest;
	private Button btnDateTo;
	private DateTime dtwTo;
	private Button btnRow;
	private Group grpRowStart;
	private Button btnRowFirst;
	private Label lblFirst;
	private Button btnRowStart;
	private Spinner spinTo;
	private Button btnRowEnd;
	private Label lblLast;
	private Button btnRowLast;
	private Group grpRowEnd;
	private Spinner spinFrom;
	private Button btnCreate;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RawDataOutputGroup(Composite parent, int style)
	{
		super(parent, style);
		setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		setText("Raw Data (to CSV)");
		setLayout(new GridLayout(2, false));
		FormData fd_grpRawData = new FormData();
		fd_grpRawData.right = new FormAttachment(100, -10);
		fd_grpRawData.top = new FormAttachment(0, 10);
		fd_grpRawData.left = new FormAttachment(0, 10);
		setLayoutData(fd_grpRawData);
		
		grpTable = new Group(this, SWT.NONE);
		grpTable.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		grpTable.setText("Table");
		grpTable.setLayout(new GridLayout(1, false));
		
		btnMonitoring = new Button(grpTable, SWT.RADIO);
		btnMonitoring.setText("MONITORING");
		
		btnLog = new Button(grpTable, SWT.RADIO);
		btnLog.setText("LOG");
		
		grpRange = new Group(this, SWT.NONE);
		grpRange.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		grpRange.setText("Range");
		grpRange.setLayout(new GridLayout(2, true));
		
		btnAll = new Button(grpRange, SWT.RADIO);
		btnAll.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnAll.setText("All (may be slow)");
		
		btnDate = new Button(grpRange, SWT.RADIO);
		btnDate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnDate.setText("Date Received / Effected");
		
		grpDateFrom = new Group(grpRange, SWT.NONE);
		grpDateFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpDateFrom.setText("From");
		grpDateFrom.setLayout(new GridLayout(2, false));
		
		btnDateEarliest = new Button(grpDateFrom, SWT.RADIO);
		btnDateEarliest.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
			}
		});
		
		lblEarliest = new Label(grpDateFrom, SWT.NONE);
		lblEarliest.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDown(MouseEvent e)
			{
				btnDateEarliest.setSelection(true);
				btnDateFrom.setSelection(false);
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
			}
		});
		lblEarliest.setText("Earliest");
		
		btnDateFrom = new Button(grpDateFrom, SWT.RADIO);
		btnDateFrom.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
			}
		});
		
		dtwFrom = new DateTime(grpDateFrom, SWT.BORDER | SWT.DROP_DOWN);
		dtwFrom.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnDateEarliest.setSelection(false);
				btnDateFrom.setSelection(true);
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
			}
		});
		
		grpDateTo = new Group(grpRange, SWT.NONE);
		grpDateTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpDateTo.setText("To");
		grpDateTo.setLayout(new GridLayout(2, false));
		
		btnDateLatest = new Button(grpDateTo, SWT.RADIO);
		btnDateLatest.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
				
			}
		});
		
		lblLatest = new Label(grpDateTo, SWT.NONE);
		lblLatest.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDown(MouseEvent e)
			{
				btnDateLatest.setSelection(true);
				btnDateTo.setSelection(false);
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
			}
		});
		lblLatest.setText("Latest");
		
		btnDateTo = new Button(grpDateTo, SWT.RADIO);
		btnDateTo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
			}
		});
		
		dtwTo = new DateTime(grpDateTo, SWT.BORDER | SWT.DROP_DOWN);
		dtwTo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnDateLatest.setSelection(false);
				btnDateTo.setSelection(true);
				btnAll.setSelection(false);
				btnDate.setSelection(true);
				btnRow.setSelection(false);
			}
		});
		
		btnRow = new Button(grpRange, SWT.RADIO);
		btnRow.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnRow.setText("Rows");
		
		grpRowStart = new Group(grpRange, SWT.NONE);
		grpRowStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpRowStart.setText("Start");
		grpRowStart.setLayout(new GridLayout(2, false));
		
		btnRowFirst = new Button(grpRowStart, SWT.RADIO);
		btnRowFirst.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
			}
		});
		
		lblFirst = new Label(grpRowStart, SWT.NONE);
		lblFirst.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDown(MouseEvent e)
			{
				btnRowFirst.setSelection(true);
				btnRowStart.setSelection(false);
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
			}
		});
		lblFirst.setText("First");
		
		btnRowStart = new Button(grpRowStart, SWT.RADIO);
		btnRowStart.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
			}
		});
		
		spinFrom = new Spinner(grpRowStart, SWT.BORDER);
		spinFrom.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				btnRowFirst.setSelection(false);
				btnRowStart.setSelection(true);
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
			}
		});
		spinFrom.setMaximum(Integer.MAX_VALUE);
		spinFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		grpRowEnd = new Group(grpRange, SWT.NONE);
		grpRowEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpRowEnd.setText("End");
		grpRowEnd.setLayout(new GridLayout(2, false));
		
		btnRowLast = new Button(grpRowEnd, SWT.RADIO);
		btnRowLast.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
			}
		});
		
		lblLast = new Label(grpRowEnd, SWT.NONE);
		lblLast.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDown(MouseEvent e)
			{
				btnRowLast.setSelection(true);
				btnRowEnd.setSelection(false);
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
			}
		});
		lblLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblLast.setText("Last");
		
		btnRowEnd = new Button(grpRowEnd, SWT.RADIO);
		btnRowEnd.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
				
			}
		});
		
		spinTo = new Spinner(grpRowEnd, SWT.BORDER);
		spinTo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				btnRowLast.setSelection(false);
				btnRowEnd.setSelection(true);
				btnAll.setSelection(false);
				btnDate.setSelection(false);
				btnRow.setSelection(true);
			}
		});
		spinTo.setMaximum(Integer.MAX_VALUE);
		spinTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		btnCreate = new Button(this, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnCreate_widgetSelected(e);
			}
		});
		btnCreate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnCreate.setText("Create");
		
		// TODO Select by * except ID and Remarks
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	protected void setEnabled(Control control, boolean enabled)
	{
		control.setEnabled(enabled);
		if (Composite.class.isInstance(control))
		{
			for (Control child : ((Composite)control).getChildren())
			{
				setEnabled(child, enabled);
			}
		}
	}
	
	protected void btnCreate_widgetSelected(SelectionEvent selectionEvent)
	{
		try
		{
			String table = null;
			boolean monitoring = btnMonitoring.getSelection();
			boolean log = btnLog.getSelection(); 
			if (monitoring && log) throw new IllegalStateException(String.format("MONITORING=%B & LOG=%B", monitoring, log));
			else if (monitoring && !log) table = "MONITORING";
			else if (!monitoring && log) table = "LOG";
			else throw new IllegalArgumentException("Please select a table.");
			
			String range = null;
			boolean all = btnAll.getSelection();
			boolean date = btnDate.getSelection();
			boolean row = btnRow.getSelection();
			if (all && !date && !row) range = "ALL";
			else if (!all && date && !row) range = "DATE";
			else if (!all && !date && row) range = "ROW";
			else if (!all && !date && !row) throw new IllegalArgumentException("Please select a range.");
			else throw new IllegalStateException(String.format("ALL=%B & DATE=%B & ROW=%B", all, date, row));
			
			if (date)
			{
				boolean dateEarliest = btnDateEarliest.getSelection();
				boolean dateFrom = btnDateFrom.getSelection();
				boolean dateLatest = btnDateLatest.getSelection();
				boolean dateTo = btnDateTo.getSelection();
				
				Calendar calendar = GregorianCalendar.getInstance();
				Calendar calFrom = (Calendar)calendar.clone(); get(calFrom, dtwFrom);
				Calendar calTooo = (Calendar)calendar.clone(); get(calTooo, dtwTo);
				if (dateFrom && dateTo)
				{
					if (calFrom.compareTo(calTooo) > 0)
					{
						set(dtwFrom, calTooo);
						set(dtwTo, calFrom);
						calendar = calTooo;
						calTooo = calFrom;
						calFrom = calendar;
					}
				}
				
				calendar = calTooo;
				calendar.roll(Calendar.DAY_OF_YEAR, true);				
				
				String dateFromData = null;
				if (dateEarliest && dateFrom) throw new IllegalStateException(String.format("EARLIEST=%B & FROM=%B", dateEarliest, dateFrom));
				else if (dateEarliest && !dateFrom) dateFromData = ">0"; 
				else if (!dateEarliest && dateFrom) dateFromData = ">=" + Common.morphDate(dtwFrom.getYear(), dtwFrom.getMonth(), dtwFrom.getDay());
				else throw new IllegalArgumentException("Please select a starting date.");
				
				String dateToData = null;
				if (dateLatest && dateTo) throw new IllegalStateException(String.format("LATEST=%B & TO=%B", dateLatest, dateTo));
				else if (dateLatest && !dateTo) dateToData = "<=99999999999999"; 
				else if (!dateLatest && dateTo) dateToData = "<" + Common.morphDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				else throw new IllegalArgumentException("Please select an ending date.");
				
				System.out.println(dateFromData + " " + dateToData);
			}
		}
		catch(IllegalArgumentException iae)
		{
			iae.printStackTrace();
			MsgBox.show(getShell(), iae.getMessage(), "Invalid input", MsgBoxButtons.OK, MsgBoxIcon.WARNING);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			MsgBox.show(getShell(), "An unexpected error occurred.", "Error", MsgBoxButtons.OK, MsgBoxIcon.ERROR);
		}
	}

	private void get(Calendar cal, DateTime dtw)
	{
		cal.set(Calendar.YEAR, dtw.getYear());
		cal.set(Calendar.MONTH, dtw.getMonth());
		cal.set(Calendar.DATE, dtw.getDay());
	}
	
	private void set(DateTime dtw, Calendar cal)
	{
		dtw.setYear(cal.get(Calendar.YEAR));
		dtw.setMonth(cal.get(Calendar.MONTH));
		dtw.setDay(cal.get(Calendar.DATE));
	}
}
