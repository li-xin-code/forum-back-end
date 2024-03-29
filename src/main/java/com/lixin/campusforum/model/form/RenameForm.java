package com.lixin.campusforum.model.form;

import com.lixin.campusforum.common.constant.consist.UserConstant;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author lixin
 */
@Data
public class RenameForm {

    @NotBlank(message = "用户名不可为空")
    @Pattern(regexp = UserConstant.USERNAME_FORMAT, message = "用户名格式错误")
    private String name;

}
