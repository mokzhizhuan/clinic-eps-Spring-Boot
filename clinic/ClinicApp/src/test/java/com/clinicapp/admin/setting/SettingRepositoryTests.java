package com.clinicapp.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.clinicapp.entity.Setting;
import com.clinicapp.entity.SettingCategory;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {

	@Autowired 
	SettingRepository repo;
	
	@Test
	public void testCreateGeneralSettings() {
		Setting siteName = new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Shopme Ltd.", SettingCategory.GENERAL);
		
		repo.saveAll(List.of(siteName, siteLogo, copyright));
		
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		
		repo.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType, 
				decimalDigits, thousandsPointType));
		
	}
	
	@Test
	public void testCreateEmailSettings() {
		Setting mailfrom = new Setting("MAIL_FROM", "shopme@gmail.com", SettingCategory.MAIL_SERVER);
		Setting mailhost = new Setting("MAIL_HOST", "smtp@gmail.com", SettingCategory.MAIL_SERVER);
		Setting mailpassword = new Setting("MAIL_PASSWORD", "password", SettingCategory.MAIL_SERVER);
		Setting mailport = new Setting("MAIL_PORT", "123", SettingCategory.MAIL_SERVER);
		Setting mailsender = new Setting("MAIL_SENDER_NAME", "Shopme Team", SettingCategory.MAIL_SERVER);
		Setting mailusername = new Setting("MAIL_USERNAME", "username", SettingCategory.MAIL_SERVER);
		Setting mailsmtp = new Setting("SMTP_AUTH", "true", SettingCategory.MAIL_SERVER);
		Setting mailsmtpauth = new Setting("SMTP_SECURED", "true", SettingCategory.MAIL_SERVER);	
		
		repo.saveAll(List.of(mailfrom, mailhost, mailpassword, mailport, mailsender, mailusername, mailsmtp, mailsmtpauth));
		
	}
	
	@Test
	public void testCreateEmailTemplate() {
		Setting customertemplate = new Setting("PATIENT_VERIFY_SUBJECT", "Please verify your registration", SettingCategory.MAIL_TEMPLATES);
		Setting customercontent = new Setting("PATIENT_VERIFY_CONTENT", "<span style='font-size=18px;'>Dear customer , please verify your registration</span>", SettingCategory.MAIL_TEMPLATES);
		Setting orderconfirmation = new Setting("ORDER_CONFIRMATION_SUBJECT", "Order Confirmation", SettingCategory.MAIL_TEMPLATES);
		Setting ordercontent = new Setting("ORDER_CONFIRMATION_CONTENT", "<span style='font-size=18px;'>Dear customer , your order is completed</span>", SettingCategory.MAIL_TEMPLATES);

		
		repo.saveAll(List.of(customertemplate, customercontent, orderconfirmation, ordercontent));
		
	}
	
	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);
		
		settings.forEach(System.out::println);
	}
}
