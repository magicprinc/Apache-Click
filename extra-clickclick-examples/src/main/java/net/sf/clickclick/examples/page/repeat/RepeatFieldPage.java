package net.sf.clickclick.examples.page.repeat;

import net.sf.clickclick.control.panel.HorizontalPanel;
import net.sf.clickclick.control.repeater.FieldRepeater;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.examples.domain.Book;
import org.apache.click.ActionListener;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.HtmlFieldSet;

import java.util.List;

public class RepeatFieldPage extends AbstractRepeatPage {
  private static final long serialVersionUID = -2081859227611880152L;

  private static final String BOOK_KEY = "book";

  private static final String CATEGORY = "category";

  private final Form form = new Form("form");

  @Override public void onInit() {
    super.onInit();
    form.add(new TextField("name"));
    form.add(new TextField("author"));
    form.add(new TextField("isbn"));
    Submit submit = new Submit("submit");

    repeater = new FieldRepeater("categories", CATEGORY) {

      @Override public void buildRow(final Object item, final RepeaterRow row, final int index) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        row.add(horizontalPanel);

        TextField field = new TextField(CATEGORY);
        horizontalPanel.add(field);

        Submit insert = new Submit("insert");
        insert.setActionListener((ActionListener) source->{
          repeater.insertItem("", index);
          return true;
        });

        Submit delete = new Submit("delete");
        delete.setActionListener((ActionListener) source->{
          repeater.removeItem(item);
          return true;
        });

        Submit moveUp = new Submit("up");
        moveUp.setActionListener((ActionListener) source->{
          repeater.moveUp(item);
          return true;
        });

        Submit moveDown = new Submit("down");
        moveDown.setActionListener((ActionListener) source->{
          repeater.moveDown(item);
          return true;
        });

        horizontalPanel.add(insert);
        horizontalPanel.add(delete);
        horizontalPanel.add(moveUp);
        horizontalPanel.add(moveDown);
      }
    };

    repeater.setDataProvider((DataProvider) ()->getBook().getCategories());

    final Submit add = new Submit("add");
    add.setActionListener((ActionListener) source->{
      repeater.addItem("");
      return true;
    });
    FieldSet fieldSet = new HtmlFieldSet("categories");
    fieldSet.add(add);
    form.add(fieldSet);
    fieldSet.add(repeater);

    submit.setActionListener((ActionListener) source->{
      onSubmit();
      return true;
    });
    form.add(submit);
    addControl(form);

    // Pre-populate repeater fields from book category items
    repeater.copyFromItems();

    // Pre-populate form with book details
    form.copyFrom(getBook());
  }

  public boolean onSubmit() {
    if (form.isValid()) {
      // Copy the new textfield values to book category items
      repeater.copyToItems();

      // Copy the form values to the book
      form.copyTo(getBook());

      List categories = getBook().getCategories();
      System.out.println("Categories after copy -> " + categories);
    } else {
      List categories = getBook().getCategories();
      System.out.println("Categories for invalid form -> " + categories);
    }
    return true;
  }

  @Override public void onRender() {
    toggleLinks(getBook().getCategories().size());
  }

  private Book getBook() {
    Book book = (Book) getContext().getSessionAttribute(BOOK_KEY);
    if (book == null) {
      book = createBook();
      getContext().setSessionAttribute(BOOK_KEY, book);
    }
    return book;
  }

  private Book createBook() {
    return new Book();
  }
}