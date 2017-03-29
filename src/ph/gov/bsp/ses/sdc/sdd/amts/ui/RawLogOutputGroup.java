package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;

public class RawLogOutputGroup extends Group
{
	
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
		GridLayout gridLayout = new GridLayout(1, false);
		setLayout(gridLayout);
		
		// TODO Implement this
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
}
