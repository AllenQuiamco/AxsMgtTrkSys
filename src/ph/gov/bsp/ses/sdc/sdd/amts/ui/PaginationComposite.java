package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Control;

public class PaginationComposite extends Composite
{
	static final int DEFAULT_ROW_START = 1; 
	static final int DEFAULT_ROW_END = 20;
	
	private Text txtLower;
	private Text txtUpper;
	private Text txtTotal;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PaginationComposite(Composite parent, int style)
	{
		super(parent, style);
		
		Button buttonFirst = new Button(this, SWT.NONE);
		buttonFirst.setImage(SWTResourceManager.getImage(PaginationComposite.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/MoveFirstHS.png"));
		buttonFirst.setBounds(1, 0, 28, 28);
		
		Button buttonPrev = new Button(this, SWT.NONE);
		buttonPrev.setImage(SWTResourceManager.getImage(PaginationComposite.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/MovePreviousHS.png"));
		buttonPrev.setBounds(29, 0, 28, 28);
		
		txtLower = new Text(this, SWT.BORDER | SWT.RIGHT);
		txtLower.setText("-1");
		txtLower.setBounds(58, 4, 60, 21);
		
		Label lblTo = new Label(this, SWT.NONE);
		lblTo.setBounds(121, 7, 11, 15);
		lblTo.setText("to");
		
		txtUpper = new Text(this, SWT.BORDER | SWT.RIGHT);
		txtUpper.setText("-1");
		txtUpper.setBounds(135, 4, 60, 21);
		
		Button buttonNext = new Button(this, SWT.NONE);
		buttonNext.setImage(SWTResourceManager.getImage(PaginationComposite.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/MoveNextHS.png"));
		buttonNext.setBounds(273, 0, 28, 28);
		
		Button buttonLast = new Button(this, SWT.NONE);
		buttonLast.setImage(SWTResourceManager.getImage(PaginationComposite.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/MoveLastHS.png"));
		buttonLast.setBounds(301, 0, 28, 28);
		
		Label lblOf = new Label(this, SWT.NONE);
		lblOf.setText("of");
		lblOf.setBounds(198, 7, 11, 15);
		
		txtTotal = new Text(this, SWT.BORDER | SWT.RIGHT);
		txtTotal.setText("-1");
		txtTotal.setEditable(false);
		txtTotal.setBounds(212, 4, 60, 21);
		
		setEnabled(false);
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	public int getRowStart()
	{
		int retval;
		
		if (this.isEnabled())
		{
			try
			{
				retval = Integer.parseInt(this.txtLower.getText());
				if (retval <= 0) throw new IllegalArgumentException("Input is not positive.");
			}
			catch (NumberFormatException nfe)
			{
				throw new IllegalArgumentException("Input is not a number.", nfe);
			}
		}
		else 
		{
			retval = DEFAULT_ROW_START;
		}
		
		return retval;
	}
	
	public int getRowEnd()
	{
		int retval;
		
		if (this.isEnabled())
		{
			try
			{
				retval = Integer.parseInt(this.txtLower.getText());
				if (retval <= 0) throw new IllegalArgumentException("Input is not positive.");
			}
			catch (NumberFormatException nfe)
			{
				throw new IllegalArgumentException("Input is not a number.", nfe);
			}
		}
		else
		{
			retval = DEFAULT_ROW_END; 
		}
		
		return retval;
	}

	public void display(final int start, final int end, final int total)
	{
		getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				setEnabled(true);
				for(Control child : getChildren()) child.setEnabled(true);
				txtLower.setText("" + start);
				txtUpper.setText("" + end);
				txtTotal.setText("" + total);
			}
		});
	}

	public void displayEmpty()
	{
		getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				setEnabled(false);
				for(Control child : getChildren()) child.setEnabled(false);
				txtLower.setText("");
				txtUpper.setText("");
				txtTotal.setText("");
			}
		});
	}
}
