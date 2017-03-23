package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import java.util.Hashtable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class PaginationComposite extends Composite
{
	static final int DEFAULT_ROW_START = 1;
	static final int DEFAULT_ROW_END = 20;
	
	private int start;
	private int end;
	private int total;
	private Text txtLower;
	private Text txtUpper;
	private Text txtTotal;
	private Runnable pageChangeAction;
	private Hashtable<String, Color> defaultColors = new Hashtable<String, Color>();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PaginationComposite(Composite parent, int style)
	{
		super(parent, style);
		
		Button buttonFirst = new Button(this, SWT.NONE);
		buttonFirst.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				computeFirst();
				if (pageChangeAction != null) pageChangeAction.run();
			}
		});
		buttonFirst.setImage(SWTResourceManager.getImage(PaginationComposite.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/MoveFirstHS.png"));
		buttonFirst.setBounds(1, 0, 28, 28);
		
		Button buttonPrev = new Button(this, SWT.NONE);
		buttonPrev.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				computePrev();
				if (pageChangeAction != null) pageChangeAction.run();
			}
		});
		buttonPrev.setImage(SWTResourceManager.getImage(PaginationComposite.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/MovePreviousHS.png"));
		buttonPrev.setBounds(29, 0, 28, 28);
		
		txtLower = new Text(this, SWT.BORDER | SWT.RIGHT);
		txtLower.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						if (txtLower.getText().equals("" + start))
						{
							txtLower.setBackground(getDefaultColor("txtLower.background"));
						}
						else
						{
							txtLower.setBackground(getColor_BG_CHANGED_SETTING());
						}
					}
				});
			}
		});
		txtLower.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.character == SWT.CR)
				{
					if (pageChangeAction != null) pageChangeAction.run();
				}
			}
		});
		txtLower.setText("-1");
		txtLower.setBounds(58, 4, 60, 21);
		this.defaultColors.put("txtLower.background", txtLower.getBackground());
		
		Label lblTo = new Label(this, SWT.NONE);
		lblTo.setBounds(121, 7, 11, 15);
		lblTo.setText("to");
		
		txtUpper = new Text(this, SWT.BORDER | SWT.RIGHT);
		txtUpper.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						if (txtUpper.getText().equals("" + end))
						{
							txtUpper.setBackground(getDefaultColor("txtUpper.background"));
						}
						else
						{
							txtUpper.setBackground(getColor_BG_CHANGED_SETTING());
						}
					}
				});
			}
		});
		txtUpper.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.character == SWT.CR)
				{
					if (pageChangeAction != null) pageChangeAction.run();
				}
			}
		});
		txtUpper.setText("-1");
		txtUpper.setBounds(135, 4, 60, 21);
		this.defaultColors.put("txtUpper.background", txtLower.getBackground());
		
		Button buttonNext = new Button(this, SWT.NONE);
		buttonNext.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				computeNext();
				if (pageChangeAction != null) pageChangeAction.run();
			}
		});
		buttonNext.setImage(SWTResourceManager.getImage(PaginationComposite.class, "/ph/gov/bsp/ses/sdc/sdd/amts/ui/rsx/MoveNextHS.png"));
		buttonNext.setBounds(273, 0, 28, 28);
		
		Button buttonLast = new Button(this, SWT.NONE);
		buttonLast.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				computeLast();
				if (pageChangeAction != null) pageChangeAction.run();
			}
		});
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
		setTabList(new Control[]{buttonFirst, buttonPrev, txtLower, txtUpper, txtTotal, buttonNext, buttonLast});
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	public int getRowStart()
	{
		int retval;
		
		try
		{
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
		}
		catch (IllegalArgumentException iae)
		{
			iae.printStackTrace();
			retval = this.start;
		}
		
		setRowStart(retval);
		
		return retval;
	}
	
	public int getRowEnd()
	{
		int retval;
		
		try
		{
			if (this.isEnabled())
			{
				try
				{
					retval = Integer.parseInt(this.txtUpper.getText());
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
		}
		catch (IllegalArgumentException iae)
		{
			iae.printStackTrace();
			retval = this.end;
		}
		
		setRowEnd(retval);
		
		return retval;
	}
	
	public int getRowTotal()
	{
		int retval;
		
		try
		{
			if (this.isEnabled())
			{
				try
				{
					retval = Integer.parseInt(this.txtTotal.getText());
					if (retval < 0) throw new IllegalArgumentException("Input is not non-negative.");
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
		}
		catch (IllegalArgumentException iae)
		{
			iae.printStackTrace();
			retval = this.total;
		}
		
		setRowTotal(retval);
		
		return retval;
	}
	
	public void display(final int start, final int end, final int total)
	{
		this.start = start;
		this.end = end;
		this.total = total;
		getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				setEnabled(true);
				for (Control child : getChildren())
					child.setEnabled(true);
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
				for (Control child : getChildren())
					child.setEnabled(false);
				txtLower.setText("");
				txtUpper.setText("");
				txtTotal.setText("");
			}
		});
	}
	
	public void setPageChangeAction(Runnable runnable)
	{
		this.pageChangeAction = runnable;
	}
	
	private void computeFirst()
	{
		int start = getRowStart();
		int end = getRowEnd();
		int total = getRowTotal();
		
		if (total == 0)
		{
			start = 0;
			end = 0;
		}
		else
		{
			if (start > end)
			{
				int temp = end;
				end = start;
				start = temp;
			}
			
			int diff = end - start;
			
			start = 1;
			end = start + diff;
			if (end > total) end = total;
		}
		
		setRowStart(start);
		setRowEnd(end);
		setRowTotal(total);
	}
	
	private void computePrev()
	{
		int start = getRowStart();
		int end = getRowEnd();
		int total = getRowTotal();
		
		if (total == 0)
		{
			start = 0;
			end = 0;
		}
		else
		{
			if (start > end)
			{
				int temp = end;
				end = start;
				start = temp;
			}
			
			int diff = end - start;
			
			start = start - (diff + 1);
			if (start <= 0) start = 1;
			
			end = start + diff;
			if (end > total) end = total;
		}
		
		setRowStart(start);
		setRowEnd(end);
		setRowTotal(total);
	}
	
	private void computeNext()
	{
		int start = getRowStart();
		int end = getRowEnd();
		int total = getRowTotal();
		
		if (total == 0)
		{
			start = 0;
			end = 0;
		}
		else
		{
			if (start > end)
			{
				int temp = end;
				end = start;
				start = temp;
			}
			
			int diff = end - start;
			
			start = end + 1;
			end = start + diff;

			if (end > total) 
			{
				end = total;
				start = end - diff;
			}
		}
		
		setRowStart(start);
		setRowEnd(end);
		setRowTotal(total);
	}
	
	private void computeLast()
	{
		int start = getRowStart();
		int end = getRowEnd();
		int total = getRowTotal();
		
		if (total == 0)
		{
			start = 0;
			end = 0;
		}
		else
		{
			if (start > end)
			{
				int temp = end;
				end = start;
				start = temp;
			}
			
			int diff = end - start;
			
			end = total;
			start = end - diff;
		}
		
		setRowStart(start);
		setRowEnd(end);
		setRowTotal(total);
	}
	
	private void setRowStart(int start)
	{
		this.txtLower.setText("" + start);
	}
	
	private void setRowEnd(int end)
	{
		this.txtUpper.setText("" + end);
	}
	
	private void setRowTotal(int end)
	{
		this.txtTotal.setText("" + end);
	}
	
	private Color getColor_BG_CHANGED_SETTING()
	{
		return new Color(this.getDisplay(), 255, 255, 225);
	}
	
	private Color getDefaultColor(String element)
	{
		return this.defaultColors.get(element);
	}
}
