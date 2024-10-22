package com.lcwd.electronic.store.dtos;

/*import jakarta.persistence.Column;
import jakarta.persistence.Id;*/
import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.aspectj.bridge.IMessage;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDto {

    private String userId;

    @Size(min = 3, max=15, message = "Invalid Name!!")
    private String name;

    //@Email(message = "Invalid Email ID")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid Email Id")
    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "password cant be blank")
    private String password;

    @Size(min=4, max=6, message = "InValid Gender")
    private String gender;

    @NotBlank(message = "Write something about yourself")
    private String about;

    @ImageNameValid
    private String imageName;

    //pattern and custom validator
}
