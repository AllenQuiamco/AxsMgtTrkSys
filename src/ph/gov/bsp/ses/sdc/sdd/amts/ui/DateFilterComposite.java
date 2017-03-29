package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.util.Date;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;

public class DateFilterComposite extends Composite
{
	private Runnable selectAction; 
	private DateFilter filter = new DateFilter();
	
	private Group grpFrom;
	private Label lblFrom;
	private Button btnEarliest;
	private Button btnFromDate;
	private DateTime dtwFrom;
	private Group grpTo;
	private Label lblTo;
	private Button btnLatest;
	private Button btnToDate;
	private DateTime dtwTo;
	
//	public static void main(String[] args)
//	{
//		final Display display = new Display();
//		
//		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
//				shell.setSize(487, 300);
//				shell.setText("Test");
//				shell.setLayout(new FillLayout());
//				
//				final DateFilterGroup dateFilterGroup = new DateFilterGroup(shell, SWT.NONE);
//				Button btnTest = new Button(shell, SWT.NONE);
//				btnTest.addSelectionListener(new SelectionAdapter()
//				{
//					@Override
//					public void widgetSelected(SelectionEvent e)
//					{
//						SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy hh:mm a");
//						
//						DateFilter filter = dateFilterGroup.getFilter();
//						Date from = filter.getFrom();
//						Date to = filter.getTo();
//						System.out.println(String.format("FROM %s TO %s", 
//								((from == null) ? "null" : formatter.format(from)),
//								((to == null) ? "null" : formatter.format(to))));
//					}
//				});
//				dateFilterGroup.setSelectAction(new Runnable()
//				{
//					@Override
//					public void run()
//					{
//						System.out.println("selectAction");
//					}
//				});
//				
//				shell.open();
//				shell.layout();
//				while (!shell.isDisposed())
//				{
//					if (!display.readAndDispatch())
//					{
//						display.sleep();
//					}
//				}
//			}
//		});
//	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DateFilterComposite(Composite parent, int style)
	{
		super(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		grpFrom = new Group(this, SWT.NONE);
		GridLayout gl_grpFrom = new GridLayout(4, false);
		gl_grpFrom.verticalSpacing = 0;
		gl_grpFrom.marginBottom = 5;
		gl_grpFrom.marginHeight = 0;
		grpFrom.setLayout(gl_grpFrom);
		
		lblFrom = new Label(grpFrom, SWT.NONE);
		lblFrom.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblFrom.setSize(28, 15);
		lblFrom.setText("From ");
		
		btnEarliest = new Button(grpFrom, SWT.RADIO);
		btnEarliest.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (btnEarliest.getSelection())
					if (selectAction != null)
						selectAction.run();
			}
		});
		btnEarliest.setSelection(true);
		btnEarliest.setSize(58, 16);
		btnEarliest.setText("Earliest");
		
		btnFromDate = new Button(grpFrom, SWT.RADIO);
		btnFromDate.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (btnFromDate.getSelection()) 
					if (selectAction != null) 
						selectAction.run();
			}
		});
		btnFromDate.setSize(13, 16);
		
		dtwFrom = new DateTime(grpFrom, SWT.BORDER | SWT.DROP_DOWN);
		dtwFrom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnEarliest.setSelection(false);
				btnFromDate.setSelection(true);
				getDisplay().asyncExec(new Runnable()
				{	
					@Override
					public void run()
					{
						Date from = new Date(), to = new Date();
						get(from, dtwFrom);
						get(to, dtwTo);
						
						//if (btnToDate.getSelection()) 
							if (from.compareTo(to) > 0) 
								set(dtwTo, from);
					}
				});
				if (selectAction != null) selectAction.run();
			}
		});
		dtwFrom.setSize(95, 24);
		
		grpTo = new Group(this, SWT.NONE);
		GridLayout gl_grpTo = new GridLayout(4, false);
		gl_grpTo.verticalSpacing = 0;
		gl_grpTo.marginBottom = 5;
		gl_grpTo.marginHeight = 0;
		grpTo.setLayout(gl_grpTo);
		
		lblTo = new Label(grpTo, SWT.NONE);
		lblTo.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTo.setBounds(0, 0, 55, 15);
		lblTo.setText("To ");
		
		btnLatest = new Button(grpTo, SWT.RADIO);
		btnLatest.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (btnLatest.getSelection())
					if (selectAction != null)
						selectAction.run();
			}
		});
		btnLatest.setSelection(true);
		btnLatest.setText("Latest");
		
		btnToDate = new Button(grpTo, SWT.RADIO);
		btnToDate.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (btnToDate.getSelection())
					if (selectAction != null)
						selectAction.run();
			}
		});
		btnToDate.setSize(13, 16);
		
		dtwTo = new DateTime(grpTo, SWT.BORDER | SWT.DROP_DOWN);
		dtwTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				btnLatest.setSelection(false);
				btnToDate.setSelection(true);
				getDisplay().asyncExec(new Runnable()
				{	
					@Override
					public void run()
					{
						Date from = new Date(), to = new Date();
						get(from, dtwFrom);
						get(to, dtwTo);
						
						//if (btnFromDate.getSelection())
							if (from.compareTo(to) > 0) 
								set(dtwFrom, to);
					}
				});
				if (selectAction != null) selectAction.run();
			}
		});
		dtwFrom.setSize(95, 24);
		
		//initDataBindings();
		Realm.runWithDefault(SWTObservables.getRealm(getDisplay()), new Runnable()
		{
			@Override
			public void run()
			{
				initDataBindings();
			}
		});
	}
	
	protected DataBindingContext initDataBindings() 
	{
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeSelectionDtwFromObserveWidget = WidgetProperties.selection().observe(dtwFrom);
		IObservableValue fromFilterObserveValue = PojoProperties.value("from").observe(filter);
		bindingContext.bindValue(observeSelectionDtwFromObserveWidget, fromFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionDtwToooObserveWidget = WidgetProperties.selection().observe(dtwTo);
		IObservableValue toFilterObserveValue = PojoProperties.value("to").observe(filter);
		bindingContext.bindValue(observeSelectionDtwToooObserveWidget, toFilterObserveValue, null, null);
		//
		return bindingContext;
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	void setSelectAction(Runnable runnable)
	{
		this.selectAction = runnable;
	}
	
//	private void get(Calendar cal, DateTime dtw)
//	{
//		cal.set(Calendar.YEAR, dtw.getYear());
//		cal.set(Calendar.MONTH, dtw.getMonth());
//		cal.set(Calendar.DATE, dtw.getDay());
//	}
	
	@SuppressWarnings("deprecation")
	private void get(Date date, DateTime dtw)
	{
		date.setYear(dtw.getYear());
		date.setMonth(dtw.getMonth());
		date.setDate(dtw.getDay());
	}
	
//	private void set(DateTime dtw, Calendar cal)
//	{
//		dtw.setYear(cal.get(Calendar.YEAR));
//		dtw.setMonth(cal.get(Calendar.MONTH));
//		dtw.setDay(cal.get(Calendar.DATE));
//	}
	
	@SuppressWarnings("deprecation")
	private void set(DateTime dtw, Date date)
	{
		dtw.setYear(date.getYear());
		dtw.setMonth(date.getMonth());
		dtw.setDay(date.getDate());
	}
	
	public DateFilter getFilter()
	{
		DateFilter retobj = new DateFilter();
		
		boolean earliest = btnEarliest.getSelection();
		boolean fromDate = btnFromDate.getSelection();
		boolean latest = btnLatest.getSelection() ;
		boolean toDate = btnToDate.getSelection();
		
		if (earliest && !fromDate) retobj.setFrom(null);
		if (!earliest && fromDate) retobj.setFrom(filter.getFrom());
		else retobj.setFrom(null);
		
		if (latest && !toDate) retobj.setTo(null);
		if (!latest && toDate) retobj.setTo(filter.getTo());
		else retobj.setTo(null);
			
		return retobj;
	}
	
}

class DateFilter
{
	private Date from = new Date();
	private Date to = new Date();
	
	public Date getFrom()
	{
		return from;
	}
	
	public void setFrom(Date from)
	{
		this.from = from;
	}
	
	public Date getTo()
	{
		return to;
	}
	
	public void setTo(Date to)
	{
		this.to = to;
	}
}