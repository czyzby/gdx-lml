package com.github.czyzby.autumn.mvc.component.ui.controller.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogController;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogShower;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;
import com.github.czyzby.lml.parser.impl.dto.StageAttacher;

/** Wraps around an object annotated with {@link com.github.czyzby.autumn.mvc.stereotype.ViewDialog}.
 *
 * @author MJ */
public class AnnotatedViewDialogController extends AbstractAnnotatedController implements
		ViewDialogController {
	private final ViewDialog dialogData;
	private final InterfaceService interfaceService;
	private final ViewDialogShower shower;
	private final ActionContainer actionContainer;
	private final String id;

	private Dialog dialog;

	public AnnotatedViewDialogController(final ViewDialog dialogData, final Object wrappedObject,
			final InterfaceService interfaceService) {
		super(wrappedObject);
		this.dialogData = dialogData;
		this.interfaceService = interfaceService;

		shower = wrappedObject instanceof ViewDialogShower ? (ViewDialogShower) wrappedObject : null;
		actionContainer = wrappedObject instanceof ActionContainer ? (ActionContainer) wrappedObject : null;
		id =
				Strings.isWhitespace(dialogData.id()) ? wrappedObject.getClass().getSimpleName() : dialogData
						.id();
	}

	@Override
	public void show(final Stage stage) {
		injectStage(stage);
		if (dialog == null || !dialogData.cacheInstance()) {
			prepareDialogInstance();
		}
		doBeforeShow();
		showDialog(stage);
	}

	private void showDialog(final Stage stage) {
		if (dialog.getUserObject() instanceof StageAttacher) {
			((StageAttacher) dialog.getUserObject()).attachToStage(stage);
		} else {
			dialog.show(stage);
		}
	}

	private void doBeforeShow() {
		if (shower != null) {
			shower.doBeforeShow(dialog);
		}
	}

	private void prepareDialogInstance() {
		final LmlParser parser = interfaceService.getParser();
		if (actionContainer != null) {
			parser.addActionContainer(getId(), actionContainer);
		}
		dialog = (Dialog) parser.parse(Gdx.files.internal(dialogData.value())).first();
		injectReferencedActors(parser.getActorsMappedById());
		if (actionContainer != null) {
			parser.removeActionContainer(getId());
		}
	}

	@Override
	public String getId() {
		return id;
	}
}