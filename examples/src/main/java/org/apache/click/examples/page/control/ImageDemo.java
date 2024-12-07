package org.apache.click.examples.page.control;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.ImageSubmit;
import org.apache.click.control.Label;
import org.apache.click.examples.page.BorderPage;
import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serial;

/** Provides an ImageSubmit control example */
@Component
@Slf4j
public class ImageDemo extends BorderPage implements Lifecycle {
	@Serial private static final long serialVersionUID = 1L;
	private final ImageSubmit colorSubmit = new ImageSubmit("save", "/assets/images/colors.gif");

	public ImageDemo() {
		val form = new Form("form");
		addControl(form);
		val buttonsForm = new Form("buttonsForm");
		addControl(buttonsForm);

		// Buttons Form
		val editSubmit = new ImageSubmit("edit", "/assets/images/edit.gif");
		editSubmit.setActionListener(this::onEditClick);
		editSubmit.setTitle("🔞 Edit");
		buttonsForm.add(editSubmit);

		val deleteSubmit = new ImageSubmit("delete", "/assets/images/delete.gif");
		deleteSubmit.setActionListener(this::onDeleteClick);
		deleteSubmit.setTitle("✖ Delete");
		buttonsForm.add(deleteSubmit);

		// Colors Form
		form.add(new Label("label", "<b>Color Chooser</b>"));

		colorSubmit.setActionListener(this::onColorClick);
		form.add(colorSubmit);
	}

	public boolean onEditClick (Control source) {
		addModel("buttonMsg", "Edit");
		return true;
	}

	public boolean onDeleteClick (Control source) {
		addModel("buttonMsg", "Delete");
		return true;
	}

	public boolean onColorClick (Control source) {
		int x = colorSubmit.getX();
		int y = colorSubmit.getY();
		String color = "no color";

		if (x > 3 && x < 31){
			if (y > 3 && y < 31){
				color = "Red";
			} else if (y > 44 && y < 71){
				color = "Green";
			}
		} else if (x > 44 && x < 71){
			if (y > 3 && y < 31){
				color = "Blue";
			} else if (y > 44 && y < 71){
				color = "White";
			}
		}
		addModel("colorMsg", "<b>%s</b>. <p/> [ x=%d, y=%d ]".formatted(color, x, y));
		return true;
	}

	@PostConstruct
	public void afterPropertiesSet () {
		log.warn("INFO: ImageDemo afterPropertiesSet");
	}

	@Override
	public void start () {
		log.warn("INFO: start ✅ is called");
	}

	@Override
	public void stop () {
		log.warn("INFO: stop 🛑 is called");
	}

	@Override
	public boolean isRunning () {
		log.warn("INFO: isRunning ❔ is called");
		return false;
	}
}