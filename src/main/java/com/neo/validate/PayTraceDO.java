package com.neo.validate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by neowyp on 2016/3/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayTraceDO implements Serializable {
    private static final long serialVersionUID = 4771155162853967835L;

    @NotBlank(message = "traceNo is not null and length eq 0!")
    private String traceNo;

    @NotNull(message = "amount is not null!")
    @Min(value = 1,message = "amount more then 1!")
    private Integer amount;

    @NotNull(message = "createDt is not null!")
    private Date createDt;

    @NotNull(message = "updateDt is not null!")
    private Date updateDt;

    @Password
    private String pwd;

}
