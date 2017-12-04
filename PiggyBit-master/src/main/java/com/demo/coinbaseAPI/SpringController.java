package com.demo.coinbaseAPI;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SpringController {

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String getSettings(Model model) {
		model.addAttribute("settingsForm", new SettingsForm());
		return "settingsForm";
	}
	
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public String settingsResult(
			@ModelAttribute("setForm") SettingsForm setForm, 
			BindingResult result, Model settingsForm) throws IOException, ParseException {
		if (result.hasErrors()) {
            return "settingsForm";
        }
		String currency = setForm.getCurrency();
		String preferredCrypto = setForm.getPreferredCrypto();
		String priceMargin = setForm.getPriceMargin();
		String recurringPeriod = setForm.getRecurringPeriod();
		
		settingsForm.addAttribute("currency", currency);
		settingsForm.addAttribute("preferredCrypto", preferredCrypto);
		settingsForm.addAttribute("priceMargin", priceMargin);
		settingsForm.addAttribute("recurringPeriod", recurringPeriod);
		
		return "settingsConfirmed";
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String cryptoForm(Model model) {
        model.addAttribute("submissionForm", new SubmissionForm());
        return "submissionForm";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(
      @ModelAttribute("subForm") SubmissionForm subForm,
      BindingResult result, Model submissionForm) throws IOException, ParseException {
        if (result.hasErrors()) {
            return "submissionForm";
        }
        
        String currency = subForm.getCurrency();
        String crypto = subForm.getCrypto();
        String price = PriceFetcher.processJSON(crypto,currency);
        
        submissionForm.addAttribute("crypto", crypto);
        submissionForm.addAttribute("currency", currency);
        submissionForm.addAttribute("price", price);

        return "Result";
    }

}