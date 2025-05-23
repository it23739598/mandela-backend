package com.example.management_system.controller;

import com.example.management_system.dto.LoginRequest;
import com.example.management_system.dto.LoginResponse;
import com.example.management_system.model.Employee;
import com.example.management_system.repository.EmployeeRepository;
import com.example.management_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*") // Adjust this to match your frontend URL
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    // Get All Employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add Employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        // Convert string date to LocalDate if needed
        if (employee.getDateOfBirth() == null && employee.getDob() != null) {
            employee.setDateOfBirth(LocalDate.parse(employee.getDob()));
        }
        return employeeService.saveEmployee(employee);
    }

    // In EmployeeController.java
    @PostMapping("/login")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginRequest request) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(request.getEmail());

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            if (employee.getNicNumber().equals(request.getNic())
                    && employee.getRole().equalsIgnoreCase(request.getRole())) {
                LoginResponse response = new LoginResponse(
                        employee.getFirstName(),
                        employee.getLastName());

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or role");
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        // Convert string date to LocalDate if needed
        if (employee.getDateOfBirth() == null && employee.getDob() != null) {
            employee.setDateOfBirth(LocalDate.parse(employee.getDob()));
        }
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    // Search Employees
    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam(required = false) String term) {
        return employeeService.searchEmployees(term);
    }
}
