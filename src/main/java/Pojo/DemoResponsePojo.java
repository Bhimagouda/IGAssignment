package Pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DemoResponsePojo {
    private String status;
    private List<EmployeeData> employeeData;
    private String message;
}
