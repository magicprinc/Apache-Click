package net.sf.clickclick.examples.services;

/**
 *
 */
public class ApplicationRegistry {


  private CustomerService customerService;


  private ApplicationRegistry() {
  }

  public static ApplicationRegistry getInstance() {
    return SingletonHolder.instance;
  }

  // --------------------------------------------------------- Public Methods

  public CustomerService getCustomerService() {
    if (customerService == null) {
      customerService = new CustomerService();
    }
    return customerService;
  }


  // Class holder lazy initialization technique
  private static class SingletonHolder {
    public static ApplicationRegistry instance = new ApplicationRegistry();
  }
}