package com.rehaanporject;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {
    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args){
        SpringApplication.run(Main.class,args);
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return  customerRepository.findAll();
    }

    record NewCustomerRequest(String name,String email,Integer age){

    }
    @PostMapping
    public void addCusotmer(@RequestBody NewCustomerRequest request){
       Customer customer = new Customer();
       customer.setName(request.name());
       customer.setEmail(request.email());
       customer.setAge(request.age());
       customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id){
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public  void  updateCustomer(@RequestBody NewCustomerRequest request,@PathVariable("customerId") Integer id){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setName(request.name());
            customer.setEmail(request.email());
            customer.setAge(request.age());
            customerRepository.save(customer);
            // Do something with the customer entity
        } else {
            throw new EntityNotFoundException("Customer with ID " + id + " not found");
            // Handle the case where the entity with the given ID is not found
        }
    }

}
