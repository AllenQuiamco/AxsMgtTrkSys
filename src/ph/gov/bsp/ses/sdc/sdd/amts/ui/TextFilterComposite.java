package ph.gov.bsp.ses.sdc.sdd.amts.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class TextFilterComposite extends Composite
{
	private LoadItemsHandler loadItemsHandler = null;
	private Runnable selectAction = null;
	private Button btnLoadItems;
	private Combo combo;
	private Button btnAny;
	private Button btnSelect;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TextFilterComposite(Composite parent, int style)
	{
		super(parent, style);
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 5;
		formLayout.marginHeight = -5;
		setLayout(formLayout);
				
		Group group = new Group(this, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.right = new FormAttachment(100, -5);
		fd_group.top = new FormAttachment(0, 5);
		fd_group.left = new FormAttachment(0, 5);
		group.setLayoutData(fd_group);
		GridLayout gl_group = new GridLayout(4, false);
		gl_group.marginBottom = 5;
		gl_group.marginHeight = 0;
		group.setLayout(gl_group);
		
		btnAny = new Button(group, SWT.RADIO);
		btnAny.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (btnAny.getSelection())
					if (selectAction != null) 
						selectAction.run();
			}
		});
		btnAny.setSelection(true);
		GridData gd_btnAny = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAny.verticalIndent = -1;
		btnAny.setLayoutData(gd_btnAny);
		btnAny.setText("Any");
		
		btnSelect = new Button(group, SWT.RADIO);
		btnSelect.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (btnSelect.getSelection())
					if (selectAction != null) 
						selectAction.run();
			}
		});
		GridData gd_btnSelect = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSelect.verticalIndent = -1;
		btnSelect.setLayoutData(gd_btnSelect);
		
		combo = new Combo(group, SWT.NONE);
		combo.addModifyListener(new ModifyListener() 
		{
			public void modifyText(ModifyEvent e) 
			{
				btnAny.setSelection(false);
				btnSelect.setSelection(true);
				if (selectAction != null) selectAction.run();
			}
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnLoadItems = new Button(group, SWT.NONE);
		GridData gd_btnLoadItems = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadItems.verticalIndent = -1;
		btnLoadItems.setLayoutData(gd_btnLoadItems);
		btnLoadItems.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (loadItemsHandler != null) 
				{
					String[] items = loadItemsHandler.loadItems();
					if (items != null) combo.setItems(items);
				}
			}
		});
		btnLoadItems.setText("Load items");
		
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setLoadItemsHandler(LoadItemsHandler handler)
	{
		this.loadItemsHandler = handler;
	}
	
	public void setSelectAction(Runnable runnable)
	{
		this.selectAction = runnable;
	}

	public String getFilter()
	{
		boolean any = btnAny.getSelection();
		boolean select = btnSelect.getSelection();
		
		if (any && select) return null;
		else if (any && !select) return null;
		else if (!any && select) return combo.getText();
		else return null;
	}
}

interface LoadItemsHandler
{
	public String[] loadItems();
}
