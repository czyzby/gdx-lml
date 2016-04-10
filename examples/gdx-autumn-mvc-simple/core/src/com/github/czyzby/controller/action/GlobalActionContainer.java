package com.github.czyzby.controller.action;

import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

/** Since this class implements {@link ActionContainer} and is annotated with {@link ViewActionContainer}, its methods
 * will be reflected and available in all LML templates. Note that this class is a component like any other, so it can
 * {@link Inject} any fields, {@link Initiate} methods, etc. */
@ViewActionContainer("global")
public class GlobalActionContainer implements ActionContainer {
    /** This is a mock-up method that does nothing. It will be available in LML templates through "close", "noOp"
     * (annotation arguments) and "emptyMethod" (method name) IDs. */
    @LmlAction({ "close", "noOp" })
    public void emptyMethod() {
    }
}
