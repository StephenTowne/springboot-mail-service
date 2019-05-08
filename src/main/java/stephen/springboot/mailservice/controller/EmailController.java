package stephen.springboot.mailservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import stephen.springboot.mailservice.common.response.ResponseResult;
import stephen.springboot.mailservice.common.response.ResponseResultGenerator;
import stephen.springboot.mailservice.dto.EmailInputDTO;
import stephen.springboot.mailservice.dto.EmailOutputDTO;
import stephen.springboot.mailservice.service.EmailService;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "邮件服务接口")
@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "获取单条邮件记录")
    @GetMapping("/{id}")
    public ResponseResult<EmailOutputDTO> one(@PathVariable Long id) {
        EmailOutputDTO emailOutputDTO = emailService.getEmailById(id);
        return ResponseResultGenerator.genSuccessResult(emailOutputDTO);
    }

    @ApiOperation(value = "删除单条邮件记录")
    @DeleteMapping("/{id}")
    public ResponseResult deleteOne(@PathVariable Long id) {
        emailService.deleteEmailById(id);
        return ResponseResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "发送邮件")
    @PostMapping("/")
    public ResponseResult newEmail(@Valid EmailInputDTO emailInputDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError error : errorList) {
                stringBuilder.append(",");
                stringBuilder.append(error.getDefaultMessage());
            }
            stringBuilder.deleteCharAt(0);
            return ResponseResultGenerator.genFailResult(stringBuilder.toString());
        } else {
            Long id = emailService.sendEmail(emailInputDTO);
            return ResponseResultGenerator.genSuccessResult(id);
        }
    }
}
