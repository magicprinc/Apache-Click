/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click.jquery.examples.services;

/**
 *
 */
public class ApplicationRegistry {

    // -------------------------------------------------------------- Variables

    private CustomerService customerService;

    private PostCodeService postCodeService;

    private FileSystemService fileSystemService;

    // -------------------------------------------------------- Constructors

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

    public PostCodeService getPostCodeService() {
        if (postCodeService == null) {
            postCodeService = new PostCodeService();
        }
        return postCodeService;
    }

    public FileSystemService getFileSystemService() {
        if (fileSystemService == null) {
            fileSystemService = new FileSystemService();
        }
        return fileSystemService;
    }

    // ---------------------------------------------------------- Inner Classes

    // Class holder lazy initialization technique
    private static class SingletonHolder {
        public static ApplicationRegistry instance = new ApplicationRegistry();
    }
}
