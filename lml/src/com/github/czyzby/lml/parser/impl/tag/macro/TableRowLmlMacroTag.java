package com.github.czyzby.lml.parser.impl.tag.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlSyntax;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.AbstractCellLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.AbstractMacroLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlBuildingAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUserObject.StandardTableTarget;
import com.github.czyzby.lml.util.LmlUserObject.TableTarget;

/** Tables utility. Adds row to the current table. Allows to set row defaults with its attributes. For example:
 *
 * <blockquote>
 *
 * <pre>
 * &lt;table&gt;
 *     First row.
 *     &lt;@row/&gt;
 *     Second row.
 * &lt;/table&gt;
 * </pre>
 *
 * </blockquote>This will append a new row after "First row." label.
 *
 * <blockquote>
 *
 * <pre>
 * &lt;table&gt;
 *     &lt;@row pad=5&gt;
 *         First row.
 *     &lt;/@row&gt;
 *     &lt;@row expandY=true&gt;
 *         Second row.
 *     &lt;/@row&gt;
 * &lt;/table&gt;
 * </pre>
 *
 * </blockquote>This will create 2 separate rows with customized row defaults. Note that row is created BEFORE tags
 * inside macro are parsed. For example:
 *
 * <blockquote>
 *
 * <pre>
 * &lt;table&gt;
 *     &lt;@row pad=5&gt;
 *         First row.
 *     &lt;/@row&gt;
 *     Same row.
 * &lt;/table&gt;
 * </pre>
 *
 * </blockquote>This would cause for both labels to be in the same row, as only 1 row is added - when the macro starts.
 *
 * @author MJ */
public class TableRowLmlMacroTag extends AbstractMacroLmlTag {
    private String content;

    public TableRowLmlMacroTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public void handleDataBetweenTags(final String rawData) {
        content = rawData;
    }

    @Override
    protected boolean supportsNamedAttributes() {
        return true;
    }

    @Override
    public void closeTag() {
        appendRow();
        if (Strings.isNotEmpty(content)) {
            appendTextToParse(content);
        }
    }

    /** Adds a row to the selected table. */
    protected void appendRow() {
        final Table table = getTable();
        final ObjectMap<String, String> attributes = getNamedAttributes();
        if (GdxMaps.isEmpty(attributes)) {
            // Adding row to main table:
            StandardTableTarget.MAIN.extract(table).row();
        } else {
            // Processing attributes:
            processTableRow(table, attributes);
        }
    }

    /** @return table extracted from parent tag. */
    protected Table getTable() {
        final Actor table = getParent().getActor();
        if (table instanceof Table) {
            return (Table) table;
        }
        getParser().throwError(
                getTagName() + " macro can be used only inside table tags (or one of its subclasses). Found \""
                        + getTagName() + "\" tag with no direct table parent.");
        return null;
    }

    /** @param table should have a row appended.
     * @param attributes need to be handled. */
    protected void processTableRow(final Table table, final ObjectMap<String, String> attributes) {
        final ObjectSet<String> processedAttributes = GdxSets.newSet();
        final LmlActorBuilder builder = new LmlActorBuilder(); // Used to determine table.
        processBuildingAttributeToDetermineTable(attributes, processedAttributes, builder);
        final Table targetTable = getTarget(builder).extract(table);
        final Cell<?> rowCell = targetTable.row();
        processCellDefaultsAttribute(attributes, processedAttributes, targetTable, rowCell);
    }

    /** @param builder may contain specific table target.
     * @return table target chosen by the builder. */
    protected TableTarget getTarget(final LmlActorBuilder builder) {
        return builder.getTableTarget() == null ? StandardTableTarget.MAIN : builder.getTableTarget();
    }

    /** This is meant to handle toButtonTable, toTitleTable, toDialogTable to choose which table should have a row
     * appended.
     *
     * @param attributes named attributes of the macro.
     * @param processedAttributes should contain processed building attributes after this method invocation.
     * @param builder used to process named attributes. */
    protected void processBuildingAttributeToDetermineTable(final ObjectMap<String, String> attributes,
            final ObjectSet<String> processedAttributes, final LmlActorBuilder builder) {
        final LmlSyntax syntax = getParser().getSyntax();
        for (final Entry<String, String> attribute : attributes) {
            final LmlBuildingAttribute<LmlActorBuilder> buildingAttribute = syntax
                    .getBuildingAttributeProcessor(builder, attribute.key);
            if (buildingAttribute != null) {
                buildingAttribute.process(getParser(), getParent(), builder, attribute.value);
                processedAttributes.add(attribute.key);
            }
        }
    }

    /** This is meant to handle cell attributes that will become defaults for the current row.
     *
     * @param attributes named attributes of the macro.
     * @param processedAttributes already processed attributes. Should be ignored.
     * @param targetTable table with the row.
     * @param cell cell of the row. Should have its defaults set. */
    protected void processCellDefaultsAttribute(final ObjectMap<String, String> attributes,
            final ObjectSet<String> processedAttributes, final Table targetTable, final Cell<?> cell) {
        final LmlSyntax syntax = getParser().getSyntax();
        for (final Entry<String, String> attribute : attributes) {
            if (processedAttributes.contains(attribute.key)) {
                continue;
            }
            final LmlAttribute<?> cellAttribute = syntax.getAttributeProcessor(targetTable, attribute.key);
            if (cellAttribute instanceof AbstractCellLmlAttribute) {
                ((AbstractCellLmlAttribute) cellAttribute).process(getParser(), getParent(), targetTable, cell,
                        attribute.value);
            } else {
                getParser().throwErrorIfStrict(
                        getTagName() + " macro can process only cell attributes. Found unknown or invalid attribute: "
                                + attribute.key);
            }
        }
    }
}
