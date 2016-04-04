package com.github.czyzby.lml.parser.impl.tag.macro;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Tables utility. Allows to set column defaults for a selected column. For example:
 *
 * <blockquote>
 *
 * <pre>
 * &lt;table&gt;
 *     &lt;@column 2 pad=3 grow=true /&gt;
 * &lt;/table&gt;
 * </pre>
 *
 * </blockquote>This will set cell defaults of column with 2 index.
 *
 * <p>
 * This macro cannot parse text between its tags. Strict parser will throw an exception if you attempt to do so.
 *
 * @author MJ */
public class TableColumnLmlMacroTag extends TableRowLmlMacroTag {
    public TableColumnLmlMacroTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public void handleDataBetweenTags(final String rawData) {
        if (Strings.isNotBlank(rawData)) {
            getParser().throwErrorIfStrict("Column defaults macro cannot parse text between tags.");
        }
    }

    @Override
    protected boolean supportsNamedAttributes() {
        return false;
    }

    @Override
    protected boolean supportsOptionalNamedAttributes() {
        return true;
    }

    @Override
    public void closeTag() {
        if (GdxArrays.isEmpty(getAttributes())) {
            getParser().throwErrorIfStrict("Column macro needs at least one attribute: column number.");
        }
        final ObjectMap<String, String> attributes = getNamedAttributes();
        final Table table = getTable();
        final int columnId = getParser().parseInt(getAttributes().first(), table);
        final ObjectSet<String> processedAttributes = GdxSets.newSet();
        final LmlActorBuilder builder = new LmlActorBuilder(); // Used to determine table.
        processBuildingAttributeToDetermineTable(attributes, processedAttributes, builder);
        final Table targetTable = getTarget(builder).extract(table);
        final Cell<?> columnCell = targetTable.columnDefaults(columnId);
        processCellDefaultsAttribute(attributes, processedAttributes, targetTable, columnCell);
    }
}
