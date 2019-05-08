package stephen.springboot.mailservice.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsEmailValidator implements ConstraintValidator<IsEmail, String> {
    // 逻辑处理
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String[] emailAddresses = value.split(",");

        for (String emailAddress : emailAddresses) {
            //利用正则表达式（可改进）验证邮箱是否符合邮箱的格式
            if (!emailAddress.matches("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$")) {
                return false;
            }
        }

        return true;
    }

}