package com.mycorp.page;

import com.mycorp.service.CustomerService;
import org.apache.click.Page;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.PageLink;
import org.apache.click.control.Table;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.util.Bindable;

import java.util.List;

public class ViewCustomers extends BorderPage {
    private static final long serialVersionUID = -8820573903081328643L;

	@Bindable public Table table = new Table();
	@Bindable public PageLink editLink = new PageLink("Edit", EditCustomer.class);
	@Bindable public ActionLink deleteLink = new ActionLink("Delete", this, "onDeleteClick");


	public ViewCustomers() {
		// Set Page to stateful to preserve Table sort and paging state while editing customers
		setStateful(true);

		table.setClass(Table.CLASS_ORANGE1);
		table.setPageSize(10);
		table.setShowBanner(true);
		table.setSortable(true);

		table.addColumn(new Column("id"));

		table.addColumn(new Column("name"));

		Column column = new Column("email");
		column.setAutolink(true);
		column.setTitleProperty("name");
		table.addColumn(column);

		table.addColumn(new Column("investments"));

		editLink.setImageSrc("/assets/images/table-edit.png");
		editLink.setTitle("Edit customer details");
		editLink.setParameter("referrer", getContext().getPagePath(ViewCustomers.class));

		deleteLink.setImageSrc("/assets/images/table-delete.png");
		deleteLink.setTitle("Delete customer record");
		deleteLink
				.setAttribute("onclick",
						"return window.confirm('Are you sure you want to delete this record?');");

		column = new Column("Action");
		column.setTextAlign("center");
		AbstractLink[] links = new AbstractLink[] { editLink, deleteLink };
		column.setDecorator(new LinkDecorator(table, links, "id"));
		column.setSortable(false);
		table.addColumn(column);
	}

	// --------------------------------------------------------- Event Handlers

	public boolean onDeleteClick() {
		Integer id = deleteLink.getValueInteger();
		getCustomerService().deleteCustomer(id);
		return true;
	}

	/**
	 * @see Page#onRender()
	 */
	@Override
	public void onRender() {
		List list = getCustomerService().getCustomers();
		table.setRowList(list);
	}

	/**
	 * Return CustomerService instance from Spring application context.
	 *
	 * @return CustomerService instance
	 */
	@Override public CustomerService getCustomerService() {
		return new CustomerService();
	}
}