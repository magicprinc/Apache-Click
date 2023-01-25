package net.sf.clickclick.examples.page.repeat;

import net.sf.clickclick.control.panel.HorizontalPanel;
import net.sf.clickclick.control.panel.VerticalPanel;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.examples.domain.Customer;
import org.apache.click.ActionListener;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.SubmitLink;

import java.io.Serial;
import java.util.List;

public class RepeatFormPage extends AbstractRepeatPage {
  @Serial private static final long serialVersionUID = -377446102701393830L;


  @Override
  public void onInit() {
    super.onInit();

    Form form = new Form("form");

    Submit add = new Submit("add");
    add.setActionListener((ActionListener) source->{
      Customer customer = new Customer();
      repeater.addItem(customer);
      return true;
    });
    form.add(add);

    addControl(form);

    repeater = new Repeater("repeater") {

      @Override public void buildRow(final Object item, final RepeaterRow row,
          final int index) {

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.addStyleClass("vertical-panel");

        final SubmitLink moveUp = new SubmitLink("up");
        final SubmitLink moveDown = new SubmitLink("down");
        verticalPanel.add(moveUp);
        verticalPanel.add(moveDown);

        Form form = new Form("form");
        row.add(form);

        FieldSet fieldSet = new FieldSet("customer");
        form.add(horizontalPanel);
        horizontalPanel.add(fieldSet);
        horizontalPanel.add(verticalPanel);

        fieldSet.add(new TextField("name")).setRequired(true);

        Submit save = new Submit("save");
        save.setActionListener((ActionListener) source->onSubmit(item, index));
        fieldSet.add(save);

        Submit insert = new Submit("insert");
        insert.setActionListener((ActionListener) source->{
          Customer customer = new Customer();
          repeater.insertItem(customer, index);
          return true;
        });
        fieldSet.add(insert);

        Submit delete = new Submit("delete");
        delete.setActionListener((ActionListener) source->{
          repeater.removeItem(item);
          return true;
        });
        fieldSet.add(delete);

        moveUp.setActionListener((ActionListener) source->{
          repeater.moveUp(item);
          return true;
        });

        moveDown.setActionListener((ActionListener) source->{
          repeater.moveDown(item);
          return true;
        });

        form.copyFrom(item);
      }
    };

    repeater.setDataProvider((DataProvider) ()->getTopCustomers());

    addControl(repeater);
  }

  public boolean onSubmit(Object item, int index) {
    RepeaterRow row = (RepeaterRow) repeater.getControls().get(index);
    Form form = (Form) row.getControl("form");
    if (form.isValid()) {
      repeater.copyTo(item);
    }
    return true;
  }

  @Override
  public void onRender() {
    toggleLinks(getTopCustomers().size());
  }


  private List<Customer> getTopCustomers() {
    List<Customer> customers = getCustomerService().getCustomers();
    int size = Math.min(5, customers.size());
    return customers.subList(0, size);
  }
}