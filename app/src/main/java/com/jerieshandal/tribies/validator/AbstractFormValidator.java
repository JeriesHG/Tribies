/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.validator;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractFormValidator
 * Created by Jeries Handal on 12/14/2016.
 * Version 1.0.0
 */
public abstract class AbstractFormValidator implements FormValidator {

    private Map<String, Object> invalidFields;

    public AbstractFormValidator(){
     invalidFields = new HashMap<>();
    }

    public abstract void validateFields();

    @Override
    public boolean validate() {
        return invalidFields.isEmpty();
    }
}
