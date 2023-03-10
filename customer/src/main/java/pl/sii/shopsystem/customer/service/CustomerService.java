package pl.sii.shopsystem.customer.service;

import pl.sii.shopsystem.customer.dto.*;

public interface CustomerService {
    CustomerOutputDto addCustomer(CustomerInputDto customerInputDto);

    CustomerOutputDto getCustomer(String id);

    void removeCustomer(CustomerEmailInputDto customerEmailInputDto);

    CustomerOutputDto updateCustomer(UpdateCustomerInputDto updateCustomerInputDto);
}
