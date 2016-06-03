package com.github.czyzby.tests.reflected;

import com.github.czyzby.lml.parser.action.ActionContainer;

/** Contains additional methods available in LML templates.
 *
 * @author MJ */
public class CustomActionContainer implements ActionContainer {
    /* templates/examples/actions.lml + nullCheck.lml + evaluate.lml */
    public String someMethod() {
        return "Greetings from CustomActionContainer.";
    }
}
