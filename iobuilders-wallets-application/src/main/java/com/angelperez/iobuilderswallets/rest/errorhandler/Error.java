package com.angelperez.iobuilderswallets.rest.errorhandler;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.FieldError;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Error {

    List<FieldError> errors;

}
