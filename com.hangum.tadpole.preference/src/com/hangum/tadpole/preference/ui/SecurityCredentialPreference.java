/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.ui;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetSecurityCredentialPreference;

/**
 * security credentials
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 23.
 *
 */
public class SecurityCredentialPreference extends TadpoleDefaulPreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(SecurityCredentialPreference.class);
	
	private Combo comboIsUse;
	private Text textAccessKey;
	private Text textSecretKey;

	/**
	 * Create the preference page.
	 */
	public SecurityCredentialPreference() {
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblUse = new Label(container, SWT.NONE);
		lblUse.setText("Use");
		
		comboIsUse = new Combo(container, SWT.READ_ONLY);
		comboIsUse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO YESNO : PublicTadpoleDefine.YES_NO.values()) {
			comboIsUse.add(YESNO.name());
		}
		comboIsUse.select(0);
		
		Label lblAccesskey = new Label(container, SWT.NONE);
		lblAccesskey.setText("Access Key");
		
		textAccessKey = new Text(container, SWT.BORDER);
		textAccessKey.setEditable(false);
		textAccessKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSecretKey = new Label(container, SWT.NONE);
		lblSecretKey.setText("Secret Key");
		
		textSecretKey = new Text(container, SWT.BORDER);
		textSecretKey.setEditable(false);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Button btnGenerateKey = new Button(container, SWT.NONE);
		btnGenerateKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!MessageDialog.openConfirm(getShell(), "Confirm", "Are you new api key?")) return;
				
				textAccessKey.setText(Utils.getUniqueID());
				textSecretKey.setText(Utils.getUniqueID());
			}
		});
		btnGenerateKey.setText("Generate Key");
		
		initDefaultValue();
		
		return container;
	}
	
	/**
	 * 페이지 초기값 로딩 
	 */
	private void initDefaultValue() {
		comboIsUse.setText(GetSecurityCredentialPreference.getSecurityCredentialUse());
		textAccessKey.setText(GetSecurityCredentialPreference.getAccessValue());
		textSecretKey.setText(GetSecurityCredentialPreference.getSecretValue());
	}
	
	@Override
	public boolean performOk() {
		String isUse		= comboIsUse.getText();
		String txtAccessKey	= textAccessKey.getText();
		String txtSecretKey = textSecretKey.getText();

		try {
			updateInfo(PreferenceDefine.SECURITY_CREDENTIAL_USE, isUse);
			updateEncriptInfo(PreferenceDefine.SECURITY_CREDENTIAL_ACCESS_KEY, txtAccessKey);
			updateEncriptInfo(PreferenceDefine.SECURITY_CREDENTIAL_SECRET_KEY, txtSecretKey);
			
		} catch(Exception e) {
			logger.error("api security credential saveing", e);
			
			MessageDialog.openError(getShell(), "Confirm", Messages.GeneralPreferencePage_2 + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		
		return super.performOk();
	}
	
	@Override
	public boolean performCancel() {
		initDefaultValue();
		
		return super.performCancel();
	}
	
	@Override
	protected void performApply() {

		super.performApply();
	}
	
	@Override
	protected void performDefaults() {
		initDefaultValue();

		super.performDefaults();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

}
