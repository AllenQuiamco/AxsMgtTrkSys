package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.util.Date;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

import ph.gov.bsp.ses.sdc.sdd.amts.Program;
import ph.gov.bsp.ses.sdc.sdd.amts.data.Filter;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBox;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxButtons;
import ph.gov.bsp.ses.sdc.sdd.util.swt.MsgBoxIcon;

public class RawLogOutputGroup extends Group
{
	private DateFilterComposite xcdfEffectedOn;
	private Button btnCreate;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RawLogOutputGroup(Composite parent, int style)
	{
		super(parent, style);
		setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		setText("Raw Data (CSV) - Log");
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginBottom = 5;
		setLayout(gridLayout);
		
		Label lblEffectedOn = new Label(this, SWT.NONE);
		GridData gd_lblEffectedOn = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblEffectedOn.verticalIndent = 19;
		lblEffectedOn.setLayoutData(gd_lblEffectedOn);
		lblEffectedOn.setText("Effected on");
		
		xcdfEffectedOn = new DateFilterComposite(this, SWT.NONE);
		
		btnCreate = new Button(this, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String text = btnCreate.getText();
				btnCreate.setEnabled(false);
				btnCreate.setText("Creating ...");
				if (create()) MsgBox.show(getShell(), "File successfully created.", "Alert", MsgBoxButtons.OK, MsgBoxIcon.INFORMATION);
				btnCreate.setText(text);
				btnCreate.setEnabled(true);
			}
		});
		GridData gd_btnCreate = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		gd_btnCreate.widthHint = 80;
		btnCreate.setLayoutData(gd_btnCreate);
		btnCreate.setText("Create");
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	protected boolean create()
	{
		Date effectedOnFrom = xcdfEffectedOn.getFilter().getFrom();
		Date effectedOnTo = xcdfEffectedOn.getFilter().getTo();
		
		Filter filter = new Filter();
		filter.add("EffectedOn", effectedOnFrom, effectedOnTo);
		
		return Program.createOutputLogRaw(getShell(), filter);
	}
	
}
