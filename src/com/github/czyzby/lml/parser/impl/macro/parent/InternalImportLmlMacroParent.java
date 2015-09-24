package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public class InternalImportLmlMacroParent extends AbstractImportLmlMacroParent {
    public InternalImportLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent,
            final LmlParser parser) {
        super(lmlMacroData, parent, parser);
    }

    @Override
    protected FileHandle getFileHandle(final String argument) {
        return Gdx.files.internal(argument);
    }
}
