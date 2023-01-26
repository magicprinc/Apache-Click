package org.apache.click.examples.service;

import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;
import org.apache.click.examples.domain.Address;
import org.apache.click.examples.domain.Client;
import org.apache.click.examples.domain.SystemCode;
import org.apache.click.extras.cayenne.CayenneTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Provides a Client Service.
 *
 * @see Client
 */
@Component
public class ClientService extends CayenneTemplate {

  @SuppressWarnings("unchecked")
  public List<Client> getClients() {
    SelectQuery query = new SelectQuery(Client.class);
    query.addOrdering("db:id", SortOrder.ASCENDING);
    return (List<Client>) performQuery(query);
  }

  public Client getClient(Object id) {
    return getObjectForPK(Client.class, id);
  }

  public void saveClient(Client client) {
    if (client.getObjectContext() == null) {
      registerNewObject(client);
    }
    commitChanges();
  }

  public Client createNewClient() {
    return newObject(Client.class);
  }

  public Address createNewAddress() {
    return newObject(Address.class);
  }

  public List<SystemCode> getTitles() {
    return (List<SystemCode>) performQuery("titles", true);// disable caching doesn't help if NO com.opensymphony.oscache.base.NeedsRefreshException
  }

  public List<SystemCode> getStates() {
    return (List<SystemCode>) performQuery("states", true);
  }
}